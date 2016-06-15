/**
 * Travel Recommender example for the jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 25/07/2006
 */
package jcolibri.examples.TravelRecommender.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;

import jcolibri.datatypes.Instance;
import jcolibri.exception.OntologyAccessException;
import es.ucm.fdi.gaia.ontobridge.OntoBridge;
import es.ucm.fdi.gaia.ontobridge.test.gui.PnlSelectInstance;

/**
 * Allows to choose the region
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class RegionSelector extends JButton {

	private static final long serialVersionUID = 1L;
	
	private static Icon INSTANCE = new javax.swing.ImageIcon(PnlSelectInstance.class.getResource("/es/ucm/fdi/gaia/ontobridge/test/gui/instance.gif"));
	
	JDialog ontoDialog;
	PnlSelectInstance ontoPanel;
	String selected;
	
	public RegionSelector(JDialog parent)
	{
		this.setText("...");
		
		ontoDialog = new JDialog(parent, true);
		OntoBridge ob = jcolibri.util.OntoBridgeSingleton.getOntoBridge();
		ontoPanel = new PnlSelectInstance(ob);
		Container main = ontoDialog.getContentPane();
		main.setLayout(new BorderLayout());
		main.add(ontoPanel, BorderLayout.CENTER);
		
		JButton select = new JButton("Select Instance");
		select.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ontoDialog.setVisible(false);	
			}
		});
		main.add(select,BorderLayout.SOUTH);
		
		ontoDialog.pack();
		
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		ontoDialog.setBounds((screenSize.width - ontoDialog.getWidth()) / 2,
			(screenSize.height - ontoDialog.getHeight()) / 2, 
			ontoDialog.getWidth(),
			ontoDialog.getHeight());
		
		this.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				selectInstance();	
			}
		});
	}
	
	public Instance getSelectedInstance()
	{
		try {
			if(selected==null)
				return null;
			return new Instance(selected);
		} catch (OntologyAccessException e) {
			org.apache.commons.logging.LogFactory.getLog(this.getClass()).error(e);
		}
		return null;
	}
	
	public void setSelectedInstance(Instance instance)
	{
		selected = instance.toString();
		this.setText(selected);
		this.setIcon(INSTANCE);
	}
	
	void selectInstance()
	{
		ontoDialog.setVisible(true);
		selected = ontoPanel.getSelectedInstance();
		if(selected==null)
		{
			this.setText("...");
			this.setIcon(null);
		}
		else
		{
			this.setText(selected);
			this.setIcon(INSTANCE);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
