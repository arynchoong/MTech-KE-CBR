/**
 * Test13a.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 23/06/2007
 */
package jcolibri.test.test13;

import java.util.Collection;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.exception.ExecutionException;
import jcolibri.extensions.textual.IE.common.BasicInformationExtractor;
import jcolibri.extensions.textual.IE.common.DomainTopicClassifier;
import jcolibri.extensions.textual.IE.common.FeaturesExtractor;
import jcolibri.extensions.textual.IE.common.GlossaryLinker;
import jcolibri.extensions.textual.IE.common.PhrasesExtractor;
import jcolibri.extensions.textual.IE.common.StopWordsDetector;
import jcolibri.extensions.textual.IE.common.TextStemmer;
import jcolibri.extensions.textual.IE.common.ThesaurusLinker;
import jcolibri.extensions.textual.IE.opennlp.IETextOpenNLP;
import jcolibri.extensions.textual.IE.opennlp.OpennlpMainNamesExtractor;
import jcolibri.extensions.textual.IE.opennlp.OpennlpPOStagger;
import jcolibri.extensions.textual.IE.opennlp.OpennlpSplitter;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.textual.OverlapCoefficient;
import jcolibri.method.retrieve.selection.SelectCases;
import jcolibri.test.main.SwingProgressBar;
import jcolibri.test.test13.connector.RestaurantsConnector;
import jcolibri.test.test13.gui.ResultFrame;
import jcolibri.test.test13.similarity.AverageMultipleTextValues;
import jcolibri.test.test13.similarity.TokensContained;

/**
 * This test shows how to use the Textual CBR extension in a Restaurant recommender. See the jcolibri.extensions.textual.IE package documentation for
 * details about this extension. This example uses the OpenNLP implementation.
 * <br>
 * It uses a custum connector (RestaurantConnector) and similarity functions (AverageMultipleTextValues and TokensContained).
 * The connector loads cases from a normal txt file and the similarity functions work with the information extracted by the textual CBR methods.
 * These methods extract information from text and store it in the other attributes of the description. That information is stored as a string with
 * several values separated with white spaces, so specific similarity measures are requiered to compare those attributes.
 * See their javadoc for more information.
 * <br>
 * To compare the texts it uses a textual similarity function from the jcolibri.method.retrieve.NNretrieval.similarity.local.textual package.
 * Test13b uses the Lucene similarity function instead that one.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * 
 * @see jcolibri.test.test13.similarity.AverageMultipleTextValues
 * @see jcolibri.test.test13.similarity.TokensContained
 * @see jcolibri.test.test13.connector.RestaurantsConnector
 * @see jcolibri.extensions.textual.IE
 */
public class Test13a implements StandardCBRApplication
{

