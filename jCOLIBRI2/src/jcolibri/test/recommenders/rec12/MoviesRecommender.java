/**
 * MoviesRecommender.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 11/11/2007
 */
package jcolibri.test.recommenders.rec12;

import java.util.Collection;

import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.PlainTextConnector;
import jcolibri.exception.ExecutionException;
import jcolibri.extensions.recommendation.ContentBasedProfile.ObtainQueryFromProfile;
import jcolibri.extensions.recommendation.casesDisplay.DisplayCasesTableMethod;
import jcolibri.extensions.recommendation.casesDisplay.UserChoice;
import jcolibri.extensions.recommendation.collaborative.CollaborativeRetrievalMethod;
import jcolibri.extensions.recommendation.collaborative.MatrixCaseBase;
import jcolibri.extensions.recommendation.collaborative.PearsonMatrixCaseBase;
import jcolibri.extensions.recommendation.conditionals.BuyOrQuit;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.selection.SelectCases;
import jcolibri.test.recommenders.rec12.moviesDB.Rating;
import jcolibri.test.recommenders.rec12.moviesDB.User;

/**
 * Single-Shot movies recommender obtaining description from profile and scoring cases using collaborative recommendation.
 * <br>
 * This recommender uses a collaborative retrieval algorithm. These collaborative algorithms 
 * return items depending on the recommendations of other users. They require an special organization of the
 * case base to be executed (see {@link jcolibri.extensions.recommendation.collaborative.PearsonMatrixCaseBase}).
 * The query is obtained from a serialized profile following the behaviour of many existing on-line movies recommenders.
 * <br>Summary:
 * <ul>
 * <li>Type: Single-Shot
 * <li>Case base: movies
 * <li>One off Preference Elicitation: Profile
 * <li>Retrieval: Collaborative + topKselection
 * <li>Display: In table (basic)
 * </ul>
 * This recommender implements the following template:<br>
 * <center><img src="../Template1_Cycle.jpg"/></center>
 * 
 * <br>Read the documentation of the recommenders extension for details about templates
 * and recommender strategies: {@link jcolibri.extensions.recommendation}
 * 
 * @see jcolibri.extensions.recommendation.ContentBasedProfile.ObtainQueryFromProfile
 * @see jcolibri.extensions.recommendation.collaborative.CollaborativeRetrievalMethod
 * @see jcolibri.method.retrieve.selection.SelectCases
 * @see jcolibri.extensions.recommendation.casesDisplay.DisplayCasesTableMethod
 * 
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 *
 */
public class MoviesRecommender implements StandardCBRApplication
{
    /** Connector object */
    Connector _connector;
    /** CaseBase object */
    PearsonMatrixCaseBase _caseBase;
    
    /* (non-Javadoc)
     * @see jcolibri.cbraplications.StandardCBRApplication#configure()
     */
    public void configure() throws ExecutionException
    {
	// Create a data base connector
	_connector = new PlainTextConnector();
	// Init the ddbb connector with the config file
	_connector.initFromXMLfile(jcolibri.util.FileIO
			.findFile("jcolibri/test/recommenders/rec12/plaintextconfig.xml"));
	// Create a Lineal case base for in-memory organization
	_caseBase = new PearsonMatrixCaseBase(new Attribute("rating", Rating.class), 20);

    }

    /* (non-Javadoc)
     * @see jcolibri.cbraplications.StandardCBRApplication#cycle(jcolibri.cbrcore.CBRQuery)
     */
    public void cycle(CBRQuery query) throws ExecutionException
    {
	query = ObtainQueryFromProfile.obtainQueryFromProfile( "src/jcolibri/test/recommenders/rec12/profile.xml");
	
	Collection<RetrievalResult> res = CollaborativeRetrievalMethod.getRecommendation(_caseBase, query, 10);
	
	Collection<CBRCase> cases = SelectCases.selectTopK(res, 5);
	
	UserChoice choice = DisplayCasesTableMethod.displayCasesInTableBasic(cases);
	
	// Buy or Quit
	if(BuyOrQuit.buyOrQuit(choice))
	    System.out.println("Finish - User Buys: "+choice.getSelectedCase());
	
	else
	    System.out.println("Finish - User Quits");

    }

    /* (non-Javadoc)
     * @see jcolibri.cbraplications.StandardCBRApplication#postCycle()
     */
    public void postCycle() throws ExecutionException
    {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see jcolibri.cbraplications.StandardCBRApplication#preCycle()
     */
    public CBRCaseBase preCycle() throws ExecutionException
    {
	// Load cases from connector into the case base
	_caseBase.init(_connector);		
	// Print the cases
	java.util.Collection<CBRCase> cases = _caseBase.getCases();
//	for(CBRCase c: cases)
//		System.out.println(c);
	org.apache.commons.logging.LogFactory.getLog(this.getClass()).info(cases.size() +" cases loaded");
	return _caseBase;
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
	//SwingProgressBar shows the progress
	jcolibri.util.ProgressController.clear();
	jcolibri.util.ProgressController.register(new jcolibri.test.main.SwingProgressBar(), MatrixCaseBase.class);

	
	
		StandardCBRApplication recommender = new MoviesRecommender();
		try
		{
		    recommender.configure();
		    
		    recommender.preCycle();
		    
		    CBRQuery query = new CBRQuery();
		    query.setDescription(new User());
		    
		    recommender.cycle(query);
		    
		    recommender.postCycle();
		    
		    //System.exit(0);
		} catch (Exception e)
		{
		    org.apache.commons.logging.LogFactory.getLog(MoviesRecommender.class).error(e);
		    
		}
		

    }

}
