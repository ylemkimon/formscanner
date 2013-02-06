package org.albertoborsetta.formscanner.gui;

import org.albertoborsetta.formscanner.commons.Constants;
import org.albertoborsetta.formscanner.gui.font.FormScannerFont;
import org.albertoborsetta.formscanner.model.FormScannerModel;

import org.apache.commons.io.FilenameUtils;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

public class RenameFileFrame extends JInternalFrame {
	private JTextField fileNameField;
	private JLabel fileExtensionField;
	private JLabel statusBar;
	private FormScannerModel model;
	
	/**
	 * Create the frame.
	 */
	public RenameFileFrame(FormScannerModel formScannerModel, String fileName) {
		model = formScannerModel;
		
		setBounds(100, 100, 396, 141);
		setName("renameFileFrame");
		setClosable(true);
		
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
		
		JLabel newFileNameLabel = new JLabel("New name:");
		panel.add(newFileNameLabel, "2, 2, right, default");
		
		fileNameField = new FileNameField(FilenameUtils.removeExtension(fileName));		
		panel.add(fileNameField, "4, 2, 3, 1, fill, default");
		
		fileExtensionField = new JLabel('.' + FilenameUtils.getExtension(fileName));
		panel.add(fileExtensionField, "8, 2");		
		
		JButton okButton = new OKButton();
		panel.add(okButton , "4, 4");
		
		JButton cancelButton = new CancelButton();
		panel.add(cancelButton, "6, 4");
				
		statusBar = new StatusBar("Renaming: " + fileName);
		getContentPane().add(statusBar, BorderLayout.SOUTH);
	}
	
	public void updateRenamedFile(String fileName) {
		statusBar.setText("Rename: " + fileName);
		fileNameField.setText(FilenameUtils.removeExtension(fileName));
		fileExtensionField.setText('.' + FilenameUtils.getExtension(fileName));
	}
	
	public String getNewFileName() {
		String fileName = fileNameField.getText() + fileExtensionField.getText(); 
		return fileName;
	}
	
	private class StatusBar extends JLabel {
		
		public StatusBar(String label) {
			super(label);
			setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			setFont(FormScannerFont.getFont());
		}
	}
	
	private class OKButton extends JButton implements ActionListener {
		
		public OKButton() {
			super("OK");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			renameFiles(Constants.RENAME_FILE_CURRENT);
		}
	}
	
	private class CancelButton extends JButton implements ActionListener {
		
		public CancelButton() {
			super("Cancel");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			renameFiles(Constants.RENAME_FILE_SKIP);
		}
	}
	
	private class FileNameField extends JTextField implements FocusListener {
		
		public FileNameField(String text) {
			super(text);
			setColumns(10);
			addFocusListener(this);
		}

		public void focusGained(FocusEvent e) {
			// Nothing to do			
		}

		public void focusLost(FocusEvent e) {
			// renameFiles(Constants.RENAME_FILE_CURRENT);
		}
		
		
	}
	
	private void renameFiles(int action) {
		model.renameFiles(action);
	}
}
