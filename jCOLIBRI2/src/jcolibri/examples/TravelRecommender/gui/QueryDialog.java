/**
 * Travel Recommender example for the jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 25/07/2006
 */
package jcolibri.examples.TravelRecommender.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import jcolibri.examples.TravelRecommender.TravelDescription;
import jcolibri.examples.TravelRecommender.TravelRecommender;
import jcolibri.examples.TravelRecommender.TravelDescription.AccommodationTypes;
import jcolibri.examples.TravelRecommender.TravelDescription.Seasons;
import jcolibri.util.FileIO;

/**
 * Query dialgo
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class QueryDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	JLabel image;
	
	JComboBox holidayType;
	SpinnerNumberModel  numberOfPersons;
	RegionSelector region;
	JComboBox transportation;
	SpinnerNumberModel  duration;
	JComboBox season;
	JComboBox accommodation;
	
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
		image.setIcon(new ImageIcon(FileIO.findFile("jcolibri/examples/TravelRecommender/gui/step1.png")));
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(image, BorderLayout.WEST);
		
		
		/**********************************************************/
		JPanel panel = new JPanel();
		//panel.setLayout(new GridLayout(8,2));
		panel.setLayout(new SpringLayout());
		
		JLabel label;
		panel.add(label = new JLabel("Attribute"));
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		panel.add(label = new JLabel("Value"));
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		
		panel.add(new JLabel("HolidayType"));
		String[] holidayTypes = {"Skiing", "Recreation", "Active", "Wandering", "Education", "Bathing", "City", "Language"};
		panel.add(holidayType = new JComboBox(holidayTypes));
		
		panel.add(new JLabel("Number of persons"));
		numberOfPersons = new SpinnerNumberModel(2,1,12,1); 
		panel.add(new JSpinner(numberOfPersons));
		
		panel.add(new JLabel("Region"));
		//String[] regions = {
		//		"AdriaticSea","Algarve","Allgaeu","Alps","Atlantic","Attica","Balaton","BalticSea","Bavaria","Belgium","BlackForest","Bornholm","Brittany","Bulgaria","Cairo","Carinthia","Chalkidiki","Corfu","Corsica","CostaBlanca","CostaBrava","CotedAzur","Crete","Czechia","Denmark","Egypt","England","ErzGebirge","Fano","France","Fuerteventura","GiantMountains","GranCanaria","Harz","Holland","Ibiza","Ireland","LakeGarda","Lolland","Madeira","Mallorca","Malta","Normandy","NorthSea","Poland","Rhodes","Riviera","SalzbergerLand","Scotland","Slowakei","Styria","Sweden","Teneriffe","Thuringia","Tunisia","TurkishAegeanSea","TurkishRiviera","Tyrol","Wales"};
		panel.add(region = new RegionSelector(this));
		
		panel.add(new JLabel("Transportation"));
		String[] transportations = {"Plane","Car","Coach","Train"};
		panel.add(transportation = new JComboBox(transportations));
		
		panel.add(new JLabel("Duration"));
		duration = new SpinnerNumberModel(7,2,31,1); 
		panel.add(new JSpinner(duration));
		
		panel.add(new JLabel("Season"));
		String[] seasons = {"January","February","March","April","May","June","July","August","September","October","November","December"};
		panel.add(season = new JComboBox(seasons));
		
		panel.add(new JLabel("Accommodation"));
		String[] accommodations = {"FiveStars","FourStars","HolidayFlat","ThreeStars","TwoStars","OneStar"};
		panel.add(accommodation = new JComboBox(accommodations));
		
		
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
					TravelRecommender.getInstance().postCycle();
				} catch (Exception ex) {
					org.apache.commons.logging.LogFactory.getLog(TravelRecommender.class).error(ex);
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
		TravelDescription desc = new TravelDescription();
		desc.setAccommodation(AccommodationTypes.valueOf((String)this.accommodation.getSelectedItem()));
		desc.setDuration(this.duration.getNumber().intValue());
		desc.setHolidayType((String)this.holidayType.getSelectedItem());
		desc.setNumberOfPersons(this.numberOfPersons.getNumber().intValue());
		desc.setRegion(this.region.getSelectedInstance());
		desc.setSeason(Seasons.valueOf((String)this.season.getSelectedItem()));
		desc.setTransportation((String)this.transportation.getSelectedItem());
		
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
