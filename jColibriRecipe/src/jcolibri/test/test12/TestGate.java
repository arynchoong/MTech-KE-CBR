/**
 * TestGate.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 19/06/2007
 */
package jcolibri.test.test12;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

import jcolibri.extensions.textual.IE.common.DomainTopicClassifier;
import jcolibri.extensions.textual.IE.common.FeaturesExtractor;
import jcolibri.extensions.textual.IE.common.PhrasesExtractor;
import jcolibri.extensions.textual.IE.common.StopWordsDetector;
import jcolibri.extensions.textual.IE.common.TextStemmer;
import jcolibri.extensions.textual.IE.gate.GateFeaturesExtractor;
import jcolibri.extensions.textual.IE.gate.GatePOStagger;
import jcolibri.extensions.textual.IE.gate.GatePhrasesExtractor;
import jcolibri.extensions.textual.IE.gate.GateSplitter;
import jcolibri.extensions.textual.IE.gate.IETextGate;
import jcolibri.util.FileIO;

/**
 * This example shows how to use the GATE methods of the Textual CBR extension.
 * <br>
 * It just parses and extracts information from the simple text file (restest.txt)
 * <p>
 * For more information about the Textual CBR extension read the jcolibri.extensions.textual.IE documentation.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.extensions.textual.IE
 */
public class TestGate
{

	public static void main(String[] args)
	{
		try {
		    	//Load the textual file
			BufferedReader br = new BufferedReader( new InputStreamReader(FileIO.findFile("jcolibri/test/test12/restest.txt").openStream()));
			String content = "";
			String line;
			while ((line = br.readLine()) != null) {
				content+=(line+"\n");
			}
			
			long startTime = new Date().getTime();
			
			//Create the IETextGate object
			IETextGate  text = new IETextGate(content);
			
			//Organize the text into paragraphs, sentences and tokens
			GateSplitter.split(text);
			
			//Remove stopwords
			StopWordsDetector.detectStopWords(text); 
			
			//Stem the text
			TextStemmer.stem(text);
			
			//Perform POS tagging with GATE
			GatePOStagger.tag(text);
			
			//Extract phrases using the common implementation
			PhrasesExtractor.loadRules("jcolibri/test/test12/phrasesRules.txt");
			PhrasesExtractor.extractPhrases(text);
			
			//Extract phrases using the GATE specific method
			GatePhrasesExtractor.loadDefaultRules();
			GatePhrasesExtractor.extractPhrases(text);

			//Extract features using the common implementation
			FeaturesExtractor.loadRules("jcolibri/test/test12/featuresRules.txt");
			FeaturesExtractor.extractFeatures(text);
			
			//Extract features using the GATE specific method
			GateFeaturesExtractor.loadDefaultRules();
			GateFeaturesExtractor.extractFeatures(text);
			
			//Classify with topic
			DomainTopicClassifier.loadRules("jcolibri/test/test12/domainRules.txt");
			DomainTopicClassifier.classifyWithTopic(text);
			
			//Print result
			System.out.println(text.printAnnotations());
			long endTime = new Date().getTime();
			System.out.println("Total time: "+ (endTime-startTime)+" milliseconds");
			
		} catch (Exception e) {
		    org.apache.commons.logging.LogFactory.getLog(TestGate.class).error(e);
		}
	}
}
