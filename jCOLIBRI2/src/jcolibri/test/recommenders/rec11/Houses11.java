/**
 * Houses11.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 23/10/2007
 */
package jcolibri.test.recommenders.rec11;

import java.util.Collection;
import java.util.HashMap;
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
import jcolibri.extensions.recommendation.navigationByProposing.queryElicitation.LessLikeThis;
import jcolibri.extensions.recommendation.navigationByProposing.queryElicitation.MoreAndLessLikeThis;
import jcolibri.extensions.recommendation.navigationByProposing.queryElicitation.MoreLikeThis;
import jcolibri.extensions.recommendation.navigationByProposing.queryElicitation.PartialMoreLikeThis;
import jcolibri.extensions.recommendation.navigationByProposing.queryElicitation.WeightedMoreLikeThis;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.DiverseByMedianRetrieval.ExpertClerkMedianScoring;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterBasedRetrievalMethod;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterConfig;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Table;
import jcolibri.method.retrieve.NNretrieval.similarity.local.recommenders.InrecaLessIsBetter;
import jcolibri.method.retrieve.NNretrieval.similarity.local.recommenders.InrecaMoreIsBetter;
import jcolibri.method.retrieve.selection.SelectCases;
import jcolibri.test.recommenders.housesData.HouseDescription;

/**
 * Conversational (type A) flats recommender using pre-selected cases at one-off preference elicitation, 
 * Navigation By Proposing and Filtering+NearestNeighbour+selectTopK retrieval.
 * <br>
 * This recommender follows the Navigation by Proposing strategy and shows the behaviour of several methods
 * that solve the <i>Iterated Preference Elecitiation</i> task that defines how to modify the user preferences.
 * There are several methods to obtain a new query from the user selections and critiques: 
 * <ul>
 *  <li>More Like This ({@link jcolibri.extensions.recommendation.navigationByProposing.queryElicitation.MoreLikeThis})
 *  <li>partial More Like This ({@link jcolibri.extensions.recommendation.navigationByProposing.queryElicitation.PartialMoreLikeThis})
 *  <li>weighted More Like ({@link jcolibri.extensions.recommendation.navigationByProposing.queryElicitation.WeightedMoreLikeThis})
 *  <li>Less Like This ({@link jcolibri.extensions.recommendation.navigationByProposing.queryElicitation.LessLikeThis})
 *  <li>More Like This + Less Like This ({@link jcolibri.extensions.recommendation.navigationByProposing.queryElicitation.MoreAndLessLikeThis})
 * <ul>
 * Modifying the code, the Iterated Preference Elecitiation strategy can be defined before running the example.
 * <br>Summary:
 * <ul>
 * <li>Type: Conversational A
 * <li>Case base: houses
 * <li>One off Preference Elicitation: k diverse cases chosen by ExpertClerkMedianScoring and shown in a table + critiques
 * <li>Retrieval: Filtering + NN + topKselection
 * <li>Display: In table with find something similar option
 * <li>Iterated Preference Elecitiation: Navigation by Proposing: by default MLT+LLT. Modifying the code, the replace query strategy can be defined before running the example.
 * </ul>
 * This recommender implements the following template:<br>
 * <center><img src="../Template10_Cycle.jpg"/></center>
 *  
 * <br>Read the documentation of the recommenders extension for details about templates
 * and recommender strategies: {@link jcolibri.extensions.recommendation}
 * 
 * @see jcolibri.method.retrieve.DiverseByMedianRetrieval.ExpertClerkMedianScoring
 * @see jcolibri.method.retrieve.FilterBasedRetrieval.FilterBasedRetrievalMethod
 * @see jcolibri.method.retrieve.NNretrieval.NNScoringMethod
 * @see jcolibri.method.retrieve.selection.SelectCases
 * @see jcolibri.extensions.recommendation.casesDisplay.DisplayCasesTableMethod
 * 
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 */
public class Houses11 implements StandardCBRApplication
{
    enum QueryElicitationStrategy {MLT, pMLT, wMLT, LLT, MLT_LLT};
    
    private QueryElicitationStrategy strategy;
    
    /** Connector object */
    Connector _connector;
    /** CaseBase object */
    CBRCaseBase _caseBase;

