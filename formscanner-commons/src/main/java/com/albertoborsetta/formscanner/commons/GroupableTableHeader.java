package com.albertoborsetta.formscanner.commons;

import javax.swing.table.JTableHeader;

import java.util.*;
import javax.swing.table.*;

/**
 * GroupableTableHeader
 *
 * @version 1.0 10/20/98
 * @author Nobuo Tamemasa
 */

public class GroupableTableHeader extends JTableHeader {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Vector<ColumnGroup> columnGroups = null;

	public GroupableTableHeader(TableColumnModel model) {
		super(model);
		setUI(new GroupableTableHeaderUI());
		setReorderingAllowed(false);
	}

	public void updateUI() {
		setUI(new GroupableTableHeaderUI());
	}

	public void setReorderingAllowed(boolean b) {
		reorderingAllowed = false;
	}

	public void addColumnGroup(ColumnGroup g) {
		if (columnGroups == null) {
			columnGroups = new Vector<ColumnGroup>();
		}
		columnGroups.addElement(g);
	}

	public Enumeration<?> getColumnGroups(TableColumn col) {
		if (columnGroups == null)
			return null;
		Enumeration<ColumnGroup> e = columnGroups.elements();
		while (e.hasMoreElements()) {
			ColumnGroup cGroup = (ColumnGroup) e.nextElement();
			Vector<?> v_ret = (Vector<?>) cGroup.getColumnGroups(col, new Vector<ColumnGroup>());
			if (v_ret != null) {
				return v_ret.elements();
			}
		}
		return null;
	}

	public void setColumnMargin() {
		if (columnGroups == null)
			return;
		int columnMargin = getColumnModel().getColumnMargin();
		Enumeration<ColumnGroup> e = columnGroups.elements();
		while (e.hasMoreElements()) {
			ColumnGroup cGroup = (ColumnGroup) e.nextElement();
			cGroup.setColumnMargin(columnMargin);
		}
	}

}
