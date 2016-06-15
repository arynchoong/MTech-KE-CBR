package jcolibri.test.test15;

import jcolibri.casebase.CachedLinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.PlainTextConnector;
import jcolibri.evaluation.Evaluator;
import jcolibri.exception.ExecutionException;
import jcolibri.extensions.maintenance_evaluation.DetailedEvaluationReport;
import jcolibri.method.reuse.classification.KNNClassificationConfig;
import jcolibri.method.revise.classification.BasicClassificationOracle;
import jcolibri.method.revise.classification.ClassificationOracle;

import org.apache.commons.logging.Log;

/**
 * Evaluable application which performs case-based maintenance. It is a normal 
 * StandardCBRApplication that stores its results in the DetailedEvaluationReport 
 * object obtained from Evaluator.getEvaluationReport().
 * @author Lisa Cummins
 * @version 1.0
 */
public class IrisMaintainedEvaluableApp implements StandardCBRApplication 
{
	Connector _connector;
	CBRCaseBase _caseBase;
	KNNClassificationConfig irisSimConfig;
	
	private Log log;
	
	/**
	 * The name of the data series containing this application's stored results
	 */
	public static final String DATA_SERIES_NAME = "RENN Iris Prediction Cost";

	public IrisMaintainedEvaluableApp(KNNClassificationConfig irisSimConfig)
	{	this.irisSimConfig = irisSimConfig;
	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#configure()
	 */
	public void configure() throws ExecutionException
	{	try
		{	_connector = new PlainTextConnector();
			_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("jcolibri/test/test15/plaintextconfig.xml"));
			_caseBase  = new CachedLinealCaseBase();
		
		} catch (Exception e)
		{	throw new ExecutionException(e);
		}
		log = org.apache.commons.logging.LogFactory.getLog(this.getClass());
	}

	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#preCycle()
	 */
	public CBRCaseBase preCycle() throws ExecutionException 
	{	_caseBase.init(_connector);		
		return _caseBase;
	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#cycle()
	 */
	public void cycle(CBRQuery query) throws ExecutionException 
	{	log.info("Query: "+ query.getDescription());

		ClassificationOracle oracle = new BasicClassificationOracle();
		double predictionCost = oracle.getPredictionCost(query, _caseBase, irisSimConfig);
		
		// Now we add the cost of the prediction to the series DATA_SERIES_NAME.
		((DetailedEvaluationReport)(Evaluator.getEvaluationReport())).
			addDataToSeries(DATA_SERIES_NAME, query, predictionCost);
	}

	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#postCycle()
	 */
	public void postCycle() throws ExecutionException 
	{	_connector.close();
	}
}