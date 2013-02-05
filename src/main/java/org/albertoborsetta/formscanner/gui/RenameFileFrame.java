package org.albertoborsetta.formscanner.gui;

import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;

import org.albertoborsetta.formscanner.gui.font.FormScannerFont;
import org.albertoborsetta.formscanner.model.FormScannerModel;

import java.awt.Dimension;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

public class RenameFileFrame extends JInternalFrame {
	private JTextField textField;
	private JLabel lblNewLabel;
	private FormScannerModel model;
	/**
	 * Create the frame.
	 */
	public RenameFileFrame(FormScannerModel formScannerModel, String fileName) {
		model = formScannerModel;
		
		setName("renameFileFrame");
		// setPreferredSize(new Dimension(30, 50));
		// setMinimumSize(new Dimension(40, 50));
		setMaximumSize(new Dimension(2147483647, 50));
		// getContentPane().setMaximumSize(new Dimension(2147483647, 50));
		setTitle("Rename file");
		setBounds(100, 100, 375, 110);
		getContentPane().setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.MIN_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.MIN_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.MIN_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.MIN_ROWSPEC,}));
		
		textField = new JTextField(fileName);
		textField.setMaximumSize(new Dimension(2147483647, 24));
		textField.setPreferredSize(new Dimension(200, 24));
		textField.setMinimumSize(new Dimension(200, 24));
		getContentPane().add(textField, "3, 1, center, center");
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("OK");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.renameNextFile();
			}
		});
		getContentPane().add(btnNewButton, "5, 1, left, center");
		
		lblNewLabel = new StatusBar("Renaming: " + fileName);
		getContentPane().add(lblNewLabel, "1, 3, 7, 1, left, center");

	}
	
	public void updateRenamedFile(String fileName) {
		lblNewLabel.setText("Rename: " + fileName);
		textField.setText(fileName);
	}
	
	public String getNewFileName() {
		return textField.getText();
	}
	
	private class StatusBar extends JLabel {
		
		public StatusBar(String label) {
			super(label);
			setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			setFont(FormScannerFont.getFont());
		}
	}

}
