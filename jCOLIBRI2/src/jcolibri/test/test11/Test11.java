/**
 * Test11.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 16/06/2007
 */
package jcolibri.test.test11;

import java.util.ArrayList;


import jcolibri.datatypes.Instance;
import jcolibri.method.retrieve.NNretrieval.similarity.local.ontology.OntCosine;
import jcolibri.method.retrieve.NNretrieval.similarity.local.ontology.OntDeep;
import jcolibri.method.retrieve.NNretrieval.similarity.local.ontology.OntDeepBasic;
import jcolibri.method.retrieve.NNretrieval.similarity.local.ontology.OntDetail;
import jcolibri.util.FileIO;
import es.ucm.fdi.gaia.ontobridge.OntoBridge;
import es.ucm.fdi.gaia.ontobridge.OntologyDocument;

/**
 * Checks the ontological similarity functions.
 * It uses the following ontology:
 * <p><img src="test11.jpg"/></p>
 * <p>The similarity funcions implement these formulas:</p>
 * <p><img src="ontsim.jpg"/></p>
 * 
 * @author Juan A. Recio-García
 * @version 1.0
 * @see jcolibri.method.retrieve.NNretrieval.similarity.local.ontology
 */
public class Test11 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			// Obtain a reference to OntoBridge
			OntoBridge ob = jcolibri.util.OntoBridgeSingleton.getOntoBridge();
			// Configure it to work with the Pellet reasoner
			ob.initWithPelletReasoner();
			// Setup the main ontology
			OntologyDocument mainOnto = new OntologyDocument("http://gaia.fdi.ucm.es/ontologies/personalTrainer.owl", 
									FileIO.findFile("jcolibri/test/test11/personalTrainer.owl").toExternalForm());
			// There are not subontologies
			ArrayList<OntologyDocument> subOntologies = new ArrayList<OntologyDocument>();
			// Load the ontology
			ob.loadOntology(mainOnto, subOntologies, false);
			
			OntCosine cosine = new OntCosine();
			OntDeepBasic deepbasic = new OntDeepBasic();
			OntDeep deep = new OntDeep();
			OntDetail detail = new OntDetail();
			
			Instance biceps = 	new Instance("biceps");
			Instance triceps = 	new Instance("triceps");
			Instance pectoralis = 	new Instance("pectoralis");
			Instance abdominis = 	new Instance("abdominis");
			Instance quadriceps = 	new Instance("quadriceps");
			Instance deltoid = 	new Instance("deltoid");

			
			System.out.println(ob.LCS("pectoralis", "deltoid"));

			
			System.out.format("\n\t\t\t\tDeepBasic\tDeep\t\tCosine\t\tDetail\n"+
					          "sim(biceps, triceps)\t\t%.3f\t\t%.3f\t\t%.3f\t\t%.3f\n",
					deepbasic.compute(biceps, triceps),
					deep.compute(biceps,triceps),
					cosine.compute(biceps, triceps),
					detail.compute(biceps,triceps)
					);

			System.out.format("sim(pectoralis, abdominis)\t%.3f\t\t%.3f\t\t%.3f\t\t%.3f\n",
					deepbasic.compute(pectoralis, abdominis),
					deep.compute(pectoralis,abdominis),
					cosine.compute(pectoralis, abdominis),
					detail.compute(pectoralis,abdominis)
					);

			System.out.format("sim(triceps, abdominis)\t\t%.3f\t\t%.3f\t\t%.3f\t\t%.3f\n",
					deepbasic.compute(triceps, abdominis),
					deep.compute(triceps,abdominis),
					cosine.compute(triceps, abdominis),
					detail.compute(triceps,abdominis)
					);
			
			System.out.format("sim(triceps, cuadriceps)\t%.3f\t\t%.3f\t\t%.3f\t\t%.3f\n",
					deepbasic.compute(triceps, quadriceps),
					deep.compute(triceps,quadriceps),
					cosine.compute(triceps, quadriceps),
					detail.compute(triceps,quadriceps)
					);
			
			System.out.format("sim(pectoralis, deltoid)\t%.3f\t\t%.3f\t\t%.3f\t\t%.3f\n",
					deepbasic.compute(pectoralis, deltoid),
					deep.compute(pectoralis,deltoid),
					cosine.compute(pectoralis, deltoid),
					detail.compute(pectoralis,deltoid)
					);
			
			
		}catch(Exception e)
		{
			org.apache.commons.logging.LogFactory.getLog(OntDeepBasic.class).error(e);
		}


	}

}
