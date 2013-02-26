package org.albertoborsetta.formscanner.gui;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JList;

import org.albertoborsetta.formscanner.commons.FormScannerFont;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class FileListFrame extends JInternalFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JScrollPane scrollPane;
	private JList list;
	private FormScannerModel model;
	
	/**
	 * Create the frame.
	 */
	public FileListFrame(FormScannerModel formScannerModel, List<String> fileList) {
		
		model = formScannerModel;
		int desktopHeight = model.getDesktopSize().height;
		
		setBounds(10, 10, 200, desktopHeight-20);
		
		setName("fileListFrame");
		setTitle("Images");		
		
		setIconifiable(true);
		setResizable(true);
		setMaximizable(true);
		
		list = new FileList(fileList.toArray());
		
		scrollPane = new JScrollPane();	
		scrollPane.setViewportView(list);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}
	
	public void updateFileList(List<String> fileList) {
		list.setListData(fileList.toArray());
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
	
	private class FileList extends JList {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public FileList(Object[] list) {
			super(list); 
			setFont(FormScannerFont.getFont());
			setSelectionMode(ListSelectionModel.SINGLE_SELECTION);			
		}
	}
}
