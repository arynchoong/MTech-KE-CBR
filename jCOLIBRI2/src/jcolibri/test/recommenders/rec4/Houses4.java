/**
 * Houses4.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 23/10/2007
 */
package jcolibri.test.recommenders.rec4;

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
import jcolibri.extensions.recommendation.conditionals.DisplayCasesIfNumber;
import jcolibri.extensions.recommendation.navigationByAsking.InformationGain;
import jcolibri.extensions.recommendation.navigationByAsking.ObtainQueryWithAttributeQuestionMethod;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterBasedRetrievalMethod;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterConfig;
import jcolibri.method.retrieve.FilterBasedRetrieval.predicates.Equal;
import jcolibri.test.recommenders.rec4.housesData.HouseDescription;

/**
 * Conversational (type B) flats recommender using Navigation by Asking and Filter retrieval.
 * <br>
 * This recommender applies the Navigation by Asking strategy to obtain the user requirements. 
 * This strategy selects an attribute of the items to be asked to the user each iteration. Depending on
 * the values of these attributes a retrieval set is obtained using filtering. 
 * If the retrieval set is small enough it is presented to the user. If it is too big or
 * the user does not find the desired item, the recommender uses again the Navigation by
 * Asking strategy to improve the user requirements.
 * <br>Summary:
 * <ul>
 * <li>Type: Conversational B.
 * <li>Case base: houses2 (price moved to the solution and priceRange in the description).
 * <li>One off Preference Elicitation: Navigation by Asking using Information Gain.
 * <li>Retrieval: Filter (in NbA only Equal() predicates are allowed).
 * <li>Display Condition: number of cases (not showing messages).
 * <li>Display: In table. "Edit Query" enabled if there are more attributes to ask.
 * <li>Iterated Preference Elicitation: Navigation by Asking using Information Gain.
 * </ul>
 * This recommender implements the following template:<br>
 * <center><img src="../Template4_Cycle.jpg"/></center>
 * 
 * <br>Read the documentation of the recommenders extension for details about templates
 * and recommender strategies: {@link jcolibri.extensions.recommendation}
 * 
 * @see jcolibri.extensions.recommendation.navigationByAsking.ObtainQueryWithAttributeQuestionMethod
 * @see jcolibri.extensions.recommendation.navigationByAsking.InformationGain
 * @see jcolibri.method.retrieve.FilterBasedRetrieval.FilterBasedRetrievalMethod
 * @see jcolibri.extensions.recommendation.conditionals.DisplayCasesIfNumber
 * @see jcolibri.extensions.recommendation.casesDisplay.DisplayCasesTableMethod
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 */
public class Houses4 implements StandardCBRApplication
{
    /** Connector object */
    Connector _connector;
    /** CaseBase object */
    CBRCaseBase _caseBase;

    /** Configuration object for Filter based retrieval */
    FilterConfig filterConfig;
    /** Configuration object for Attribute Question */
    Map<Attribute,String> labels;
        
    public void configure() throws ExecutionException
    {
	// Create a data base connector
	_connector = new PlainTextConnector();
	// Init the ddbb connector with the config file
	_connector.initFromXMLfile(jcolibri.util.FileIO
			.findFile("jcolibri/test/recommenders/rec4/housesData/plaintextconfig.xml"));
	// Create a Lineal case base for in-memory organization
	_caseBase = new LinealCaseBase();
	
	//Configure the Filter
	filterConfig = new FilterConfig();
	filterConfig.addPredicate(new Attribute("area", HouseDescription.class), new Equal());
	filterConfig.addPredicate(new Attribute("beds", HouseDescription.class), new Equal());
	filterConfig.addPredicate(new Attribute("priceRange", HouseDescription.class), new Equal());
	filterConfig.addPredicate(new Attribute("furnished", HouseDescription.class), new Equal());
	filterConfig.addPredicate(new Attribute("type", HouseDescription.class), new Equal());
	filterConfig.addPredicate(new Attribute("baths", HouseDescription.class), new Equal());
	
	//Configure labels for attribute question
	labels = new HashMap<Attribute,String>();
    }

    public void cycle(CBRQuery query) throws ExecutionException
    {	
	// Select attribute
	Collection<CBRCase> workingCases = _caseBase.getCases();
	Attribute att = InformationGain.getMoreIGattribute(workingCases,true, _caseBase.getCases());
	// If there are not more attributes to ask, the method resturn null.
	// And the ObtainQueryWithAttributeQuestion method receives that null and shows nothing.
	// In that case, ObtainQueryWithAttributeQuestion returns false;
	
	// Ask for the attribute
	boolean _continue = ObtainQueryWithAttributeQuestionMethod.obtainQueryWithAttributeQuestion(query, att, labels, workingCases);

	sequence1(query, _continue);
    }
    
    
    public void sequence1(CBRQuery query, boolean _continue)  throws ExecutionException
    {
	// Retrieve cases
	Collection<CBRCase> workingCases = FilterBasedRetrievalMethod.filterCases(_caseBase.getCases(), query, filterConfig);
    	
	// Display?
	if(DisplayCasesIfNumber.displayCases(50, 1, workingCases,false))
	    sequence2(query, workingCases, _continue);
	else
	    sequence3(query, workingCases);
	

    }
    
    public void sequence2(CBRQuery query, Collection<CBRCase> workingCases, boolean _continue)  throws ExecutionException
    {
	// Display in cases. The chosen method depends on the AttributeSelection algorithm.
	UserChoice choice;
	if(_continue)
	    choice = DisplayCasesTableMethod.displayCasesInTableEditQuery(workingCases);	
	else
	    choice = DisplayCasesTableMethod.displayCasesInTableBasic(workingCases);	
	

	if(ContinueOrFinish.continueOrFinish(choice))
	    sequence3(query,workingCases);
	else
	    sequence4(choice, workingCases);
    }
    
    public void sequence3(CBRQuery query, Collection<CBRCase> workingCases)  throws ExecutionException
    {
	//Select attribute
	Attribute att = InformationGain.getMoreIGattribute(workingCases,false, _caseBase.getCases());
	
	//Ask attribute
	boolean _continue = ObtainQueryWithAttributeQuestionMethod.obtainQueryWithAttributeQuestion(query, att, labels,workingCases);

	sequence1(query, _continue);
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
	StandardCBRApplication recommender = new Houses4();
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
	    org.apache.commons.logging.LogFactory.getLog(Houses4.class).error(e);
	    
	}
	

    }

}
