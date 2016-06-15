/**
 * Houses5.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 23/10/2007
 */
package jcolibri.test.recommenders.rec5;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.PlainTextConnector;
import jcolibri.exception.ExecutionException;
import jcolibri.extensions.recommendation.casesDisplay.DisplayCasesTableMethod;
import jcolibri.extensions.recommendation.casesDisplay.UserChoice;
import jcolibri.extensions.recommendation.conditionals.BuyOrQuit;
import jcolibri.extensions.recommendation.conditionals.ContinueOrFinish;
import jcolibri.extensions.recommendation.navigationByAsking.ObtainQueryWithAttributeQuestionMethod;
import jcolibri.extensions.recommendation.navigationByAsking.SimilarityInfluence;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Table;
import jcolibri.method.retrieve.NNretrieval.similarity.local.recommenders.InrecaLessIsBetter;
import jcolibri.method.retrieve.NNretrieval.similarity.local.recommenders.InrecaMoreIsBetter;
import jcolibri.method.retrieve.NNretrieval.similarity.local.recommenders.McSherryMoreIsBetter;
import jcolibri.method.retrieve.selection.SelectCases;
import jcolibri.test.recommenders.housesData.HouseDescription;

/**
 * Conversational (type A) flats recommender using Navigation by Asking and KNN retrieval.
 * <br>
 * This recommender combines Navigation by Asking and Nearest Neighbour retrieval.
 * To select the attribute asked to the user, it applies the Similarity Influence method.
 * Then, the NN scoring method is executed and the retrieved items are presented to the
 * user. If the user does not find the desired item, the system asks again for the value
 * of another attribute.
 * <br>
 * Note!: the Similarity influence method runs very slowly. To speed up the execution, only
 * 200 items are loaded.
 * <br>Summary:
 * <ul>
 * <li>Type: Conversational A
 * <li>Case base: houses
 * <li>One off Preference Elicitation: Navigation by Asking using Similarity Influence (note: that method is very slow)
 * <li>Retrieval: NN + topKselection
 * <li>Display: In table
 * <li>Iterated Preference Elecitiation: Navigation by Asking using Similarity Influence (note: that method is very slow)
 * </ul>
 * This recommender implements the following template:<br>
 * <center><img src="../Template5_Cycle.jpg"/></center>
 * 
 * <br>Read the documentation of the recommenders extension for details about templates
 * and recommender strategies: {@link jcolibri.extensions.recommendation}
 * 
 * @see jcolibri.extensions.recommendation.navigationByAsking.ObtainQueryWithAttributeQuestionMethod
 * @see jcolibri.extensions.recommendation.navigationByAsking.SimilarityInfluence
 * @see jcolibri.method.retrieve.NNretrieval.NNScoringMethod
 * @see jcolibri.method.retrieve.selection.SelectCases
 * @see jcolibri.extensions.recommendation.casesDisplay.DisplayCasesTableMethod
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 */
public class Houses5 implements StandardCBRApplication
{
    /** Connector object */
    Connector _connector;
    /** CaseBase object */
    CBRCaseBase _caseBase;

    /** NN configuration*/
    NNConfig simConfig;
 
    /** Obtain query configuration*/
    Collection<Attribute> hiddenAtts;
    /** Obtain query configuration*/
    Map<Attribute,String> labels;
    
    //MODIFICATION TO SPEED UP THE EXECUTION
    Collection<CBRCase> workingCases;
    
    public void configure() throws ExecutionException
    {
	// Create a data base connector
	_connector = new PlainTextConnector();
	// Init the ddbb connector with the config file
	_connector.initFromXMLfile(jcolibri.util.FileIO
			.findFile("jcolibri/test/recommenders/housesData/plaintextconfig.xml"));
	// Create a Lineal case base for in-memory organization
	_caseBase = new LinealCaseBase();
	
	//Lets configure the KNN
	simConfig = new NNConfig();
	// Set the average() global similarity function for the description of the case
	simConfig.setDescriptionSimFunction(new Average());
	simConfig.addMapping(new Attribute("area", HouseDescription.class), new Table("jcolibri/test/recommenders/housesData/area.csv"));
	simConfig.addMapping(new Attribute("beds", HouseDescription.class), new McSherryMoreIsBetter(0,0));
	simConfig.addMapping(new Attribute("price", HouseDescription.class), new InrecaLessIsBetter(2000, 0.5));
	simConfig.addMapping(new Attribute("furnished", HouseDescription.class), new Equal());
	simConfig.addMapping(new Attribute("type", HouseDescription.class), new Equal());
	simConfig.addMapping(new Attribute("baths", HouseDescription.class), new InrecaMoreIsBetter(0.5));

	
	// Configure obtain query method
	hiddenAtts = new ArrayList<Attribute>();
	hiddenAtts.add(new Attribute("beds", HouseDescription.class));
	labels = new HashMap<Attribute,String>();
	labels.put(new Attribute("price", HouseDescription.class), "Max price");
	labels.put(new Attribute("baths", HouseDescription.class), "Min bahtrooms");

    }

