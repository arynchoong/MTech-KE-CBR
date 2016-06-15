/**
 * SpamFilterApp.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 01/08/2007
 */
package jcolibri.test.test16;

import java.util.Collection;

import jcolibri.casebase.CachedLinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.evaluation.Evaluator;
import jcolibri.exception.ExecutionException;
import jcolibri.extensions.textual.IE.common.StopWordsDetector;
import jcolibri.extensions.textual.IE.common.TextStemmer;
import jcolibri.extensions.textual.IE.opennlp.OpennlpSplitter;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.reuse.classification.KNNClassificationConfig;
import jcolibri.method.reuse.classification.KNNClassificationMethod;
import jcolibri.method.revise.classification.BasicClassificationOracle;
import jcolibri.method.revise.classification.ClassificationOracle;

/**
 * Spam filter application.
 * It is configured through the set() methods. 
 * The cycle method() runs the application and stores the evaluation results in the evaluation report object.
 * After running all cycles, some statistics may be read using the get() methods.
 * <br>
 * The corpus used by this application must be a zip file with several textual files (one per email).
 * The filename must start by ham or spam depending on the class.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class SpamFilterApp implements StandardCBRApplication
{
    Connector _connector;
    CBRCaseBase _caseBase;
    
    private int k = 3;
    private LocalSimilarityFunction similFunc = null; 
    private KNNClassificationMethod clasifMethod = null;
    private String corpusZipFile = null;

    private double tp;
    private double tn;
    private double fp;
    private double fn;
    
    private KNNClassificationConfig spamFilterSimConfig;	

    /**
     * Creates a spam filter application that uses the corpus indicated by the parameter.
     */
    public SpamFilterApp(String corpusZipFile)
    {
	this.corpusZipFile = corpusZipFile;
	spamFilterSimConfig = new KNNClassificationConfig();
    }
    
    
    /* (non-Javadoc)
     * @see jcolibri.cbraplications.StandardCBRApplication#configure()
     */
    public void configure() throws ExecutionException
    {
	_connector = new EmailConnector(corpusZipFile);
	_caseBase = new CachedLinealCaseBase();
    }

    /* (non-Javadoc)
     * @see jcolibri.cbraplications.StandardCBRApplication#preCycle()
     */
    public CBRCaseBase preCycle() throws ExecutionException
    {
	_caseBase.init(_connector);	
	
	Collection<CBRCase> cases = _caseBase.getCases();
	
	//Organize cases into paragraphs, sentences and tokens
	OpennlpSplitter.split(cases);
	//Detect stopwords
	StopWordsDetector.detectStopWords(cases);
	//Stem text
	TextStemmer.stem(cases);

	tn = tp = fp = fn = 0;
	
	return _caseBase;

    }
    
    /* (non-Javadoc)
     * @see jcolibri.cbraplications.StandardCBRApplication#cycle(jcolibri.cbrcore.CBRQuery)
     */
    public void cycle(CBRQuery query) throws ExecutionException
    {
	KNNClassificationConfig spamFilterSimConfig = getKNNConfig();
	
	double predictionCost;

	
	ClassificationOracle oracle = new BasicClassificationOracle();
	predictionCost = oracle.getPredictionCost(query, _caseBase, spamFilterSimConfig);
	
	CBRCase _case = (CBRCase)query;
	EmailSolution sol = (EmailSolution)_case.getSolution();
	String _class = sol.getEmailClass();
	if(predictionCost == 0) // Prediction was ok
	{
	    if(_class.equals(EmailSolution.SPAM))
		tp++;
	    else
		tn++;
	}
	else
	{
	    if(_class.equals(EmailSolution.SPAM))
		fn++;
	    else
		fp++;
	}
	    
	Evaluator.getEvaluationReport().addDataToSeries("Evaluation", new Double(predictionCost));
     }
    

    /* (non-Javadoc)
     * @see jcolibri.cbraplications.StandardCBRApplication#postCycle()
     */
    public void postCycle() throws ExecutionException
    {
	// TODO Auto-generated method stub

    }
    
    /**
     * Returns the KNN configuration
     */
    public KNNClassificationConfig getKNNConfig()
    {
	spamFilterSimConfig.setDescriptionSimFunction(new Average());
	spamFilterSimConfig.setClassificationMethod(clasifMethod);
	spamFilterSimConfig.setK(k);
	spamFilterSimConfig.addMapping(new Attribute("content",EmailDescription.class), similFunc);
	return spamFilterSimConfig;
    }
    
    /**
     * Sets the k
     * @param k The k to set.
     */
    public void setK(int k)
    {
        this.k = k;
    }

    /**
     * Sets the similarity function
     * @param similFunc The similFunc to set.
     */
    public void setSimilFunc(LocalSimilarityFunction similFunc)
    {
        this.similFunc = similFunc;
    }
    
    /**
     * Sets the classification method.
     * @param clasifMethod The clasifMethod to set.
     */
    public void setClasifMethod(KNNClassificationMethod clasifMethod)
    {
        this.clasifMethod = clasifMethod;
    }

    /**
     * Returns the false negatives.
     */
    public double getFalseNegatives()
    {
        return fn;
    }


    /**
     * Returns the false positives.
     */
    public double getFalsePositives()
    {
        return fp;
    }


    /**
     * Returns the true positives
     */
    public double getTruePositives()
    {
        return tp;
    }
    
    /**
     * Returns the true negatives
     */
    public double getTrueNegatives()
    {
        return tn;
    }

}
