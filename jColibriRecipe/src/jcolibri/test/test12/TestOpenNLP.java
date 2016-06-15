/**
 * TestOpenNLP.java
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
import jcolibri.extensions.textual.IE.opennlp.IETextOpenNLP;
import jcolibri.extensions.textual.IE.opennlp.OpennlpMainNamesExtractor;
import jcolibri.extensions.textual.IE.opennlp.OpennlpPOStagger;
import jcolibri.extensions.textual.IE.opennlp.OpennlpSplitter;
import jcolibri.util.FileIO;


/**
 * This example shows how to use the OpenNLP methods of the Textual CBR extension.
 * <br>
 * It just parses and extracts information from the simple text file (restest.txt)
 * <p>
 * For more information about the Textual CBR extension read the jcolibri.extensions.textual.IE documentation.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.extensions.textual.IE
 */
public class TestOpenNLP
{

    public static void main(String[] args)
	{
		try {
		    	// Load the textual file
			BufferedReader br = new BufferedReader( new InputStreamReader(FileIO.findFile("jcolibri/test/test12/restest.txt").openStream()));
			String content = "";
			String line;
			while ((line = br.readLine()) != null) {
				content+=(line+"\n");
			}
			
			long startTime = new Date().getTime();
			
			//Create the IETextOpenNLP object
			IETextOpenNLP text = new IETextOpenNLP(content);

			//Organize the text into paragraphs, sentences and tokens
			OpennlpSplitter.split(text);
			
			//Remove stopwords
			StopWordsDetector.detectStopWords(text); 
			
			//Stem the text
			TextStemmer.stem(text);
			
			//Perform POS tagging with OpenNLP
			OpennlpPOStagger.tag(text);
			
			//Extract main names of the sentence using OpenNLP
			OpennlpMainNamesExtractor.extractMainNames(text);
			
			//Extract phrases using the common implementation
			PhrasesExtractor.loadRules(FileIO.findFile("jcolibri/test/test12/phrasesRules.txt").getPath());
			PhrasesExtractor.extractPhrases(text);
			
			//Extract features using the common implementation
			FeaturesExtractor.loadRules("jcolibri/test/test12/featuresRules.txt");
			FeaturesExtractor.extractFeatures(text);

			//Classify with topic
			DomainTopicClassifier.loadRules("jcolibri/test/test12/domainRules.txt");
			DomainTopicClassifier.classifyWithTopic(text);
			
			//Print result
			System.out.println(text.printAnnotations());
			long endTime = new Date().getTime();
			System.out.println("Total time: "+ (endTime-startTime)+" milliseconds");

		} catch (Exception e) {
			org.apache.commons.logging.LogFactory.getLog(TestOpenNLP.class).error(e);
		}
	}

}
