/**
 * Test7.java
 * jCOLIBRI2 framework. 
 * @author Lisa Cummins
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 03/05/2007
 */
package jcolibri.test.test7;

import java.util.Collection;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.PlainTextConnector;
import jcolibri.exception.ExecutionException;
import jcolibri.method.maintenance.TwoStepCaseBaseEditMethod;
import jcolibri.method.maintenance.algorithms.ICFFull;
import jcolibri.method.maintenance.algorithms.ICFRedundancyRemoval;
import jcolibri.method.maintenance.algorithms.RENNNoiseReduction;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.reuse.classification.KNNClassificationConfig;
import jcolibri.method.reuse.classification.SimilarityWeightedVotingMethod;
import jcolibri.test.main.SwingProgressBar;

/**
 * This example shows how to run maintenance algorithms over the case base.
 * The "class" of the case is defined by the id attribute of the solution. 
 * @author Lisa Cummins
 * @version 1.0
 */
public class Test7 implements StandardCBRApplication {

	Connector _connector;
	CBRCaseBase _caseBase;
	
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.StandardCBRApplication#configure()
	 */
	public void configure() throws ExecutionException {
		try{
			_connector = new PlainTextConnector();
			_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("jcolibri/test/test7/plaintextconfig.xml"));
			_caseBase  = new LinealCaseBase();
			} catch (Exception e){
				throw new ExecutionException(e);
		}

	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.StandardCBRApplication#preCycle()
	 */
	public CBRCaseBase preCycle() throws ExecutionException {
		_caseBase.init(_connector);
		return _caseBase;
	}

	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.StandardCBRApplication#cycle()
	 */
	public void cycle(CBRQuery q) throws ExecutionException {
		
		// Configure KNN
		KNNClassificationConfig irisSimConfig = new KNNClassificationConfig();
		
		irisSimConfig.setDescriptionSimFunction(new Average());
		irisSimConfig.addMapping(new Attribute("sepalLength",IrisDescription.class), new Interval(3.6));
		irisSimConfig.addMapping(new Attribute("sepalWidth",IrisDescription.class), new Interval(2.4));
		irisSimConfig.addMapping(new Attribute("petalLength",IrisDescription.class), new Interval(5.9));
		irisSimConfig.addMapping(new Attribute("petalWidth", IrisDescription.class), new Interval(2.4));
		irisSimConfig.setClassificationMethod(new SimilarityWeightedVotingMethod());
		irisSimConfig.setK(3);

		// Run a 2 step Maintenance method
		TwoStepCaseBaseEditMethod edit = new ICFFull(new RENNNoiseReduction(), new ICFRedundancyRemoval());		
		Collection<CBRCase> deleted = edit.retrieveCasesToDelete(_caseBase.getCases(), irisSimConfig);
		
		System.out.println();		
		System.out.println("Num Cases deleted by Alg: " + deleted.size());
		System.out.println("Cases deleted by Alg: ");
		for(CBRCase c: deleted)
		{	System.out.println(c.getID());
		}
	}

	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.StandardCBRApplication#postCycle()
	 */
	public void postCycle() throws ExecutionException {
		_connector.close();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Test7 test = new Test7();

		//SwingProgressBar shows the progress
		jcolibri.util.ProgressController.clear();
		jcolibri.util.ProgressController.register(new SwingProgressBar(),RENNNoiseReduction.class);
		jcolibri.util.ProgressController.register(new SwingProgressBar(),ICFRedundancyRemoval.class);

		
		try {
			test.configure();
			test.preCycle();
			test.cycle(null);
		} catch (ExecutionException e) {
			org.apache.commons.logging.LogFactory.getLog(Test7.class).error(e);
		}

	}

}