    Connector _connector;
    CBRCaseBase _caseBase;

    
    /*
     * (non-Javadoc)
     * 
     * @see jcolibri.cbraplications.BasicCBRApplication#configure()
     */
    public void configure() throws ExecutionException
    {
	try
	{
	    //Use a custom connector
	    _connector = new RestaurantsConnector("jcolibri/test/test13/restaurants-large-v2.txt");
	    _caseBase = new LinealCaseBase();
	    
	    //To show the progress
	    jcolibri.util.ProgressController.clear();
	    SwingProgressBar pb = new SwingProgressBar();
	    jcolibri.util.ProgressController.register(pb);   
	} catch (Exception e)
	{
	    throw new ExecutionException(e);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcolibri.cbraplications.StandardCBRApplication#preCycle()
     */
    public CBRCaseBase preCycle() throws ExecutionException
    {
	//In the precycle we pre-compute the information extraction in the case base
	
	//Initialize Wordnet
	ThesaurusLinker.loadWordNet();
	//Load user-specific glossary
	GlossaryLinker.loadGlossary("jcolibri/test/test13/glossary.txt");
	//Load phrases rules
	PhrasesExtractor.loadRules("jcolibri/test/test13/phrasesRules.txt");
	//Load features rules
	FeaturesExtractor.loadRules("jcolibri/test/test13/featuresRules.txt");
	//Load topic rules
	DomainTopicClassifier.loadRules("jcolibri/test/test13/domainRules.txt");
	
	//Obtain cases
	_caseBase.init(_connector);
	Collection<CBRCase> cases = _caseBase.getCases();

	//Perform IE methods in the cases
	
	//Organize cases into paragraphs, sentences and tokens
	OpennlpSplitter.split(cases);
	//Detect stopwords
	StopWordsDetector.detectStopWords(cases);
	//Stem text
	TextStemmer.stem(cases);
	//Perform POS tagging
	OpennlpPOStagger.tag(cases);
	//Extract main names
	OpennlpMainNamesExtractor.extractMainNames(cases);
	//Extract phrases
	PhrasesExtractor.extractPhrases(cases);
	//Extract features
	FeaturesExtractor.extractFeatures(cases);
	//Classify with a topic
	DomainTopicClassifier.classifyWithTopic(cases);
	//Perform IE copying extracted features or phrases into other attributes of the case
	BasicInformationExtractor.extractInformation(cases);
	
	return _caseBase;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcolibri.cbraplications.StandardCBRApplication#cycle(jcolibri.cbrcore.CBRQuery)
     */
    public void cycle(CBRQuery query) throws ExecutionException
    {
	Collection<CBRCase> cases = _caseBase.getCases();
	
	//Perform IE methods in the cases
	
	//Organize the query into paragraphs, sentences and tokens
	OpennlpSplitter.split(query);
	//Detect stopwords
	StopWordsDetector.detectStopWords(query);
	//Stem query
	TextStemmer.stem(query);
	//Perform POS tagging in the query
	OpennlpPOStagger.tag(query);
	//Extract main names
	OpennlpMainNamesExtractor.extractMainNames(query);
	
	//Now that we have the query we relate cases tokens with the query tokens
	//Using the user-defined glossary
	GlossaryLinker.LinkWithGlossary(cases, query);
	//Using wordnet
	ThesaurusLinker.linkWithWordNet(cases, query);
	
	//Extract phrases
	PhrasesExtractor.extractPhrases(query);
	//Extract features
	FeaturesExtractor.extractFeatures(query);
	//Classify with a topic
	DomainTopicClassifier.classifyWithTopic(query);
	//Perform IE copying extracted features or phrases into other attributes of the query
	BasicInformationExtractor.extractInformation(query);
	
	//Now we configure the NN method with some user-defined similarity measures
	NNConfig nnConfig = new NNConfig();
	nnConfig.setDescriptionSimFunction(new Average());
	
	nnConfig.addMapping(new Attribute("location", RestaurantDescription.class), new Equal());
	
	//To compare text we use the OverlapCofficient
	nnConfig.addMapping(new Attribute("description", RestaurantDescription.class), new OverlapCoefficient());
	//This function takes a string with several numerical values and computes the average
	nnConfig.addMapping(new Attribute("price", RestaurantDescription.class), new AverageMultipleTextValues(1000));
	//This function takes a string with several words separated by whitespaces, converts it to a set of tokens and
	//computes the size of the intersection of the query set and the case set normalized with the case set
	nnConfig.addMapping(new Attribute("foodType", RestaurantDescription.class), new TokensContained());
	nnConfig.addMapping(new Attribute("food", RestaurantDescription.class), new TokensContained());
	nnConfig.addMapping(new Attribute("alcohol", RestaurantDescription.class), new Equal());
	nnConfig.addMapping(new Attribute("takeout", RestaurantDescription.class), new Equal());
	nnConfig.addMapping(new Attribute("delivery", RestaurantDescription.class), new Equal());
	nnConfig.addMapping(new Attribute("parking", RestaurantDescription.class), new Equal());
	nnConfig.addMapping(new Attribute("catering", RestaurantDescription.class), new Equal());
	
	
	System.out.println("RESULT:");
	Collection<RetrievalResult> res = NNScoringMethod.evaluateSimilarity(cases, query, nnConfig);
	res = SelectCases.selectTopKRR(res, 5);
	
	for(RetrievalResult rr: res)
	    System.out.println(rr);
	
	//Show the result
	RestaurantDescription qrd = (RestaurantDescription)query.getDescription();
	CBRCase mostSimilar = res.iterator().next().get_case();
	RestaurantDescription rrd = (RestaurantDescription)mostSimilar.getDescription();
	new ResultFrame(qrd.getDescription().getRAWContent(), rrd.getName(), rrd.getAddress(), rrd.getDescription().getRAWContent());
	
	
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcolibri.cbraplications.StandardCBRApplication#postCycle()
     */
    public void postCycle() throws ExecutionException
    {
	jcolibri.extensions.textual.wordnet.WordNetBridge.deInit();
	_connector.close();

    }

    
    public static void main(String[] args)
    {
	Test13a test = new Test13a();
	try
	{
	    test.configure();
	    
	    CBRCaseBase caseBase = test.preCycle();
	   
	    System.out.println("CASE BASE: ");
	    for(CBRCase c: caseBase.getCases())
		System.out.println(c);
	    System.out.println("Total: "+caseBase.getCases().size()+" cases");

	    boolean _continue = true;
	    while(_continue)
	    {
        	    String queryString = javax.swing.JOptionPane.showInputDialog("Please enter the restaurant description:");
        	    if(queryString == null)
        		_continue = false;
        	    else
        	    {	
                	    CBRQuery query = new CBRQuery();
                	    RestaurantDescription queryDescription = new RestaurantDescription();
                	    queryDescription.setDescription(new IETextOpenNLP(queryString));
                	    query.setDescription(queryDescription);
                	    
                	    test.cycle(query);
        	    }
	    }
	    test.postCycle();
	    
	} catch (ExecutionException e)
	{
	    org.apache.commons.logging.LogFactory.getLog(Test13a.class).error(e);
	}
    }
}
