/**
 * Houses6.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 23/10/2007
 */
package jcolibri.test.recommenders.rec6;

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
import jcolibri.extensions.recommendation.navigationByProposing.CriticalUserChoice;
import jcolibri.extensions.recommendation.navigationByProposing.CritiqueOption;
import jcolibri.extensions.recommendation.navigationByProposing.DisplayCasesTableWithCritiquesMethod;
import jcolibri.extensions.recommendation.navigationByProposing.queryElicitation.MoreLikeThis;
import jcolibri.method.gui.formFilling.ObtainQueryWithFormMethod;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterBasedRetrievalMethod;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterConfig;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.NotEqual;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.QueryLess;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.QueryMore;
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
 * Conversational (type A) flats recommender using Navigation by Proposing and Filtered+NearestNeighbour+topKselection retrieval.
 * <br>
 * This recommender implements the Navigation by Proposing strategy. 
 * This strategy selects and displays some items to the user and the he/she makes
 * a critique over one of the displayed items (i.e.: "like this but cheaper").
 * To obtain the user preferences this recommender uses a form. Then, the filtering method
 * is executed using as filters the critiques over the previously presented items. Finally, the 
 * Nearest Neighbour method is applied to the filtered set to obtain the items displayed
 * to the user. 
 * <br>Summary:
 * <ul>
 * <li>Type: Conversational A
 * <li>Case base: houses
 * <li>One off Preference Elicitation: Form filling (without initial values)
 * <li>Retrieval: Filtering + NN + topKselection
 * <li>Display: In table with critiques
 * <li>Iterated Preference Elecitiation: Navigation by Proposing: More Like This.
 * </ul>
 * This recommender implements the following template:<br>
 * <center><img src="../Template6_Cycle.jpg"/></center>
 * 
 * <br>Read the documentation of the recommenders extension for details about templates
 * and recommender strategies: {@link jcolibri.extensions.recommendation}
 * 
 * @see jcolibri.method.gui.formFilling.ObtainQueryWithFormMethod
 * @see jcolibri.method.retrieve.FilterBasedRetrieval.FilterBasedRetrievalMethod
 * @see jcolibri.method.retrieve.NNretrieval.NNScoringMethod
 * @see jcolibri.method.retrieve.selection.SelectCases
 * @see jcolibri.extensions.recommendation.casesDisplay.DisplayCasesTableMethod
 * @see jcolibri.extensions.recommendation.navigationByProposing.queryElicitation.MoreLikeThis
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 */
public class Houses6 implements StandardCBRApplication
{
    /** Connector object */
    Connector _connector;
    /** CaseBase object */
    CBRCaseBase _caseBase;

    /** KNN configuration*/
    NNConfig simConfig;
    
    /** Obtain query configuration*/
    Collection<Attribute> hiddenAtts;
    /** Obtain query configuration*/
    Map<Attribute,String> labels;
    
    /** Critiques configuration object */
    Collection<CritiqueOption> critiques;
    
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
	labels.put(new Attribute("price", HouseDescription.class), "Max price");
	labels.put(new Attribute("baths", HouseDescription.class), "Min bahtrooms");
	
	// Critiques configuration
	critiques = new ArrayList<CritiqueOption>();
	critiques.add(new CritiqueOption("More Beds",new Attribute("beds", HouseDescription.class),new QueryLess()));
	critiques.add(new CritiqueOption("Cheaper",new Attribute("price", HouseDescription.class),new QueryMore()));
	critiques.add(new CritiqueOption("More Bathrooms",new Attribute("baths", HouseDescription.class),new QueryLess()));
	critiques.add(new CritiqueOption("Change Area",new Attribute("area", HouseDescription.class),new jcolibri.method.retrieve.FilterBasedRetrieval.predicates.Equal()));
	critiques.add(new CritiqueOption("Another Area",new Attribute("area", HouseDescription.class),new NotEqual()));
    }

    public void cycle(CBRQuery query) throws ExecutionException
    {	
	// Obtain query with form filling
	ObtainQueryWithFormMethod.obtainQueryWithoutInitialValues(query,hiddenAtts,labels);

	// Jump to main conversation
	sequence1(query, new FilterConfig());

    }
    
    
    public void sequence1(CBRQuery query, FilterConfig filterConfig)  throws ExecutionException
    {	
	// Execute Filter
	Collection<CBRCase> filtered = FilterBasedRetrievalMethod.filterCases(_caseBase.getCases(), query, filterConfig);
	
	// Execute NN
	Collection<RetrievalResult> retrievedCases = NNScoringMethod.evaluateSimilarity(filtered, query, simConfig);
	
	// Select cases
	Collection<CBRCase> selectedCases = SelectCases.selectTopK(retrievedCases, 10);
    	
	// Obtain critizied query
	CriticalUserChoice choice = DisplayCasesTableWithCritiquesMethod.displayCasesInTableWithCritiques(selectedCases, critiques, _caseBase.getCases());
	
	if(ContinueOrFinish.continueOrFinish(choice))
	    sequence2(choice.getSelectedCaseAsQuery(), choice);
	else
	    sequence3(choice, selectedCases);
    }
    
    public void sequence2(CBRQuery query, CriticalUserChoice cuc) throws ExecutionException
    {
	// Replaze current query with the critizied one
	MoreLikeThis.moreLikeThis(query, cuc.getSelectedCase());
	sequence1(query, cuc.getFilterConfig());
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
	StandardCBRApplication recommender = new Houses6();
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
	    org.apache.commons.logging.LogFactory.getLog(Houses6.class).error(e);
	    
	}
	

    }

}
