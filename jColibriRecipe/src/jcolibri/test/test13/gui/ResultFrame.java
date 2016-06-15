/**
 * RestaurantsConnector.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 23/06/2007
 */
package jcolibri.test.test13.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.SystemColor;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * Swing dialog to show the result of the recommendation.
 * @author Juan Antonio Recio García
 * @version 1.0
 */

public class ResultFrame extends JDialog {

    private static final long serialVersionUID = 1L;

    JLabel jLabel1 = new JLabel();

	JLabel logo = new JLabel();

	JScrollPane jScrollPane1 = new JScrollPane();

	JPanel jPanel1 = new JPanel();

	Border border1;

	TitledBorder titledBorder1;

	JPanel jPanel2 = new JPanel();

	Border border2;

	TitledBorder titledBorder2;

	JEditorPane queryTextPane = new JEditorPane();

	JLabel jLabel2 = new JLabel();

	JTextField restaurant = new JTextField();

	JLabel jLabel3 = new JLabel();

	JScrollPane jScrollPane2 = new JScrollPane();

	JEditorPane AddressTextPane = new JEditorPane();

	JLabel jLabel4 = new JLabel();

	JScrollPane jScrollPane3 = new JScrollPane();

	JEditorPane descriptionTextPane = new JEditorPane();

	public ResultFrame(String query, String restaurant, String address,
			String description) throws HeadlessException {
		try {
			jbInit();
			java.awt.Dimension screenSize = java.awt.Toolkit
					.getDefaultToolkit().getScreenSize();
			setBounds((screenSize.width - this.getWidth()) / 2,
					(screenSize.height - this.getHeight()) / 2, getWidth(),
					getHeight());
			this.queryTextPane.setText(query);
			this.restaurant.setText(restaurant);
			this.AddressTextPane.setText(address);
			this.descriptionTextPane.setText(description);
			this.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws HeadlessException {
		ResultFrame resultFrame1 = new ResultFrame("query", "restaurant",
				"address", "description");
		resultFrame1.setVisible(true);
	}

	private void jbInit() throws Exception {
		border1 = BorderFactory.createBevelBorder(BevelBorder.LOWERED,
				Color.white, Color.white, new Color(124, 124, 124), new Color(
						178, 178, 178));
		titledBorder1 = new TitledBorder(BorderFactory.createLineBorder(
				Color.black, 1), "Result");
		border2 = BorderFactory.createLineBorder(SystemColor.controlText, 1);
		titledBorder2 = new TitledBorder(border2, "Query");
		jLabel1.setFont(new java.awt.Font("Dialog", 1, 16));
		jLabel1.setForeground(Color.blue);
		jLabel1.setText("jCOLIBRI Chef Recommendation");
		jLabel1.setBounds(new Rectangle(100, 11, 271, 29));
		this.getContentPane().setBackground(Color.white);
		this.getContentPane().setLayout(null);
		logo.setText("");
		logo.setBounds(new Rectangle(291, 203, 138, 283));
		logo.setIcon(new javax.swing.ImageIcon(jcolibri.util.FileIO.findFile("jcolibri/test/test13/gui/chef.jpg")));
		jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1.setBounds(new Rectangle(9, 21, 404, 41));
		jPanel1.setBackground(Color.white);
		jPanel1.setBorder(titledBorder1);
		jPanel1.setBounds(new Rectangle(15, 132, 256, 345));
		jPanel1.setLayout(null);
		jPanel2.setBackground(Color.white);
		jPanel2.setBorder(titledBorder2);
		jPanel2.setBounds(new Rectangle(14, 44, 423, 76));
		jPanel2.setLayout(null);
		queryTextPane.setEditable(false);
		queryTextPane.setText("");
		jLabel2.setText("Restaurant");
		jLabel2.setBounds(new Rectangle(9, 19, 214, 20));
		restaurant.setText("");
		restaurant.setBounds(new Rectangle(7, 42, 237, 23));
		restaurant.setEditable(false);
		restaurant.setBackground(Color.white);
		jLabel3.setBounds(new Rectangle(9, 74, 214, 20));
		jLabel3.setText("Address");
		jScrollPane2
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane2.setBounds(new Rectangle(7, 98, 234, 40));
		jLabel4.setBounds(new Rectangle(9, 149, 214, 20));
		jLabel4.setText("Description");
		jScrollPane3
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane3
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane3.setVerifyInputWhenFocusTarget(true);
		jScrollPane3.setBounds(new Rectangle(8, 173, 233, 163));
		AddressTextPane.setText("");
		AddressTextPane.setEditable(false);
		descriptionTextPane.setText("");
		descriptionTextPane.setEditable(false);
		this.getContentPane().add(jPanel1, null);
		this.getContentPane().add(logo, null);
		this.getContentPane().add(jPanel2, null);
		jPanel2.add(jScrollPane1, null);
		this.getContentPane().add(jLabel1, null);
		jScrollPane1.getViewport().add(queryTextPane, null);
		jPanel1.add(restaurant, null);
		jPanel1.add(jLabel2, null);
		jPanel1.add(jLabel3, null);
		jPanel1.add(jScrollPane2, null);
		jScrollPane2.getViewport().add(AddressTextPane, null);
		jPanel1.add(jScrollPane3, null);
		jPanel1.add(jLabel4, null);
		jScrollPane3.getViewport().add(descriptionTextPane, null);
		this.setSize(new Dimension(465, 515));
	}

}