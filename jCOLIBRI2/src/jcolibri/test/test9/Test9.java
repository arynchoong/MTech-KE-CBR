/**
 * Test9.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 03/05/2007
 */
package jcolibri.test.test9;


import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.PlainTextConnector;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;

/**
 * This example shows how to visualizate a case base using the InfoVisual library develped by Josep Lluis Arcos (IIIA-CSIC).
 * The class of each case is defined by the id attribute of the solution.
 * @author Juan A. Recio-Garcia
 *
 */
public class Test9 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
		    	jcolibri.util.ProgressController.clear();
		    	jcolibri.util.ProgressController.register(new jcolibri.test.main.SwingProgressBar(), jcolibri.extensions.visualization.CasesVisualization.class);
		    
			//Configure connector and case base
			Connector _connector = new PlainTextConnector();
			_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("jcolibri/test/test9/plaintextconfig.xml"));
			CBRCaseBase _caseBase  = new LinealCaseBase();
			
			// Load cases
			_caseBase.init(_connector);
			
			// Configure NN
			
			NNConfig simConfig = new NNConfig();
			simConfig.setDescriptionSimFunction(new Average());
			simConfig.addMapping(new Attribute("sepalLength",IrisDescription.class), new Interval(3.6));
			simConfig.addMapping(new Attribute("sepalWidth", IrisDescription.class), new Interval(2.4));
			simConfig.addMapping(new Attribute("petalLength",IrisDescription.class), new Interval(5.9));
			simConfig.addMapping(new Attribute("petalWidth", IrisDescription.class), new Interval(2.4));
			
			// Visualize the case base
			jcolibri.extensions.visualization.CasesVisualization.visualize(_caseBase.getCases(), simConfig);
			
		} catch (Exception e) {
			org.apache.commons.logging.LogFactory.getLog(Test9.class).error(e);
		}


	}

}
