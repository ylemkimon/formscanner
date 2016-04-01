package com.albertoborsetta.formscanner.gui;

import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.JXList;

import java.awt.BorderLayout;

import com.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import com.albertoborsetta.formscanner.gui.builder.ListBuilder;
import com.albertoborsetta.formscanner.gui.builder.ScrollPaneBuilder;
import com.albertoborsetta.formscanner.model.FormScannerModel;

@Deprecated
public class FileListFrame extends ScrollablePanel {

	private static final long serialVersionUID = 1L;

	private final JScrollPane scrollPane;
	private JXList list;

	/**
	 * Create the frame.
	 *
	 * @param model
	 * @param fileList
	 */
	public FileListFrame(FormScannerModel model, String[] fileList) {
		super(model);

		setName(Frame.FILE_LIST_FRAME.name());

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
		return (String) list.getSelectedValue();
	}

	public String getItemByIndex(int index) {
		list.setSelectedIndex(index);
		return (String) list.getSelectedValue();
	}

	public int getSelectedItemIndex() {
		int index = 0;
		if (!list.isSelectionEmpty()) {
			index = list.getSelectedIndex();
		}
		return index;
	}

	private JScrollPane getPanel(String[] fileList) {
		if (fileList != null) {
			list = new ListBuilder(fileList, orientation).withSelectionMode(ListSelectionModel.SINGLE_SELECTION)
					.build();

			return new ScrollPaneBuilder(list, orientation).build();
		}
		return null;
	}
}
