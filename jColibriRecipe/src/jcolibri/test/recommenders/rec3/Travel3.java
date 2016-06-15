/**
 * Travel3.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 23/10/2007
 */
package jcolibri.test.recommenders.rec3;

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
import jcolibri.connector.DataBaseConnector;
import jcolibri.datatypes.Instance;
import jcolibri.exception.ExecutionException;
import jcolibri.extensions.recommendation.casesDisplay.DisplayCasesTableMethod;
import jcolibri.extensions.recommendation.casesDisplay.UserChoice;
import jcolibri.extensions.recommendation.conditionals.BuyOrQuit;
import jcolibri.extensions.recommendation.conditionals.ContinueOrFinish;
import jcolibri.extensions.recommendation.conditionals.DisplayCasesIfNumber;
import jcolibri.method.gui.formFilling.ObtainQueryWithFormMethod;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterBasedRetrievalMethod;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterConfig;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.Equal;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.OntologyCompatible;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.QueryLessOrEqual;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.QueryMoreOrEqual;
import jcolibri.test.recommenders.travelData.TravelDescription;
import jcolibri.test.recommenders.travelData.TravelDescription.AccommodationTypes;
import jcolibri.test.recommenders.travelData.TravelDescription.Seasons;
import jcolibri.util.FileIO;
import es.ucm.fdi.gaia.ontobridge.OntoBridge;
import es.ucm.fdi.gaia.ontobridge.OntologyDocument;

/**
 * Conversational (type B) trips recommender using form-filling and Filter-Based retrieval.
 * <br>
 * This recommender obtains the user preferences through a form. Then, it performs the 
 * retrieval filtering the items that obbey the user preferences. If the retrieval set
 * is small enough, items are shown to the user. If the retrieval set is too big or 
 * the user does not find the desired item, the system presents again a form to modify
 * the user requirements. The form has some initial values and custom labels. 
 * <ul>
 * <li>Type: Conversational B
 * <li>Case base: travel
 * <li>One off Preference Elicitation: Form Filling with initial values and custom labels
 * <li>Retrieval: Filter
 * <li>Display Condition: number of cases (showing messages)
 * <li>Display:  In table with "Edit Query" enabled
 * <li>Iterated Preference Elicitation: Form Filling with initial values and custom labels
 * </ul>
 * This recommender implements the following template:<br>
 * <center><img src="../Template3_Cycle.jpg"/></center>
 * 
 * <br>Read the documentation of the recommenders extension for details about templates
 * and recommender strategies: {@link jcolibri.extensions.recommendation}
 * 
 * @see jcolibri.method.gui.formFilling.ObtainQueryWithFormMethod
 * @see jcolibri.method.retrieve.FilterBasedRetrieval.FilterBasedRetrievalMethod
 * @see jcolibri.extensions.recommendation.conditionals.DisplayCasesIfNumber
 * @see jcolibri.extensions.recommendation.casesDisplay.DisplayCasesTableMethod
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 *
 */
public class Travel3 implements StandardCBRApplication
{
    /** Connector object */
    Connector _connector;
    /** CaseBase object */
    CBRCaseBase _caseBase;

    /** Configuration object for Form Filling */
    Map<Attribute,String> labels;
    /** Configuration object for Form Filling */
    ArrayList<Attribute> hiddenAtts;
    /** Configuration object for Filter Based retrieval*/
    FilterConfig filterConfig;
    
