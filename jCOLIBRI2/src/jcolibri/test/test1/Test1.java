/**
 * Test1.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 28/11/2006
 */
package jcolibri.test.test1;

import java.util.Collection;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
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
 * Test 1 shows how to use a simple data base connector and perform the KNN retrieval. It uses the travel example with cases that only have description
 * (without solution or result).
 * <br>
 * This example uses the DataBase connector that is implemented using the <a href="www.hibernate.org">Hibernate</a> library. 
 * That library is a Java Data Objects implementation that automatically manages the persistence of 
 * Java beans in relational data bases.
 * <br>For an introduction to hibernate see: <a href="http://www.hibernate.org/hib_docs/v3/reference/en/html/tutorial.html">http://www.hibernate.org/hib_docs/v3/reference/en/html/tutorial.html</a>
 * <p> 
 * The DataBase connector in jcolibri/test/test1/databaseconfig.xml and that xml defines the configuration of Hibernate and
 * the mapping of the description of our case with the data base. In this example, all the attributes of the description bean are stored in the same table.
 * <br>
 * <ul>
 * <li><b>databaseconfig.xml</b>.<br>Connector configuration file: indicates the path to the hibernate config file, the name of the class/bean that stores the description and its mapping file.<br>
 * <pre>
 * &lt;DataBaseConfiguration&gt;
 *	  &lt;HibernateConfigFile&gt;jcolibri/test/test1/hibernate.cfg.xml&lt;/HibernateConfigFile&gt;
 *	  &lt;DescriptionMappingFile&gt;jcolibri/test/test1/TravelDescription.hbm.xml&lt;/DescriptionMappingFile&gt;
 *	  &lt;DescriptionClassName&gt;jcolibri.test.test1.TravelDescription&lt;/DescriptionClassName&gt;
 * &lt;/DataBaseConfiguration&gt;
 * </pre>
 * 
 * <li><b>hibernate.cfg.xml</b>.<br>Hibernate configuration file: developers should only modify the database connection settings.<br>
 * <pre>
 * &lt;hibernate-configuration&gt;
 *   &lt;session-factory&gt;
 *       &lt;!-- Database connection settings --&gt;
 *       &lt;property name="connection.driver_class"&gt;org.hsqldb.jdbcDriver&lt;/property&gt;
 *       &lt;property name="connection.url"&gt;jdbc:hsqldb:hsql://localhost/travel&lt;/property&gt;
 *       &lt;property name="connection.username"&gt;sa&lt;/property&gt;
 *       &lt;property name="connection.password"&gt;&lt;/property&gt;
 *		
 *       &lt;!-- JDBC connection pool (use the built-in) --&gt;
 *       &lt;property name="connection.pool_size"&gt;1&lt;/property&gt;
 *
 *       &lt;!-- SQL dialect --&gt;
 *       &lt;property name="dialect"&gt;org.hibernate.dialect.HSQLDialect&lt;/property&gt;
 *
 *       &lt;!-- Enable Hibernate's automatic session context management --&gt;
 *       &lt;property name="current_session_context_class"&gt;thread&lt;/property&gt;
 *
 *       &lt;!-- Disable the second-level cache  --&gt;
 *       &lt;property name="cache.provider_class"&gt;org.hibernate.cache.NoCacheProvider&lt;/property&gt;
 *
 *       &lt;!-- Echo all executed SQL to stdout --&gt;
 *      &lt;property name="show_sql"&gt;true&lt;/property&gt;   
 *    &lt;/session-factory&gt;
 * &lt;/hibernate-configuration&gt;
 * </pre>
 * <p>This test uses the HSQLDB data base server with an example table containing 
 * the data of the case base. Developers can use any other DBMS changing the 
 * hibernate configuration file.
 * <br>
 * If you use another database change the driver, url and dialect fields. 
 * For example, to use a MySQL server you should use:
 * <pre>
 *       &lt;property name="connection.driver_class"&gt;com.mysql.jdbc.Driver&lt;/property&gt;
 *       &lt;property name="connection.url"&gt;jdbc:mysql://localhost:3306/travel&lt;/property&gt;
 *       &lt;property name="dialect"&gt;org.hibernate.dialect.MySQLDialect&lt;/property&gt;
 * </pre>
 * For other configuration settings see hiberante documentation: 
 * <a href="http://www.hibernate.org/hib_docs/v3/reference/en/html/session-configuration.html">http://www.hibernate.org/hib_docs/v3/reference/en/html/session-configuration.html</a>
 * </p>
 * </li>
 * <li><b>TravelDescription.hbm.xml</b>.<br> Hibernate mapping file for the description bean the case (TravelDescription.java).
 * It stores each attribute of the description in a column of the table Travel.<br>
 * <pre>
 * &lt;hibernate-mapping default-lazy="false"&gt;
 *  &lt;class name="jcolibri.test.test1.TravelDescription" table="Travel"&gt;
 *	  &lt;id name="caseId" column="caseId"&gt;
 *	    &lt;generator class="native"/&gt;
 *	  &lt;/id&gt;
 *	  &lt;property name="HolidayType" column="HolidayType"/&gt;
 *	  &lt;property name="Price" column="Price"/&gt;
 *	  &lt;property name="NumberOfPersons" column="NumberOfPersons"/&gt;
 *	  &lt;property name="Region" column="Region"/&gt;
 *	  &lt;property name="Transportation" column="Transportation"/&gt;
 *	  &lt;property name="Duration" column="Duration"/&gt;
 *	  &lt;property name="Season" column="Season"/&gt;
 *	  &lt;property name="Accomodation" column="Accommodation"/&gt;
 *	  &lt;property name="Hotel" column="Hotel"/&gt;	
 *  &lt;/class&gt;
 * &lt;/hibernate-mapping&gt;
 * </pre>
 * Here we set that TravelDescription is mapped in the Travel table. caseId is the primary key of the table and Hibernate will use a 
 * native key generator for new cases (there are different ways to create primary keys, for more information see Hiberante documentation).
 * Each attribute is mapped into a table with the same name. You should notice that here we don't indicate the type of the attributes. 
 * Hibernate automatically detects the type and converts from/to the database. Anyway if you want to use an unrecognoized type that hibernate does not
 * understand or create your own one you can do it by implementing the jcolibri.connector.TypeAdaptor interface in your type.
 * <br>
 * This is the structure of the table:
 * <pre>
 * +----------+-------------+-------+-----------------+--------+----------------+----------+--------+---------------+--------------------------+
 * | caseId   | HolidayType | Price | NumberOfPersons | Region | Transportation | Duration | Season | Accommodation | Hotel                    |
 * +----------+-------------+-------+-----------------+--------+----------------+----------+--------+---------------+--------------------------+
 * | Journey1 | Bathing     |  2498 |               2 | Egypt  | Plane          |       14 | April  | TwoStars      | Hotel White House, Egypt |
 * | Journey2 | Bathing     |  3066 |               3 | Egypt  | Plane          |       21 | May    | TwoStars      | Hotel White House, Egypt | 
 * ...
 * </pre>
 * The travel.sql file contains the code to create this data base.
 * </ul>
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.test.test1.TravelDescription
 * @see jcolibri.connector.DataBaseConnector
 */
