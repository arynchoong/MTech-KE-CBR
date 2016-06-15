/**
 * Houses14.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 23/10/2007
 */
package jcolibri.test.recommenders.rec14;

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
import jcolibri.method.gui.formFilling.ObtainQueryWithFormMethod;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterBasedRetrievalMethod;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterConfig;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.Equal;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.QueryLessOrEqual;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.QueryMoreOrEqual;
import jcolibri.test.recommenders.housesData.HouseDescription;

/**
 * Single-Shot flats recommender using form-filling and Filter-Based retrieval.
 * <br> 
 * This is the typical web recommender. It obtains the user preferences using a form, retrieves filtering the items
 * that match with the query, displays the retrieved items and finishes.
 * <br>Summary:
 * <ul>
 * <li>Type: Single-Shot
 * <li>Case base: houses
 * <li>One off Preference Elicitation: Form Filling without initial values and custom labels
 * <li>Retrieval: Filter
 * <li>Display: In table (basic)
 * </ul>
 * This recommender implements the following template:<br>
 * <center><img src="../Template12_Cycle.jpg"/></center>
 * 
 * <br>Read the documentation of the recommenders extension for details about templates
 * and recommender strategies: {@link jcolibri.extensions.recommendation}
 * 
 * @see jcolibri.method.gui.formFilling.ObtainQueryWithFormMethod
 * @see jcolibri.method.retrieve.FilterBasedRetrieval.FilterBasedRetrievalMethod
 * @see jcolibri.extensions.recommendation.casesDisplay.DisplayCasesTableMethod
 * 
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 *
 */
public class Houses14 implements StandardCBRApplication
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
	// Create a data base connector
	_connector = new PlainTextConnector();
	// Init the ddbb connector with the config file
	_connector.initFromXMLfile(jcolibri.util.FileIO
			.findFile("jcolibri/test/recommenders/housesData/plaintextconfig.xml"));
	// Create a Lineal case base for in-memory organization
	_caseBase = new LinealCaseBase();

	// Configure Form Filling
	hiddenAtts = new ArrayList<Attribute>();
	labels = new HashMap<Attribute,String>();
	labels.put(new Attribute("beds", HouseDescription.class), "Min bedrooms");
	labels.put(new Attribute("price", HouseDescription.class), "Max price");
	labels.put(new Attribute("baths", HouseDescription.class), "Min bahtrooms");
	
	//Configure the Filter based retrieval
	filterConfig = new FilterConfig();
	filterConfig.addPredicate(new Attribute("area", HouseDescription.class), new Equal());
	filterConfig.addPredicate(new Attribute("beds", HouseDescription.class), new QueryLessOrEqual());
	filterConfig.addPredicate(new Attribute("price", HouseDescription.class), new QueryMoreOrEqual());
	filterConfig.addPredicate(new Attribute("furnished", HouseDescription.class), new Equal());
	filterConfig.addPredicate(new Attribute("type", HouseDescription.class), new Equal());
	filterConfig.addPredicate(new Attribute("baths", HouseDescription.class), new QueryLessOrEqual());


    }

    public void cycle(CBRQuery query) throws ExecutionException
    {	
	// Obtain the query
	ObtainQueryWithFormMethod.obtainQueryWithoutInitialValues(query,hiddenAtts,labels);

	// Retrieve using filter based retrieval
	Collection<CBRCase> retrievedCases = FilterBasedRetrievalMethod.filterCases(_caseBase.getCases(), query, filterConfig);
    	
	// Display cases in table
	UserChoice choice = DisplayCasesTableMethod.displayCasesInTableBasic(retrievedCases);
	
	// Buy or Quit
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
	StandardCBRApplication recommender = new Houses14();
	try
	{
	    recommender.configure();
	    
	    recommender.preCycle();
	    
	    CBRQuery query = new CBRQuery();
	    
	    HouseDescription hd = new HouseDescription();
	    /*
	    hd.setArea(HouseDescription.Area.Hampstead);
	    hd.setBaths(1);
	    hd.setBeds(HouseDescription.Beds.two);
	    hd.setFurnished(true);
	    hd.setPrice(500);
	    hd.setType(HouseDescription.Type.Flat);
	    */
	    query.setDescription(hd);
	    
	    recommender.cycle(query);
	    
	    recommender.postCycle();
	    
	    //System.exit(0);
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(Houses14.class).error(e);
	    
	}
	

    }

}
