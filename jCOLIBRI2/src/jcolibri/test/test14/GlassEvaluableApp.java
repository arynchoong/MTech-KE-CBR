package jcolibri.test.test14;

import jcolibri.casebase.CachedLinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.PlainTextConnector;
import jcolibri.evaluation.Evaluator;
import jcolibri.exception.ExecutionException;
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
public class GlassEvaluableApp implements StandardCBRApplication 
{
	Connector _connector;
	CBRCaseBase _caseBase;
	KNNClassificationConfig glassSimConfig;
	
	private Log log;
	
	/**
	 * The name of the data series containing this application's stored results
	 */
	public static final String DATA_SERIES_NAME = "Glass Prediction Cost";
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#configure()
	 */
	public void configure() throws ExecutionException
	{	try
		{	_connector = new PlainTextConnector();
			_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("jcolibri/test/test14/plaintextconfigGlass.xml"));
			_caseBase  = new CachedLinealCaseBase();
	
			// Configure KNN
			glassSimConfig = new KNNClassificationConfig();
			
			glassSimConfig.setDescriptionSimFunction(new Average());
			glassSimConfig.addMapping(new Attribute("ri",GlassDescription.class), new Interval(0.02278));
			glassSimConfig.addMapping(new Attribute("na",GlassDescription.class), new Interval(6.65));
			glassSimConfig.addMapping(new Attribute("mg",GlassDescription.class), new Interval(4.49));
			glassSimConfig.addMapping(new Attribute("al",GlassDescription.class), new Interval(3.21));
			glassSimConfig.addMapping(new Attribute("si",GlassDescription.class), new Interval(5.6));
			glassSimConfig.addMapping(new Attribute("k",GlassDescription.class), new Interval(6.21));
			glassSimConfig.addMapping(new Attribute("ca",GlassDescription.class), new Interval(10.76));
			glassSimConfig.addMapping(new Attribute("ba",GlassDescription.class), new Interval(3.15));
			glassSimConfig.addMapping(new Attribute("fe",GlassDescription.class), new Interval(0.51));
			glassSimConfig.setClassificationMethod(new SimilarityWeightedVotingMethod());
			glassSimConfig.setK(3);
			
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
		double predictionCost = oracle.getPredictionCost(query, _caseBase, glassSimConfig);
		
		// Now we add the cost of the prediction to the series "Glass Prediction Cost".
		Evaluator.getEvaluationReport().addDataToSeries(DATA_SERIES_NAME, predictionCost);
	}

	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#postCycle()
	 */
	public void postCycle() throws ExecutionException 
	{    	_connector.close();
	}
}