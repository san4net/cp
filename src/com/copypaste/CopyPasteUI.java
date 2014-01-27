package com.copypaste;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang.StringUtils;

import com.copypaste.logic.CopyLogic;
import com.copypaste.util.Constants;
import com.copypaste.util.Utils;

public class CopyPasteUI<E> {

	private static final String FILE_LOCATION = "c:/non_os/projects/serialize/cp.ser";
	private CopyLogic copyLogic;
	private JFrame frame = new JFrame("copyPaste");
	private JLabel srcLabel;
	private JLabel destLabel;
	private JButton copyPasteButton = new JButton("Copy Paste");
	private JComboBox<E> srcCombo;
	private JComboBox<E> destCombo;
	// radio button
	private static ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton fileButton;
	private JRadioButton directoryButton;
	private JRadioButton directoryAndSubDirectoryButton;

	private static Object[][] errCodeErrMsg = { { 0, "done" },
			{ 1, "Pls enter proper url" }, { 2, "Entered file doesnot exist" } };

	private WindowListener windowListener = Utils.getWindowListener(this);

	public void injectLogic(CopyLogic logic) {
		this.copyLogic = logic;
	}


	private void createComponents() {
		frame = new JFrame("copyPaste");
		frame.addWindowListener(windowListener);
		srcLabel = new JLabel("Copy : ", JLabel.TRAILING);
		destLabel = new JLabel("Paste : ");

		copyPasteButton = new JButton("Copy Paste");
		copyPasteButton.addActionListener(listener);

		fileButton = new JRadioButton("file");
		fileButton.setActionCommand(Constants.actionCommands[0]);
		;
		fileButton.setName("fileButton");

		directoryButton = new JRadioButton("Directory");
		directoryButton.setActionCommand(Constants.actionCommands[1]);
		directoryButton.setName("directoryButton");

		directoryAndSubDirectoryButton = new JRadioButton("directoryAndSubDir");
		directoryAndSubDirectoryButton.setActionCommand(Constants.actionCommands[2]);
		directoryAndSubDirectoryButton
				.setName("directoryAndSubDirectoryButton");

		buttonGroup.add(fileButton);
		buttonGroup.add(directoryButton);
		buttonGroup.add(directoryAndSubDirectoryButton);
	}

	private void putThingsTogetherForDisplay() {
		Utils.checkNotNull(srcCombo);
		Utils.checkNotNull(destCombo);
		srcCombo.setSize(new Dimension(450, srcCombo.getPreferredSize().height));
		destCombo.setSize(new Dimension(450,
				destCombo.getPreferredSize().height));

		SpringLayout layout = new SpringLayout();

		JPanel mainPanel = new JPanel(layout);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(fileButton);
		buttonPanel.add(directoryButton);
		buttonPanel.add(directoryAndSubDirectoryButton);
		buttonPanel.setBorder(BorderFactory
				.createTitledBorder("Advance Option"));

		Container contentPane = frame.getContentPane();
		mainPanel.add(buttonPanel);
		mainPanel.add(srcLabel);
		mainPanel.add(srcCombo);

		mainPanel.add(destLabel);
		mainPanel.add(destCombo);

		mainPanel.add(copyPasteButton);
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

		layout.putConstraint(SpringLayout.NORTH, copyPasteButton, index,
				SpringLayout.SOUTH, buttonPanel);
		layout.putConstraint(SpringLayout.WEST, copyPasteButton, index,
				SpringLayout.EAST, destCombo);

		frame.add(mainPanel);
	}

	private void initializeComponent() {
		deserialize();
	}

	public void showGui() {
		if (SwingUtilities.isEventDispatchThread()) {
			createComponents();
			initializeComponent();
			putThingsTogetherForDisplay();
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			frame.pack();
			frame.setSize(new Dimension(1100, 300));
			frame.setVisible(true);
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					showGui();
				}
			});
		}
	}

	public static void main(String[] args) {
		final CopyPasteUI<String> cp = new CopyPasteUI<>();
		cp.injectLogic(new MemoryMappedCopyLogicImpl());
		if (SwingUtilities.isEventDispatchThread()) {
			cp.showGui();
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					cp.showGui();
				}
			});
		}

	}

	private ActionListener listener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			copyPasteButton.setEnabled(false);

			String srcLoc = (String) srcCombo.getSelectedItem();
			String destLoc = (String) destCombo.getSelectedItem();

			if (StringUtils.isEmpty(srcLoc) || StringUtils.isEmpty(destLoc)) {
				JOptionPane.showMessageDialog(frame, "Pls enter proper url");
				return;
			}

			Integer result = null;
			
			try {
				result = copyLogic.startCopy(srcLoc, destLoc,buttonGroup.getSelection().getActionCommand());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(frame,
					errCodeErrMsg[result.intValue()][1]);
			copyPasteButton.setEnabled(true);
		}
	};


	public void serialize() throws IOException {
		File f = new File(FILE_LOCATION);
		if (!f.exists()) {
			f.createNewFile();
		}
		try (FileOutputStream fOut = new FileOutputStream(f);
				ObjectOutputStream objectOut = new ObjectOutputStream(fOut);) {
			objectOut.writeObject(srcCombo);
			objectOut.writeObject(destCombo);
			System.out.println("object written");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void deserialize() {
		File f = new File(FILE_LOCATION);
		if (!f.exists())
			return;

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

}
