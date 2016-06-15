/**
 * Test13b.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 23/06/2007
 */
package jcolibri.test.test13;

import java.util.Collection;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.exception.ExecutionException;
import jcolibri.extensions.textual.IE.opennlp.IETextOpenNLP;
import jcolibri.extensions.textual.lucene.LuceneIndex;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.textual.LuceneTextSimilarity;
import jcolibri.method.retrieve.selection.SelectCases;
import jcolibri.test.main.SwingProgressBar;
import jcolibri.test.test13.connector.RestaurantsConnector;
import jcolibri.test.test13.gui.ResultFrame;

/**
 * This test shows how to use the Apache Lucene search engine in a Restaurant recommender. 
 * <br>
 * It uses a custum connector (RestaurantConnector) that loads cases from a normal txt file.
 * <br>
 * To compare the texts it uses the Lucene similarity function implemented in jcolibri.method.retrieve.NNretrieval.similarity.local.textual.LuceneTextSimilarity
 * <br>
 * Test13a shows whot tu use other textual similarity function from the jcolibri.method.retrieve.NNretrieval.similarity.local.textual package.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.method.retrieve.NNretrieval.similarity.local.textual.LuceneTextSimilarity
 * @see jcolibri.test.test13.connector.RestaurantsConnector
 */
public class Test13b implements StandardCBRApplication
{

    Connector _connector;
    CBRCaseBase _caseBase;

    LuceneIndex luceneIndex;
    
    /*
     * (non-Javadoc)
     * 
     * @see jcolibri.cbraplications.BasicCBRApplication#configure()
     */
    public void configure() throws ExecutionException
    {
	try
	{
	    _connector = new RestaurantsConnector("jcolibri/test/test13/restaurants-large-v2.txt");
	    _caseBase = new LinealCaseBase();
	    
	    jcolibri.util.ProgressController.clear();
	    SwingProgressBar pb = new SwingProgressBar();
	    jcolibri.util.ProgressController.register(pb);   
	} catch (Exception e)
	{
	    throw new ExecutionException(e);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcolibri.cbraplications.StandardCBRApplication#preCycle()
     */
    public CBRCaseBase preCycle() throws ExecutionException
    {
	_caseBase.init(_connector);

	//Here we create the Lucene index
	luceneIndex = jcolibri.method.precycle.LuceneIndexCreator.createLuceneIndex(_caseBase);
	
	return _caseBase;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcolibri.cbraplications.StandardCBRApplication#cycle(jcolibri.cbrcore.CBRQuery)
     */
    public void cycle(CBRQuery query) throws ExecutionException
    {
	Collection<CBRCase> cases = _caseBase.getCases();
	
	NNConfig nnConfig = new NNConfig();
	nnConfig.setDescriptionSimFunction(new Average());
	
	
	//We only compare the "description" attribute using Lucene
	Attribute textualAttribute = new Attribute("description", RestaurantDescription.class);
	nnConfig.addMapping(textualAttribute, new LuceneTextSimilarity(luceneIndex,query,textualAttribute, true));

	
	System.out.println("RESULT: ");
	
	Collection<RetrievalResult> res = NNScoringMethod.evaluateSimilarity(cases, query, nnConfig);
	res = SelectCases.selectTopKRR(res, 5);
	
	for(RetrievalResult rr: res)
	    System.out.println(rr);
	
	RestaurantDescription qrd = (RestaurantDescription)query.getDescription();
	CBRCase mostSimilar = res.iterator().next().get_case();
	RestaurantDescription rrd = (RestaurantDescription)mostSimilar.getDescription();
	new ResultFrame(qrd.getDescription().getRAWContent(), rrd.getName(), rrd.getAddress(), rrd.getDescription().getRAWContent());

    }

    /*
     * (non-Javadoc)
     * 
     * @see jcolibri.cbraplications.StandardCBRApplication#postCycle()
     */
    public void postCycle() throws ExecutionException
    {
	_connector.close();

    }

    
    public static void main(String[] args)
    {
	Test13b test = new Test13b();
	try
	{
	    test.configure();
	    
	    CBRCaseBase caseBase = test.preCycle();
	   
	    System.out.println("CASE BASE: ");
	    for(CBRCase c: caseBase.getCases())
		System.out.println(c);
	    System.out.println("Total: "+caseBase.getCases().size()+" cases");

	    boolean _continue = true;
	    while(_continue)
	    {
        	    String queryString = javax.swing.JOptionPane.showInputDialog("Please enter the restaurant description:");
        	    if(queryString == null)
        		_continue = false;
        	    else
        	    {	
                	    CBRQuery query = new CBRQuery();
                	    RestaurantDescription queryDescription = new RestaurantDescription();
                	    queryDescription.setDescription(new IETextOpenNLP(queryString));
                	    query.setDescription(queryDescription);
                	    
                	    test.cycle(query);
        	    }
	    }
	    test.postCycle();
	    
	} catch (ExecutionException e)
	{
	    org.apache.commons.logging.LogFactory.getLog(Test13b.class).error(e);
	}
    }
}
