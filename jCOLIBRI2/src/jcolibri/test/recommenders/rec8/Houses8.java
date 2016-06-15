/**
 * Houses8.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 23/10/2007
 */
package jcolibri.test.recommenders.rec8;

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
import jcolibri.extensions.recommendation.askingAndProposing.AskingAndProposingPreferenceElicitation;
import jcolibri.extensions.recommendation.askingAndProposing.DisplayCasesIfNumberAndChangeNavigation;
import jcolibri.extensions.recommendation.casesDisplay.UserChoice;
import jcolibri.extensions.recommendation.conditionals.BuyOrQuit;
import jcolibri.extensions.recommendation.conditionals.ContinueOrFinish;
import jcolibri.extensions.recommendation.navigationByAsking.InformationGain;
import jcolibri.extensions.recommendation.navigationByAsking.ObtainQueryWithAttributeQuestionMethod;
import jcolibri.extensions.recommendation.navigationByAsking.SelectAttributeMethod;
import jcolibri.extensions.recommendation.navigationByProposing.CriticalUserChoice;
import jcolibri.extensions.recommendation.navigationByProposing.CritiqueOption;
import jcolibri.extensions.recommendation.navigationByProposing.DisplayCasesTableWithCritiquesMethod;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterBasedRetrievalMethod;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterConfig;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.Equal;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.NotEqual;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.QueryLess;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.QueryMore;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.EnumDistance;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Table;
import jcolibri.method.retrieve.NNretrieval.similarity.local.recommenders.InrecaMoreIsBetter;
import jcolibri.method.retrieve.NNretrieval.similarity.local.recommenders.McSherryMoreIsBetter;
import jcolibri.method.retrieve.selection.diversity.BoundedGreedySelection;
import jcolibri.test.recommenders.rec8.housesData.HouseDescription;

/**
 * Conversational (type B) flats recommender using both Navigation by Asking and Navigation by Proposing.
 * <br>
 * This example reproduces the behaviour of the ExpertClerk system. It works as a Navigation by Asking system until the number
 * of cases is small enough and then it changes to Navigation by Proposing.
 * <p>See:
 * <p>
 * H. Shimazu. ExpertClerk: A Conversational Case-Based Reasoning Tool for 
 * Developing Salesclerk Agents in E-Commerce Webshops. Artif. Intell. Rev., 
 * 18(3-4):223-244, 2002.
 * <p>
 * <br>Summary:
 * <ul>
 * <li>Type: Conversational B.
 * <li>Case base: houses2 (price moved to the solution and priceRange in the description).
 * <li>One off Preference Elicitation: Navigation by Asking using Information Gain.
 * <li>Retrieval: Filter-Based
 * <li>Display Condition: number of cases. If the number of cases is less than a value it changes the Iterated Preference Elicitation to NbP
 * <li>Display: In table with critiques. (Cases are only shown in NbP mode)
 * <li>Iterated Preference Elicitation: Depends on the mode. The method AskingAndProposingPreferenceElicitation selects an attribute 
 * using InformationGain in NbA mode or simply replaces current query with the critizied one in NbP.
 * </ul>
 * This recommender implements the following template:<br>
 * <center><img src="../Template8_Cycle.jpg"/></center>
 * 
 * <br>Read the documentation of the recommenders extension for details about templates
 * and recommender strategies: {@link jcolibri.extensions.recommendation}
 * 
 * @see jcolibri.extensions.recommendation.navigationByAsking.ObtainQueryWithAttributeQuestionMethod
 * @see jcolibri.extensions.recommendation.navigationByAsking.InformationGain
 * @see jcolibri.method.retrieve.FilterBasedRetrieval.FilterBasedRetrievalMethod
 * @see jcolibri.extensions.recommendation.casesDisplay.DisplayCasesTableMethod
 * @see jcolibri.extensions.recommendation.askingAndProposing.DisplayCasesIfNumberAndChangeNavigation
 * @see jcolibri.method.retrieve.selection.diversity.BoundedGreedySelection
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 */
public class Houses8 implements StandardCBRApplication
{
    /** Connector object */
    Connector _connector;
    /** CaseBase object */
    CBRCaseBase _caseBase;

    FilterConfig filterConfig;  
    NNConfig simConfig;
    SelectAttributeMethod selectAttributeMethod;
    Collection<CritiqueOption> critiques;
    
