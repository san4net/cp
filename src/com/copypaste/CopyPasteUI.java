package com.copypaste;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang.StringUtils;

import com.copypaste.logic.CopyLogic;
import com.copypaste.util.Utils;

public class CopyPasteUI<E> implements Serializable {
	private CopyLogic copyLogic;
	private JFrame frame = new JFrame("copyPaste");
	private JLabel srcLabel = new JLabel("Copy : ", JLabel.TRAILING);
	private JLabel destLabel = new JLabel("Paste : ");
	private JButton copyPasteButton = new JButton("Copy Paste");
	private JTextField srcField = new JTextField(40);
	private JTextField destField = new JTextField(40);

	private JComboBox<E> srcCombo = new HistoricalCombo<>();
	private JComboBox<E> destCombo = new HistoricalCombo<>();

	private static Object[][] errCodeErrMsg = { { 0, "done" },
			{ 1, "Pls enter proper url" }, { 2, "Entered file doesnot exist" } };

	private WindowListener windowListener = Utils.getWindowListener();

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

		srcField = new JTextField(40);
		destField = new JTextField(40);

		srcCombo.setSize(new Dimension(450, srcCombo.getPreferredSize().height));
		destCombo.setSize(new Dimension(450,
				destCombo.getPreferredSize().height));

		SpringLayout layout = new SpringLayout();

		JPanel panel = new JPanel(layout);
		Container contentPane = frame.getContentPane();
		panel.add(srcLabel);
		panel.add(srcCombo);

		panel.add(destLabel);
		panel.add(destCombo);

		panel.add(copyPasteButton);

		// Adjust constraints for the label so it's at (5,5).
		layout.putConstraint(SpringLayout.WEST, srcLabel, 5, SpringLayout.WEST,
				contentPane);
		layout.putConstraint(SpringLayout.NORTH, srcLabel, 5,
				SpringLayout.NORTH, contentPane);

		layout.putConstraint(SpringLayout.WEST, srcCombo, 5, SpringLayout.EAST,
				srcLabel);
		layout.putConstraint(SpringLayout.NORTH, srcCombo, 5,
				SpringLayout.NORTH, contentPane);

		layout.putConstraint(SpringLayout.WEST, destLabel, 5,
				SpringLayout.EAST, srcCombo);
		layout.putConstraint(SpringLayout.NORTH, destLabel, 5,
				SpringLayout.NORTH, contentPane);

		layout.putConstraint(SpringLayout.WEST, destCombo, 5,
				SpringLayout.EAST, destLabel);
		layout.putConstraint(SpringLayout.NORTH, destCombo, 5,
				SpringLayout.NORTH, contentPane);

		layout.putConstraint(SpringLayout.WEST, copyPasteButton, 5,
				SpringLayout.EAST, destCombo);
		layout.putConstraint(SpringLayout.NORTH, copyPasteButton, 5,
				SpringLayout.NORTH, contentPane);

		frame.add(panel);
	}
	
   public void showGui(){
	    createComponents();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setSize(new Dimension(1100, 100));
		frame.setVisible(true);
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
				result = copyLogic.startCopy(srcLoc, destLoc);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(frame,
					errCodeErrMsg[result.intValue()][1]);
			copyPasteButton.setEnabled(true);
		}
	};
}
