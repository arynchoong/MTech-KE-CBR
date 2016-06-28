/**
 * Travel Recommender example for the jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 25/07/2006
 */
package jcolibri.mtechkecbr.RecipeRecommender.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

import jcolibri.cbrcore.CBRCase;
import jcolibri.mtechkecbr.RecipeRecommender.RecipeDescription;
import jcolibri.mtechkecbr.RecipeRecommender.RecipeSolution;
import jcolibri.mtechkecbr.RecipeRecommender.RecipeRecommender;
import jcolibri.util.FileIO;

/**
 * Revision Dialog
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class RevisionDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	JLabel image;
	
	JTextField name;
	JComboBox difficulty;
	SpinnerNumberModel  servingSize;
	SpinnerNumberModel  prepTime;
	SpinnerNumberModel  cookTime;
	CuisineSelector cuisine;
	JComboBox dishType;
	JComboBox equipment;
	JComboBox cookingMethod;
	JComboBox mainIngredient;
	JLabel caseId;
	JTextField ingredients;
	JTextField method;
	
	ArrayList<CBRCase> cases;
	int currentCase;
	
	public RevisionDialog(JFrame main)
	{
		super(main, true);
		configureFrame();
	}
	
	private void configureFrame()
	{
		try
		{
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1)
		{
		}
		
		this.setTitle("Revise Cases");

		
		image = new JLabel();
		image.setIcon(new ImageIcon(FileIO.findFile("jcolibri/mtechkecbr/RecipeRecommender/gui/step5.png")));
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(image, BorderLayout.WEST);
		
		
		/**********************************************************/
		JPanel panel = new JPanel();
		//panel.setLayout(new GridLayout(8,2));
		panel.setLayout(new SpringLayout());
		
		JLabel label;

		panel.add(label = new JLabel("Description"));
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		panel.add(label = new JLabel());

		panel.add(new JLabel("Name"));
		panel.add(name = new JTextField());

		panel.add(new JLabel("Difficulty level"));
		String[] difficultyLevels = {"easy","medium","hard"};
		panel.add(difficulty = new JComboBox(difficultyLevels));

		panel.add(new JLabel("Serving size"));
		servingSize = new SpinnerNumberModel(2,1,12,1); 
		panel.add(new JSpinner(servingSize));
	
		panel.add(new JLabel("Prep duration"));
		prepTime = new SpinnerNumberModel(180,10,180,10); 
		panel.add(new JSpinner(prepTime));
		
		panel.add(new JLabel("Cooking duration"));
		cookTime = new SpinnerNumberModel(180,30,180,30); 
		panel.add(new JSpinner(cookTime));
		
		panel.add(new JLabel("Cuisine"));
		//String[] cuisines = {
		panel.add(cuisine = new CuisineSelector(this));
		
		panel.add(new JLabel("Type of Meal"));
		String[] TypeOfMeals = {"Anything", "Appetizer", "Main", "Dessert", "Snacks", "Drinks"};
		panel.add(dishType = new JComboBox(TypeOfMeals));
		
		panel.add(new JLabel("Equipment"));
		String[] Equipments = {"Anything", "Wok", "Oven", "Pot", "Frying pan", "Blender"};
		panel.add(equipment = new JComboBox(Equipments));

		panel.add(new JLabel("Cooking method"));
		String[] cookingMethods = {"Anything", "Stir frying", "Pan frying", "Deep frying", "Bake", "Steam", "Boiling", "Roasting", "Grill", "Mix"};
		panel.add(cookingMethod = new JComboBox(cookingMethods));
		
		panel.add(new JLabel("Main ingredient"));
		String[] mainIngredients = {"Anything", "Seafood", "Chicken", "Pork", "Beef", "Mutton", "Vegetables", "Fruits", "Rice", "Tofu", "Eggs", "Sweet"};
		panel.add(mainIngredient = new JComboBox(mainIngredients));
		
		panel.add(label = new JLabel("Solution"));
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		panel.add(label = new JLabel());

		panel.add(new JLabel("Ingredients"));
		panel.add(ingredients = new JTextField());
		
		panel.add(new JLabel("Method"));
		panel.add(method = new JTextField());
	
