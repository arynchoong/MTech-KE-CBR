/**
 * Test16.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 02/08/2007
 */
package jcolibri.test.test16;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.evaluation.EvaluationReport;
import jcolibri.evaluation.Evaluator;
import jcolibri.evaluation.evaluators.HoldOutEvaluator;
import jcolibri.evaluation.evaluators.LeaveOneOutEvaluator;
import jcolibri.evaluation.evaluators.NFoldEvaluator;
import jcolibri.evaluation.evaluators.SameSplitEvaluator;
import jcolibri.exception.ExecutionException;
import jcolibri.extensions.textual.IE.common.StopWordsDetector;
import jcolibri.extensions.textual.IE.common.TextStemmer;
import jcolibri.extensions.textual.IE.opennlp.OpennlpSplitter;
import jcolibri.extensions.visualization.CasesVisualization;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import jcolibri.method.retrieve.NNretrieval.similarity.local.textual.CosineCoefficient;
import jcolibri.method.retrieve.NNretrieval.similarity.local.textual.DiceCoefficient;
import jcolibri.method.retrieve.NNretrieval.similarity.local.textual.JaccardCoefficient;
import jcolibri.method.retrieve.NNretrieval.similarity.local.textual.OverlapCoefficient;
import jcolibri.method.retrieve.NNretrieval.similarity.local.textual.compressionbased.CompressionBased;
import jcolibri.method.retrieve.NNretrieval.similarity.local.textual.compressionbased.GZipCompressor;
import jcolibri.method.retrieve.NNretrieval.similarity.local.textual.compressionbased.NormalisedCompression;
import jcolibri.method.reuse.classification.KNNClassificationMethod;
import jcolibri.method.reuse.classification.MajorityVotingMethod;
import jcolibri.method.reuse.classification.SimilarityWeightedVotingMethod;
import jcolibri.method.reuse.classification.UnanimousVotingMethod;
import jcolibri.test.main.SwingProgressBar;
import jcolibri.util.ProgressController;
import jcolibri.util.ProgressListener;

/**
 * This test shows how to evaluate the textual similarity functions using classification.
 * It uses a corpus of email (divided into ham and spam messages) and also allows to 
 * visualize the case base using a chosen similarity function.
 * <br>
 * The corpus is packed into the lib\textual\spamcorpus\spamcorpus.jar file and was extracted
 * from the Apache Spamassassin project (http://spamassassin.apache.org/publiccorpus/).
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.method.retrieve.NNretrieval.similarity.local.textual
 * @see jcolibri.method.reuse.classification
 * @see jcolibri.evaluation
 */
public class Test16 extends JFrame
{
    private static final long serialVersionUID = 1L;

    JRadioButton corpus300;

    JRadioButton corpus600;

    JRadioButton leaveOneOut;

    JRadioButton holdOutFixed10;

    JRadioButton holdOutFixed20;

    JRadioButton holdOutFixed30;
    
    JRadioButton sameSplit;
	
    JRadioButton sameSplitGenerate;

    JButton saveSplit;

    JTextField sameSplitGenerateFile;
    
    JRadioButton sameSplitReuse;

    JButton loadSplit;

    JTextField sameSplitReuseFile;
    
    JLabel sameSplitGeneratePercent;
    
    SpinnerNumberModel sameSplitPercent;

    JSpinner sameSplitGeneratePercentSpinner;

    JRadioButton holdOut;

    JLabel holdOutLabel1;

    SpinnerNumberModel holdOutPercent;

    JSpinner holdOutPercentSpinner;

    JLabel holdOutLabel2;

    SpinnerNumberModel holdOutRepetitions;

    JSpinner holdOutRepetitionsSpinner;

    JRadioButton nFold;

    JLabel nFoldLabel1;

    SpinnerNumberModel nFoldFolds;

    JSpinner nFoldFoldsSpinner;

    JLabel nFoldLabel2;

    SpinnerNumberModel nFoldRepetitions;

    JSpinner nFoldRepetitionsSpinner;

    JRadioButton compressionBased;

    JRadioButton normalizedCompression;

    JRadioButton cosine;

    JRadioButton dice;

    JRadioButton jaccard;

    JRadioButton overlap;

    JRadioButton majorityVoting;

    JRadioButton weightedVoting;

    JRadioButton unanimousVoting;
    
    SpinnerNumberModel kValue;

    JSpinner kValueSpinner;

    JButton evaluate;

