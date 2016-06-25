package jcolibri.mtechkecbr.RecipeRecommender.gui;

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

import jcolibri.cbrcore.CBRQuery;
import jcolibri.mtechkecbr.RecipeRecommender.RecipeDescription;
import jcolibri.mtechkecbr.RecipeRecommender.RecipeRecommender;
import jcolibri.util.FileIO;

public class QueryDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	JLabel image;
	
	JComboBox<String> MainIngredient;
	JComboBox<String> TypeOfMeal;
	JComboBox<String> DietaryRequirement;
	JComboBox<String> TypeOfCuisine;
	JComboBox<String> DifficultyLevel;
	JCheckBox HealthyOption;
	JLabel SelectionsMade;
	SpinnerNumberModel numberOfPersons;
	SpinnerNumberModel CookingDuration;
	RegionSelector region;
	SpinnerNumberModel  duration;
    JList<String> PreferenceList;	
    DefaultListModel<String> listModel;
    Boolean m_bFirstTimeRunning = true;

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
		panel.add(MainIngredient = new JComboBox<String>(MainIngredients));
		
		panel.add(new JLabel("Type Of Meal"));
		String[] TypeOfMeals = {"Anything", "Appetizer", "Main Course", "Dessert", "Drinks"};
		panel.add(TypeOfMeal = new JComboBox<String>(TypeOfMeals));

		panel.add(new JLabel("Dietary Requirement"));
		String[] DietaryRequirements = {"None","Vegan","Halal","Nuts Free", "Non Spicy"};
		panel.add(DietaryRequirement = new JComboBox<String>(DietaryRequirements));

		panel.add(new JLabel("Type Of Cuisine"));
		String[] TypeOfCuisines = {"Anything", "Chinese", "Indian", "Malay/Indonesian", "Nyonya", "Western"};
		panel.add(TypeOfCuisine = new JComboBox<String>(TypeOfCuisines));

		panel.add(new JLabel("Cooking Duration (Maximum)"));
		CookingDuration = new SpinnerNumberModel(120,10,120,10); 
		panel.add(new JSpinner(CookingDuration));

		panel.add(new JLabel("Difficulty Level"));
		String[] DifficultyLevels = {"Anything", "Easy", "Medium", "Hard"};
		panel.add(DifficultyLevel = new JComboBox<String>(DifficultyLevels));

		panel.add(new JLabel("Size Of Meal (Number of Persons)"));
		numberOfPersons = new SpinnerNumberModel(2,2,20,1); 
		panel.add(new JSpinner(numberOfPersons));
		
		panel.add(new JLabel("Others"));
		panel.add(HealthyOption = new JCheckBox("Healthy Options /(less oil, non deep fry, etc/)"));

        //create the list
		panel.add(SelectionsMade = new JLabel("Selections Made"));
	    //create the model and add elements
        listModel = new DefaultListModel<>();
        UpdatePreference();
        PreferenceList = new JList<>(listModel);        
        PreferenceList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                	SelectionsMade.setText(PreferenceList.getSelectedValue().toString());
                }
            }
        });
        panel.add(PreferenceList);       
                
//		Lay out the panel.
		Utils.makeCompactGrid(panel,
		                10, 2, //rows, cols
		                6, 6,        //initX, initY
		                10, 15);       //xPad, yPad
		
		JPanel panelAux = new JPanel();
		panelAux.setLayout(new BorderLayout());
		panelAux.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		panelAux.add(panel,BorderLayout.NORTH);
		
		JPanel buttonsUpdateUpDown = new JPanel();
		buttonsUpdateUpDown.setLayout(new BorderLayout());
		
		JButton buttonUpdate = new JButton("Update");
		buttonUpdate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					RecipeRecommender.getInstance().postCycle();
				} catch (Exception ex) {
					org.apache.commons.logging.LogFactory.getLog(RecipeRecommender.class).error(ex);
				}
				UpdatePreference();
			}
		});
		buttonsUpdateUpDown.add(buttonUpdate, BorderLayout.WEST);

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
		if (listModel.size() > 0)
			listModel.removeAllElements();
		listModel.addElement("Main Ingredient: " + (java.lang.String) MainIngredient.getSelectedItem());
		listModel.addElement("Type Of Meal: " + (java.lang.String) TypeOfMeal.getSelectedItem());
		listModel.addElement("Dietary Req: " + (java.lang.String) DietaryRequirement.getSelectedItem());
		listModel.addElement("Type Of Cuisine: " + (java.lang.String) TypeOfCuisine.getSelectedItem());
		listModel.addElement("Diff. Level:" + (java.lang.String) DifficultyLevel.getSelectedItem());
		listModel.addElement("Number Of Persons: " + numberOfPersons.getNumber().toString());
		listModel.addElement("Cooking Duration: " + CookingDuration.getNumber());
		if (HealthyOption.isSelected())
			listModel.addElement("Healthy Option:" + "Yes");
		else
			listModel.addElement("Healthy Option:" + "No");
		if (!m_bFirstTimeRunning)
		{		
			PreferenceList.updateUI();			
		}
		else
			m_bFirstTimeRunning = false;
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
