package com.copypaste.impls;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import org.apache.commons.lang.StringUtils;

import com.copypaste.CopyPasteUI;
import com.copypaste.HistoricalCombo;
import com.copypaste.util.Constants;
import com.copypaste.util.Constants.COPY_OPTION;
import com.copypaste.util.Constants.STATE;
import com.copypaste.util.Utils;

public  class CopyPasteUIImpl<E>  extends CopyPasteUI<E>  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3188773904926030910L;
	//TODO to come from property file
	private static final String SERIALIZATION_LOCATION = "c:/temp/cp.txt";
	private final String fileName= "cp.txt";
	private Object sharedChannel = new Object();
	private CopyPasteTask copypastTask = new CopyPasteTask<>(sharedChannel);
	private JLabel srcLabel;
	private JLabel destLabel;
	private JButton copyPasteButton ;
	private JComboBox<E> srcCombo;
	private JComboBox<E> destCombo;
	private static ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton fileButton;
	private JRadioButton directoryButton;
	private JProgressBar progressBar;
	private JTextArea taskOutput;
    private Task backGroundTask;
	private static Object[][] errCodeErrMsg = { { 0, "done" },
			{ 1, "Pls enter proper url" }, { 2, "Entered file doesnot exist" },
			{ 3, "Directory doesnot exit" } };

	private WindowListener windowListener = Utils.getWindowListener(this);

	public CopyPasteUIImpl(String title) {
		super(title);
	}

	private void createComponents() {
		addWindowListener(windowListener);
		srcLabel = new JLabel("Copy : ", JLabel.TRAILING);
		destLabel = new JLabel("Paste : ");

		copyPasteButton = new JButton("Copy");
		copyPasteButton.addActionListener(listener);

		fileButton = new JRadioButton("file");
		fileButton.setActionCommand(Constants.actionCommands[0]);
		fileButton.setName("fileButton");

		directoryButton = new JRadioButton("Directory");
		directoryButton.setActionCommand(Constants.actionCommands[1]);
		directoryButton.setName("directoryButton");
		
		progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        
        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);
        
		buttonGroup.add(fileButton);
		buttonGroup.add(directoryButton);
	}

	private void putThingsTogetherForDisplay() {
		Utils.checkNotNull(srcCombo);
		Utils.checkNotNull(destCombo);
		srcCombo.setSize(new Dimension(700, srcCombo.getPreferredSize().height));
		destCombo.setSize(new Dimension(700, destCombo.getPreferredSize().height));

		SpringLayout layout = new SpringLayout();

		JPanel mainPanel = new JPanel(layout);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(fileButton);
		buttonPanel.add(directoryButton);
		buttonPanel.setBorder(BorderFactory
				.createTitledBorder("Advance Option"));

		Container contentPane = getContentPane();
		mainPanel.add(buttonPanel);
		mainPanel.add(srcLabel);
		mainPanel.add(srcCombo);

		mainPanel.add(destLabel);
		mainPanel.add(destCombo);

		mainPanel.add(copyPasteButton);
		mainPanel.add(progressBar);
		mainPanel.add(taskOutput);
		
		int index = 5;
		layout.putConstraint(SpringLayout.NORTH, buttonPanel, index,
				SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, buttonPanel, index,
				SpringLayout.WEST, contentPane);

		index = index + 5;
		// Adjust constraints for the label so it's at (5,5).
		layout.putConstraint(SpringLayout.NORTH, srcLabel, index,
				SpringLayout.SOUTH, buttonPanel);
		layout.putConstraint(SpringLayout.WEST, srcLabel, index,
				SpringLayout.WEST, contentPane);

		layout.putConstraint(SpringLayout.NORTH, srcCombo, index,
				SpringLayout.SOUTH, buttonPanel);
		layout.putConstraint(SpringLayout.WEST, srcCombo, index,
				SpringLayout.EAST, srcLabel);

		layout.putConstraint(SpringLayout.NORTH, destLabel, index,
				SpringLayout.SOUTH, buttonPanel);
		layout.putConstraint(SpringLayout.WEST, destLabel, index,
				SpringLayout.EAST, srcCombo);

		layout.putConstraint(SpringLayout.NORTH, destCombo, index,
				SpringLayout.SOUTH, buttonPanel);
		layout.putConstraint(SpringLayout.WEST, destCombo, index,
				SpringLayout.EAST, destLabel);

		layout.putConstraint(SpringLayout.NORTH, copyPasteButton, index+5,
				SpringLayout.SOUTH, srcLabel);
		layout.putConstraint(SpringLayout.WEST, copyPasteButton, index+5,
				SpringLayout.WEST, contentPane);
		
		layout.putConstraint(SpringLayout.NORTH, progressBar, index+10, SpringLayout.SOUTH, srcLabel);
		layout.putConstraint(SpringLayout.WEST, progressBar, index+5, SpringLayout.EAST, copyPasteButton);
		
		layout.putConstraint(SpringLayout.NORTH, taskOutput , index+5, SpringLayout.SOUTH, progressBar);
		layout.putConstraint(SpringLayout.WEST, taskOutput , index+5, SpringLayout.WEST, contentPane);
		
		add(mainPanel);
	}

	private void initializeComponent() {
		fileButton.setSelected(true);
		deserialize();
	}

	public static void main(String[] args) {
		final CopyPasteUIImpl<String> cp = new CopyPasteUIImpl<String>(
				"copy paste");
		if (SwingUtilities.isEventDispatchThread()) {
			cp.build();
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					cp.build();
				}
			});
		}

	}

	private ActionListener listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (sharedChannel) {
				copyPasteButton.setEnabled(false);
				String srcLoc = (String) srcCombo.getSelectedItem();
				String destLoc = (String) destCombo.getSelectedItem();

				if (StringUtils.isEmpty(srcLoc) || StringUtils.isEmpty(destLoc)) {
					JOptionPane.showMessageDialog((Component) e.getSource(), "Pls enter proper url");
					copyPasteButton.setEnabled(true);
					return;
				}

				Integer result = null;
				try {
					copypastTask.setCurrentState(STATE.DOING);
					copypastTask.setSourceLocation(srcLoc);
					copypastTask.setDestLocation(destLoc);
					copypastTask
							.setFile(fileButton.isSelected() ? COPY_OPTION.FILE
									: COPY_OPTION.DIRECTORY);
					copypastTask.start();
					backGroundTask = new Task();
					backGroundTask.addPropertyChangeListener(propertyChangeListener);
					backGroundTask.execute();
					
					while (copypastTask.getCurrentState() != STATE.READEY) {
						sharedChannel.wait();
					}
					showUI();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		}
	};
	
	private PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if ("progress".equals(evt.getPropertyName())) {
	        int progress = (Integer) evt.getNewValue();
	        progressBar.setValue(progress);
	        taskOutput.append(String.format(
	                "Completed %d%% of task.\n", backGroundTask.getProgress()));
	    } 
			
		}
	};

	public void serialize() throws IOException {
		File f = new File(SERIALIZATION_LOCATION);
		
		if (!f.exists()) {
			f.createNewFile();
		}
		try (FileOutputStream fOut = new FileOutputStream(f);
				ObjectOutputStream objectOut = new ObjectOutputStream(fOut);) {
			objectOut.writeObject(srcCombo);
			objectOut.writeObject(destCombo);
			System.out.println("object written");
			if(backGroundTask != null){
				backGroundTask.cancel(true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void deserialize() {
		File f = new File(SERIALIZATION_LOCATION);
		if (!f.exists()){
			srcCombo = new HistoricalCombo<>();
			destCombo = new HistoricalCombo<>();
			return;
		}

		try (FileInputStream fIn = new FileInputStream(f);
				ObjectInputStream objectOut = new ObjectInputStream(fIn);) {
			srcCombo = (HistoricalCombo<E>) objectOut.readObject();
			destCombo = (HistoricalCombo<E>) objectOut.readObject();
		} catch (Exception ex) {
			ex.printStackTrace();
			srcCombo = new HistoricalCombo<>();
			destCombo = new HistoricalCombo<>();
		}
	}

	@Override
	public CopyPasteUI<E> build() {
		if (SwingUtilities.isEventDispatchThread()) {
			createComponents();
			initializeComponent();
			putThingsTogetherForDisplay();
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					build();
				}
			});
		}
		return this;
	}

	@Override
	public void showUI() {
		//copyPasteButton.setEnabled(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		pack();
		setSize(new Dimension(600, 300));
		setVisible(true);

	}
	
	class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            setProgress(0);
            
            while(copypastTask.completedSize() != copypastTask.size()){
            	 try {
                     Thread.sleep(1000);
                 } catch (InterruptedException ignore) {}
            	 
            	double percentage = ( copypastTask.completedSize()/copypastTask.size()) * 100;
            	int temp = (int) percentage;
            	setProgress(temp);
            } 
            
            if( copypastTask.completedSize() == copypastTask.size()){
            	setProgress(100);
            }
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            setCursor(null); //turn off the wait cursor
            taskOutput.append("Done!\n");
            taskOutput.append(copypastTask.getSummary());
        }
    }
	
}