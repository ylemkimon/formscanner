package org.albertoborsetta.formscanner.gui.view;

import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import java.awt.BorderLayout;

import javax.swing.JList;

import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import org.albertoborsetta.formscanner.gui.builder.ListBuilder;
import org.albertoborsetta.formscanner.gui.builder.ScrollPaneBuilder;
import org.albertoborsetta.formscanner.gui.model.FormScannerModel;

public class FileListFrame extends InternalFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JScrollPane scrollPane;
	private JList<String> list;
	
	/**
	 * Create the frame.
	 */
	public FileListFrame(FormScannerModel model, String[] fileList) {
		super(model);
		setBounds(model.getLastPosition(Frame.FILE_LIST_FRAME));
		
		setName(Frame.FILE_LIST_FRAME.name());
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
