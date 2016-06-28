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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.JTextArea;

import jcolibri.cbrcore.CBRCase;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.mtechkecbr.RecipeRecommender.RecipeDescription;
import jcolibri.mtechkecbr.RecipeRecommender.RecipeRecommender;
import jcolibri.mtechkecbr.RecipeRecommender.RecipeSolution;
import jcolibri.util.FileIO;
import java.awt.ScrollPane;

/**
 * Result dialog
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class ResultDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	JLabel image;
	
	JTextField name;
	JTextField servings;
	JTextField cuisine;
	JTextField typeOfMeal;
	JTextField cooktime;
	JTextField mainIngredient;
	JTextField difficultyLevel;
	JLabel caseId;
	JTextArea ingredients;
	JTextArea method;
	JScrollPane scroller;
	JScrollPane scroller1;
	ArrayList<RetrievalResult> cases;
	int currentCase;
	
	public ResultDialog(JFrame main)
	{
		super(main,true);
		setPreferredSize(new Dimension(800, 600));
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
		
		this.setTitle("Retrived cases");

		
		image = new JLabel();
		image.setIcon(new ImageIcon(FileIO.findFile("jcolibri/mtechkecbr/RecipeRecommender/gui/step3.png")));
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
		
		panel.add(new JLabel("Serving size"));
		panel.add(this.servings = new JTextField());
		
		panel.add(new JLabel("Cuisine"));
		panel.add(this.cuisine = new JTextField());
		
		panel.add(new JLabel("Type Of Meal"));
		panel.add(this.typeOfMeal = new JTextField());
		
		panel.add(new JLabel("Cooking time"));
		panel.add(this.cooktime = new JTextField());
		
		panel.add(new JLabel("Main Ingredient"));
		panel.add(this.mainIngredient = new JTextField());
		
		panel.add(new JLabel("Difficulty Level"));
		panel.add(this.difficultyLevel = new JTextField());
		
		panel.add(label = new JLabel("Solution"));
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		panel.add(label = new JLabel());

		
		panel.add(new JLabel("Ingredients"));
		ingredients = new JTextArea(3,1);
		scroller1 = new JScrollPane(ingredients,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scroller1);
		ingredients.setAutoscrolls(false);
		ingredients.setWrapStyleWord(true);
		ingredients.setLineWrap(true);
		ingredients.setEditable(false);
		
		panel.add(new JLabel("Method"));
		method = new JTextArea(3,1);
		scroller = new JScrollPane(method,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);	
		panel.add(scroller);
		
		method.setAutoscrolls(false);
		method.setWrapStyleWord(true);
		method.setLineWrap(true);
		method.setEditable(false);
//		JScrollPane scroller = new JScrollPane(method);
//      scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//      scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
       

		
//		Lay out the panel.
		Utils.makeCompactGrid(panel,
		                11, 2, //rows, cols
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
		this.setResizable(true);
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
	
	
	public void showCases(Collection<RetrievalResult> eval, Collection<CBRCase> selected)
	{
		cases = new ArrayList<RetrievalResult>();
		for(RetrievalResult rr: eval)
			if(selected.contains(rr.get_case()))
				cases.add(rr);
		currentCase = 0;
		showCase();
	}
	
	void showCase()
	{
		RetrievalResult rr = cases.get(currentCase);
		double sim = rr.getEval();
		
		CBRCase _case = rr.get_case();
		this.caseId.setText(_case.getID().toString()+" -> "+sim+" ("+(currentCase+1)+"/"+cases.size()+")");
		
		RecipeDescription desc = (RecipeDescription) _case.getDescription();
		this.name.setText(desc.getCaseId());
		this.servings.setText(desc.getNumberOfPersons().toString());
		this.cuisine.setText(desc.getTypeOfCuisine());
		this.typeOfMeal.setText(desc.getTypeOfMeal());
		this.cooktime.setText(desc.getCookingDuration().toString());
		this.mainIngredient.setText(desc.getMainIngredient());
		this.difficultyLevel.setText(desc.getDifficultyLevel().toString());
		
		RecipeSolution sol = (RecipeSolution) _case.getSolution();
		this.ingredients.setText(sol.getDetailedIngredients().toString());
		this.method.setText(sol.getMethod());
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ResultDialog qf = new ResultDialog(null);
		qf.setVisible(true);
		System.out.println("Bye");
	}

	

}
