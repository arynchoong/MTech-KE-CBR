/**
 * Houses13.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 23/10/2007
 */
package jcolibri.test.recommenders.rec13;

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
import jcolibri.extensions.recommendation.casesDisplay.UserChoice;
import jcolibri.extensions.recommendation.conditionals.BuyOrQuit;
import jcolibri.extensions.recommendation.conditionals.ContinueOrFinish;
import jcolibri.extensions.recommendation.conditionals.DisplayCasesIfNumber;
import jcolibri.extensions.recommendation.navigationByProposing.CriticalUserChoice;
import jcolibri.extensions.recommendation.navigationByProposing.CritiqueOption;
import jcolibri.extensions.recommendation.navigationByProposing.DisplayCasesTableWithCritiquesMethod;
import jcolibri.extensions.recommendation.navigationByProposing.queryElicitation.MoreLikeThis;
import jcolibri.extensions.recommendation.tabuList.TabuList;
import jcolibri.method.gui.formFilling.ObtainQueryWithFormMethod;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterBasedRetrievalMethod;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterConfig;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.NotEqual;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.QueryLess;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.QueryMore;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.QueryMoreOrEqual;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.Threshold;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Table;
import jcolibri.method.retrieve.NNretrieval.similarity.local.recommenders.InrecaLessIsBetter;
import jcolibri.method.retrieve.NNretrieval.similarity.local.recommenders.InrecaMoreIsBetter;
import jcolibri.method.retrieve.selection.compromiseDriven.CompromiseDrivenSelection;
import jcolibri.test.recommenders.housesData.HouseDescription;