    public void configure() throws ExecutionException
    {
	// Create a data base connector
	_connector = new PlainTextConnector();
	// Init the ddbb connector with the config file
	_connector.initFromXMLfile(jcolibri.util.FileIO
			.findFile("jcolibri/test/recommenders/rec8/housesData/plaintextconfig.xml"));
	// Create a Lineal case base for in-memory organization
	_caseBase = new LinealCaseBase();
	

	filterConfig = new FilterConfig();
	filterConfig.addPredicate(new Attribute("area", HouseDescription.class), new Equal());
	filterConfig.addPredicate(new Attribute("beds", HouseDescription.class), new Equal());
	filterConfig.addPredicate(new Attribute("priceRange", HouseDescription.class), new Equal());
	filterConfig.addPredicate(new Attribute("furnished", HouseDescription.class), new Equal());
	filterConfig.addPredicate(new Attribute("type", HouseDescription.class), new Equal());
	filterConfig.addPredicate(new Attribute("baths", HouseDescription.class), new Equal());

	//Lets configure the KNN
	simConfig = new NNConfig();
	// Set the average() global similarity function for the description of the case
	simConfig.setDescriptionSimFunction(new Average());
	simConfig.addMapping(new Attribute("area", HouseDescription.class), new Table("jcolibri/test/recommenders/housesData/area.csv"));
	simConfig.addMapping(new Attribute("beds", HouseDescription.class), new McSherryMoreIsBetter(0,0));
	simConfig.addMapping(new Attribute("priceRange", HouseDescription.class), new EnumDistance());
	simConfig.addMapping(new Attribute("furnished", HouseDescription.class), new jcolibri.method.retrieve.NNretrieval.similarity.local.Equal());
	simConfig.addMapping(new Attribute("type", HouseDescription.class), new jcolibri.method.retrieve.NNretrieval.similarity.local.Equal());
	simConfig.addMapping(new Attribute("baths", HouseDescription.class), new InrecaMoreIsBetter(0.5));
	
	selectAttributeMethod = new InformationGain(_caseBase.getCases()); 
	
	
	critiques = new ArrayList<CritiqueOption>();
	critiques.add(new CritiqueOption("More Beds",new Attribute("beds", HouseDescription.class),new QueryLess()));
	critiques.add(new CritiqueOption("Cheaper",new Attribute("priceRange", HouseDescription.class),new QueryMore()));
	critiques.add(new CritiqueOption("More Bathrooms",new Attribute("baths", HouseDescription.class),new QueryLess()));
	critiques.add(new CritiqueOption("Change Area",new Attribute("area", HouseDescription.class),new jcolibri.method.retrieve.FilterBasedRetrieval.predicates.Equal()));
	critiques.add(new CritiqueOption("Another Area",new Attribute("area", HouseDescription.class),new NotEqual()));
	
	
    }

    public void cycle(CBRQuery query) throws ExecutionException
    {	
	Attribute att = selectAttributeMethod.getAttribute(_caseBase.getCases(), query);
	
	Map<Attribute,String> labels = new HashMap<Attribute,String>();
	ObtainQueryWithAttributeQuestionMethod.obtainQueryWithAttributeQuestion(query, att, labels, _caseBase.getCases());

	sequence1(query, _caseBase.getCases(), filterConfig);

    }
    
    
    public void sequence1(CBRQuery query, Collection<CBRCase> cases, FilterConfig fc)  throws ExecutionException
    {
	Collection<CBRCase> workingCases =  FilterBasedRetrievalMethod.filterCases(cases, query, fc);
	
	if(DisplayCasesIfNumberAndChangeNavigation.displayCasesIfNumberAndChangeNavigation(50, workingCases))
	    sequence2(query, workingCases);
	else
	    sequence3(query, workingCases, null);
    }
    
    public void sequence2(CBRQuery query, Collection<CBRCase> workingCases)  throws ExecutionException
    {
	Collection<RetrievalResult> retrievedCases = NNScoringMethod.evaluateSimilarity(workingCases, query, simConfig);
	
	Collection<CBRCase> selectedCases = BoundedGreedySelection.boundedGreddySelection(retrievedCases, query, simConfig,3,3);
	
	CriticalUserChoice choice = DisplayCasesTableWithCritiquesMethod.displayCasesInTableWithCritiques(selectedCases, critiques, workingCases);

	if(ContinueOrFinish.continueOrFinish(choice))
	    sequence3(query,workingCases, choice);
	else
	    sequence4(choice, workingCases);
    }
    
    public void sequence3(CBRQuery query, Collection<CBRCase> workingCases, CriticalUserChoice choice)  throws ExecutionException
    {
	AskingAndProposingPreferenceElicitation.doPreferenceElicitation(query, workingCases, selectAttributeMethod, choice);
	if(choice != null)
	    sequence1(query, workingCases, choice.getFilterConfig());
	else
	    sequence1(query, workingCases, filterConfig);
    }
    
    public void sequence4(UserChoice choice, Collection<CBRCase> workingCases)  throws ExecutionException
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
	StandardCBRApplication recommender = new Houses8();
	try
	{
	    recommender.configure();
	    
	    recommender.preCycle();
	    
	    CBRQuery query = new CBRQuery();
	    
	    HouseDescription hd = new HouseDescription();
	    //hd.setArea("Hampstead");
	    //hd.setBaths(1);
	    //hd.setBeds(HouseDescription.Beds.two);
	    //hd.setFurnished(true);
	    //hd.setPrice(500);
	    //hd.setType(HouseDescription.Type.Flat);
	    
	    query.setDescription(hd);
	    
	    recommender.cycle(query);
	    
	    //System.exit(0);
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(Houses8.class).error(e);
	    
	}
	

    }

}