    public void cycle(CBRQuery query) throws ExecutionException
    {	
	// Select attribute using SimVar
	Collection<CBRCase> loadedCases = _caseBase.getCases();
	
	// MODIFICATION TO SPEED UP THE EXECUTION
	workingCases = new ArrayList<CBRCase>();
	java.util.Iterator<CBRCase> iter = loadedCases.iterator();
	for(int i=0; i<200; i++)
	    workingCases.add(iter.next());
	// MODIFICATION END
	
	Attribute att = SimilarityInfluence.getMoreSimVarAttribute(workingCases,query, simConfig, true);

	// Obtain attribute value
	ObtainQueryWithAttributeQuestionMethod.obtainQueryWithAttributeQuestion(query, att, labels, workingCases);

	// Go to main conversation cycle
	sequence1(query);

    }
    
    
    public void sequence1(CBRQuery query)  throws ExecutionException
    {	
	// Execute KNN
	// MODIFICATION TO SPEED UP THE EXECUTION. Use _caseBase.getCases() insead working cases
	// to use the whole case base.
	Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(workingCases, query, simConfig);
	
	// Select cases
	Collection<CBRCase> retrievedCases = SelectCases.selectTopK(eval, 5);
    	
	// Display cases
	UserChoice choice = DisplayCasesTableMethod.displayCasesInTableEditQuery(retrievedCases);	

	if(ContinueOrFinish.continueOrFinish(choice))
	    sequence2(query);
	else
	    sequence3(choice, retrievedCases);
    }
    
    public void sequence2(CBRQuery query) throws ExecutionException
    {
	// Select attribute using SimVar
	
	// MODIFICATION TO SPEED UP THE EXECUTION. Use _caseBase.getCases() insead working cases
	// to use the whole case base.
	//Collection<CBRCase> workingCases = _caseBase.getCases();
	
	Attribute att = SimilarityInfluence.getMoreSimVarAttribute(workingCases,query, simConfig, true);

	// Obtain attribute value
	ObtainQueryWithAttributeQuestionMethod.obtainQueryWithAttributeQuestion(query, att, labels, workingCases);

	// Go to main conversation cycle
	sequence1(query);
    }
    
    public void sequence3(UserChoice choice, Collection<CBRCase> retrievedCases)  throws ExecutionException
    {
	if(BuyOrQuit.buyOrQuit(choice))
	    System.out.println("Finish - User Buys: "+choice.getSelectedCase());
	
	else
	    System.out.println("Finish - User Quits");
    }

    public void postCycle() throws ExecutionException
    {
    }

    public CBRCaseBase preCycle() throws ExecutionException
    {
	// Load cases from connector into the case base
	_caseBase.init(_connector);		
	// Print the cases
	java.util.Collection<CBRCase> cases = _caseBase.getCases();
	for(CBRCase c: cases)
		System.out.println(c);
	return _caseBase;
    }
    
    public static void main(String[] args) {
	
	//SwingProgressBar shows the progress
	jcolibri.util.ProgressController.clear();
	jcolibri.util.ProgressController.register(new jcolibri.test.main.SwingProgressBar(), SimilarityInfluence.class);

	
	StandardCBRApplication recommender = new Houses5();
	try
	{
	    recommender.configure();
	    
	    recommender.preCycle();
	    
	    CBRQuery query = new CBRQuery();
	    
	    HouseDescription hd = new HouseDescription();
	    
	    query.setDescription(hd);
	    
	    recommender.cycle(query);
	    
	    recommender.postCycle();
	    
	    //System.exit(0);
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(Houses5.class).error(e);
	    
	}
	

    }

}
