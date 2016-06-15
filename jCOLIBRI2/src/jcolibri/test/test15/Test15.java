package jcolibri.test.test15;

import jcolibri.cbrcore.Attribute;
import jcolibri.evaluation.Evaluator;
import jcolibri.evaluation.evaluators.SameSplitEvaluator;
import jcolibri.extensions.maintenance_evaluation.DetailedEvaluationReport;
import jcolibri.extensions.maintenance_evaluation.evaluators.MaintenanceHoldOutEvaluator;
import jcolibri.extensions.maintenance_evaluation.evaluators.MaintenanceSameSplitEvaluator;
import jcolibri.method.maintenance.algorithms.BBNRNoiseReduction;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.reuse.classification.KNNClassificationConfig;
import jcolibri.method.reuse.classification.SimilarityWeightedVotingMethod;

/**
 * This example shows how to evaluate a dataset at the same time 
 * with respect to its accuracy before and after maintenance. The dataset
 * is split, and it is evaluated. Then it is edited by a maintenance algorithm,
 * and reevaluated. The results are stored to file.
 * 
 * It uses a CBR application (a StandardCBRApplication implementation) 
 * that must store its results in the DetailedEvaluationReport.
 * 
 * @author Lisa Cummins
 * @version 1.0
 * 
 */
public class Test15 
{
	/**
	 * Runs the example to compute accuracy before and after a case-base maintenance
	 * algorithm is run on the dataset. 
	 * @param args
	 */
	public static void main(String[] args) 
	{	// Configure KNN
		KNNClassificationConfig irisSimConfig = new KNNClassificationConfig();
		
		irisSimConfig.setDescriptionSimFunction(new Average());
		irisSimConfig.addMapping(new Attribute("sepalLength",IrisDescription.class), new Interval(3.6));
		irisSimConfig.addMapping(new Attribute("sepalWidth",IrisDescription.class), new Interval(2.4));
		irisSimConfig.addMapping(new Attribute("petalLength",IrisDescription.class), new Interval(5.9));
		irisSimConfig.addMapping(new Attribute("petalWidth", IrisDescription.class), new Interval(2.4));
		irisSimConfig.setClassificationMethod(new SimilarityWeightedVotingMethod());
		irisSimConfig.setK(3);
		
		//SwingProgressBar shows the progress
		jcolibri.util.ProgressController.clear();
		jcolibri.util.ProgressController.register(new jcolibri.test.main.SwingProgressBar(), MaintenanceSameSplitEvaluator.class);

		int splitPercent = 30;
		
		// Example of the Same-Split Hold-Out evaluation
		SameSplitEvaluator split = new SameSplitEvaluator();
		split.init(new IrisEvaluableApp());
		split.generateSplit(splitPercent, "split.txt");

		//Run the evaluation on the original dataset
		MaintenanceSameSplitEvaluator eval = new MaintenanceSameSplitEvaluator();
		eval.init(new IrisEvaluableApp());
		eval.HoldOutfromFile("split.txt");
		Double avgCost = ((DetailedEvaluationReport)(Evaluator.getEvaluationReport())).
			getAverageOfQueryDataSeries(IrisEvaluableApp.DATA_SERIES_NAME);
		Double percentAccuracy = (1.0 - avgCost) * 100;
		Evaluator.getEvaluationReport().putOtherData(IrisEvaluableApp.DATA_SERIES_NAME + 
			" Accuracy", "" + percentAccuracy + "%");
		
		// Maintain CB and re-run evaluation
		eval = new MaintenanceSameSplitEvaluator();
		eval.init(new IrisMaintainedEvaluableApp(irisSimConfig), new BBNRNoiseReduction(), irisSimConfig);
		eval.HoldOutfromFile("split.txt");
		avgCost = ((DetailedEvaluationReport)(Evaluator.getEvaluationReport())).
			getAverageOfQueryDataSeries(IrisMaintainedEvaluableApp.DATA_SERIES_NAME);
		percentAccuracy = (1.0 - avgCost) * 100;
		Evaluator.getEvaluationReport().putOtherData(IrisMaintainedEvaluableApp.DATA_SERIES_NAME + 
			" Accuracy", "" + percentAccuracy + "%");
		double avgPercentReduced = ((DetailedEvaluationReport)(Evaluator.getEvaluationReport())).
			getAverageOfDataSeries(MaintenanceHoldOutEvaluator.PERCENT_REDUCED);
		Evaluator.getEvaluationReport().putOtherData(IrisMaintainedEvaluableApp.DATA_SERIES_NAME + 
			" CB Reduction", "" + avgPercentReduced + "%");
		((DetailedEvaluationReport)(Evaluator.getEvaluationReport())).
			removeDataSeries(MaintenanceHoldOutEvaluator.PERCENT_REDUCED);
		
		//Print the results to screen and to file
		System.out.println(Evaluator.getEvaluationReport());
		((DetailedEvaluationReport)(Evaluator.getEvaluationReport())).printDetailedEvaluationReport(
			"results.txt");
	}
}