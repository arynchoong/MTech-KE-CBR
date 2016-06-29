package jcolibri.mtechkecbr.RecipeRecommender.gui;

import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jcolibri.cbrcore.CBRQuery;
import jcolibri.mtechkecbr.RecipeRecommender.RecipeDescription;
import jcolibri.mtechkecbr.RecipeRecommender.RecipeRecommender;
import jcolibri.mtechkecbr.RecipeRecommender.SimilAlgo;
import jcolibri.util.FileIO;

public class QueryDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	JLabel image;
	
	JComboBox<String> MainIngredient;
	JComboBox<String> TypeOfMeal;
	CuisineSelector Cuisine;
	JComboBox<String> DifficultyLevel;
	JCheckBox HealthyOption;
	JCheckBox HalalOption;
	JCheckBox VeganOption;
	JCheckBox NutsFreeOption;
	JCheckBox SpicyOption;	
	JLabel SelectionsMade;
	SpinnerNumberModel numberOfPersons;
	SpinnerNumberModel CookingDuration;
    JList<String> PreferenceList;	
    DefaultListModel<String> listModel;
    Boolean m_bFirstTimeRunning = true;
    Boolean m_bServingSizeChanged = false;
    Boolean m_bCookDurationChanged = false;
    
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
		String[] MainIngredients = {"Anything", "Chicken", "Duck", "Pork", "Beef", "Mutton", "Fish", "Vegetables", "Fruits", "Rice", "Noodles", "Flour"};
		panel.add(MainIngredient = new JComboBox<String>(MainIngredients));
		MainIngredient.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					RecipeRecommender.getInstance().postCycle();
				} catch (Exception ex) {
					org.apache.commons.logging.LogFactory.getLog(RecipeRecommender.class).error(ex);
				}
				UpdatePreference();
			}
		});
		
		panel.add(new JLabel("Type Of Meal"));
		String[] TypeOfMeals = {"Anything", "Appetizer", "Main Course", "Dessert", "Snacks"};
		panel.add(TypeOfMeal = new JComboBox<String>(TypeOfMeals));
		TypeOfMeal.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					RecipeRecommender.getInstance().postCycle();
				} catch (Exception ex) {
					org.apache.commons.logging.LogFactory.getLog(RecipeRecommender.class).error(ex);
				}
				UpdatePreference();
			}
		});

		panel.add(new JLabel("Type Of Cuisine"));
		panel.add(Cuisine = new CuisineSelector(this));
		Cuisine.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				try {
					RecipeRecommender.getInstance().postCycle();
				} catch (Exception ex) {
					org.apache.commons.logging.LogFactory.getLog(RecipeRecommender.class).error(ex);
				}
				UpdatePreference();
			}
		});

		panel.add(new JLabel("Cooking Duration (Maximum)"));
		CookingDuration = new SpinnerNumberModel(120,10,120,10); 
		panel.add(new JSpinner(CookingDuration));
		CookingDuration.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				try {
					RecipeRecommender.getInstance().postCycle();
				} catch (Exception ex) {
					org.apache.commons.logging.LogFactory.getLog(RecipeRecommender.class).error(ex);
				}
				m_bCookDurationChanged = true;
				UpdatePreference();
			}
		});

		panel.add(new JLabel("Difficulty Level"));
		String[] DifficultyLevels = {"Anything", "easy", "medium", "hard"};
		panel.add(DifficultyLevel = new JComboBox<String>(DifficultyLevels));
		DifficultyLevel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					RecipeRecommender.getInstance().postCycle();
				} catch (Exception ex) {
					org.apache.commons.logging.LogFactory.getLog(RecipeRecommender.class).error(ex);
				}
				UpdatePreference();
			}
		});

		panel.add(new JLabel("Size Of Meal (Number of Persons)"));
		numberOfPersons = new SpinnerNumberModel(2,2,20,1); 
		panel.add(new JSpinner(numberOfPersons));
		numberOfPersons.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				try {
					RecipeRecommender.getInstance().postCycle();
				} catch (Exception ex) {
					org.apache.commons.logging.LogFactory.getLog(RecipeRecommender.class).error(ex);
				}
				m_bServingSizeChanged = true;
				UpdatePreference();
			}
		});
		
		panel.add(new JLabel("Other Dietary Requirements"));
		panel.add(HealthyOption = new JCheckBox("Healthy Options /(less oil, non deep fry, etc/)"));
		HealthyOption.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					RecipeRecommender.getInstance().postCycle();
				} catch (Exception ex) {
					org.apache.commons.logging.LogFactory.getLog(RecipeRecommender.class).error(ex);
				}
				UpdatePreference();
			}
		});
		
		panel.add(HalalOption = new JCheckBox("Halal"));
		HealthyOption.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					RecipeRecommender.getInstance().postCycle();
				} catch (Exception ex) {
					org.apache.commons.logging.LogFactory.getLog(RecipeRecommender.class).error(ex);
				}
				UpdatePreference();
			}
		});
		panel.add(VeganOption = new JCheckBox("Vegetarian"));
		VeganOption.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					RecipeRecommender.getInstance().postCycle();
				} catch (Exception ex) {
					org.apache.commons.logging.LogFactory.getLog(RecipeRecommender.class).error(ex);
				}
				UpdatePreference();
			}
		});
		panel.add(NutsFreeOption = new JCheckBox("Nuts Free"));
		NutsFreeOption.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					RecipeRecommender.getInstance().postCycle();
				} catch (Exception ex) {
					org.apache.commons.logging.LogFactory.getLog(RecipeRecommender.class).error(ex);
				}
				UpdatePreference();
			}
		});
		panel.add(SpicyOption = new JCheckBox("Spicy"));
		SpicyOption.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					RecipeRecommender.getInstance().postCycle();
				} catch (Exception ex) {
					org.apache.commons.logging.LogFactory.getLog(RecipeRecommender.class).error(ex);
				}
				UpdatePreference();
			}
		});

        //create the list
		panel.add(SelectionsMade = new JLabel("Selections Made"));
	    //create the model and add elements
        listModel = new DefaultListModel<>();
        PreferenceList = new JList<>(listModel);        
        PreferenceList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
