/**
 * Test10.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 07/06/2007
 */
package jcolibri.test.test10;


import java.util.ArrayList;
import java.util.Collection;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.OntologyConnector;
import jcolibri.datatypes.Instance;
import jcolibri.exception.ExecutionException;
import jcolibri.exception.OntologyAccessException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.ontology.OntCosine;
import jcolibri.method.retrieve.NNretrieval.similarity.local.ontology.OntDeep;
import jcolibri.method.retrieve.NNretrieval.similarity.local.ontology.OntDeepBasic;
import jcolibri.method.retrieve.NNretrieval.similarity.local.ontology.OntDetail;
import jcolibri.method.retrieve.selection.SelectCases;


/**
 * This example shows how to use the ontology connector and the ontology-based similarity functions.
 * <br>
 * To use the ontology connector, all the attributes of the description and the solution must be Instance typed.
 * <p>
 * This test shows an example where a case is composed by a description and a solution following the mapping:
 * <p><center><img src="test10mapping.jpg"/></center></p>
 * To configure the connector with that mapping we use the configuration file:
 * <pre>
 * &lt;OntologyConfiguration&gt;
 *	&lt;MainOntology&gt;
 *		&lt;URL&gt;http://gaia.fdi.ucm.es/ontologies/vacation.owl&lt;/URL&gt;
 *		&lt;LocalCopy&gt;jcolibri/test/test10/vacation.owl&lt;/LocalCopy&gt;
 *	&lt;/MainOntology&gt;	
 *	
 *	&lt;!-- There are not subontologies --&gt;
 *	
 *	&lt;CaseMainConcept&gt;VACATION_CASE&lt;/CaseMainConcept&gt;
 *	
 *	&lt;DescriptionClassName&gt;jcolibri.test.test10.TravelDescription&lt;/DescriptionClassName&gt;
 *	&lt;DescriptionMappings&gt;
 *		&lt;Map&gt;
 *			&lt;Property&gt;HAS-DESTINATION&lt;/Property&gt;
 *			&lt;Concept&gt;DESTINATION&lt;/Concept&gt;
 *			&lt;Attribute&gt;Destination&lt;/Attribute&gt;
 *		&lt;/Map&gt;
 *		&lt;Map&gt;
 *			&lt;Property&gt;HAS-CATEGORY&lt;/Property&gt;
 *			&lt;Concept&gt;CATEGORY&lt;/Concept&gt;
 *			&lt;Attribute&gt;Accommodation&lt;/Attribute&gt;
 *		&lt;/Map&gt;
 *		&lt;Map&gt;
 *			&lt;Property&gt;HAS-PERSONS&lt;/Property&gt;
 *			&lt;Concept&gt;PERSONS&lt;/Concept&gt;
 *			&lt;Attribute&gt;NumberOfPersons&lt;/Attribute&gt;
 *		&lt;/Map&gt;
 *		&lt;Map&gt;
 *			&lt;Property&gt;HAS-TRANSPORTATION&lt;/Property&gt;
 *			&lt;Concept&gt;TRANSPORTATION&lt;/Concept&gt;
 *			&lt;Attribute&gt;Transportation&lt;/Attribute&gt;
 *		&lt;/Map&gt;
 *		&lt;Map&gt;
 *			&lt;Property&gt;HAS-SEASON&lt;/Property&gt;
 *			&lt;Concept&gt;SEASON&lt;/Concept&gt;
 *			&lt;Attribute&gt;Season&lt;/Attribute&gt;
 *		&lt;/Map&gt;
 *		&lt;Map&gt;
 *			&lt;Property&gt;HAS-HOLIDAY_TYPE&lt;/Property&gt;
 *			&lt;Concept&gt;HOLIDAY_TYPE&lt;/Concept&gt;
 *			&lt;Attribute&gt;HolidayType&lt;/Attribute&gt;
 *		&lt;/Map&gt;
 *		&lt;Map&gt;
 *			&lt;Property&gt;HAS-DURATION&lt;/Property&gt;
 *			&lt;Concept&gt;DURATION&lt;/Concept&gt;
 *			&lt;Attribute&gt;Duration&lt;/Attribute&gt;
 *		&lt;/Map&gt;
 *	&lt;/DescriptionMappings&gt;
 *	
 *	&lt;SolutionClassName&gt;jcolibri.test.test10.TravelSolution&lt;/SolutionClassName&gt;
 *	&lt;SolutionMappings&gt;
 *		&lt;Map&gt;
 *			&lt;Property&gt;HAS-PRICE&lt;/Property&gt;
 *			&lt;Concept&gt;PRICE&lt;/Concept&gt;
 *			&lt;Attribute&gt;price&lt;/Attribute&gt;
 *		&lt;/Map&gt;
 *	&lt;/SolutionMappings&gt;
 * &lt;/OntologyConfiguration&gt;
 * </pre>
 * <p>
 * The source code shows how to use the connector and then an complete CBR cycle
 * using the similarity functions implemented in the package:
 * jcolibri.method.retrieve.NNretrieval.similarity.local.ontology
 * <p><img src="ontsim.jpg"/></p>
 * 
 *  
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.connector.OntologyConnector
 * @see jcolibri.method.retrieve.NNretrieval.similarity.local.ontology
 * @see jcolibri.test.test10.TravelDescription
 * @see jcolibri.test.test10.TravelSolution
 */