    /** KNN configuration*/
    NNConfig simConfig;
    /** Filter configuration */
    FilterConfig filterConfig;


    
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
	simConfig.addMapping(new Attribute("beds", HouseDescription.class), new InrecaMoreIsBetter(0.5));
	simConfig.addMapping(new Attribute("price", HouseDescription.class), new InrecaLessIsBetter(2000, 0.5));
	simConfig.addMapping(new Attribute("furnished", HouseDescription.class), new Equal());
	simConfig.addMapping(new Attribute("type", HouseDescription.class), new Equal());
	simConfig.addMapping(new Attribute("baths", HouseDescription.class), new InrecaMoreIsBetter(0.5));

	// Filter configuration
	filterConfig = new FilterConfig();
	
	/*// UNCOMMENT THIS CODE TO DEFINE THE ITERATED PREFERENCE ELECITATION BEFORE RUNNING THE EXAMPLE
	 Object[] possibleValues = QueryElicitationStrategy.values();
	 strategy = (QueryElicitationStrategy)JOptionPane.showInputDialog(null,
	             "Choose one strategy for the example", "Choose strategy",
	             JOptionPane.INFORMATION_MESSAGE, null,
	             possibleValues, possibleValues[0]);
	*/ 
	strategy = QueryElicitationStrategy.MLT_LLT;
    }

    public void cycle(CBRQuery query) throws ExecutionException
    {	
	HashMap<Attribute,Double> thresholds = new HashMap<Attribute,Double>();
	thresholds.put(new Attribute("beds", HouseDescription.class), 2.0);
	thresholds.put(new Attribute("price", HouseDescription.class), 300.0);
	thresholds.put(new Attribute("baths", HouseDescription.class), 2.0);
	thresholds.put(new Attribute("area", HouseDescription.class), 0.99);
	thresholds.put(new Attribute("type", HouseDescription.class), 0.99);
	
	// Retrieve Cases
	Collection<RetrievalResult> retrievedCases = ExpertClerkMedianScoring.getDiverseByMedian(_caseBase.getCases(), simConfig, thresholds);
	
	// Select Cases
	Collection<CBRCase> selectedCases = SelectCases.selectTopK(retrievedCases, 5);
		
	UserChoice choice = DisplayCasesTableMethod.displayCasesInTableSelectCase(selectedCases); 

	// Jump to main conversation
	sequence1(choice.getSelectedCaseAsQuery());

    }
    
    
    public void sequence1(CBRQuery query)  throws ExecutionException
    {	
	// Execute Filter
	Collection<CBRCase> filtered = FilterBasedRetrievalMethod.filterCases(_caseBase.getCases(), query, filterConfig);
	
	// Execute NN
	Collection<RetrievalResult> retrievedCases = NNScoringMethod.evaluateSimilarity(filtered, query, simConfig);
	
	// Select cases
	Collection<CBRCase> selectedCases = SelectCases.selectTopK(retrievedCases, 10);
    	
	System.out.println("Retrieved cases");
	for(CBRCase c: selectedCases)
	    System.out.println(c);
	
	// Obtain case
	UserChoice choice = DisplayCasesTableMethod.displayCasesInTableSelectCase(selectedCases);
	
	if(ContinueOrFinish.continueOrFinish(choice))
	    sequence2(query,choice,selectedCases);
	else
	    sequence3(choice, selectedCases);
    }
    
    public void sequence2(CBRQuery query, UserChoice uc, Collection<CBRCase> proposedCases) throws ExecutionException
    {
	if(strategy == QueryElicitationStrategy.MLT)
	    MoreLikeThis.moreLikeThis(query, uc.getSelectedCase());
	else if(strategy == QueryElicitationStrategy.pMLT)
	    PartialMoreLikeThis.partialMoreLikeThis(query, uc.getSelectedCase(), proposedCases);
	else if(strategy == QueryElicitationStrategy.wMLT)
	    WeightedMoreLikeThis.weightedMoreLikeThis(query, uc.getSelectedCase(), proposedCases, simConfig);
	else if(strategy == QueryElicitationStrategy.LLT)
	    LessLikeThis.lessLikeThis(query, uc.getSelectedCase(), proposedCases, filterConfig);
	else if(strategy == QueryElicitationStrategy.MLT_LLT)
	    MoreAndLessLikeThis.moreAndLessLikeThis(query, uc.getSelectedCase(), proposedCases, filterConfig);
	 
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
	StandardCBRApplication recommender = new Houses11();
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
	    org.apache.commons.logging.LogFactory.getLog(Houses11.class).error(e);
	    
	}
	

    }

}