/**
 * Conversational (type B) flats recommender using Navigation by Proposing and Filtering + Nearest Neighbour + Compromise Driven Selection.
 * <br>
 * This recommender follows the Navigation by Proposing strategy with two important features: it uses Compromise Driven selection after
 * the NN scoring and it manages a tabu list of prevously displayed items.
 * <br>
 * The compromise driven selection method chooses cases according to their compromises with the user's query.
 * See {@link jcolibri.method.retrieve.selection.compromiseDriven.CompromiseDrivenSelection} for details.
 * <br>
 * The tabu list avoids displaying again an item that has been already presented to the user. 
 * See {@link jcolibri.extensions.recommendation.tabuList.TabuList} for details.
 * <br>Summary:
 * <ul>
 * <li>Type: Conversational B
 * <li>Case base: houses
 * <li>One off Preference Elicitation: Form filling (with initial values)
 * <li>Retrieval: Filtered + KNN + Compromise Driven selection 
 * <li>Display: In table with critiques
 * <li>Iterated Preference Elecitiation: Navigation by Proposing: MLT.
 * </ul>
 * This recommender implements the following template:<br>
 * <center><img src="../Template11_Cycle.jpg"/></center>
 * 
 * <br>Read the documentation of the recommenders extension for details about templates
 * and recommender strategies: {@link jcolibri.extensions.recommendation}
 * 
 * @see jcolibri.method.gui.formFilling.ObtainQueryWithFormMethod
 * @see jcolibri.method.retrieve.FilterBasedRetrieval.FilterBasedRetrievalMethod
 * @see jcolibri.method.retrieve.NNretrieval.NNScoringMethod
 * @see jcolibri.method.retrieve.selection.compromiseDriven.CompromiseDrivenSelection
 * @see jcolibri.extensions.recommendation.casesDisplay.DisplayCasesTableMethod
 * @see jcolibri.extensions.recommendation.navigationByProposing.queryElicitation.MoreLikeThis
 * 
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 */
public class Houses13 implements StandardCBRApplication
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
    
    /** Critiques configuration object */
    Collection<CritiqueOption> critiques;
    
    /** CDR configuration */
    FilterConfig preferences;
    
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


	// Obtain query configuration
	hiddenAtts = new ArrayList<Attribute>();
	labels = new HashMap<Attribute,String>();
	labels.put(new Attribute("beds", HouseDescription.class), "Min bedrooms");
	labels.put(new Attribute("price", HouseDescription.class), "Approximate price");
	labels.put(new Attribute("baths", HouseDescription.class), "Min bahtrooms");
	
	// Critiques configuration
	critiques = new ArrayList<CritiqueOption>();
	critiques.add(new CritiqueOption("More Beds",new Attribute("beds", HouseDescription.class),new QueryLess()));
	critiques.add(new CritiqueOption("Cheaper",new Attribute("price", HouseDescription.class),new QueryMore()));
	critiques.add(new CritiqueOption("More Bathrooms",new Attribute("baths", HouseDescription.class),new QueryLess()));
	critiques.add(new CritiqueOption("Change Area",new Attribute("area", HouseDescription.class),new jcolibri.method.retrieve.FilterBasedRetrieval.predicates.Equal()));
	critiques.add(new CritiqueOption("Another Area",new Attribute("area", HouseDescription.class),new NotEqual()));
	
	preferences = new FilterConfig();
	preferences.addPredicate(new Attribute("beds", HouseDescription.class), new QueryMoreOrEqual());
	preferences.addPredicate(new Attribute("price", HouseDescription.class), new Threshold(50));
	preferences.addPredicate(new Attribute("baths", HouseDescription.class), new QueryMoreOrEqual());
	preferences.addPredicate(new Attribute("furnished", HouseDescription.class), new jcolibri.method.retrieve.FilterBasedRetrieval.predicates.Equal());
	preferences.addPredicate(new Attribute("type", HouseDescription.class), new jcolibri.method.retrieve.FilterBasedRetrieval.predicates.Equal());
	
    }

    public void cycle(CBRQuery query) throws ExecutionException
    {	
	// Obtain query with form filling
	ObtainQueryWithFormMethod.obtainQueryWithInitialValues(query, hiddenAtts,labels);

	// Jump to main conversation
	sequence1(query, new FilterConfig());

    }
    
    
    public void sequence1(CBRQuery query, FilterConfig filterConfig)  throws ExecutionException
    {	
	// Execute Filter
	Collection<CBRCase> filtered = FilterBasedRetrievalMethod.filterCases(_caseBase.getCases(), query, filterConfig);
	
	// Execute NN
	Collection<RetrievalResult> retrievedCases = NNScoringMethod.evaluateSimilarity(filtered, query, simConfig);
	
	// Select Cases
	Collection<CBRCase> selectedCases = CompromiseDrivenSelection.CDR(query, retrievedCases, preferences);
    	
	// Remove cases in Tabu List
	selectedCases = TabuList.removeTabuList(selectedCases);

	if(DisplayCasesIfNumber.displayCasesWithMessage(Integer.MAX_VALUE, 1, selectedCases))
	    sequence2(selectedCases);
	else
	    System.exit(0);
	
    }
    
    public void sequence2(Collection<CBRCase> selectedCases) throws ExecutionException
    {
	// Obtain critizied query
	CriticalUserChoice choice = DisplayCasesTableWithCritiquesMethod.displayCasesInTableWithCritiques(selectedCases, critiques, _caseBase.getCases());

	// Update Tabu list
	TabuList.updateTabuList(selectedCases);
	
	if(ContinueOrFinish.continueOrFinish(choice))
	    sequence3(choice.getSelectedCaseAsQuery(), choice);
	else
	    sequence4(choice, selectedCases);
	
    }
    
    public void sequence3(CBRQuery query, CriticalUserChoice cuc) throws ExecutionException
    {
	// Replaze current query with the critizied one
	MoreLikeThis.moreLikeThis(query, cuc.getSelectedCase());
	
	sequence1(query, cuc.getFilterConfig());
    }
    
    public void sequence4(UserChoice choice, Collection<CBRCase> retrievedCases)  throws ExecutionException
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
	StandardCBRApplication recommender = new Houses13();
	try
	{
	    recommender.configure();
	    
	    recommender.preCycle();
	    
	    CBRQuery query = new CBRQuery();
	    
	    HouseDescription hd = new HouseDescription();
	    hd.setArea(HouseDescription.Area.Hampstead);
	    hd.setBaths(1);
	    hd.setBeds(HouseDescription.Beds.two);
	    hd.setFurnished(true);
	    hd.setPrice(500);
	    hd.setType(HouseDescription.Type.Flat);
	    query.setDescription(hd);
	    
	    recommender.cycle(query);
	    
	    recommender.postCycle();
	    
	    //System.exit(0);
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(Houses13.class).error(e);
	    
	}
	

    }

}
