package jcolibri.test.test14;

import jcolibri.evaluation.Evaluator;
import jcolibri.extensions.maintenance_evaluation.DetailedEvaluationReport;
import jcolibri.extensions.maintenance_evaluation.evaluators.MaintenanceHoldOutEvaluator;


/**
 * This example shows how to evaluate two datasets at the same time 
 * with respect to their accuracy.
 * It uses a CBR application (a StandardCBRApplication implementation) 
 * that must store its results in the DetailedEvaluationReport.
 * 
 * @author Lisa Cummins
 * @version 1.0
 */
public class Test14 
{
	/**
	 * Runs the example to compute accuracy of two datasets. 
	 * @param args
	 */
	public static void main(String[] args) 
	{	
		//SwingProgressBar shows the progress
	    	jcolibri.util.ProgressController.clear();
		jcolibri.util.ProgressController.register(new jcolibri.test.main.SwingProgressBar(), MaintenanceHoldOutEvaluator.class);
		
		// Example of the Hold-Out evaluation
		MaintenanceHoldOutEvaluator eval = new MaintenanceHoldOutEvaluator();
		eval.init(new IrisEvaluableApp());
		eval.HoldOut(20, 3);
		Double avgCost = ((DetailedEvaluationReport)(Evaluator.getEvaluationReport())).
			getAverageOfQueryDataSeries(IrisEvaluableApp.DATA_SERIES_NAME);
		Double percentAccuracy = (1.0 - avgCost) * 100;
		Evaluator.getEvaluationReport().putOtherData(IrisEvaluableApp.DATA_SERIES_NAME + 
			" Accuracy", "" + percentAccuracy);
		
		eval.init(new GlassEvaluableApp());
		eval.HoldOut(20, 3);
		avgCost = ((DetailedEvaluationReport)(Evaluator.getEvaluationReport())).
			getAverageOfDataSeries(GlassEvaluableApp.DATA_SERIES_NAME);
		percentAccuracy = (1.0 - avgCost) * 100;
		Evaluator.getEvaluationReport().putOtherData(GlassEvaluableApp.DATA_SERIES_NAME + 
			" Accuracy", "" + percentAccuracy);
		
		System.out.println(Evaluator.getEvaluationReport());
		jcolibri.evaluation.tools.EvaluationResultGUI.show(Evaluator.getEvaluationReport(), "Test14 - Evaluation",false);
	}
}