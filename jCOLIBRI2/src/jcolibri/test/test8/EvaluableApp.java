/**
 * EvaluableApp.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 11/05/2007
 */
package jcolibri.test.test8;

import java.util.Collection;

import jcolibri.casebase.CachedLinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.DataBaseConnector;
import jcolibri.evaluation.Evaluator;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;

import org.apache.commons.logging.Log;

/**
 * Evaluable application. It is a normal StandardCBRApplication that stores its results in the EvaluationReport object
 * obtained from Evaluator.getEvaluationReport()
 * @author Juan A. Recio-Garci
 * @version 1.0
 * 
 * @see jcolibri.evaluation.Evaluator
 * @see jcolibri.evaluation.EvaluationReport
 *
 */
public class EvaluableApp implements StandardCBRApplication {

	Connector _connector;
	CBRCaseBase _caseBase;
	
	private Log log;
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#configure()
	 */
	public void configure() throws ExecutionException{
		try{
		_connector = new DataBaseConnector();
		_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("jcolibri/test/test8/databaseconfig.xml"));
		_caseBase  = new CachedLinealCaseBase();
		} catch (Exception e){
			throw new ExecutionException(e);
		}
		log = org.apache.commons.logging.LogFactory.getLog(this.getClass());

	}

	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#preCycle()
	 */
	public CBRCaseBase preCycle() throws ExecutionException {
		_caseBase.init(_connector);		
		java.util.Collection<CBRCase> cases = _caseBase.getCases();
		for(CBRCase c: cases)
			System.out.println(c);
		return _caseBase;
	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#cycle()
	 */
	public void cycle(CBRQuery query) throws ExecutionException 
	{		
		NNConfig simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		simConfig.addMapping(new Attribute("Accomodation", TravelDescription.class), new Equal());
		Attribute duration = new Attribute("Duration", TravelDescription.class);
		simConfig.addMapping(duration, new Interval(31));
		simConfig.setWeight(duration, 0.5);
		simConfig.addMapping(new Attribute("HolidayType", TravelDescription.class), new Equal());
		simConfig.addMapping(new Attribute("NumberOfPersons", TravelDescription.class), new Equal());
		simConfig.addMapping(new Attribute("Price", TravelDescription.class), new Interval(4000));
		
		log.info("Query: "+ query.getDescription());

		
		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
		
		// Now we add the similarity of the most similar case to the serie "Similarity".
		Evaluator.getEvaluationReport().addDataToSeries("Similarity", new Double(eval.iterator().next().getEval()));
	}

	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#postCycle()
	 */
	public void postCycle() throws ExecutionException {
		this._caseBase.close();

	}

}
