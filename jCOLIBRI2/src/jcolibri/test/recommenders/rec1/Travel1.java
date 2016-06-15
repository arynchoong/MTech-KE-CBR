/**
 * Travel1.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 23/10/2007
 */
package jcolibri.test.recommenders.rec1;

import java.util.ArrayList;
import java.util.Collection;

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
import jcolibri.method.gui.formFilling.ObtainQueryWithFormMethod;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.EnumCyclicDistance;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Threshold;
import jcolibri.method.retrieve.NNretrieval.similarity.local.ontology.OntCosine;
import jcolibri.method.retrieve.NNretrieval.similarity.local.recommenders.InrecaLessIsBetter;
import jcolibri.method.retrieve.NNretrieval.similarity.local.recommenders.McSherryMoreIsBetter;
import jcolibri.method.retrieve.selection.SelectCases;
import jcolibri.test.recommenders.travelData.TravelDescription;
import jcolibri.test.recommenders.travelData.TravelDescription.AccommodationTypes;
import jcolibri.test.recommenders.travelData.TravelDescription.Seasons;
import jcolibri.util.FileIO;
import es.ucm.fdi.gaia.ontobridge.OntoBridge;
import es.ucm.fdi.gaia.ontobridge.OntologyDocument;

/**
 * Simple Single-Shot flats recommender using form-filling, Nearest Neighbour retrieval and top k selection .
 * <br>
 * This recommender allows user to define his/her requirements using a form. 
 * Then, it retrieves the k most similar cases using Nearest Neighbour retrieval.
 * These k cases are displayed to the user in a table and the system finishes.
 * <br>Summary:
 * <ul>
 * <li>Type: Single-Shot
 * <li>Case base: travel
 * <li>One off Preference Elicitation: Form Filling with initial values
 * <li>Retrieval:  NN + topKselection
 * <li>Display: In table
 * </ul>
 * This recommender implements the following template:<br>
 * <center><img src="../Template1_Cycle.jpg"/></center>
 * 
 * <br>Read the documentation of the recommenders extension for details about templates
 * and recommender strategies: {@link jcolibri.extensions.recommendation}
 * 
 * @see jcolibri.method.gui.formFilling.ObtainQueryWithFormMethod
 * @see jcolibri.method.retrieve.NNretrieval.NNScoringMethod
 * @see jcolibri.method.retrieve.selection.SelectCases
 * @see jcolibri.extensions.recommendation.casesDisplay.DisplayCasesTableMethod
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 *
 */
public class Travel1 implements StandardCBRApplication
{
    /** Connector object */
    Connector _connector;
    /** CaseBase object */
    CBRCaseBase _caseBase;

    NNConfig simConfig;
    
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
	
	
	// Now configure the KNN
	simConfig = new NNConfig();
	// Set the average() global similarity function for the description of the case
	simConfig.setDescriptionSimFunction(new Average());
	// HolidayType --> equal()
	simConfig.addMapping(new Attribute("HolidayType", TravelDescription.class), new Equal());
	// NumberOfPersons --> Threshold(2)
	simConfig.addMapping(new Attribute("NumberOfPersons", TravelDescription.class), new Threshold(2));
	// Region --> ontCosine()
	simConfig.addMapping(new Attribute("Region", TravelDescription.class), new OntCosine());
	// Transportation --> equal()
	simConfig.addMapping(new Attribute("Transportation", TravelDescription.class), new Equal());
	// Duration --> interval(31)
	simConfig.addMapping(new Attribute("Duration", TravelDescription.class), new Interval(31));
	// Season --> EnumCyclicDistance
	simConfig.addMapping(new Attribute("Season", TravelDescription.class), new EnumCyclicDistance());
	// Accomodation --> McSherryMoreIsBetter
	simConfig.addMapping(new Attribute("Accommodation", TravelDescription.class), new McSherryMoreIsBetter(0,0));
	// Price --> InrecaLessIsBetter(4000)
	simConfig.addMapping(new Attribute("Price", TravelDescription.class), new InrecaLessIsBetter(4000, 0.5));
	
	

    }

    public void cycle(CBRQuery query) throws ExecutionException
    {
	//Obtain query
	ObtainQueryWithFormMethod.obtainQueryWithInitialValues(query,null,null);
	
	// Execute KNN
	Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
	
	// Select cases
	Collection<CBRCase> retrievedCases = SelectCases.selectTopK(eval, 5);
	
	// Display cases
	UserChoice choice = DisplayCasesTableMethod.displayCasesInTableBasic(retrievedCases);

	// Buy or Quit?
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
	StandardCBRApplication recommender = new Travel1();
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
	    org.apache.commons.logging.LogFactory.getLog(Travel1.class).error(e);
	    
	}
	

    }

}