public class Test10 implements StandardCBRApplication {

	Connector _connector;
	CBRCaseBase _caseBase;
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.BasicCBRApplication#configure()
	 */
	public void configure() throws ExecutionException{
		try{
		_connector = new OntologyConnector();
		_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("jcolibri/test/test10/ontologyconfig.xml"));
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
		// We are using ontology-based similarity functions
		NNConfig simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		simConfig.addMapping(new Attribute("Accommodation", TravelDescription.class), new OntCosine());
		simConfig.addMapping(new Attribute("Duration", TravelDescription.class), new OntDetail());
		simConfig.addMapping(new Attribute("HolidayType", TravelDescription.class), new OntDeep());
		simConfig.addMapping(new Attribute("NumberOfPersons", TravelDescription.class), new OntDeepBasic());
		simConfig.addMapping(new Attribute("Transportation", TravelDescription.class), new OntDeepBasic());
		simConfig.addMapping(new Attribute("Season", TravelDescription.class), new Equal());
		
		
		System.out.println("Query:");
		System.out.println(query);
		System.out.println();
		
		
		/********* Execute NN ************/
		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
		
		/********* Select cases **********/
		eval = SelectCases.selectTopKRR(eval, 10);
		
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
	 * Tests the OntologyConnector. It reads the cases, creates a new one, 
	 * stores the new case in the case base, closes the connector generating a new owl file,
	 * reads the cases again, deletes the new case and closes generating a new owl file that is equal to the original one.
	 */
	public static void main(String[] args) {
		try {
			
			//Configure connector and caseBase
			OntologyConnector _connector = new OntologyConnector();
			_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("jcolibri/test/test10/ontologyconfig.xml"));
			CBRCaseBase _caseBase  = new LinealCaseBase();
			
			//Load cases
			_caseBase.init(_connector);
			System.out.println("Loaded cases:");
			for(jcolibri.cbrcore.CBRCase c: _caseBase.getCases())
				System.out.println(c);

			//Obtain the first case
			CBRCase aCase = _caseBase.getCases().iterator().next();
			
			//Create a new copy
			aCase = jcolibri.util.CopyUtils.copyCBRCase(aCase);

			//Lets modify the id attribute with a new instance
			Instance newId = Instance.createInstance("newInstance", _connector.getCaseMainConcept());

			//modify the description id
			Attribute descriptionIdAttribute = aCase.getDescription().getIdAttribute();
			descriptionIdAttribute.setValue(aCase.getDescription(), newId);
			//modify the solution id
			Attribute solutionIdAttribute = aCase.getSolution().getIdAttribute();
			solutionIdAttribute.setValue(aCase.getSolution(), newId);
			
			//Store the new case
			ArrayList<CBRCase> cases = new ArrayList<CBRCase>();
			cases.add(aCase);
			_connector.storeCases(cases);
			
			//Close the connector. This generates a new owl file
			_connector.close();
			
			//Lets start again
			_connector = new OntologyConnector();
			_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("jcolibri/test/test10/ontologyconfig.xml"));
			_caseBase  = new LinealCaseBase();
			
			_caseBase.init(_connector);

			//Print cases with the new one
			System.out.println("Loaded cases: (the new case should be there)");
			for(jcolibri.cbrcore.CBRCase c: _caseBase.getCases())
			{
				System.out.println(c);
				//Obtain a reference to the new case
				if(c.getID().toString().equals("newInstance"))
					aCase = c;
			}
			
			//forget that case
			cases.clear();
			cases.add(aCase);
			_caseBase.forgetCases(cases);
			
			//close and generate a new owl file equal to the original one.
			_connector.close();
			
			
			
			/*************************************************************************************************************/
			/****************** Lets test the ontological similarity functions *******************************************/
			
			Test10 test = new Test10();
			
			//Normal configure and preCycle calls
			test.configure();
			test.preCycle();
			
			//Create a query
			TravelDescription queryDesc = new TravelDescription();
			try {
				queryDesc.setAccommodation(new Instance("THREESTARS"));
				queryDesc.setDuration(new Instance("I10"));
				queryDesc.setHolidayType(new Instance("RECREATION"));
				queryDesc.setNumberOfPersons(new Instance("I4"));
				queryDesc.setSeason(new Instance("April"));
				queryDesc.setTransportation(new Instance("PLANE"));
			} catch (OntologyAccessException e) {
				throw new ExecutionException(e);
			}
			CBRQuery query = new CBRQuery();
			query.setDescription(queryDesc);
			
			//Execute cycle with the query
			test.cycle(query);
			
			//Postcycle
			test.postCycle();
		} catch (ExecutionException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
