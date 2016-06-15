package jcolibri.test.test15;

import jcolibri.casebase.CachedLinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.PlainTextConnector;
import jcolibri.evaluation.Evaluator;
import jcolibri.exception.ExecutionException;
import jcolibri.extensions.maintenance_evaluation.DetailedEvaluationReport;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.reuse.classification.KNNClassificationConfig;
import jcolibri.method.reuse.classification.SimilarityWeightedVotingMethod;
import jcolibri.method.revise.classification.BasicClassificationOracle;
import jcolibri.method.revise.classification.ClassificationOracle;

import org.apache.commons.logging.Log;

/**
 * Evaluable application. It is a normal StandardCBRApplication that 
 * stores its results in the DetailedEvaluationReport 
 * object obtained from Evaluator.getEvaluationReport().
 * @author Lisa Cummins
 * @version 1.0
 */
public class IrisEvaluableApp implements StandardCBRApplication 
{
	Connector _connector;
	CBRCaseBase _caseBase;
	KNNClassificationConfig irisSimConfig;
	
	private Log log;
	
	/**
	 * The name of the data series containing this application's stored results
	 */
	public static final String DATA_SERIES_NAME = "Iris Prediction Cost";

	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#configure()
	 */
	public void configure() throws ExecutionException
	{	try
		{	_connector = new PlainTextConnector();
			_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("jcolibri/test/test15/plaintextconfig.xml"));
			_caseBase  = new CachedLinealCaseBase();
			
			// Configure KNN
			irisSimConfig = new KNNClassificationConfig();
			
			irisSimConfig.setDescriptionSimFunction(new Average());
			irisSimConfig.addMapping(new Attribute("sepalLength",IrisDescription.class), new Interval(3.6));
			irisSimConfig.addMapping(new Attribute("sepalWidth",IrisDescription.class), new Interval(2.4));
			irisSimConfig.addMapping(new Attribute("petalLength",IrisDescription.class), new Interval(5.9));
			irisSimConfig.addMapping(new Attribute("petalWidth", IrisDescription.class), new Interval(2.4));
			irisSimConfig.setClassificationMethod(new SimilarityWeightedVotingMethod());
			irisSimConfig.setK(3);

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
	{   	_connector.close();
	}
}