    JButton visualize;

    
    
    
    public Test16()
    {
	configure();
    }

    public void configure()
    {
	try
	{
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception e)
	{
	}
	
	/** ****** Corpus Panel ************ */

	JPanel corpusPanel = new JPanel();
	corpusPanel.setLayout(new GridLayout(2, 1));
	corpusPanel.setBorder(BorderFactory.createTitledBorder("Corpus"));

	corpus300 = new JRadioButton("Corpus - 300 emails");
	corpus600 = new JRadioButton("Corpus - 600 emails");

	ButtonGroup corpusGroup = new ButtonGroup();
	corpusGroup.add(corpus300);
	corpusGroup.add(corpus600);

	corpusPanel.add(corpus300);
	corpusPanel.add(corpus600);

	corpus300.setSelected(true);

	/** ****** Evaluator Panel ************ */

	JPanel evaluatorPanel = new JPanel();
	evaluatorPanel.setBorder(BorderFactory.createTitledBorder("Evaluator"));
	evaluatorPanel.setLayout(new BoxLayout(evaluatorPanel, BoxLayout.Y_AXIS));

	leaveOneOut = new JRadioButton("Leave One Out");
	leaveOneOut.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e)
	    {
		enableGroup("none");
	    }
	});

	holdOutFixed10 = new JRadioButton("Hold Out - Query Set Fixed 10%");
	holdOutFixed10.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e)
	    {
		enableGroup("none");
	    }
	});
	holdOutFixed20 = new JRadioButton("Hold Out - Query Set Fixed 20%");
	holdOutFixed20.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e)
	    {
		enableGroup("none");
	    }
	});
	holdOutFixed30 = new JRadioButton("Hold Out - Query Set Fixed 30%");
	holdOutFixed30.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e)
	    {
		enableGroup("none");
	    }
	});

	
	sameSplit = new JRadioButton("Hold Out - Query Set Custom");
	sameSplit.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e)
	    {
		enableGroup("samesplit");
	    }
	});
	
	JPanel sameSplitPanel = new JPanel();
	sameSplitPanel.setLayout(new BoxLayout(sameSplitPanel, BoxLayout.Y_AXIS));
	sameSplitPanel.setBorder(BorderFactory.createEmptyBorder(2,15,2,2));
	
	JPanel sameSplitPanel1 = new JPanel();
	sameSplitGenerate = new JRadioButton("Generate New Query Set & Evaluate");
	sameSplitGenerate.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e)
	    {
		loadSplit.setEnabled(false);
		sameSplitReuseFile.setEnabled(false);
		saveSplit.setEnabled(true);
		sameSplitGenerateFile.setEnabled(true);
		sameSplitGeneratePercentSpinner.setEnabled(true);
		sameSplitGeneratePercent.setEnabled(true);
	    }
	});
	saveSplit = new JButton("Save Query Set");	
	sameSplitGenerateFile = new JTextField(20);
	saveSplit.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e)
	    {
		JFileChooser jfc = new JFileChooser(".");
		int returnVal = jfc.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		    sameSplitGenerateFile.setText(jfc.getSelectedFile().toString());
	    }
	});

	JPanel sameSplitPanel1b = new JPanel();
	sameSplitGeneratePercent = new JLabel("Query Set percent:");
	sameSplitPercent = new SpinnerNumberModel(10, 5, 95, 5);
	sameSplitGeneratePercentSpinner = new JSpinner(sameSplitPercent);
	
	
	JPanel sameSplitPanel2 = new JPanel();	
	sameSplitReuse = new JRadioButton("Evaluate Using Existing Query Set");
	sameSplitReuse.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e)
	    {
		loadSplit.setEnabled(true);
		sameSplitReuseFile.setEnabled(true);
		saveSplit.setEnabled(false);
		sameSplitGenerateFile.setEnabled(false);
		sameSplitGeneratePercentSpinner.setEnabled(false);
		sameSplitGeneratePercent.setEnabled(false);
	    }
	});
	loadSplit = new JButton("Open Query Set");
	sameSplitReuseFile = new JTextField(20);
	loadSplit.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e)
	    {
		JFileChooser jfc = new JFileChooser(".");
		int returnVal = jfc.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		    sameSplitReuseFile.setText(jfc.getSelectedFile().toString());
	    }
	});


	sameSplitPanel1.add(saveSplit);
	sameSplitPanel1.add(sameSplitGenerateFile);
	sameSplitPanel1b.add(sameSplitGeneratePercent);
	sameSplitPanel1b.add(sameSplitGeneratePercentSpinner);
	sameSplitPanel2.add(loadSplit);
	sameSplitPanel2.add(sameSplitReuseFile);
	
	sameSplitPanel.add(sameSplitGenerate);
	sameSplitPanel.add(sameSplitPanel1);
	sameSplitPanel.add(sameSplitPanel1b);
	sameSplitPanel.add(sameSplitReuse);
	sameSplitPanel.add(sameSplitPanel2);
	sameSplitPanel1.setAlignmentX(JComponent.LEFT_ALIGNMENT);
	sameSplitPanel2.setAlignmentX(JComponent.LEFT_ALIGNMENT);
	sameSplitPanel1b.setAlignmentX(JComponent.LEFT_ALIGNMENT);
	
	ButtonGroup sameSplitGroup = new ButtonGroup();
	sameSplitGroup.add(sameSplitGenerate);
	sameSplitGroup.add(sameSplitReuse);
	
	sameSplitGenerate.setSelected(true);
	loadSplit.setEnabled(false);
	sameSplitReuseFile.setEnabled(false);
	
	holdOut = new JRadioButton("Hold Out - Query Set Random");
	holdOut.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e)
	    {
		enableGroup("holdout");
	    }
	});

	JPanel holdOutOptions = new JPanel();
	holdOutLabel1 = new JLabel("Query Set Percent");
	holdOutOptions.add(holdOutLabel1);
	holdOutPercent = new SpinnerNumberModel(10, 5, 95, 5);
	holdOutPercentSpinner = new JSpinner(holdOutPercent);
	holdOutOptions.add(holdOutPercentSpinner);
	holdOutLabel2 = new JLabel("Repetitions");
	holdOutOptions.add(holdOutLabel2);
	holdOutRepetitions = new SpinnerNumberModel(1, 1, 10, 1);
	holdOutRepetitionsSpinner = new JSpinner(holdOutRepetitions);
	holdOutOptions.add(holdOutRepetitionsSpinner);

	nFold = new JRadioButton("N-Fold - Random");
	nFold.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e)
	    {
		enableGroup("nfold");
	    }
	});

	JPanel nFoldOptions = new JPanel();
	nFoldLabel1 = new JLabel("Folds");
	nFoldOptions.add(nFoldLabel1);
	nFoldFolds = new SpinnerNumberModel(4, 2, 10, 1);
	nFoldFoldsSpinner = new JSpinner(nFoldFolds);
	nFoldOptions.add(nFoldFoldsSpinner);
	nFoldLabel2 = new JLabel("Repetitions");
	nFoldOptions.add(nFoldLabel2);
	nFoldRepetitions = new SpinnerNumberModel(1, 1, 10, 1);
	nFoldRepetitionsSpinner = new JSpinner(nFoldRepetitions);
	nFoldOptions.add(nFoldRepetitionsSpinner);

	ButtonGroup evaluatorGroup = new ButtonGroup();
	evaluatorGroup.add(leaveOneOut);
	evaluatorGroup.add(holdOutFixed10);
	evaluatorGroup.add(holdOutFixed20);
	evaluatorGroup.add(holdOutFixed30);
	evaluatorGroup.add(sameSplit);
	evaluatorGroup.add(holdOut);
	evaluatorGroup.add(nFold);

	evaluatorPanel.add(leaveOneOut);
	evaluatorPanel.add(holdOutFixed10);
	evaluatorPanel.add(holdOutFixed20);
	evaluatorPanel.add(holdOutFixed30);
	evaluatorPanel.add(holdOut);
	evaluatorPanel.add(holdOutOptions);
	evaluatorPanel.add(nFold);
	evaluatorPanel.add(nFoldOptions);
	evaluatorPanel.add(sameSplit);
	evaluatorPanel.add(sameSplitPanel);

	leaveOneOut.setAlignmentX(Component.LEFT_ALIGNMENT);
	holdOutFixed10.setAlignmentX(Component.LEFT_ALIGNMENT);
	holdOutFixed20.setAlignmentX(Component.LEFT_ALIGNMENT);
	holdOutFixed30.setAlignmentX(Component.LEFT_ALIGNMENT);
	sameSplit.setAlignmentX(Component.LEFT_ALIGNMENT);
	sameSplitPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
	holdOut.setAlignmentX(Component.LEFT_ALIGNMENT);
	holdOutOptions.setAlignmentX(Component.LEFT_ALIGNMENT);
	nFold.setAlignmentX(Component.LEFT_ALIGNMENT);
	nFoldOptions.setAlignmentX(Component.LEFT_ALIGNMENT);


	
	leaveOneOut.setSelected(true);

	this.setEnabledHoldOut(false);
	this.setEnabledNFold(false);
	this.setEnabledSameSplit(false);

	/** ****** Similarity Panel ************ */

	JPanel similPanel = new JPanel();
	similPanel.setBorder(BorderFactory.createTitledBorder("Similarity Function"));
	similPanel.setLayout(new GridLayout(6, 1));

	compressionBased = new JRadioButton("Compression");
	normalizedCompression = new JRadioButton("Normalized Compression");
	cosine = new JRadioButton("Cosine Coefficient");
	dice = new JRadioButton("Dice Coefficient");
	jaccard = new JRadioButton("Jaccard Coefficient");
	overlap = new JRadioButton("Overlap Coefficient");

	ButtonGroup similGroup = new ButtonGroup();
	similGroup.add(compressionBased);
	similGroup.add(normalizedCompression);
	similGroup.add(cosine);
	similGroup.add(dice);
	similGroup.add(jaccard);
	similGroup.add(overlap);

	similPanel.add(compressionBased);
	similPanel.add(normalizedCompression);
	similPanel.add(cosine);
	similPanel.add(dice);
	similPanel.add(jaccard);
	similPanel.add(overlap);

	compressionBased.setSelected(true);

	/** ****** Classification Panel ************ */

	JPanel clasifPanel = new JPanel();
	clasifPanel.setBorder(BorderFactory.createTitledBorder("KNN Classification Method"));
	clasifPanel.setLayout(new GridLayout(4, 1));

	majorityVoting = new JRadioButton("Majority Voting Method");
	weightedVoting = new JRadioButton("Weighted Voting Method");
	unanimousVoting = new JRadioButton("Unanimous Voting Method");

	JPanel kPanel = new JPanel();
	kPanel.add(new JLabel("K value"));
	kValue = new SpinnerNumberModel(3, 1, 10, 1);
	kValueSpinner = new JSpinner(kValue);
	kPanel.add(kValueSpinner);

	ButtonGroup clasifGroup = new ButtonGroup();
	clasifGroup.add(majorityVoting);
	clasifGroup.add(weightedVoting);
	clasifGroup.add(unanimousVoting);

	clasifPanel.add(kPanel);
	clasifPanel.add(weightedVoting);
	clasifPanel.add(majorityVoting);
	clasifPanel.add(unanimousVoting);


	weightedVoting.setSelected(true);

	/** ********* Buttons ********** */

	JPanel buttons = new JPanel();

	evaluate = new JButton("Evaluate Application");
	visualize = new JButton("Visualize Case Base using Similarity Function");

	buttons.add(evaluate);
	buttons.add(visualize);

	evaluate.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e)
	    {
		evaluate();
	    }
	});

	visualize.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e)
	    {
		visualize();
	    }
	});

	/************ Progress Listener ****************/
	//SwingProgressBar shows the progress
	ProgressListener listener =  new SwingProgressBar();
	ProgressController.clear();
	ProgressController.register(listener, CasesVisualization.class);
	ProgressController.register(listener, LeaveOneOutEvaluator.class);
	ProgressController.register(listener, NFoldEvaluator.class);
	ProgressController.register(listener, SameSplitEvaluator.class);
	ProgressController.register(listener, HoldOutEvaluator.class);
	ProgressController.register(listener, OpennlpSplitter.class);
	ProgressController.register(listener, StopWordsDetector.class);
	ProgressController.register(listener, TextStemmer.class);

	
	/** ********* Main Panel ********** */

	JPanel central = new JPanel();
	central.setLayout(new BoxLayout(central, BoxLayout.X_AXIS));
	central.add(corpusPanel);
	central.add(similPanel);
	central.add(evaluatorPanel);
	central.add(clasifPanel);

	JPanel main = new JPanel();
	main.setLayout(new BorderLayout());

	main.add(central, BorderLayout.CENTER);
	main.add(buttons, BorderLayout.SOUTH);

	this.getContentPane().add(main);
	this.pack();

	this.setTitle("Spam Filter Toy");
	Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	setBounds((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2, getWidth(),
		getHeight());

//        this.addWindowListener(new WindowAdapter() 
//        {   public void windowClosing(WindowEvent arg0) 
//            {   
//            	System.exit(0);
//            }
//        });
    }

    void enableGroup(String group)
    {
	if (group.equals("nfold"))
	{
	    setEnabledNFold(true);
	    setEnabledHoldOut(false);
	    setEnabledSameSplit(false);
	} else if (group.equals("holdout"))
	{
	    setEnabledNFold(false);
	    setEnabledSameSplit(false);
	    setEnabledHoldOut(true);
	}
	else if (group.equals("samesplit"))
	{
	    setEnabledNFold(false);
	    setEnabledSameSplit(true);
	    setEnabledHoldOut(false);
	}
	else if(group.equals("none"))
	{
	    setEnabledNFold(false);
	    setEnabledSameSplit(false);
	    setEnabledHoldOut(false);
	}
    }


    private void setEnabledSameSplit(boolean enabled)
    {
	this.sameSplitGenerate.setEnabled(enabled);
	this.sameSplitReuse.setEnabled(enabled);
	if(!enabled)
	{
	    this.saveSplit.setEnabled(enabled);
	    this.loadSplit.setEnabled(enabled);
	    this.sameSplitGenerateFile.setEnabled(enabled);
	    this.sameSplitReuseFile.setEnabled(enabled);
	    this.sameSplitGeneratePercentSpinner.setEnabled(enabled);
	    this.sameSplitGeneratePercent.setEnabled(enabled);
	}
	else
	{
	    
	    if(sameSplitGenerate.isSelected())
	    {
		saveSplit.setEnabled(true);
		sameSplitGenerateFile.setEnabled(true);
		sameSplitGeneratePercentSpinner.setEnabled(true);
		sameSplitGeneratePercent.setEnabled(true);
	    }
	    else
	    {
		loadSplit.setEnabled(true);
		sameSplitReuseFile.setEnabled(true);
	    }
	}
    }
    
    private void setEnabledNFold(boolean enabled)
    {
	nFoldLabel1.setEnabled(enabled);
	nFoldLabel2.setEnabled(enabled);
	nFoldFoldsSpinner.setEnabled(enabled);
	nFoldRepetitionsSpinner.setEnabled(enabled);
    }

    private void setEnabledHoldOut(boolean enabled)
    {
	holdOutLabel1.setEnabled(enabled);
	holdOutLabel2.setEnabled(enabled);
	holdOutPercentSpinner.setEnabled(enabled);
	holdOutRepetitionsSpinner.setEnabled(enabled);
    }

    void setButtonsEnabled(boolean enabled)
    {
	visualize.setEnabled(enabled);
	evaluate.setEnabled(enabled);
    }
    
    void evaluate()
    {
	SpamFilterApp app = configureApp();
	Thread thread = new Thread(new Evaluation(app));
	thread.start();
    }

    /**
         * Thread for running the Visualization
         * 
         * @author Juan A. Recio-Garcia
         * @version 1.0
         */
    class Evaluation implements Runnable
    {
	SpamFilterApp app;

	Evaluation(SpamFilterApp app)
	{
	    this.app = app;
	}

	public void run()
	{
	    setButtonsEnabled(false);
	    try
	    {
		if (leaveOneOut.isSelected())
		{
		    LeaveOneOutEvaluator eval = new LeaveOneOutEvaluator();
		    eval.init(app);
		    eval.LeaveOneOut(); 
		} 
		else if(sameSplit.isSelected())
		{
		    SameSplitEvaluator eval = new SameSplitEvaluator();
		    eval.init(app);
		    if(sameSplitGenerate.isSelected())
		    {
			String file = sameSplitGenerateFile.getText();
			int percent = sameSplitPercent.getNumber().intValue();
			eval.generateSplit(percent, file);
			eval.HoldOutfromFile(file);
		    }
		    else if(sameSplitReuse.isSelected())
		    {
			eval.HoldOutfromFile(sameSplitReuseFile.getText());
		    }
		} 
		else if (holdOut.isSelected())
		{
		    HoldOutEvaluator eval = new HoldOutEvaluator();
		    eval.init(app);
		    int testPercent = holdOutPercent.getNumber().intValue();
		    int repetitions = holdOutRepetitions.getNumber().intValue();
		    eval.HoldOut(testPercent, repetitions);
		} 
		else if (nFold.isSelected())
		{
		    NFoldEvaluator eval = new NFoldEvaluator();
		    eval.init(app);
		    int folds = nFoldFolds.getNumber().intValue();
		    int repetitions = nFoldRepetitions.getNumber().intValue();
		    eval.NFoldEvaluation(folds, repetitions);
		} 
		else
		{
		    String filename = "jcolibri/test/test16/splits/corpus";
		    if(corpus300.isSelected())
			filename+="300-";
		    else
			filename+="600-";
		    
		    if(holdOutFixed10.isSelected())
			filename+="10.split";
		    else if(holdOutFixed20.isSelected())
			filename+="20.split";
		    else if(holdOutFixed30.isSelected())
			filename+="30.split";
			
		    SameSplitEvaluator eval = new SameSplitEvaluator();
		    eval.init(app);
		    eval.HoldOutfromFile(filename);
		    
		}

		double tp = app.getTruePositives();
		double fp = app.getFalsePositives();
		double fn = app.getFalseNegatives();
		double tn = app.getTrueNegatives();
		double precision = tp / (tp + fp);
		double recall    = tp / (tp + fn);

		EvaluationReport report = Evaluator.getEvaluationReport();
		report.putOtherData("Precision", String.valueOf(precision));
		report.putOtherData("Recall", String.valueOf(recall));
		report.putOtherData("True Spam", String.valueOf(tp));
		report.putOtherData("True Ham", String.valueOf(tn));
		report.putOtherData("False Spam", String.valueOf(fp));
		report.putOtherData("False Ham", String.valueOf(fn));

		System.out.println(report);
		jcolibri.evaluation.tools.EvaluationResultGUI.show(report, "Spam Filter Toy - Evaluation", false);

	    } catch (Exception e)
	    {
		org.apache.commons.logging.LogFactory.getLog(SpamFilterApp.class).error(e);

	    }
	    setButtonsEnabled(true);
	}
    }
    
    void visualize()
    {
	SpamFilterApp app = configureApp();
	Thread thread = new Thread(new Visualization(app));
	thread.start();
    }
    
    /**
         * Thread for running the Visualization
         * 
         * @author Juan A. Recio-Garcia
         * @version 1.0
         */
    class Visualization implements Runnable
    {
	SpamFilterApp app;
	Visualization(SpamFilterApp app)
	{
	    this.app = app;
	}
	
	public void run()
	{
	    setButtonsEnabled(false);
        	try
        	{
        	    app.configure();
        	    CBRCaseBase _caseBase = app.preCycle();
        	    jcolibri.extensions.visualization.CasesVisualization.visualize(_caseBase.getCases(), app.getKNNConfig());
        	} catch (ExecutionException e)
        	{
        	    org.apache.commons.logging.LogFactory.getLog(SpamFilterApp.class).error(e);
        	    
        	}
    	    setButtonsEnabled(true);

	}
    }
    
    private SpamFilterApp configureApp()
    {
	// Connector
	String corpusFile = null;
	if(this.corpus300.isSelected())
	    corpusFile = "jcolibri/test/test16/corpus300.zip";
	else if(this.corpus600.isSelected())
	    corpusFile = "jcolibri/test/test16/corpus600.zip";
	
	// Simil function
	LocalSimilarityFunction similFunc = null;
	if(this.compressionBased.isSelected())
	    similFunc = new CompressionBased(new GZipCompressor());
	else if(this.normalizedCompression.isSelected())
	    similFunc = new NormalisedCompression(new GZipCompressor());
	else if(this.cosine.isSelected())
	    similFunc = new CosineCoefficient();
	else if(this.dice.isSelected())
	    similFunc = new DiceCoefficient();
	else if(this.jaccard.isSelected())
	    similFunc = new JaccardCoefficient();
	else if(this.overlap.isSelected())
	    similFunc = new OverlapCoefficient();
	
	// Classification method
	KNNClassificationMethod classifMethod = null;
	if(this.majorityVoting.isSelected())
	    classifMethod = new MajorityVotingMethod();
	else if(this.weightedVoting.isSelected())
	    classifMethod = new SimilarityWeightedVotingMethod();
	else if(this.unanimousVoting.isSelected())
	    classifMethod = new UnanimousVotingMethod(EmailSolution.SPAM);
	
	//K
	int k = this.kValue.getNumber().intValue();
	
	SpamFilterApp app = new SpamFilterApp(corpusFile);
	app.setSimilFunc(similFunc);
	app.setClasifMethod(classifMethod);
	app.setK(k);
	
	return app;
    }
    
    public static void main(String[] args) 
    {
	Test16 test = new Test16();
	test.setVisible(true);
    }
}
