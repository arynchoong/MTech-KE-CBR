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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

import jcolibri.cbrcore.CBRQuery;
import jcolibri.mtechkecbr.RecipeRecommender.RecipeDescription;
import jcolibri.mtechkecbr.RecipeRecommender.RecipeRecommender;
import jcolibri.mtechkecbr.RecipeRecommender.RecipeDescription.AccommodationTypes;
import jcolibri.mtechkecbr.RecipeRecommender.RecipeDescription.Seasons;
import jcolibri.util.FileIO;

/**
 * Query dialgo
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class QueryDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	JLabel image;
	
	JComboBox MainIngredient;
	JComboBox TypeOfMeal;
	JComboBox DietaryRequirement;
	JComboBox TypeOfCuisine;
	JComboBox DifficultyLevel;
	JCheckBox HealthyOption;
	SpinnerNumberModel numberOfPersons;
	SpinnerNumberModel CookingDuration;
	RegionSelector region;
	SpinnerNumberModel  duration;
	
	public QueryDialog(JFrame parent)
	{
		super(parent,true);
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
		
		this.setTitle("Configure Query");

		
		image = new JLabel();
		image.setIcon(new ImageIcon(FileIO.findFile("jcolibri/mtechkecbr/RecipeRecommender/gui/step1.png")));
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(image, BorderLayout.WEST);
		
		
		/**********************************************************/
		JPanel panel = new JPanel();
		//panel.setLayout(new GridLayout(8,2));
		panel.setLayout(new SpringLayout());
		
		JLabel label;
		panel.add(label = new JLabel("Criteria"));
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		panel.add(label = new JLabel("Value(s)"));
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		
		panel.add(new JLabel("Main Ingredient"));
		String[] MainIngredients = {"Anything", "Chicken", "Duck", "Pork", "Dough", "Vegetables", "Fruits"};
		panel.add(MainIngredient = new JComboBox(MainIngredients));
		
		panel.add(new JLabel("Type Of Meal"));
		String[] TypeOfMeals = {"Anything", "Appetizer", "Main Course", "Dessert", "Drinks"};
		panel.add(TypeOfMeal = new JComboBox(TypeOfMeals));

		panel.add(new JLabel("Dietary Requirement"));
		String[] DietaryRequirements = {"None","Vegan","Halal","Nuts Free", "Non Spicy"};
		panel.add(DietaryRequirement = new JComboBox(DietaryRequirements));

		panel.add(new JLabel("Type Of Cuisine"));
		String[] TypeOfCuisines = {"Anything", "Chinese", "Indian", "Malay/Indonesian", "Nyonya", "Western"};
		panel.add(TypeOfCuisine = new JComboBox(TypeOfCuisines));

		panel.add(new JLabel("Cooking Duration (Maximum)"));
		CookingDuration = new SpinnerNumberModel(30,10,120,10); 
		panel.add(new JSpinner(CookingDuration));

		panel.add(new JLabel("Difficulty Level"));
		String[] DifficultyLevels = {"Anything", "Easy", "Medium", "Hard"};
		panel.add(DifficultyLevel = new JComboBox(DifficultyLevels));

		panel.add(new JLabel("Size Of Meal (Number of Persons)"));
		numberOfPersons = new SpinnerNumberModel(2,2,20,1); 
		panel.add(new JSpinner(numberOfPersons));
		
		panel.add(new JLabel("Others"));
		panel.add(HealthyOption = new JCheckBox("Healthy Options /(less oil, non deep fry, etc/)"));
		
//		Lay out the panel.
		Utils.makeCompactGrid(panel,
		                8, 2, //rows, cols
		                6, 6,        //initX, initY
		                10, 10);       //xPad, yPad
		
		JPanel panelAux = new JPanel();
		panelAux.setLayout(new BorderLayout());
		panelAux.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		panelAux.add(panel,BorderLayout.NORTH);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new BorderLayout());
		
		JButton ok = new JButton("Set Query >>");
		ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				setQuery();
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
	
	void setQuery()
	{
		this.setVisible(false);
	}
	
	public CBRQuery getQuery()
	{
		RecipeDescription desc = new RecipeDescription();
//		desc.setAccommodation(AccommodationTypes.valueOf((String)this.accommodation.getSelectedItem()));
//		desc.setDuration(this.duration.getNumber().intValue());
//		desc.setHolidayType((String)this.holidayType.getSelectedItem());
//		desc.setNumberOfPersons(this.numberOfPersons.getNumber().intValue());
//		desc.setRegion(this.region.getSelectedInstance());
//		desc.setSeason(Seasons.valueOf((String)this.season.getSelectedItem()));
//		desc.setTransportation((String)this.transportation.getSelectedItem());
		
		CBRQuery query = new CBRQuery();
		query.setDescription(desc);
		
		return query;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		QueryDialog qf = new QueryDialog(null);
		qf.setVisible(true);
		System.out.println("Bye");
	}

	

}
