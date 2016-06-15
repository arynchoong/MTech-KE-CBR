/**
 * Test6.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 03/05/2007
 */
package jcolibri.test.test6;


import java.util.ArrayList;
import java.util.Date;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.PlainTextConnector;
import jcolibri.exception.AttributeAccessException;
import jcolibri.exception.ExecutionException;

/**
 * This example shows how to use the Plain Text connector.
 * Here we only read the cases and store a new one in the persistence file.
 * <p>
 * The case base (iris_data_jCOLIBRI.txt) contains information about iris:
 * <pre>
 * #Columns are: Sepal Length, Sepal Width, Petal Length, Petal Width, Type of Iris,
 * 
 * Case 1,5.1,3.5,1.4,0.2,Iris-setosa
 * Case 2,4.9,3,1.4,0.2,Iris-setosa
 * Case 3,4.7,3.2,1.3,0.2,Iris-setosa
 * ...
 * </pre>
 * 
 * These cases are mapped into the following structure:
 *  * <pre>
 * Case
 *  |
 *  +- Description
 *  |       |
 *  |       +- id *          (1)
 *  |       +- sepalLength   (2)
 *  |       +- sepalWidth    (3)
 *  |       +- petalLength   (4)
 *  |       +- petalWidth    (5)
 *  |
 *  +- Solution
 *          |
 *          +- type *        (6)
 * </pre>
 * The attributes with * are the ids of the compound objects and the numbers between parenthesis are the corresponding columns in the text file.
 * <p>
 * The mapping is configured by the <b>plaintextconfig.xml</b> file following the schema defined in PlainTextConnector:
 * <pre>
 * &lt;TextFileConfiguration&gt;
 *	&lt;FilePath&gt;jcolibri/test/test6/iris_data_jCOLIBRI.txt&lt;/FilePath&gt;
 *	&lt;Delimiters&gt;,&lt;/Delimiters&gt;
 *	&lt;DescriptionClassName&gt;jcolibri.test.test6.IrisDescription&lt;/DescriptionClassName&gt;
 *	&lt;DescriptionMappings&gt;
 *		&lt;Map&gt;sepalLength&lt;/Map&gt;
 *		&lt;Map&gt;sepalWidth&lt;/Map&gt;
 *		&lt;Map&gt;petalLength&lt;/Map&gt;
 *		&lt;Map&gt;petalWidth&lt;/Map&gt;		
 *	&lt;/DescriptionMappings&gt;
 *	&lt;SolutionClassName&gt;jcolibri.test.test6.IrisSolution&lt;/SolutionClassName&gt;
 *	&lt;SolutionMappings&gt;
 *      &lt;Map&gt;type&lt;/Map&gt;
 *	&lt;/SolutionMappings&gt;
 * &lt;/TextFileConfiguration&gt;
 * </pre>
 * First, we define the path containing the data and the characters used as delimiters (comma in this example).
 * <br>
 * Then we map each part of the case. Following the order of the columns in the text file we have to indicate to which attributes are mapped.
 * This connector only uses the id of the description. It must be the first column of each row and is not included in the mapping file
 * <br>
 * 
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * 
 * @see jcolibri.connector.PlainTextConnector
 */
public class Test6 implements StandardCBRApplication {

	Connector _connector;
	CBRCaseBase _caseBase;
	
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.StandardCBRApplication#configure()
	 */
	public void configure() throws ExecutionException {
		try{
			_connector = new PlainTextConnector();
			_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("jcolibri/test/test6/plaintextconfig.xml"));
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
		java.util.Collection<CBRCase> cases = _caseBase.getCases();
		for(CBRCase c: cases)
			System.out.println(c);
		return _caseBase;
	}

	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.StandardCBRApplication#cycle()
	 */
	public void cycle(CBRQuery query) throws ExecutionException {
		//Obtain only the first case
		CBRCase newcase = _caseBase.getCases().iterator().next();
		//Modify its id attribute and store it back
		Attribute id = newcase.getDescription().getIdAttribute();
		try {
			Date d = new Date();
			id.setValue(newcase.getDescription(), ("case "+d.toString()).replaceAll(" ", "_"));
		} catch (AttributeAccessException e) {
			org.apache.commons.logging.LogFactory.getLog(this.getClass()).error(e);
		}
		
		ArrayList<CBRCase> casestoLearnt = new ArrayList<CBRCase>();
		casestoLearnt.add(newcase);
		_caseBase.learnCases(casestoLearnt);

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
		Test6 test = new Test6();
		try {
			test.configure();
			test.preCycle();
			test.cycle(null);
		} catch (ExecutionException e) {
			org.apache.commons.logging.LogFactory.getLog(Test6.class).error(e);
		}

	}

}
