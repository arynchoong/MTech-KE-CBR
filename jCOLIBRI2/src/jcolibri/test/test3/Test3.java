/**
 * Test3.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 10/01/2007
 */
package jcolibri.test.test3;


import java.util.Collection;
import java.util.HashMap;

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
import jcolibri.method.reuse.CombineQueryAndCasesMethod;

/**
 * This example shows how to use a compound attribute in the description of a case and how to complete the CBR cycle.
 * <br>
 * Now, the description has not a string defining the region. Instead that, it has other bean named "Region" that stores different attributes about the region (nearest city, airport, currency...).
 * This way, the structure of the case is:
 * <pre>
 * Case
 *  |
 *  +- Description
 *          |
 *          +- caseId
 *          +- HollidayType
 *          +- Price
 *          +- NumberOfPersons
 *          +- Region
 *          |     |
 *          |     +- regionId
 *          |     +- regionName
 *          |     +- NearestCity
 *          |     +- Airport
 *          |     +- Currency
 *          +- Transportation
 *          +- Duration
 *          +- Season
 *          +- Accomodation
 *          +- Hotel
 * </pre>
 * <br>
 * This structure is mapped to the travelext data base that can be created using the travelext.sql file.
 * It is composed by two tables: travel and region. Travel has a number in the regionid column identifying the row in the region table that contains the information:
 * <pre>
 * travel:
 * +-----------+-------------+-------+-----------------+----------+----------------+----------+-----------+---------------+-----------------------------+
 * | caseId    | HolidayType | Price | NumberOfPersons | regionId | Transportation | Duration | Season    | Accommodation | Hotel                       |
 * +-----------+-------------+-------+-----------------+----------+----------------+----------+-----------+---------------+-----------------------------+
 * | Journey11 | City        |  1978 |               2 |        1 | Plane          |        7 | April     | ThreeStars    | Hotel Victoria, Cairo       |
 * | Journey21 | Recreation  |  1568 |               2 |        2 | Car            |       14 | May       | TwoStars      | Hotel Ostend, Belgium       |
 *  ...
 * region:
 * +----------+------------+-------------+---------------------------+----------------+
 * | regionId | RegionName | NearestCity | Airport                   | Currency       |
 * +----------+------------+-------------+---------------------------+----------------+
 * |        1 | Cairo      | Cairo       | Cairo Airport             | Egyptian Pound |
 * |        2 | Belgium    | Brussels    | Brussels National Airport | Euro           |
 *  ...
 * </pre>
 * To map the case structure with the data base we have to modify these lines in hibernate.cfg.xml:
 * <pre>
 * &lt;hibernate-mapping default-lazy="false"&gt;
 *   &lt;class name="jcolibri.test.test3.TravelDescription" table="travel"&gt;
 *      ...
 *      &lt;many-to-one name="Region" column="regionId" not-null="true" cascade="save-update"/&gt; 
 *      ...
 *   &lt;/class&gt;
 *   &lt;class name="jcolibri.test.test3.Region" table="region"&gt;
 *  	&lt;id name="id" column="regionId"&gt;
 *  	&lt;/id&gt;
 *  	&lt;property name="region" column="RegionName"/&gt;
 *  	&lt;property name="city" column="NearestCity"/&gt;
 *  	&lt;property name="airport" column="Airport"/&gt;
 *  	&lt;property name="currency" column="Currency"/&gt;
 *   &lt;/class&gt;
 * &lt;/hibernate-mapping&gt;
 * </pre>
 * The many-to-one tag indicates the association between the regionId column in the travel table with the values of the region table.
 * <p>
 * This example also shows how to complete the CBR cycle including reuse, revise and retain methods:
 * <ul>
 * <li>Reuse. To reuse a case we have to combine its solution with the description of the query. 
 * It is done by the method: CombineQueryAndCasesMethod.combine()
 * <li>Revise. A simple revision consists on defining new values for the ids of the compound attributes to save the new case into the persistence layer. 
 * It is done by: DefineNewIdsMethod.defineNewIdsMethod()
 * <li>Retain. Consists on saving a new case into the persistence layer. This is performed by: StoreCasesMethod.storeCase()
 * </ul>
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.test.test3.TravelDescription
 * @see jcolibri.test.test3.Region
 * @see jcolibri.method.reuse.CombineQueryAndCasesMethod
 * @see jcolibri.method.revise.DefineNewIdsMethod
 * @see jcolibri.method.retain.StoreCasesMethod
 * 
 */
