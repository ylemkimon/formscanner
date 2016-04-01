package com.albertoborsetta.formscanner.commons;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;


/**
  * ColumnGroup
  *
  * @version 1.0 10/20/98
  * @author Nobuo Tamemasa
  */

public class ColumnGroup {
	protected TableCellRenderer renderer;
	protected Vector<Object> v;
	protected String text;
	protected int margin = 0;

	public ColumnGroup(String text) {
		this(null, text);
	}

	public ColumnGroup(TableCellRenderer renderer, String text) {
		if (renderer == null) {
			this.renderer = new DefaultTableCellRenderer() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					JTableHeader header = table.getTableHeader();
					if (header != null) {
						setForeground(header.getForeground());
						setBackground(header.getBackground());
						setFont(header.getFont());
					}
					setHorizontalAlignment(JLabel.CENTER);
					setText((value == null) ? "" : value.toString());
					setBorder(UIManager.getBorder("TableHeader.cellBorder"));
					return this;
				}
			};
		} else {
			this.renderer = renderer;
		}
		this.text = text;
		v = new Vector<Object>();
	}

	/**
	 * @param obj
	 *            TableColumn or ColumnGroup
	 */
	public void add(Object obj) {
		if (obj == null) {
			return;
		}
		v.addElement(obj);
	}

	/**
	 * @param c
	 *            TableColumn
	 * @param v
	 *            ColumnGroups
	 */
	public Vector<ColumnGroup> getColumnGroups(TableColumn c, Vector<ColumnGroup> g) {
		g.addElement(this);
		if (v.contains(c))
			return g;
		Enumeration<Object> e = v.elements();
		while (e.hasMoreElements()) {
			Object obj = e.nextElement();
			if (obj instanceof ColumnGroup) {
				Vector<ColumnGroup> groups = (Vector<ColumnGroup>) ((ColumnGroup) obj).getColumnGroups(c, (Vector<ColumnGroup>) g.clone());
				if (groups != null)
					return groups;
			}
		}
		return null;
	}

	public TableCellRenderer getHeaderRenderer() {
		return renderer;
	}

	public void setHeaderRenderer(TableCellRenderer renderer) {
		if (renderer != null) {
			this.renderer = renderer;
		}
	}

	public Object getHeaderValue() {
		return text;
	}

	public Dimension getSize(JTable table) {
		Component comp = renderer.getTableCellRendererComponent(table, getHeaderValue(), false, false, -1, -1);
		int height = comp.getPreferredSize().height;
		int width = 0;
		Enumeration<Object> e = v.elements();
		while (e.hasMoreElements()) {
			Object obj = e.nextElement();
			if (obj instanceof TableColumn) {
				TableColumn aColumn = (TableColumn) obj;
				width += aColumn.getWidth();
				width += margin;
			} else {
				width += ((ColumnGroup) obj).getSize(table).width;
			}
		}
		return new Dimension(width, height);
	}

	public void setColumnMargin(int margin) {
		this.margin = margin;
		Enumeration<Object> e = v.elements();
		while (e.hasMoreElements()) {
			Object obj = e.nextElement();
			if (obj instanceof ColumnGroup) {
				((ColumnGroup) obj).setColumnMargin(margin);
			}
		}
	}
}