public class Test1 implements StandardCBRApplication {

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
		_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("jcolibri/test/test1/databaseconfig.xml"));
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
		// Print the cases
		java.util.Collection<CBRCase> cases = _caseBase.getCases();
		for(CBRCase c: cases)
			System.out.println(c);
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
		simConfig.addMapping(new Attribute("Accomodation", TravelDescription.class), new Equal());
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
	 * Main function
	 */
	public static void main(String[] args) {
	    	// Launch DDBB manager
	    	jcolibri.test.database.HSQLDBserver.init();
		// Create the application
		Test1 test1 = new Test1();
		try {
			// Configure it
			test1.configure();
			// Run the precycle --> load the cases
			test1.preCycle();
			
			//BufferedReader reader  = new BufferedReader(new InputStreamReader(System.in));			
			//do
			{		
				// Configure the query. Queries only have description.
				TravelDescription queryDesc = new TravelDescription();
				queryDesc.setAccomodation("ThreeStars");
				queryDesc.setDuration(7);
				queryDesc.setHolidayType("Recreation");
				queryDesc.setNumberOfPersons(2);
				queryDesc.setPrice(700);
				
				CBRQuery query = new CBRQuery();
				query.setDescription(queryDesc);
				
				// Run a cycle with the query
				test1.cycle(query);
				
				System.out.println("Cycle finished. Type exit to idem or enter to repeat the cycle");
			}
			//while(!reader.readLine().equals("exit"));
			
			// Run the postcycle
			test1.postCycle();

			//Shutdown DDBB manager
		    	jcolibri.test.database.HSQLDBserver.shutDown();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
