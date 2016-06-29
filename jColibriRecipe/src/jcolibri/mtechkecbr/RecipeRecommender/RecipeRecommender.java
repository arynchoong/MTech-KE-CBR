/**
 * Recipe Recommender for MTech KE CBR project PT-02
 * adapted from:
 * Travel Recommender example for the jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 25/07/2006
 */
package jcolibri.mtechkecbr.RecipeRecommender;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.DataBaseConnector;
import jcolibri.mtechkecbr.RecipeRecommender.gui.AutoAdaptationDialog;
import jcolibri.mtechkecbr.RecipeRecommender.gui.QueryDialog;
import jcolibri.mtechkecbr.RecipeRecommender.gui.ResultDialog;
import jcolibri.mtechkecbr.RecipeRecommender.gui.RetainDialog;
import jcolibri.mtechkecbr.RecipeRecommender.gui.RevisionDialog;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.selection.SelectCases;
import jcolibri.method.reuse.NumericDirectProportionMethod;
import jcolibri.util.FileIO;
import es.ucm.fdi.gaia.ontobridge.OntoBridge;
import es.ucm.fdi.gaia.ontobridge.OntologyDocument;
import jcolibri.mtechkecbr.RecipeRecommender.SimilAlgo;


/**
 * Implementes the recommender main class
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class RecipeRecommender implements StandardCBRApplication {

	private static RecipeRecommender _instance = null;
	public  static RecipeRecommender getInstance()
	{
		if(_instance == null)
		   _instance = new RecipeRecommender();
		return _instance;
	}
	
	private RecipeRecommender()
	{
	}
	
	/** Connector object */
	Connector _connector;
	/** CaseBase object */
	CBRCaseBase _caseBase;

	ResultDialog resultDialog;
	AutoAdaptationDialog autoAdaptDialog;
	RevisionDialog revisionDialog;
	RetainDialog retainDialog;
	static Boolean m_bHalalOption=false;
	static int m_nHalalOptionPriority=99;
	static Boolean m_bVeganOption=false;
	static int m_nVeganOptionPriority=99;
	static Boolean m_bNutsFreeOption=false;
	static int m_nNutsOptionPriority=99;
	static Boolean m_bHealthyOption=false;
	static int m_nHealthyOptionPriority=99;
	static Boolean m_bNonSpicyOption=false;
	static int m_nNonSpicyOptionPriority=99;
	
	
	public void configure() throws ExecutionException {
		try {
			//Emulate data base server
			jcolibri.test.database.HSQLDBserver.init();
			
			// Create a data base connector
			_connector = new DataBaseConnector();
			// Init the ddbb connector with the config file
			_connector.initFromXMLfile(jcolibri.util.FileIO
					.findFile("jcolibri/mtechkecbr/RecipeRecommender/databaseconfig.xml"));
			// Create a Lineal case base for in-memory organization
			_caseBase = new LinealCaseBase();
			
			// Obtain a reference to OntoBridge
			OntoBridge ob = jcolibri.util.OntoBridgeSingleton.getOntoBridge();
			// Configure it to work with the Pellet reasoner
			ob.initWithPelletReasoner();
			// Setup the main ontology
			OntologyDocument mainOnto = new OntologyDocument("http://gaia.fdi.ucm.es/ontologies/recipe-cuisines.owl", 
									 FileIO.findFile("jcolibri/mtechkecbr/RecipeRecommender/recipe-cuisines.owl").toExternalForm());
			// There are not subontologies
			ArrayList<OntologyDocument> subOntologies = new ArrayList<OntologyDocument>();
			// Load the ontology
			ob.loadOntology(mainOnto, subOntologies, false);

			// Create the dialogs
			resultDialog     = new ResultDialog(main);
			autoAdaptDialog  = new AutoAdaptationDialog(main);
			revisionDialog   = new RevisionDialog(main);
			retainDialog     = new RetainDialog(main);
			
		} catch (Exception e) {
			throw new ExecutionException(e);
		}
	}
	
	private static Integer nullAttributes;
	private static Integer AttributesSize;
	private static double[] AttrWeights = new double[7];
	//******************************************************************/
	// Similarity
	//******************************************************************/
	
	/** Configures the similarity */
	private NNConfig getSimilarityConfig() {
		NNConfig simConfig = new NNConfig();
		
		simConfig
				.setDescriptionSimFunction(new jcolibri.method.retrieve.NNretrieval.similarity.global.Average());
		Attribute attribute0 = new Attribute("DifficultyLevel",
				RecipeDescription.class);		
		simConfig
				.addMapping(
						attribute0,
						new jcolibri.method.retrieve.NNretrieval.similarity.local.Equal());
		simConfig.setWeight(attribute0, AttrWeights[0]);
		Attribute attribute1 = new Attribute("NumberOfPersons", RecipeDescription.class);
		simConfig
				.addMapping(
						attribute1,
						new jcolibri.method.retrieve.NNretrieval.similarity.local.Equal());
		simConfig.setWeight(attribute1, AttrWeights[1]);
		Attribute attribute2 = new Attribute("CookingDuration", RecipeDescription.class);
		simConfig
				.addMapping(
						attribute2,
						new jcolibri.method.retrieve.NNretrieval.similarity.local.Threshold(new Double(10))); // ten minutes +- 
		simConfig.setWeight(attribute2, AttrWeights[2]);
		//
		Attribute attribute3 = new Attribute("Cuisine", RecipeDescription.class);
		simConfig
				.addMapping(
						attribute3,
						new jcolibri.method.retrieve.NNretrieval.similarity.local.Equal());
		simConfig.setWeight(attribute3, AttrWeights[3]);
		//
		Attribute attribute4 = new Attribute("TypeOfMeal", RecipeDescription.class);
		simConfig
				.addMapping(
						attribute4,
						new jcolibri.method.retrieve.NNretrieval.similarity.local.MaxString());
		simConfig.setWeight(attribute4, AttrWeights[4]);
		//
		Attribute attribute5 = new Attribute("MainIngredient", RecipeDescription.class);
		simConfig
				.addMapping(
						attribute5,
						new jcolibri.method.retrieve.NNretrieval.similarity.local.MaxString());
		simConfig.setWeight(attribute5, AttrWeights[5]);
		//
		Attribute attribute6 = new Attribute("CookingMethod", RecipeDescription.class);
		simConfig
				.addMapping(
						attribute6,
						new jcolibri.method.retrieve.NNretrieval.similarity.local.MaxString());
		simConfig.setWeight(attribute6, AttrWeights[6]);
		//
		return simConfig;
	}
	
	public CBRCaseBase preCycle() throws ExecutionException {
		// Load cases from connector into the case base
		_caseBase.init(_connector);		
		// Print the cases
		java.util.Collection<CBRCase> cases = _caseBase.getCases();
		for(CBRCase c: cases)
			System.out.println(c);
		return _caseBase;
	}

	public void cycle(CBRQuery query) throws ExecutionException {
		// SIMILARITY ALGORITHM
		NNConfig simConfig = getSimilarityConfig();
		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(
				_caseBase.getCases(), query, simConfig);
		// Select k cases
		Collection<CBRCase> selectedcases = SelectCases.selectTopK(eval, 5);
		ArrayList<CBRCase> updatedcases = new ArrayList<CBRCase>(); 
		if (m_bNonSpicyOption)
		{
			String szDescString;			
			for (CBRCase scase : selectedcases)
			{
				szDescString = scase.getDescription().toString();				
				if (! (szDescString.toLowerCase().contains("chillies") || szDescString.toLowerCase().contains("chilli") || szDescString.toLowerCase().contains("curry")) )
					updatedcases.add(scase);				
			}
		}				
		
		// Show result
		resultDialog.showCases(eval, selectedcases);
		resultDialog.setVisible(true);
		
		// Show adaptation dialog
		autoAdaptDialog.setVisible(true);
		
		// Adapt depending on user selection
		if(autoAdaptDialog.adapt_Difficulty_Preptime())
		{
			// Compute a direct proportion between the "Difficulty Level" and "Prep time" attributes.
			NumericDirectProportionMethod.directProportion(	new Attribute("DifficultyLevel",RecipeDescription.class), 
				 											new Attribute("PrepDuration",RecipeDescription.class), 
				 											query, selectedcases);
		}
		
		if(autoAdaptDialog.adapt_Servings_Cooktime())
		{
			// Compute a direct proportion between the "Serving size" and "Cooking duration" attributes.
			NumericDirectProportionMethod.directProportion(	new Attribute("NumberOfPersons",RecipeDescription.class), 
				 											new Attribute("CookingDuration",RecipeDescription.class), 
				 											query, selectedcases);
		}
		
		// Revise
		revisionDialog.showCases(selectedcases);
		revisionDialog.setVisible(true);
		
		// Retain
		retainDialog.showCases(selectedcases, _caseBase.getCases().size());
		retainDialog.setVisible(true);
		Collection<CBRCase> casesToRetain = retainDialog.getCasestoRetain();
		_caseBase.learnCases(casesToRetain);
		
	}

	public void postCycle() throws ExecutionException {
		_connector.close();
		jcolibri.test.database.HSQLDBserver.shutDown();
	}

	static JFrame main;
	void showMainFrame()
	{
		main = new JFrame("Recipe Recommender");
		main.setResizable(false);
		main.setUndecorated(true);
		JLabel label = new JLabel(new ImageIcon(jcolibri.util.FileIO.findFile("/jcolibri/test/main/jcolibri2.jpg")));
		main.getContentPane().add(label);
		main.pack();
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		main.setBounds((screenSize.width - main.getWidth()) / 2,
			(screenSize.height - main.getHeight()) / 2, 
			main.getWidth(),
			main.getHeight());
		main.setVisible(true);
	}
	
	public static void main(String[] args) {
	
		RecipeRecommender recommender = getInstance();
		recommender.showMainFrame();
		
		try
		{
			recommender.configure();
			recommender.preCycle();
			
			QueryDialog qf = new QueryDialog(main);
			

			boolean cont = true;
			while(cont)
			{
				qf.setVisible(true);
				ArrayList<SimilAlgo> sconf = qf.getVals();
				ArrayList<OtherUserOption> otheruseroptions = qf.getOtherUserOptions();
				System.out.println(sconf);			
				
				CBRQuery query = new CBRQuery();
				RecipeDescription desc = new RecipeDescription();
				
				String sMethodName;
				String sMethodVal;
				java.lang.reflect.Method method;
				
				for (OtherUserOption opt : otheruseroptions) {
					if (opt.getAttributeName().contains("Healthy"))
					{
						m_bHealthyOption = opt.getAttributeValue();
						m_nHealthyOptionPriority = opt.getAttributePriority();
					}
					else if (opt.getAttributeName().contains("Halal"))
					{
						m_bHalalOption = opt.getAttributeValue();
						m_nHalalOptionPriority = opt.getAttributePriority();
					}
					else if (opt.getAttributeName().contains("Vegan"))
					{
						m_bVeganOption = opt.getAttributeValue();
						m_nVeganOptionPriority = opt.getAttributePriority();
					}
					else if (opt.getAttributeName().contains("NutsFree"))
					{
						m_bNutsFreeOption = opt.getAttributeValue();
						m_nVeganOptionPriority = opt.getAttributePriority();
					}
					else if (opt.getAttributeName().contains("NonSpicy"))
					{
						m_bNonSpicyOption = opt.getAttributeValue();
						m_nNonSpicyOptionPriority = opt.getAttributePriority();
					}
				    System.out.println(opt);
				}
				
				int k=0;
				AttributesSize = sconf.size();
				nullAttributes = 0;	
				for (SimilAlgo p : sconf) {
					sMethodName = "set"+p.getAttributeName();
					if (p.getAttributeName().contains("NumberOfPersons") || p.getAttributeName().contains("CookingDuration")){
						method = desc.getClass().getDeclaredMethod(sMethodName,Integer.class);
					}
					else
						method = desc.getClass().getDeclaredMethod(sMethodName,String.class);
					System.out.println(sMethodName);
					sMethodVal = p.getAttributeValue();
					if (p.getAttributeName().contains("NumberOfPersons") || p.getAttributeName().contains("CookingDuration")){
						if (sMethodVal.contains("null")) {
							sMethodVal="0";
						}
						method.invoke(desc, Integer.parseInt(sMethodVal));
					}
					else
						method.invoke(desc, sMethodVal);
				    //System.out.println(p);
				}
				
				// Weights calculation here
				System.out.println(AttributesSize.toString()+" "+nullAttributes.toString());
				for (int i=0; i<sconf.size();i++){
					if ((m_bHealthyOption == true)&&(m_nHealthyOptionPriority != 99))
					{
						if (sconf.get(i).getAttributeName().contains("CookingMethod"))
						{
							method = desc.getClass().getDeclaredMethod("setCookingMethod",String.class);
							AttrWeights[i] = 1.0;
							method.invoke(desc, "steam");
						}						
					}
					if (((m_bHalalOption == true)&&(m_nHalalOptionPriority != 99))||((m_bVeganOption==true)&&(m_nVeganOptionPriority != 99)))
					{
						if (sconf.get(i).getAttributeName().contains("Cuisine"))
						{
							method = desc.getClass().getDeclaredMethod("setCuisine",String.class);
							method.invoke(desc, "Indonesian");	
							if (m_bHalalOption & !m_bVeganOption)						
								AttrWeights[i] = 1;
							else
								AttrWeights[i] = 0.7;
						}
						else if (sconf.get(i).getAttributeName().contains("MainIngredient"))
						{
							method = desc.getClass().getDeclaredMethod("setMainIngredient",String.class);
							method.invoke(desc, "Vegetable");								
							if (m_bVeganOption & !m_bHalalOption)
								AttrWeights[i] = 1;								
							else
								AttrWeights[i] = 0.7;
						}
						else {
							if (sconf.get(i).getAttributePriority()!=99) {
								// Do not overwrite weights set when Healthy Option is true
								if ((m_bHealthyOption == false)||(!sconf.get(i).getAttributeName().contains("CookingMethod")))
								{
									if (sconf.get(i).getAttributePriority()>6)
										AttrWeights[i] = (7 - ((double) sconf.get(i).getAttributePriority()-5)) / 6;
									else
										AttrWeights[i] = (7 - (double) sconf.get(i).getAttributePriority()) / 6;
								}
							}							
						}
					}
					else
					{
						if (sconf.get(i).getAttributePriority()!=99) {
							// Do not overwrite weights set when Healthy Option is true
							if ((m_bHealthyOption == false)||(!sconf.get(i).getAttributeName().contains("CookingMethod")))
							{
								if (sconf.get(i).getAttributePriority()>6)
									AttrWeights[i] = (7 - ((double) sconf.get(i).getAttributePriority()-5)) / 6;
								else
									AttrWeights[i] = (7 - (double) sconf.get(i).getAttributePriority()) / 6;
							}
						}
					}
					System.out.print("["+sconf.get(i).getAttributePriority().toString()+"|"+AttrWeights[i]+"]");
				}

				query.setDescription(desc);
				recommender.cycle(query);
				int ans = javax.swing.JOptionPane.showConfirmDialog(null, "CBR cycle finished, query again?", "Cycle finished", javax.swing.JOptionPane.YES_NO_OPTION);
				cont = (ans == javax.swing.JOptionPane.YES_OPTION);
			}

			recommender.postCycle();
		}catch(Exception e)
		{
			org.apache.commons.logging.LogFactory.getLog(RecipeRecommender.class).error(e);
			javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
		}
		System.exit(0);
	}
}

