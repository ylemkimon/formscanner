package org.albertoborsetta.formscanner.gui;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;

import org.albertoborsetta.formscanner.gui.font.FormScannerFont;
import org.albertoborsetta.formscanner.model.FormScannerModel;

import java.awt.BorderLayout;
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
		
		setBounds(100, 100, 396, 141);
		setName("renameFileFrame");
		
		setTitle("Rename file");
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblNewName = new JLabel("New name:");
		panel.add(lblNewName, "2, 2, right, default");
		
		textField = new JTextField(fileName);
		panel.add(textField, "4, 2, 3, 1, fill, default");
		textField.setColumns(10);
		
		JLabel lblext = new JLabel(".ext");
		panel.add(lblext, "8, 2");		
		
		JButton btnNewButton = new JButton("OK");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.renameNextFile();
			}
		});
		panel.add(btnNewButton, "4, 4");
		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO
			}
		});
		panel.add(btnNewButton_1, "6, 4");
		
		
		lblNewLabel = new StatusBar("Renaming: " + fileName);
		getContentPane().add(lblNewLabel, BorderLayout.SOUTH);
		
		/*
		 * 
		
		
		 */

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
