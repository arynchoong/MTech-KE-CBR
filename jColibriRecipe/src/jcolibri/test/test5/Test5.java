/**
 * Test5.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 11/01/2007
 */
package jcolibri.test.test5;


import java.util.ArrayList;
import java.util.Collection;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.DataBaseConnector;
import jcolibri.datatypes.Instance;
import jcolibri.exception.ExecutionException;
import jcolibri.exception.OntologyAccessException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.NNretrieval.similarity.local.ontology.OntCosine;
import jcolibri.method.retrieve.selection.SelectCases;
import jcolibri.method.reuse.CombineQueryAndCasesMethod;
import jcolibri.method.reuse.NumericDirectProportionMethod;
import jcolibri.util.FileIO;
import es.ucm.fdi.gaia.ontobridge.OntoBridge;
import es.ucm.fdi.gaia.ontobridge.OntologyDocument;


/**
 * This example shows how to map an attribute to an ontology and compute an ontology-based similarity function in the KNN.
 * jCOLIBRI uses the OntoBridge library to do the mapping an manage the ontologies. 
 * <br>
 * The mapping consists on reading the values of an attribute from the persistence layer and link them with instances of an ontology.
 * In this example we are going to map the values of the Season column in the travelext database with the instances of the concept "SEASON" of the ontology vacation.owl.
 * This ontology contains information about travels and vacations. The SEASON concept organizes the information as shown in this figure:
 * <br><img src="vacation_season.jpg"/><br>
 * To map this concept with the attributes in the database we have to change the type of the season attribute in the description to <b>Instance</b>.
 * This type automatically connects with the ontology and links its value to the proper instance. To configure the mapping we have to modify the <b>travelDescription.hbm.xml</b> file pointing out that we are using a Instance:
 * <pre>
 *  ...
 * 	&lt;property name="Season" column="Season"&gt;
 *		&lt;type name="jcolibri.connector.databaseutils.GenericUserType"&gt;
 *			&lt;param name="className"&gt;jcolibri.datatypes.Instance&lt;/param&gt;
 *		&lt;/type&gt;
 *	&lt;/property&gt;
 *  ...
 * </pre>
 * And finally, in the configure() method of our application we have to setup OntoBridge with the ontology:
 * <pre>
 *      ...
 * 		OntoBridge ob = jcolibri.util.OntoBridgeSingleton.getOntoBridge();
 *		ob.initWithPelletReasoner();
 *		OntologyDocument mainOnto = new OntologyDocument("http://gaia.fdi.ucm.es/ontologies/vacation.owl", 
 *		FileIO.findFile("jcolibri/test/test5/vacation.owl").toURI().toString());
 *		ArrayList<OntologyDocument> subOntologies = new ArrayList<OntologyDocument>(); 		
 *		ob.loadOntology(mainOnto, subOntologies, false);
 *      ...
 * </pre>
 * With these changes the ontology is loaded and the values of Season are mapped to its instances. 
 * Now, we can compute similarity functions that use information from the ontology. Here we are using the OntCosine() method:
 * <pre>
 *      simConfig.addMapping(new Attribute("Season", TravelDescription.class), new OntCosine());
 * </pre>
 * For more information about OntoBridge read its documentation or visit the web page: <a href="http://gaia.fdi.ucm.es/projects/ontobridge/">http://gaia.fdi.ucm.es/projects/ontobridge/</a>.
 *   
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * 
 * @see jcolibri.test.test5.TravelDescription
 * @see jcolibri.method.retrieve.NNretrieval.similarity.local.ontology.OntCosine
 * 
 *
 */
public class Test5 implements StandardCBRApplication {

	Connector _connector;
	CBRCaseBase _caseBase;
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#configure()
	 */
	public void configure() throws ExecutionException{
		try{
		_connector = new DataBaseConnector();
		_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("jcolibri/test/test5/databaseconfig.xml"));
		_caseBase  = new LinealCaseBase();
		
		// Obtain a reference to OntoBridge
		OntoBridge ob = jcolibri.util.OntoBridgeSingleton.getOntoBridge();
		// Configure it to work with the Pellet reasoner
		ob.initWithPelletReasoner();
		// Setup the main ontology
		OntologyDocument mainOnto = new OntologyDocument("http://gaia.fdi.ucm.es/ontologies/vacation.owl", 
								 FileIO.findFile("jcolibri/test/test5/vacation.owl").toExternalForm());
		// There are not subontologies
		ArrayList<OntologyDocument> subOntologies = new ArrayList<OntologyDocument>();
		// Load the ontology
		ob.loadOntology(mainOnto, subOntologies, false);
		
		// Print the instances of SEASON
		System.out.println("Instances of SEASON");
		for(java.util.Iterator<String> i = ob.listInstances("SEASON"); i.hasNext();)
			System.out.println(i.next());
		
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
		
		// Configure the OntCosine() function for the similarity of Season
		simConfig.addMapping(new Attribute("Season", TravelDescription.class), new OntCosine());
		
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
		
		NumericDirectProportionMethod.directProportion(	new Attribute("NumberOfPersons",TravelDescription.class), 
													 	new Attribute("price",TravelSolution.class), 
													 	query, selectedcases);
		NumericDirectProportionMethod.directProportion(	new Attribute("Duration",TravelDescription.class), 
			 											new Attribute("price",TravelSolution.class), 
			 											query, selectedcases);
		// Print the retrieval
		System.out.println("Retrieved cases:");
		for(RetrievalResult nse: eval)
			System.out.println(nse);
		
		System.out.println("Query:");
		System.out.println(query);
		
		Collection<CBRCase> newcases = CombineQueryAndCasesMethod.combine(query, selectedcases);
		System.out.println("Combined cases");
		for(jcolibri.cbrcore.CBRCase c: newcases)
			System.out.println(c);
		
		/********* Revise **********/
		/*
		HashMap<Attribute, Object> componentsKeys = new HashMap<Attribute,Object>();
		componentsKeys.put(new Attribute("caseId",TravelDescription.class), "test3id");	
		componentsKeys.put(new Attribute("id",TravelSolution.class), "test3id");	
		jcolibri.method.revise.DefineNewIdsMethod.defineNewIdsMethod(newcases, componentsKeys);
		
		System.out.println("Cases with new Id");
		for(jcolibri.cbrcore.CBRCase c: newcases)
			System.out.println(c);
		*/
		/********* Retain **********/
		//jcolibri.method.retain.StoreCasesMethod.storeCases(_caseBase, newcases);
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

		Test5 test4 = new Test5();
		try {
			test4.configure();
			test4.preCycle();
			
			//BufferedReader reader  = new BufferedReader(new InputStreamReader(System.in));			
			//do
			//{		
			/********* Query Definition **********/
			TravelDescription queryDesc = new TravelDescription();
			queryDesc.setAccommodation(TravelDescription.AccommodationTypes.ThreeStars);
			queryDesc.setDuration(10);
			queryDesc.setHolidayType("Recreation");
			queryDesc.setNumberOfPersons(4);
			try {
				queryDesc.setSeason(new Instance("April"));
			} catch (OntologyAccessException e) {
				throw new ExecutionException(e);
			}
			
			Region region = new Region();
			region.setRegion("Bulgaria");
			region.setCity("Sofia");
			region.setCurrency("Euro");
			region.setAirport("airport");
			queryDesc.setRegion(region);
			
			CBRQuery query = new CBRQuery();
			query.setDescription(queryDesc);
			
			test4.cycle(query);
			
			//	System.out.println("Cycle finished. Type exit to idem");
			//}while(!reader.readLine().equals("exit"));
			
			test4.postCycle();
			
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
