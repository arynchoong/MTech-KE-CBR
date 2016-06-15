package jcolibri.test.main;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 * Progress bar window (using swing) that shows method progress.
 * 
 * @author Juan A. Recio-García
 * @version 1.0
 */
public class SwingProgressBar extends JFrame implements jcolibri.util.ProgressListener {
	private static final long serialVersionUID = 1L;

	JPanel jPanel1 = new JPanel();

	JProgressBar jProgressBar1 = new JProgressBar();

	JLabel jLabel1 = new JLabel();

	double stepPercentage;

	double progress;

	public SwingProgressBar() {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() throws Exception {
		jPanel1.setLayout(null);
		this.getContentPane().setLayout(null);
		jPanel1.setBounds(new Rectangle(5, 5, 333, 80));
		this.setResizable(false);
		this.setTitle("Method Progress");
		jProgressBar1.setStringPainted(true);
		jProgressBar1.setBounds(new Rectangle(30, 40, 274, 16));
		jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel1.setText("");
		jLabel1.setBounds(new Rectangle(30, 9, 275, 23));
		this.getContentPane().add(jPanel1, null);
		jPanel1.add(jLabel1, null);
		jPanel1.add(jProgressBar1, null);
		int w = 350;
		int h = 100;
		this.setSize(w, h);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		this.setLocation(x, y);
	}

	/**
	 * Sets the label text.
	 * 
	 * @param text
	 *            label text.
	 */
	void setText(String text) {
		jLabel1.setText(text);
	}

	/**
	 * Sets the limit of the progress bar count.
	 * 
	 * @param limit
	 *            limit of the progress bar count.
	 */
	void setLimit(int limit) {
		this.stepPercentage = 100.0 / (double) limit;
	}

	/**
	 * Increase the progress bar count in 1 unit.
	 */
	public void step() {
        if(!jProgressBar1.isIndeterminate())
        {
    		progress += stepPercentage;
    		jProgressBar1.setValue((int) progress);
        }
		/*
        Rectangle progressRect = getBounds();
		progressRect.x = 0;
		progressRect.y = 0;
		this.paintComponents(this.getGraphics());
		*/
	}

	/**
	 * Initializes the progress bar frame.
	 * 
	 * @param text
	 *            text of the label.
	 * @param limit
	 *            limit of the progress bar count.
	 */
	public void init(String text, int limit) {
		if(limit>-1)
		{
	        jProgressBar1.setIndeterminate(false);
	        jProgressBar1.setStringPainted(true);
	        this.setVisible(true);
			jProgressBar1.setValue(0);
			setText(text);
			setLimit(limit);
			progress = 0;
		}
		else
			init(text);
	}
    
    /**
     * Initializes the progress bar frame for an indeterminated length.
     * 
     * @param text
     *            text of the label.
     */
    private void init(String text)
    {
        jProgressBar1.setIndeterminate(true);
        jProgressBar1.setStringPainted(false);
        this.setVisible(true);
        setText(text);
    }

	public void finish() {
		this.setVisible(false);
		
	}

	
}