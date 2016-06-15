/**
 * Test2.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 28/11/2006
 */
package jcolibri.test.test2;

import java.util.Collection;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.DataBaseConnector;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.selection.SelectCases;

/**
 * This second test shows how to use enumerated values and user defined data types as attributes.<br>
 * <ul>
 * <li><b>Enums:</b><br>
 * In the TravelDescription class we define the enumeration:
 * <pre>
 * enum AccommodationTypes  { OneStar, TwoStars, ThreeStars, HolidayFlat, FourStars, FiveStars};
 * </pre>
 * for the accomodation attribute. To manage that attribute we only have to modify the <b>TravelDescription.hbm.xml</b>
 * with the following code:
 * <pre>
 *  ...
 * 	&lt;property name="Accommodation" column="Accommodation"&gt;
 *		&lt;type name="jcolibri.connector.databaseutils.EnumUserType"&gt;
 *			&lt;param name="enumClassName"&gt;jcolibri.test.test2.TravelDescription$AccommodationTypes&lt;/param&gt;
 *    	&lt;/type&gt;
 * 	&lt;/property&gt;
 * 	...
 * </pre>
 * The EnumUserType class is a utility class of the connector that allows managing Enums. It recieves the class name of you enum as parameter.
 * <li><b>User defined data types:</b><br>
 * To create your own data type implement the jcolibri.connector.TypeAdaptor interface. MyStringType.java is a simple example were we are wrapping a string.
 * You have to define the toString(), fromString() and equals() methods. That way the value of your data type will be mapped using a string representation in the presistence media.
 * <br>
 * In this example we have typed the "Hotel" attribute of the description as "MyStringType. To manage the attribute we have to modify the <b>TravelDescription.hbm.xml</b> file with:
 * <pre>
 * 	&lt;property name="Hotel" column="Hotel"&gt;
 *		&lt;type name="jcolibri.connector.databaseutils.GenericUserType"&gt;
 *			&lt;param name="className"&gt;jcolibri.test.test2.MyStringType&lt;/param&gt;
 *      &lt;/type&gt;
 * 	&lt;/property&gt;
 * </pre>
 * The GenericUserType class is the utility class that allows to manage TypeAdaptor implementations in the data base connector.
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.connector.TypeAdaptor
 * @see jcolibri.test.test2.TravelDescription
 * @see jcolibri.test.test2.MyStringType
 */
public class Test2 implements StandardCBRApplication {

	/** Connector object */
	Connector _connector;
	/** CaseBase object */
	CBRCaseBase _caseBase;
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#configure()
	 */
	public void configure() throws ExecutionException{
		try{
		// Create a data base connector
		_connector = new DataBaseConnector();
		// Init the ddbb connector with the config file
		_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("jcolibri/test/test2/databaseconfig.xml"));
		// Create a Lineal case base for in-memory organization
		_caseBase  = new LinealCaseBase();
		} catch (Exception e){
			throw new ExecutionException(e);
		}
	}

	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#preCycle()
	 */
	public CBRCaseBase preCycle() throws ExecutionException {
		// Load cases from connector into the case base
		_caseBase.init(_connector);		
		return _caseBase;
	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#cycle()
	 */
	public void cycle(CBRQuery query) throws ExecutionException 
	{		
		// First configure the KNN
		NNConfig simConfig = new NNConfig();
		// Set the average() global similarity function for the description of the case
		simConfig.setDescriptionSimFunction(new Average());
		// The accomodation attribute uses the equal() local similarity function
		simConfig.addMapping(new Attribute("Accommodation", TravelDescription.class), new Equal());
		// For the duration attribute we are going to set its local similarity function and the weight
		Attribute duration = new Attribute("Duration", TravelDescription.class);
		simConfig.addMapping(duration, new Interval(31));
		simConfig.setWeight(duration, 0.5);
		// HolidayType --> equal()
		simConfig.addMapping(new Attribute("HolidayType", TravelDescription.class), new Equal());
		// NumberOfPersons --> equal()
		simConfig.addMapping(new Attribute("NumberOfPersons", TravelDescription.class), new Equal());
		// Price --> InrecaLessIsBetter()
		simConfig.addMapping(new Attribute("Price", TravelDescription.class), new Interval(4000));
		
		
		// A bit of verbose
		System.out.println("Query Description:");
		System.out.println(query.getDescription());
		System.out.println();
		
		// Execute NN
		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
		
		// Select k cases
		eval = SelectCases.selectTopKRR(eval, 5);
		
		// Print the retrieval
		System.out.println("Retrieved cases:");
		for(RetrievalResult nse: eval)
			System.out.println(nse);
	}

	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#postCycle()
	 */
	public void postCycle() throws ExecutionException {
		this._caseBase.close();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    	// Launch DDBB manager
	    	jcolibri.test.database.HSQLDBserver.init();
	    	
		// Create the application
		Test2 test2 = new Test2();
		try {
			//Configure it
			test2.configure();
			// Run the precycle --> load the cases
			test2.preCycle();
			
			//BufferedReader reader  = new BufferedReader(new InputStreamReader(System.in));			
			//do{		
				TravelDescription queryDesc = new TravelDescription();
				// The accommodation is a value of the Enum
				queryDesc.setAccommodation(TravelDescription.AccommodationTypes.ThreeStars);
				queryDesc.setDuration(7);
				queryDesc.setHolidayType("Recreation");
				queryDesc.setNumberOfPersons(2);
				queryDesc.setPrice(700);
				
				CBRQuery query = new CBRQuery();
				query.setDescription(queryDesc);
				
				// Execute query
				test2.cycle(query);
				
			//System.out.println("Cycle finished. Type exit to idem or enter to repeat the cycle");
			//}while(!reader.readLine().equals("exit"));
			
			test2.postCycle();
			
			//Shutdown DDBB manager
		    	jcolibri.test.database.HSQLDBserver.shutDown();

		} catch (ExecutionException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