//                	SelectionsMade.setText(PreferenceList.getSelectedValue().toString());
                }
            }
        });
        panel.add(PreferenceList);       
        UpdatePreference();
                
//		Lay out the panel.
		Utils.makeCompactGrid(panel,
		                11, 2, //rows, cols
		                6, 6,        //initX, initY
		                10, 15);       //xPad, yPad
		
		JPanel panelAux = new JPanel();
		panelAux.setLayout(new BorderLayout());
		panelAux.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		panelAux.add(panel,BorderLayout.NORTH);
		
		JPanel buttonsUpdateUpDown = new JPanel();
		buttonsUpdateUpDown.setLayout(new BorderLayout());
		
		JButton buttonResetDefault = new JButton("Reset Default");
		buttonResetDefault.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					RecipeRecommender.getInstance().postCycle();
				} catch (Exception ex) {
					org.apache.commons.logging.LogFactory.getLog(RecipeRecommender.class).error(ex);
				}
				ResetDefault();
			}
		});
		buttonsUpdateUpDown.add(buttonResetDefault, BorderLayout.WEST);
		
		JButton buttonMoveUp = new JButton("Move Up");
		buttonMoveUp.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					RecipeRecommender.getInstance().postCycle();
				} catch (Exception ex) {
					org.apache.commons.logging.LogFactory.getLog(RecipeRecommender.class).error(ex);
				}
				SelectionMoveUp();
			}
		});
		buttonsUpdateUpDown.add(buttonMoveUp, BorderLayout.CENTER);
		JButton buttonMoveDown = new JButton("Move Down");
		buttonMoveDown.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					RecipeRecommender.getInstance().postCycle();
				} catch (Exception ex) {
					org.apache.commons.logging.LogFactory.getLog(RecipeRecommender.class).error(ex);
				}
				SelectionMoveDown();
			}
		});
		buttonsUpdateUpDown.add(buttonMoveDown, BorderLayout.EAST);

		panelAux.add(buttonsUpdateUpDown, BorderLayout.AFTER_LINE_ENDS);

		JPanel buttons = new JPanel();
		buttons.setLayout(new BorderLayout());

		JButton ok = new JButton("Submit Query >>");
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
		
		panelAux.add(buttons, BorderLayout.AFTER_LAST_LINE);
		this.getContentPane().add(panelAux, BorderLayout.CENTER);
		
		/**********************************************************/
		
		
		this.pack();
		this.setSize(800, this.getHeight());
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
	
	private void UpdatePreference()
	{
		if (m_bFirstTimeRunning)
		{		
			if (listModel.size() > 0)
				listModel.removeAllElements();
			listModel.addElement("Main Ingredient: " + (java.lang.String) MainIngredient.getSelectedItem());
			listModel.addElement("Type Of Meal: " + (java.lang.String) TypeOfMeal.getSelectedItem());
			if(Cuisine.getSelectedInstance() == null)
				listModel.addElement("Type Of Cuisine: none");
			else
				listModel.addElement("Type Of Cuisine: " + Cuisine.getSelectedInstance().toString());
			listModel.addElement("Difficulty Level:" + (java.lang.String) DifficultyLevel.getSelectedItem());
			listModel.addElement("Number Of Persons: " + numberOfPersons.getNumber().toString());
			listModel.addElement("Cooking Duration: " + CookingDuration.getNumber());
			String TagOptions = new String("Options:");
			if (HealthyOption.isSelected())
				TagOptions = TagOptions + " Healthy";
			if (HalalOption.isSelected())
				TagOptions = TagOptions + " Halal";
			if (VeganOption.isSelected())
				TagOptions = TagOptions + " Vegetarian";
			if (NutsFreeOption.isSelected())
				TagOptions = TagOptions + " Nuts Free";
			if (SpicyOption.isSelected())
				TagOptions = TagOptions + " Spicy";
			listModel.addElement(TagOptions);
		}
		else
		{
			for (int i=0; i<PreferenceList.getModel().getSize(); i++)
			{
				if (listModel.getElementAt(i).contains("Cooking Duration"))
				{
					listModel.setElementAt("Cooking Duration: " + CookingDuration.getNumber(), i);
				}
				else if (listModel.getElementAt(i).contains("Difficulty Level"))
				{
					listModel.setElementAt("Difficulty Level: " + (java.lang.String) DifficultyLevel.getSelectedItem(), i);					
				}
				else if (listModel.getElementAt(i).contains("Type Of Cuisine"))
				{
					if(Cuisine.getSelectedInstance() == null)
						listModel.setElementAt("Type Of Cuisine: none", i);
					else
						listModel.setElementAt("Type Of Cuisine: " + Cuisine.getSelectedInstance().toString(), i);					
				}
				else if (listModel.getElementAt(i).contains("Main Ingredient"))
				{
					listModel.setElementAt("Main Ingredient: " + (java.lang.String) MainIngredient.getSelectedItem(), i);				
				}
				else if (listModel.getElementAt(i).contains("Type Of Meal"))
				{
					listModel.setElementAt("Type Of Meal: " + (java.lang.String) TypeOfMeal.getSelectedItem(), i);				
				}
				else if (listModel.getElementAt(i).contains("Number Of Persons"))
				{
					listModel.setElementAt("Number Of Persons: " + numberOfPersons.getNumber().toString(), i);					
				}
				else if (listModel.getElementAt(i).contains("Options"))
				{			
					String TagOptions = new String("Options:");
					if (HealthyOption.isSelected())
						TagOptions = TagOptions + " Healthy";
					if (HalalOption.isSelected())
						TagOptions = TagOptions + " Halal";
					if (VeganOption.isSelected())
						TagOptions = TagOptions + " Vegetarian";
					if (NutsFreeOption.isSelected())
						TagOptions = TagOptions + " Nuts Free";
					if (SpicyOption.isSelected())
						TagOptions = TagOptions + " Spicy";
					listModel.setElementAt(TagOptions, i);
				}								
			}						
		}
		PreferenceList.updateUI();			
		m_bFirstTimeRunning = false;
	}
	
	private void ResetDefault()
	{
		MainIngredient.setSelectedIndex(0);
		TypeOfMeal.setSelectedIndex(0);
		Cuisine.setInstanceEmpty();
		DifficultyLevel.setSelectedIndex(0);
		numberOfPersons.setValue((java.lang.Integer)2);
		CookingDuration.setValue((java.lang.Integer)120);
		HealthyOption.setSelected(false);
		HalalOption.setSelected(false);
		VeganOption.setSelected(false);
		NutsFreeOption.setSelected(false);
		SpicyOption.setSelected(false);
	}
	
	private void SelectionMoveUp()
	{
		if ( PreferenceList.isSelectionEmpty() )
			JOptionPane.showMessageDialog(null, "nothing selected", "Move Up", JOptionPane.INFORMATION_MESSAGE);
		else
		{
			String szSelectedListItem =  PreferenceList.getSelectedValue().toString();
			int nSelectedListItem = PreferenceList.getSelectedIndex();
			if ( (nSelectedListItem > 0) ) 
			{
				String szSwappedListItem = listModel.getElementAt(nSelectedListItem-1);
				listModel.setElementAt(szSelectedListItem, nSelectedListItem-1);				
				listModel.setElementAt(szSwappedListItem, nSelectedListItem);	
				PreferenceList.updateUI();
				PreferenceList.setSelectedIndex(nSelectedListItem-1);
				PreferenceList.updateUI();
			}
			else
				JOptionPane.showMessageDialog(null, "Cannot move up anymore!", "Move Up", JOptionPane.INFORMATION_MESSAGE);				
		}
	}
	
	private void SelectionMoveDown()
	{
		if ( PreferenceList.isSelectionEmpty() )
			JOptionPane.showMessageDialog(null, "nothing selected", "Move Down", JOptionPane.INFORMATION_MESSAGE);
		else
		{
			String szSelectedListItem =  PreferenceList.getSelectedValue().toString();
			int nSelectedListItem = PreferenceList.getSelectedIndex();
			if ( nSelectedListItem < (PreferenceList.getModel().getSize()-1) ) 
			{
				String szSwappedListItem = listModel.getElementAt(nSelectedListItem+1);
				listModel.setElementAt(szSelectedListItem, nSelectedListItem+1);				
				listModel.setElementAt(szSwappedListItem, nSelectedListItem);
				PreferenceList.updateUI();
				PreferenceList.setSelectedIndex(nSelectedListItem+1);
				PreferenceList.updateUI();
			}
			else
				JOptionPane.showMessageDialog(null, "Cannot move down anymore!", "Move Down", JOptionPane.INFORMATION_MESSAGE);				
		}
	}	

	public ArrayList getVals()
	{
		int nPriorityLevel=0;
		String sAttribValue;
		String sAttribName;
		ArrayList<SimilAlgo> simConfig = new ArrayList<SimilAlgo>();
		
		sAttribName = "DifficultyLevel";
		sAttribValue = (String) DifficultyLevel.getSelectedItem();
		if (!sAttribValue.contains("Anything")) {
			for (int i=0; i<PreferenceList.getModel().getSize(); i++) {
				if (listModel.getElementAt(i).contains("Difficulty Level"))
					nPriorityLevel=i+1;
			}			
		}
		else {
			nPriorityLevel=99;	
			sAttribValue = "null";
		}
		simConfig.add(new SimilAlgo(sAttribName,sAttribValue,nPriorityLevel));
		
		sAttribName = "NumberOfPersons";
		sAttribValue = numberOfPersons.getNumber().toString();
		if (m_bServingSizeChanged) {
			for (int i=0; i<PreferenceList.getModel().getSize(); i++) {
				if (listModel.getElementAt(i).contains("Number Of Persons"))
					nPriorityLevel=i+1;
			}			
		}
		else {
			nPriorityLevel=99;	
			sAttribValue = "null";
		}
		simConfig.add(new SimilAlgo(sAttribName,sAttribValue,nPriorityLevel));
		
		sAttribName = "CookingDuration";
		sAttribValue = CookingDuration.getNumber().toString();
		if (m_bCookDurationChanged) {
			for (int i=0; i<PreferenceList.getModel().getSize(); i++) {
				if (listModel.getElementAt(i).contains("Cooking Duration"))
					nPriorityLevel=i+1;
			}			
		}
		else {
			nPriorityLevel=99;	
			sAttribValue = "null";
		}
		simConfig.add(new SimilAlgo(sAttribName,sAttribValue,nPriorityLevel));
		
		sAttribName = "Cuisine";
		if(Cuisine.getSelectedInstance() != null)
		{
			sAttribValue = Cuisine.getSelectedInstance().toString();
			for (int i=0; i<PreferenceList.getModel().getSize(); i++) {
				if (listModel.getElementAt(i).contains("Type Of Cuisine"))
					nPriorityLevel=i+1;
			}			
		}
		else {
			nPriorityLevel=99;	
			sAttribValue = "null";
		}
		simConfig.add(new SimilAlgo(sAttribName,sAttribValue,nPriorityLevel));
		
		sAttribName = "TypeOfMeal";
		sAttribValue = (String) TypeOfMeal.getSelectedItem();
		if (!sAttribValue.contains("Anything")) {
			for (int i=0; i<PreferenceList.getModel().getSize(); i++) {
				if (listModel.getElementAt(i).contains("Type Of Meal"))
					nPriorityLevel=i+1;
			}			
		}
		else {
			nPriorityLevel=99;	
			sAttribValue = "null";
		}
		simConfig.add(new SimilAlgo(sAttribName,sAttribValue,nPriorityLevel));
		
		sAttribName = "MainIngredient";
		sAttribValue = (String) MainIngredient.getSelectedItem();
		if (!sAttribValue.contains("Anything")) {
			for (int i=0; i<PreferenceList.getModel().getSize(); i++) {
				if (listModel.getElementAt(i).contains("Main Ingredient"))
					nPriorityLevel=i+1;
			}			
		}
		else {
			nPriorityLevel=99;	
			sAttribValue = "null";
		}
		simConfig.add(new SimilAlgo(sAttribName,sAttribValue,nPriorityLevel));

		sAttribName = "Tags";
		sAttribValue = getTagsString();
		if (!getTagsString().isEmpty()) {
			for (int i=0; i<PreferenceList.getModel().getSize(); i++) {
				if (listModel.getElementAt(i).contains("Option"))
					nPriorityLevel=i+1;
			}			
		}
		else {
			nPriorityLevel=99;	
			sAttribValue = "null";
		}
		simConfig.add(new SimilAlgo(sAttribName,sAttribValue,nPriorityLevel));

		return simConfig;
	}
	// @param args
	public static void main(String[] args) {
		QueryDialog qf = new QueryDialog(null);
		qf.setVisible(true);
		System.out.println("Bye");
	}

	public String getTagsString() {
		String TagAttributeValue = new String("");
		if (HalalOption.isSelected())
			TagAttributeValue = TagAttributeValue + "Halal;";
		if (VeganOption.isSelected())
			TagAttributeValue = TagAttributeValue + "Vegetarian;";
		if (HealthyOption.isSelected())
			TagAttributeValue = TagAttributeValue + "Healthy;";
		if (NutsFreeOption.isSelected())
			TagAttributeValue = TagAttributeValue + "NoNuts;";
		if (SpicyOption.isSelected())
			TagAttributeValue = TagAttributeValue + "Spicy";
		return TagAttributeValue;
	}

}
