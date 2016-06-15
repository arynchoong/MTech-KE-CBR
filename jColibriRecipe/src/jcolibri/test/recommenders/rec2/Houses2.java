/**
 * Houses2.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 23/10/2007
 */
package jcolibri.test.recommenders.rec2;

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
import jcolibri.method.gui.formFilling.ObtainQueryWithFormMethod;
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
 * Conversational (type A) flats recommender using form-filling, Nearest Neighbour retrieval and top k selection .
 * <br>
 * This recommender obtains the user preferences using a form. Then it computes 
 * Nearest Neighbour scoring to obtain the most similar cases. 
 * If the user does not find the desired item, he/she can refine the requirements
 * using again a form. The form contains initial values and some attributes are
 * hidden (defined by the designer).
 * <br>Summary:
 * <ul>
 * <li>Type: Conversational A
 * <li>Case base: houses
 * <li>One off Preference Elicitation: Form Filling with initial values, hidden attributes and custom labels
 * <li>Retrieval: Nearest Neighbour + topKselection
 * <li>Display: In table
 * <li>Iterated Preference Elecitiation: Form Filling with initial values, hidden attributes and custom labels
 * </ul>
 * This recommender implements the following template:<br>
 * <center><img src="../Template2_Cycle.jpg"/></center>
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
 */
public class Houses2 implements StandardCBRApplication
{
    /** Connector object */
    Connector _connector;
    /** CaseBase object */
    CBRCaseBase _caseBase;

    /** KNN Config */
    NNConfig simConfig;
    
    /** Configuration objects for obtain query */
    Collection<Attribute> hiddenAtts;
    /** Configuration objects for obtain query */
    Map<Attribute,String> labels;
    
    public void configure() throws ExecutionException
    {
	// Create a data base connector
	_connector = new PlainTextConnector();
	// Init the ddbb connector with the config file
	_connector.initFromXMLfile(jcolibri.util.FileIO
			.findFile("jcolibri/test/recommenders/housesData/plaintextconfig.xml"));
	// Create a Lineal case base for in-memory organization
	_caseBase = new LinealCaseBase();

	//Configure the KNN
	simConfig = new NNConfig();
	// Set the average() global similarity function for the description of the case
	simConfig.setDescriptionSimFunction(new Average());
	simConfig.addMapping(new Attribute("area", HouseDescription.class), new Table("jcolibri/test/recommenders/housesData/area.csv"));
	simConfig.addMapping(new Attribute("beds", HouseDescription.class), new McSherryMoreIsBetter(0,0));
	simConfig.addMapping(new Attribute("price", HouseDescription.class), new InrecaLessIsBetter(2000, 0.5));
	simConfig.addMapping(new Attribute("furnished", HouseDescription.class), new Equal());
	simConfig.addMapping(new Attribute("type", HouseDescription.class), new Equal());
	simConfig.addMapping(new Attribute("baths", HouseDescription.class), new InrecaMoreIsBetter(0.5));

	
	// Configuration objects for obtain query
	hiddenAtts = new ArrayList<Attribute>();
	hiddenAtts.add(new Attribute("beds", HouseDescription.class));
	labels = new HashMap<Attribute,String>();
	labels.put(new Attribute("price", HouseDescription.class), "Max price");
	labels.put(new Attribute("baths", HouseDescription.class), "Min bahtrooms");
    }

    public void cycle(CBRQuery query) throws ExecutionException
    {	
	//Obtain the query
	ObtainQueryWithFormMethod.obtainQueryWithInitialValues(query,hiddenAtts,labels);

	//Jump to converstaion cycle
	sequence1(query);
    }
    
    
    public void sequence1(CBRQuery query)
    {
	// Execute KNN
	Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
	
	// Select cases
	Collection<CBRCase> retrievedCases = SelectCases.selectTopK(eval, 5);
    	
	// Display cases
	UserChoice choice = DisplayCasesTableMethod.displayCasesInTableEditQuery(retrievedCases);	

	// Continue or Finish
	if(ContinueOrFinish.continueOrFinish(choice))
	    sequence2(query);
	else
	    sequence3(choice, retrievedCases);
    }
    
    public void sequence2(CBRQuery query)
    {
	// Obtain query again
	ObtainQueryWithFormMethod.obtainQueryWithInitialValues(query,hiddenAtts,labels);
	
	// Jump to conversation cycle
	sequence1(query);
    }
    
    
    public void sequence3(UserChoice choice, Collection<CBRCase> retrievedCases)
    {
	// Finishing: buy or quit
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
	StandardCBRApplication recommender = new Houses2();
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
	    org.apache.commons.logging.LogFactory.getLog(Houses2.class).error(e);
	    
	}
	

    }

}
