package org.albertoborsetta.formscanner.gui;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JList;

import org.albertoborsetta.formscanner.gui.font.FormScannerFont;

import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;

public class FileListFrame extends JInternalFrame {
	
	private JScrollPane scrollPane;
	private JList list;
	
	/**
	 * Create the frame.
	 */
	public FileListFrame(List<String> fileList) {
		setName("fileListFrame");
		setTitle("Images");
		setBounds(100, 100, 198, 597);
		setLocation(10, 10);
		
		setFrameIcon(new ImageIcon(FormScanner.class.getResource("/org/albertoborsetta/formscanner/gui/icons/file.gif")));
		setIconifiable(true);
		setResizable(true);
		setMaximizable(true);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		list = new JList(fileList.toArray());
		list.setFont(FormScannerFont.getFont());
		scrollPane.setViewportView(list);
	}
	
	public void updateFileList(List<String> fileList) {
		list.setListData(fileList.toArray());
	}
	
	public void selectFile(int index) {
		list.setSelectedIndex(index);
	}
	
}