    public void configure() throws ExecutionException
    {
	//Emulate data base server
	jcolibri.test.database.HSQLDBserver.init();
	
	// Create a data base connector
	_connector = new DataBaseConnector();
	// Init the ddbb connector with the config file
	_connector.initFromXMLfile(jcolibri.util.FileIO
			.findFile("jcolibri/test/recommenders/travelData/databaseconfig.xml"));
	// Create a Lineal case base for in-memory organization
	_caseBase = new LinealCaseBase();
	
	// Obtain a reference to OntoBridge
	OntoBridge ob = jcolibri.util.OntoBridgeSingleton.getOntoBridge();
	// Configure it to work with the Pellet reasoner
	ob.initWithPelletReasoner();
	// Setup the main ontology
	OntologyDocument mainOnto = new OntologyDocument("http://gaia.fdi.ucm.es/ontologies/travel-destinations.owl", 
							 FileIO.findFile("jcolibri/test/recommenders/travelData/travel-destinations.owl").toExternalForm());
	// There are not subontologies
	ArrayList<OntologyDocument> subOntologies = new ArrayList<OntologyDocument>();
	// Load the ontology
	ob.loadOntology(mainOnto, subOntologies, false);
	
	hiddenAtts = new ArrayList<Attribute>();
	labels = new HashMap<Attribute,String>();
	labels.put(new Attribute("Duration", TravelDescription.class), "Min duration");
	labels.put(new Attribute("Accommodation", TravelDescription.class), "Min accommodation");
	labels.put(new Attribute("Price", TravelDescription.class), "Max price");
	
	//Configure the Filter
	filterConfig = new FilterConfig();
	filterConfig.addPredicate(new Attribute("HolidayType", TravelDescription.class), new Equal());
	filterConfig.addPredicate(new Attribute("NumberOfPersons", TravelDescription.class), new Equal());
	filterConfig.addPredicate(new Attribute("Region", TravelDescription.class), new OntologyCompatible());
	filterConfig.addPredicate(new Attribute("Transportation", TravelDescription.class), new Equal());
	filterConfig.addPredicate(new Attribute("Duration", TravelDescription.class), new QueryLessOrEqual());
	filterConfig.addPredicate(new Attribute("Season", TravelDescription.class), new Equal());
	filterConfig.addPredicate(new Attribute("Accommodation", TravelDescription.class), new QueryLessOrEqual());
	filterConfig.addPredicate(new Attribute("Price", TravelDescription.class), new QueryMoreOrEqual());
    }

    public void cycle(CBRQuery query) throws ExecutionException
    {	
	// Obtain the query
	ObtainQueryWithFormMethod.obtainQueryWithInitialValues(query,hiddenAtts,labels);


	// Jump to the conversation cycle
	sequence1(query);
    }
    
    
    public void sequence1(CBRQuery query)
    {
	// Retrieve using filter based retrieval
	Collection<CBRCase> retrievedCases = FilterBasedRetrievalMethod.filterCases(_caseBase.getCases(), query, filterConfig);
    	
	// Display condition based on the number of cases.
	if(DisplayCasesIfNumber.displayCases(50, 1, retrievedCases,true))
	    sequence2(query, retrievedCases);
	else
	    sequence3(query);
    }
    
    public void sequence2(CBRQuery query, Collection<CBRCase> retrievedCases)
    {
	// Display cases in table
	UserChoice choice = DisplayCasesTableMethod.displayCasesInTableEditQuery(retrievedCases);		

	if(ContinueOrFinish.continueOrFinish(choice))
	    sequence3(query);
	else
	    sequence4(choice, retrievedCases);
    }
    
    
    public void sequence3(CBRQuery query)
    {
	// Refine query and back to the main sequence
	ObtainQueryWithFormMethod.obtainQueryWithInitialValues(query,hiddenAtts,labels);

	sequence1(query);
    }
    
    public void sequence4(UserChoice choice, Collection<CBRCase> retrievedCases)
    {
	if(BuyOrQuit.buyOrQuit(choice))
	    System.out.println("Finish - User Buys: "+choice.getSelectedCase());
	
	else
	    System.out.println("Finish - User Quits");
    }

    public void postCycle() throws ExecutionException
    {
	_connector.close();
	jcolibri.test.database.HSQLDBserver.shutDown();
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
	StandardCBRApplication recommender = new Travel3();
	try
	{
	    recommender.configure();
	    
	    recommender.preCycle();
	    
	    CBRQuery query = new CBRQuery();
	    
	    TravelDescription td = new TravelDescription();
	    td.setAccommodation(AccommodationTypes.ThreeStars);
	    td.setDuration(7);
	    td.setHolidayType("bathing");
	    td.setNumberOfPersons(2);
	    td.setRegion(new Instance("Spain"));
	    td.setSeason(Seasons.July);
	    td.setTransportation("plane");
	    td.setPrice(1000);

	    query.setDescription(td);
	        
	    recommender.cycle(query);
	    
	    recommender.postCycle();
	    
	    System.exit(0);
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(Travel3.class).error(e);
	    
	}
	

    }

}
