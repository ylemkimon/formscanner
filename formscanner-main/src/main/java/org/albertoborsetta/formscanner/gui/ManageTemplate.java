package org.albertoborsetta.formscanner.gui;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JButton;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

public class ManageTemplate extends JInternalFrame {
	private JPanel panel;
	private JTable table;
	private JTable table_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManageTemplate frame = new ManageTemplate();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ManageTemplate() {
		setName("ManageTemplate");
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setTitle("Manage Template");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setName("TemplateList");
		getContentPane().add(tabbedPane);
		
		panel = new JPanel();
		tabbedPane.addTab("Template list", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout(0, 0));
		panel.add(panel_1, BorderLayout.CENTER);
		
		JList list = new JList();
		List<String> listData = new ArrayList<String>();
		listData.add("a");
		listData.add("b");
		listData.add("c");
		list.setListData(listData.toArray());
		panel_1.add(list, BorderLayout.CENTER);
		
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));		
		
		JButton btnAdd = new JButton("Add");
		panel_2.add(btnAdd, "2, 2");
		
		JButton btnNewButton = new JButton("Remove");
		panel_2.add(btnNewButton, "4, 2");
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Properties", null, panel_3, null);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JButton btnOK = new JButton("OK");
		panel_4.add(btnOK, "2, 2, right, default");
		
		JButton btnCancel = new JButton("Cancel");
		panel_4.add(btnCancel, "4, 2, right, default");
		
		JPanel panel_5 = new JPanel();
		panel_3.add(panel_5, BorderLayout.CENTER);
		panel_5.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblType = new JLabel("Type");
		panel_5.add(lblType, "2, 2, right, default");
		
		JComboBox comboBox = new JComboBox();
		panel_5.add(comboBox, "4, 2, fill, default");
		
		JLabel lblNRowscolumns = new JLabel("Number of rows/columns");
		panel_5.add(lblNRowscolumns, "2, 4, right, default");
		
		JSpinner spinner = new JSpinner();
		panel_5.add(spinner, "4, 4");
		
		JLabel lblNValues = new JLabel("Number of values");
		panel_5.add(lblNValues, "2, 6, right, default");
		
		JSpinner spinner_1 = new JSpinner();
		panel_5.add(spinner_1, "4, 6");
		
		JPanel panel_6 = new JPanel();
		tabbedPane.addTab("Position", null, panel_6, null);
		panel_6.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_7 = new JPanel();
		panel_6.add(panel_7, BorderLayout.SOUTH);
		panel_7.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JButton btnOK1 = new JButton("OK");
		panel_7.add(btnOK1, "2, 2, right, default");
		
		JButton btnCancel1 = new JButton("Cancel");
		panel_7.add(btnCancel1, "4, 2, right, default");
		
		JPanel panel_8 = new JPanel();
		panel_8.setLayout(new BorderLayout(0, 0));
		panel_6.add(panel_8, BorderLayout.CENTER);
		
		table_1 = new JTable();
		panel_8.add(table_1);
	}
}