//		Lay out the panel.
		Utils.makeCompactGrid(panel,
		                14, 2, //rows, cols
		                6, 6,        //initX, initY
		                30, 10);       //xPad, yPad
		
		JPanel casesPanel = new JPanel();
		casesPanel.setLayout(new BorderLayout());
		casesPanel.add(panel, BorderLayout.CENTER);
		
		JPanel casesIterPanel = new JPanel();
		casesIterPanel.setLayout(new FlowLayout());
		JButton prev = new JButton("<<");
		casesIterPanel.add(prev);
		casesIterPanel.add(caseId = new JLabel("Case id"));
		JButton follow = new JButton(">>");
		casesIterPanel.add(follow);
		
		prev.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				saveCase();
				currentCase = (currentCase+cases.size()-1) % cases.size();
				showCase();
			}
		});
		
		follow.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				saveCase();
				currentCase = (currentCase+1) % cases.size();
				showCase();
			}
		});
		
		casesPanel.add(casesIterPanel, BorderLayout.NORTH);
		
		JPanel buttonsDetailed = new JPanel();
		buttonsDetailed.setLayout(new BorderLayout());	
		buttonsDetailed.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JButton MethodDetailed = new JButton("Method Detailed");
		MethodDetailed.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ShowCookingMethodDetails();
			}
		});
		buttonsDetailed.add(MethodDetailed, BorderLayout.CENTER);
		JButton IngredientDetailed = new JButton("Ingredient Detailed");
		IngredientDetailed.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ShowIngredientsDetails();			}
		});
		buttonsDetailed.add(IngredientDetailed, BorderLayout.EAST);

		JPanel panelAux = new JPanel();
		panelAux.setLayout(new BorderLayout());
		panelAux.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panelAux.add(casesPanel,BorderLayout.NORTH);
		panelAux.add(buttonsDetailed, BorderLayout.AFTER_LINE_ENDS);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new BorderLayout());
		
		JButton ok = new JButton("Set Revisions >>");
		ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				saveCase();
				next();
			}
		});
		buttons.add(ok,BorderLayout.CENTER);
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					RecipeRecommender.getInstance().postCycle();
				} catch (Exception ex) {
					org.apache.commons.logging.LogFactory.getLog(RecipeRecommender.class).error(ex);
				}
				System.exit(-1);
			}
		});
		buttons.add(exit,BorderLayout.WEST);
		
		panelAux.add(buttons, BorderLayout.SOUTH);
		this.getContentPane().add(panelAux, BorderLayout.CENTER);
		
		/**********************************************************/
		
		
		this.pack();
		this.setSize(600, this.getHeight());
		this.setResizable(false);
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width - this.getWidth()) / 2,
			(screenSize.height - this.getHeight()) / 2, 
			getWidth(),
			getHeight());
	}
	
	void next()
	{
		this.setVisible(false);
	}
	
	
	public void showCases(Collection<CBRCase> cases)
	{
		this.cases = new ArrayList<CBRCase>(cases);
		currentCase = 0;
		showCase();
	}
	
	void showCase()
	{
		CBRCase _case = cases.get(currentCase);
		this.caseId.setText(_case.getID().toString()+" ("+(currentCase+1)+"/"+cases.size()+")");
		
		RecipeDescription desc = (RecipeDescription) _case.getDescription();
		
		this.name.setText(desc.getCaseId());
		this.difficulty.setSelectedItem(desc.getDifficultyLevel().toString());
		this.servingSize.setValue(desc.getNumberOfPersons());
		this.prepTime.setValue(desc.getPrepDuration());
		this.cookTime.setValue(desc.getCookingDuration());
		this.dishType.setSelectedItem(desc.getTypeOfMeal());
		this.equipment.setSelectedItem(desc.getEquipment());
		this.cookingMethod.setSelectedItem(desc.getCookingMethod());
		this.mainIngredient.setSelectedItem(desc.getMainIngredient());
		
		RecipeSolution sol = (RecipeSolution) _case.getSolution();
		this.cuisine.setSelectedInstance(sol.getCuisine()); // temporarily placed in recipeSolution until field mapping is resolved.
		this.ingredients.setText(sol.getDetailedIngredients().toString());
		this.method.setText(sol.getMethod());
	}

	void ShowCookingMethodDetails()
	{
		String szMessage;
		String szFormattedMessage;
		szMessage = (String) this.method.getText();
		szFormattedMessage = szMessage.replace(";", "\n");
		JOptionPane.showMessageDialog (this, szFormattedMessage, "Method Detailed", JOptionPane.INFORMATION_MESSAGE);	
	}
	void ShowIngredientsDetails()
	{
		String szMessage;
		String szFormattedMessage;
		szMessage = (String) this.ingredients.getText();
		szFormattedMessage = szMessage.replace(";", "\n");
		JOptionPane.showMessageDialog (this, szFormattedMessage, "Ingredients Detailed", JOptionPane.INFORMATION_MESSAGE);	
	}
	void saveCase()
	{
		CBRCase _case = cases.get(currentCase);
		this.caseId.setText(_case.getID().toString()+" ("+(currentCase+1)+"/"+cases.size()+")");
		
		RecipeDescription desc = (RecipeDescription) _case.getDescription();
		desc.setCaseId(this.name.getText());
		desc.setDifficultyLevel((String)this.difficulty.getSelectedItem());
		desc.setNumberOfPersons(this.servingSize.getNumber().intValue());
		desc.setPrepDuration(this.prepTime.getNumber().intValue());
		desc.setCookingDuration(this.cookTime.getNumber().intValue());
		desc.setTypeOfMeal((String)this.dishType.getSelectedItem());
		desc.setCookingMethod((String)this.cookingMethod.getSelectedItem());
		desc.setMainIngredient((String)this.mainIngredient.getSelectedItem());
		
		RecipeSolution sol = (RecipeSolution) _case.getSolution();
		sol.setCuisine(this.cuisine.getSelectedInstance()); 		// temporarily placed in recipeSolution until field mapping is resolved.
		sol.setDetailedIngredients(this.ingredients.getText());
		sol.setMethod(this.method.getText());
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RevisionDialog qf = new RevisionDialog(null);
		qf.setVisible(true);
		System.out.println("Bye");
	}

	

}
