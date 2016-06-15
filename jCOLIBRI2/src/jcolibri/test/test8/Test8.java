/**
 * Test8.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 07/05/2007
 */
package jcolibri.test.test8;

import jcolibri.evaluation.Evaluator;
import jcolibri.evaluation.evaluators.*;


/**
 * This example shows how to evaluate an application.
 * It uses a CBR application (a StandardCBRApplication implementation) that must store its results in the EvaluationReport.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * 
 * @see jcolibri.evaluation.Evaluator
 * @see jcolibri.evaluation.EvaluationReport
 * @see jcolibri.test.test8.EvaluableApp
 *
 */
public class Test8 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    	// Launch DDBB manager
	    	jcolibri.test.database.HSQLDBserver.init();

		//SwingProgressBar shows the progress
	    	jcolibri.util.ProgressController.clear();
		jcolibri.util.ProgressController.register(new jcolibri.test.main.SwingProgressBar(), HoldOutEvaluator.class);
		
		// Example of the Leave-One-Out evaluation
		
		//LeaveOneOutEvaluator eval = new LeaveOneOutEvaluator();
		//eval.init(new EvaluableApp());
		//eval.LeaveOneOut();
		
		// Example of the Hold-Out evaluation
		
		HoldOutEvaluator eval = new HoldOutEvaluator();
		eval.init(new EvaluableApp());
		eval.HoldOut(5, 1);
		
		
		// Example of the Same-Split evaluation
		
		//SameSplitEvaluator eval = new SameSplitEvaluator();
		//eval.init(new EvaluableApp());
		//eval.generateSplit(5, "split1.txt");
		//eval.HoldOutfromFile("split1.txt");

		System.out.println(Evaluator.getEvaluationReport());
		jcolibri.evaluation.tools.EvaluationResultGUI.show(Evaluator.getEvaluationReport(), "Test8 - Evaluation", false);
		
		//Shutdown DDBB manager
	    	jcolibri.test.database.HSQLDBserver.shutDown();

	}

}
