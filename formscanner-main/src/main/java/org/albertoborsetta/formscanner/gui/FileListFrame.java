package org.albertoborsetta.formscanner.gui;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import java.awt.BorderLayout;

import javax.swing.JList;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.gui.builder.ListBuilder;
import org.albertoborsetta.formscanner.gui.builder.ScrollPaneBuilder;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class FileListFrame extends JInternalFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JScrollPane scrollPane;
	private JList<String> list;
	private FormScannerModel model;
	
	/**
	 * Create the frame.
	 */
	public FileListFrame(FormScannerModel model, String[] fileList) {
		
		this.model = model;
		int desktopHeight = this.model.getDesktopSize().height;
		
		setBounds(10, 10, 200, desktopHeight-20);
		
		setName(FormScannerConstants.FILE_LIST_FRAME_NAME);
		setTitle(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.FILE_LIST_FRAME_TITLE));		
		
		setIconifiable(true);
		setResizable(true);
		setMaximizable(true);
		
		scrollPane = getPanel(fileList);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void updateFileList(String[] fileList) {
		list.setListData(fileList);
	}
	
	public void selectFile(int index) {
		list.setSelectedIndex(index);
	}
	
	public String getSelectedItem() {
		return list.getSelectedValue().toString();
	}
	
	public String getItemByIndex(int index) {
		list.setSelectedIndex(index);
		return list.getSelectedValue().toString();
	}
	
	public int getSelectedItemIndex() {
		int index = 0;
		if (!list.isSelectionEmpty()) {
			index = list.getSelectedIndex(); 
		}
		return index;
	}
	
	private JScrollPane getPanel(String[] fileList) {
		list = new ListBuilder(fileList)
			.withSelectionMode(ListSelectionModel.SINGLE_SELECTION)
			.build();
		
		return new ScrollPaneBuilder(list)
			.build();
	} 
}