public class Test3 implements StandardCBRApplication {

	Connector _connector;
	CBRCaseBase _caseBase;
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#configure()
	 */
	public void configure() throws ExecutionException{
		try{
		_connector = new DataBaseConnector();
		_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("jcolibri/test/test3/databaseconfig.xml"));
		_caseBase  = new LinealCaseBase();
		} catch (Exception e){
			throw new ExecutionException(e);
		}
	}

	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#preCycle()
	 */
	public CBRCaseBase preCycle() throws ExecutionException {
		_caseBase.init(_connector);	
		for(jcolibri.cbrcore.CBRCase c: _caseBase.getCases())
			System.out.println(c);
		return _caseBase;
	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#cycle()
	 */
	public void cycle(CBRQuery query) throws ExecutionException 
	{		
		/********* NumericSim Retrieval **********/
		
		NNConfig simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		simConfig.addMapping(new Attribute("Accommodation", TravelDescription.class), new Equal());
		Attribute duration = new Attribute("Duration", TravelDescription.class);
		simConfig.addMapping(duration, new Interval(31));
		simConfig.setWeight(duration, 0.5);
		simConfig.addMapping(new Attribute("HolidayType", TravelDescription.class), new Equal());
		simConfig.addMapping(new Attribute("NumberOfPersons", TravelDescription.class), new Equal());
		simConfig.addMapping(new Attribute("Price", TravelDescription.class), new Interval(4000));
		
		simConfig.addMapping(new Attribute("Region",   TravelDescription.class), new Average());
		simConfig.addMapping(new Attribute("region",   Region.class), new Equal());
		simConfig.addMapping(new Attribute("city",     Region.class), new Equal());
		simConfig.addMapping(new Attribute("airport",  Region.class), new Equal());
		simConfig.addMapping(new Attribute("currency", Region.class), new Equal());

		
		System.out.println("Query:");
		System.out.println(query);
		System.out.println();
		
		
		/********* Execute NN ************/
		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
		
		/********* Select cases **********/
		Collection<CBRCase> selectedcases = SelectCases.selectTopK(eval, 1);
		
		/********* Reuse **********/
		
		//Combine query description with cases solutions, obtaining a list of new cases.
		Collection<CBRCase> newcases = CombineQueryAndCasesMethod.combine(query, selectedcases);
		System.out.println("Combined cases");
		for(jcolibri.cbrcore.CBRCase c: newcases)
			System.out.println(c);
		
		/********* Revise **********/
		// Lets store only the best case
		CBRCase bestCase = newcases.iterator().next();
		
		// Define new ids for the compound attributes
		HashMap<Attribute, Object> componentsKeys = new HashMap<Attribute,Object>();
		componentsKeys.put(new Attribute("caseId",TravelDescription.class), "test3id");	
		componentsKeys.put(new Attribute("id",Region.class), 7);	
		jcolibri.method.revise.DefineNewIdsMethod.defineNewIdsMethod(bestCase, componentsKeys);
		
		System.out.println("Cases with new Id");
		System.out.println(bestCase);
		
		/********* Retain **********/
		
		// Uncomment next line to store cases into persistence
		//jcolibri.method.retain.StoreCasesMethod.storeCase(_caseBase, bestCase);
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

		Test3 test3 = new Test3();
		try {
			test3.configure();
			test3.preCycle();
			
			//BufferedReader reader  = new BufferedReader(new InputStreamReader(System.in));			
			//do
			//{	
			
			/********* Query Definition **********/
			TravelDescription queryDesc = new TravelDescription();
			queryDesc.setAccommodation(TravelDescription.AccommodationTypes.ThreeStars);
			queryDesc.setDuration(7);
			queryDesc.setHolidayType("Recreation");
			queryDesc.setNumberOfPersons(2);
			queryDesc.setPrice(700);
			
			Region region = new Region();
			region.setRegion("Bulgaria");
			region.setCity("Sofia");
			region.setCurrency("Euro");
			region.setAirport("airport");
			queryDesc.setRegion(region);
			
			CBRQuery query = new CBRQuery();
			query.setDescription(queryDesc);
			
			test3.cycle(query);
			
			//	System.out.println("Cycle finished. Type exit to idem");
			//}while(!reader.readLine().equals("exit"));
			
			test3.postCycle();
			
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
