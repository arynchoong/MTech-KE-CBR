/**
 * Restaurant7.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 02/11/2007
 */
package jcolibri.test.recommenders.rec7;

import java.util.Collection;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.exception.ExecutionException;
import jcolibri.extensions.recommendation.ContentBasedProfile.ObtainQueryFromProfile;
import jcolibri.extensions.textual.IE.common.BasicInformationExtractor;
import jcolibri.extensions.textual.IE.common.DomainTopicClassifier;
import jcolibri.extensions.textual.IE.common.FeaturesExtractor;
import jcolibri.extensions.textual.IE.common.GlossaryLinker;
import jcolibri.extensions.textual.IE.common.PhrasesExtractor;
import jcolibri.extensions.textual.IE.common.StopWordsDetector;
import jcolibri.extensions.textual.IE.common.TextStemmer;
import jcolibri.extensions.textual.IE.common.ThesaurusLinker;
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
import jcolibri.test.test13.gui.ResultFrame;
import jcolibri.test.test13.similarity.AverageMultipleTextValues;
import jcolibri.test.test13.similarity.TokensContained;

/**
 * Single-Shot restaurants recommender using profiles, Nearest Neighbour retrieval and top k selection .
 * <br>
 * This is the typical recommender that obtains the user preferences from a profile, 
 * then computes Nearest Neigbour retrieval + top K selection, displays the retrieved
 * items and finishes.
 * <br>Summary:
 * <ul>
 * <li>Type: Single-Shot
 * <li>Case base: restaurants
 * <li>One off Preference Elicitation: Profile
 * <li>Retrieval:  Nearest Neighbour+ selectTopK
 * <li>Display: Custom window.
 * </ul>
 * This recommender implements the following template:<br>
 * <center><img src="../Template7_Cycle.jpg"/></center>
 * 
 * <br>Read the documentation of the recommenders extension for details about templates
 * and recommender strategies: {@link jcolibri.extensions.recommendation}
 * 
 * @see jcolibri.extensions.recommendation.ContentBasedProfile.ObtainQueryFromProfile
 * @see jcolibri.method.retrieve.NNretrieval.NNScoringMethod
 * @see jcolibri.method.retrieve.selection.SelectCases
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 *
 */
public class Restaurant7 implements StandardCBRApplication
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
	query = ObtainQueryFromProfile.obtainQueryFromProfile( "src/jcolibri/test/recommenders/rec7/profile.xml");
	
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
	
	//Now we configure the KNN method with some user-defined similarity measures
	NNConfig knnConfig = new NNConfig();
	knnConfig.setDescriptionSimFunction(new Average());
	
	knnConfig.addMapping(new Attribute("location", RestaurantDescription.class), new Equal());
	
	//To compare text we use the OverlapCofficient
	knnConfig.addMapping(new Attribute("description", RestaurantDescription.class), new OverlapCoefficient());
	//This function takes a string with several numerical values and computes the average
	knnConfig.addMapping(new Attribute("price", RestaurantDescription.class), new AverageMultipleTextValues(1000));
	//This function takes a string with several words separated by whitespaces, converts it to a set of tokens and
	//computes the size of the intersecction of the query set and the case set normalized with the case set
	knnConfig.addMapping(new Attribute("foodType", RestaurantDescription.class), new TokensContained());
	knnConfig.addMapping(new Attribute("food", RestaurantDescription.class), new TokensContained());
	knnConfig.addMapping(new Attribute("alcohol", RestaurantDescription.class), new Equal());
	knnConfig.addMapping(new Attribute("takeout", RestaurantDescription.class), new Equal());
	knnConfig.addMapping(new Attribute("delivery", RestaurantDescription.class), new Equal());
	knnConfig.addMapping(new Attribute("parking", RestaurantDescription.class), new Equal());
	knnConfig.addMapping(new Attribute("catering", RestaurantDescription.class), new Equal());
	
	System.out.println("RESULT:");
	
	Collection<RetrievalResult> res = NNScoringMethod.evaluateSimilarity(cases, query, knnConfig);
	res = SelectCases.selectTopKRR(res, 5);
	
	for(RetrievalResult rr: res)
	    System.out.println(rr);
	
	//Show the result
	RestaurantDescription qrd = (RestaurantDescription)query.getDescription();
	CBRCase mostSimilar = res.iterator().next().get_case();
	RestaurantDescription rrd = (RestaurantDescription)mostSimilar.getDescription();
	new ResultFrame(qrd.getDescription().toString(), rrd.getName(), rrd.getAddress(), rrd.getDescription().toString());
	
	
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
	Restaurant7 test = new Restaurant7();
	try
	{
	    test.configure();
	    
	    CBRCaseBase caseBase = test.preCycle();
	   
	    System.out.println("CASE BASE: ");
	    for(CBRCase c: caseBase.getCases())
		System.out.println(c);
	    System.out.println("Total: "+caseBase.getCases().size()+" cases");

	    
            test.cycle(null);

	    test.postCycle();
	    
	} catch (ExecutionException e)
	{
	    org.apache.commons.logging.LogFactory.getLog(Restaurant7.class).error(e);
	}
    }

}
