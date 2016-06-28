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
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

import jcolibri.cbrcore.CBRCase;
import jcolibri.mtechkecbr.RecipeRecommender.RecipeDescription;
import jcolibri.mtechkecbr.RecipeRecommender.RecipeRecommender;
import jcolibri.mtechkecbr.RecipeRecommender.RecipeSolution;
import jcolibri.util.FileIO;

/**
 * Retain dialog
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class RetainDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private static int numcases = 0;
	private String recipeName;
	
	JLabel image;
	
	JTextField name;
	JTextField servings;
	JTextField cuisine;
	JTextField preptime;
	JTextField cooktime;
	JTextField typeOfMeal;
	JTextField equipment;
	JTextField cookingMethod;
	JTextField mainIngredient;
	JTextField difficultyLevel;
	JLabel caseId;
	JTextArea ingredients;
	JTextArea method;
	JTextField idEditor;
	JButton setId;
	JCheckBox saveCheck;
	
	ArrayList<CBRCase> cases;
	int currentCase;
	
	ArrayList<CBRCase> casesToRetain;
	
	public RetainDialog(JFrame main)
	{
		super(main,true);
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
		
		this.setTitle("Revise cases");

		
		image = new JLabel();
		image.setIcon(new ImageIcon(FileIO.findFile("jcolibri/mtechkecbr/RecipeRecommender/gui/step6.png")));
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

		panel.add(new JLabel("name"));
		panel.add(name = new JTextField());
		
		panel.add(new JLabel("Difficulty Level"));
		panel.add(this.difficultyLevel = new JTextField());
		
		panel.add(new JLabel("Serving size"));
		panel.add(this.servings = new JTextField());

		panel.add(new JLabel("Prep time"));
		panel.add(this.preptime = new JTextField());
		
		panel.add(new JLabel("Cooking time"));
		panel.add(this.cooktime = new JTextField());
		
		panel.add(new JLabel("Cuisine"));
		panel.add(this.cuisine = new JTextField());
		
		panel.add(new JLabel("Type Of Meal"));
		panel.add(this.typeOfMeal = new JTextField());
		
		panel.add(new JLabel("Equipment"));
		panel.add(this.equipment = new JTextField());
		
		panel.add(new JLabel("Cooking method"));
		panel.add(this.cookingMethod = new JTextField());
		
		panel.add(new JLabel("Main Ingredient"));
		panel.add(this.mainIngredient = new JTextField());
		
		panel.add(label = new JLabel("Solution"));
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		panel.add(label = new JLabel());

		panel.add(new JLabel("Ingredients"));
		ingredients = new JTextArea(2,1);
		JScrollPane scroller1 = new JScrollPane(ingredients,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scroller1);
		ingredients.setAutoscrolls(false);
		ingredients.setWrapStyleWord(true);
		ingredients.setLineWrap(true);
		ingredients.setEditable(false);
		
		panel.add(new JLabel("Method"));
		method = new JTextArea(3,1);
		JScrollPane scroller = new JScrollPane(method,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);	
		panel.add(scroller);		
		method.setAutoscrolls(false);
		method.setWrapStyleWord(true);
		method.setLineWrap(true);
		method.setEditable(false);
		
		
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
				currentCase = (currentCase+cases.size()-1) % cases.size();
				showCase();
			}
		});
		
		follow.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				currentCase = (currentCase+1) % cases.size();
				showCase();
			}
		});
		
		casesPanel.add(casesIterPanel, BorderLayout.NORTH);
		
		
		JPanel defineIdsPanel = new JPanel();
		saveCheck = new JCheckBox("Save Case with new Id:");
		defineIdsPanel.add(saveCheck);
		saveCheck.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				enableSaveCase();
			}
		});
		idEditor = new JTextField(20);
		defineIdsPanel.add(idEditor);
		setId = new JButton("Apply");
		defineIdsPanel.add(setId);
		
		setId.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				setId();
			}
		});
		enableSaveCase();
		
		casesPanel.add(defineIdsPanel, BorderLayout.SOUTH);
		
		JPanel panelAux = new JPanel();
		panelAux.setLayout(new BorderLayout());
		panelAux.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		panelAux.add(casesPanel,BorderLayout.NORTH);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new BorderLayout());
		
		JButton ok = new JButton("Next >>");
		ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
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
	
	void enableSaveCase()
	{
		idEditor.setEnabled(saveCheck.isSelected());
		setId.setEnabled(saveCheck.isSelected());
	}
	
	public void showCases(Collection<CBRCase> eval, int casebasesize)
	{
		cases = new ArrayList<CBRCase>(eval);
		casesToRetain = new ArrayList<CBRCase>();
		currentCase = 0;
		if(numcases<casebasesize)
			numcases = casebasesize+1;
		idEditor.setText("Recipe"+(++numcases));
		showCase();
	}
	
	void showCase()
	{
		
		CBRCase _case = cases.get(currentCase);
		this.caseId.setText(_case.getID().toString()+" ("+(currentCase+1)+"/"+cases.size()+")");
		
		RecipeDescription desc = (RecipeDescription) _case.getDescription();
		this.name.setText(desc.getCaseId());
		this.recipeName = desc.getCaseId();

		this.servings.setText(desc.getNumberOfPersons().toString());
		this.typeOfMeal.setText(desc.getTypeOfMeal());
		this.cooktime.setText(desc.getCookingDuration().toString());
		this.mainIngredient.setText(desc.getMainIngredient());
		this.difficultyLevel.setText(desc.getDifficultyLevel().toString());
		
		RecipeSolution sol = (RecipeSolution) _case.getSolution();
		this.cuisine.setText(sol.getCuisine().toString()); // temporarily placed in recipeSolution until field mapping is resolved.
		this.ingredients.setText(sol.getDetailedIngredients().toString());
		this.method.setText(sol.getMethod());
	}
	
	
	void setId()
	{
		CBRCase _case = cases.get(currentCase);
		cases.remove(_case);
		
		RecipeDescription desc = (RecipeDescription) _case.getDescription();
		desc.setCaseId(idEditor.getText());
		RecipeDescription sol = (RecipeDescription) _case.getSolution();
		sol.setCaseId(idEditor.getText());
		casesToRetain.add(_case);
		
		currentCase = 0;
		idEditor.setText("Journey"+(++numcases));
		saveCheck.setSelected(false);
		enableSaveCase();
		showCase();
	}
	
	
	public Collection<CBRCase> getCasestoRetain()
	{
		return casesToRetain;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RetainDialog qf = new RetainDialog(null);
		qf.setVisible(true);
		System.out.println("Bye");
	}

	

}
