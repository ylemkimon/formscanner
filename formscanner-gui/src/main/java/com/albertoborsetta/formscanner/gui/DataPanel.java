package com.albertoborsetta.formscanner.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.JXButton;

import com.albertoborsetta.formscanner.api.FormArea;
import com.albertoborsetta.formscanner.api.FormGroup;
import com.albertoborsetta.formscanner.api.FormQuestion;
import com.albertoborsetta.formscanner.api.FormTemplate;
import com.albertoborsetta.formscanner.commons.FormScannerFont;
import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.TextAreaOutputStream;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.FieldsTableColumn;
import com.albertoborsetta.formscanner.commons.resources.FormScannerResources;
import com.albertoborsetta.formscanner.commons.resources.FormScannerResourcesKeys;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.controller.DataPanelController;
import com.albertoborsetta.formscanner.controller.ImagesGridController;
import com.albertoborsetta.formscanner.controller.FieldsGridController;
import com.albertoborsetta.formscanner.controller.ConsoleController;
import com.albertoborsetta.formscanner.gui.builder.ButtonBuilder;
import com.albertoborsetta.formscanner.gui.builder.PanelBuilder;
import com.albertoborsetta.formscanner.gui.builder.ScrollPaneBuilder;
import com.albertoborsetta.formscanner.gui.builder.TabbedPaneBuilder;
import com.albertoborsetta.formscanner.gui.builder.ToggleButtonBuilder;
import com.albertoborsetta.formscanner.gui.builder.ToolBarBuilder;
import com.albertoborsetta.formscanner.model.FormScannerModel;

public class DataPanel extends JSplitPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton openTemplateButton;
	private JButton newTemplateButton;
	private JButton saveTemplateButton;
	private DataPanelController bottomPanelController;
	private FieldsGridController fieldsGridController;
	private ImagesGridController imagesGridController;
	private JPanel bottomPanelControls;
	private JTable fieldsTable;
	private JTable imagesTable;
	private JButton addFieldButton;
	private JButton removeFieldButton;
	private JButton clearTemplateButton;

	private ComponentOrientation orientation;
	private FormScannerModel model;
	private FormScannerWorkspace workspace;
	private float defaultDividerLocation = 0.25F;
	private JButton openTemplateImage;
	private boolean isLoaded;
	private JPanel templatePanel;
	private JScrollPane scrollTemplateGrid;

	private JXButton openImagesButton;
	private JXButton clearImagesButton;
	private JXButton startButton;
	private JXButton startAllButton;
	private JXButton reloadButton;

	private JScrollPane scrollImagesGrid;

	private JPanel imagesPanel;

	protected class DataTableModel extends DefaultTableModel {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		boolean isTemplateTable;

		public DataTableModel(boolean isTemplateTable) {
			super();
			this.isTemplateTable = isTemplateTable;
		}

		public DataTableModel(int rows, int cols, boolean isTemplateTable) {
			super(rows, cols);
			this.isTemplateTable = isTemplateTable;
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			if (isTemplateTable)
				return col <= 1;
			else 
				return col == 0;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (isTemplateTable) {
				if ((model.isGroupsEnabled() && columnIndex == 3) || (!model.isGroupsEnabled() && columnIndex == 2)) {
					return Boolean.class;
				}
			}
			return super.getColumnClass(columnIndex);
		}
	}

	public class DataTableCellRenderer extends JTextArea implements TableCellRenderer {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		private final ArrayList<ArrayList<Integer>> rowColHeight = new ArrayList<>();

		public DataTableCellRenderer() {
			setLineWrap(true);
			setWrapStyleWord(true);
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
			setFont(table.getFont());
			if (hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
				if (table.isCellEditable(row, column)) {
					setForeground(UIManager.getColor("Table.focusCellForeground"));
					setBackground(UIManager.getColor("Table.focusCellBackground"));
				}
			} else {
				setBorder(new EmptyBorder(1, 2, 1, 2));
			}
			if (value != null) {
				setText(value.toString());
			} else {
				setText(StringUtils.EMPTY);
			}
			adjustRowHeight(table, row, column);
			return this;
		}

		/**
		 * Calculate the new preferred height for a given row, and sets the
		 * height on the table.
		 */
		private void adjustRowHeight(JTable table, int row, int column) {
			int cWidth = table.getTableHeader().getColumnModel().getColumn(column).getWidth();
			setSize(new Dimension(cWidth, 1000));
			int prefH = getPreferredSize().height;
			while (rowColHeight.size() <= row) {
				rowColHeight.add(new ArrayList<Integer>(column));
			}
			ArrayList<Integer> colHeights = rowColHeight.get(row);
			while (colHeights.size() <= column) {
				colHeights.add(0);
			}
			colHeights.set(column, prefH);
			int maxH = prefH;
			for (Integer colHeight : colHeights) {
				if (colHeight > maxH) {
					maxH = colHeight;
				}
			}
			if (table.getRowHeight(row) != maxH) {
				table.setRowHeight(row, maxH);
			}
		}
	}

	private class DataPanelControls extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		protected DataPanelControls() {
			JToggleButton bottomToggleButton = new ToggleButtonBuilder(orientation)
					.withActionListener(bottomPanelController)
					.withActionCommand(FormScannerConstants.TOGGLE_BOTTOM_PANEL).withText("Data")
					.withBorder(new EmptyBorder(2, 5, 3, 5)).withFontSize(11).build();

			setComponentOrientation(orientation);
			setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
			add(bottomToggleButton);
		}
	}

	public DataPanel(FormScannerModel model, JDesktopPane desktop) {
		super(JSplitPane.VERTICAL_SPLIT);
		this.model = model;
		orientation = model.getOrientation();
		workspace = model.getWorkspace();
		isLoaded = false;

		BasicSplitPaneUI columnBasicSplit = (BasicSplitPaneUI) getUI();
		columnBasicSplit.getDivider().setBorder(new LineBorder(Color.WHITE, 1) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
				g.setColor(Color.LIGHT_GRAY);
				g.drawLine(c.getWidth() - 1, 0, c.getWidth() - 1, c.getHeight());
			}
		});

		setFont(FormScannerFont.getFont());
		setComponentOrientation(orientation);
		setDividerSize(2);
		setBorder(null);
		setOneTouchExpandable(false);
		setContinuousLayout(true);
		setTopComponent(desktop);
		setBottomComponent(createContentPanel());
		setResizeWeight(0.8);
		setDividerLocation(defaultDividerLocation);
	}

	private JPanel createContentPanel() {
		bottomPanelController = DataPanelController.getInstance(model);
		bottomPanelController.add(this);

		templatePanel = createTemplatePanel();
		imagesPanel = createImagesPanel();

		JTabbedPane tabbedPane = new TabbedPaneBuilder(orientation)
				.addTab(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.IMAGES_LABEL),
						FormScannerResources.getIconFor(FormScannerResourcesKeys.IMAGES_ICON, 16), imagesPanel)
				.addTab(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_LABEL),
						FormScannerResources.getIconFor(FormScannerResourcesKeys.TEMPLATE_ICON, 16), templatePanel)
				// TODO Comment for debugging
				// .addTab(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.CONSOLE_LABEL),
				// FormScannerResources.getIconFor(FormScannerResourcesKeys.CONSOLE_ICON,
				// 16), createConsolePanel())
				.build();

		return new PanelBuilder(orientation).withBorder(new EtchedBorder()).withLayout(new BorderLayout())
				.add(tabbedPane, BorderLayout.CENTER).build();
	}

	public JPanel getPanelControls() {
		if (bottomPanelControls == null) {
			bottomPanelControls = new DataPanelControls();
		}
		return bottomPanelControls;
	}

	private JPanel createTemplatePanel() {
		JPanel templateToolBar = createTemplateToolbar();

		JPanel templateGridPanel = createTemplateGridPanel();

		scrollTemplateGrid = new JScrollPane(templateGridPanel);

		return new PanelBuilder(orientation).withLayout(new BorderLayout()).add(templateToolBar, BorderLayout.NORTH)
				.add(scrollTemplateGrid, BorderLayout.CENTER).build();
	}

	private JPanel createTemplateGridPanel() {
		fieldsTable = createFieldsTable();
		JScrollPane fieldsTableScrollPane = new ScrollPaneBuilder(fieldsTable, orientation).build();

		return new PanelBuilder(orientation).withLayout(new BorderLayout())
				.addComponent(fieldsTableScrollPane, BorderLayout.CENTER).build();
	}

	private JTable createFieldsTable() {
		DataTableModel fieldsTableModel = new DataTableModel(true);
		
		fieldsGridController = FieldsGridController.getInstance(model);
		fieldsGridController.add(this);
		fieldsTableModel.addTableModelListener(fieldsGridController);
		
		JTable table = new JTable(fieldsTableModel);

		for (FieldsTableColumn tableColumn : FieldsTableColumn.values()) {
			if (tableColumn.equals(FieldsTableColumn.GROUP_COLUMN) && !model.isGroupsEnabled()) {
				continue;
			}
			fieldsTableModel.addColumn(FormScannerTranslation.getTranslationFor(tableColumn.getValue()));
		}

		table.setDefaultRenderer(Object.class, new DataTableCellRenderer());
		table.setRowSorter(new TableRowSorter<>(fieldsTableModel));
		table.setComponentOrientation(orientation);
		table.setRowSelectionAllowed(true);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(fieldsGridController);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		return table;
	}

	public void setupFieldsTable() {
		templatePanel.remove(scrollTemplateGrid);
		JPanel templateGridPanel = createTemplateGridPanel();
		scrollTemplateGrid = new JScrollPane(templateGridPanel);
		templatePanel.add(scrollTemplateGrid, BorderLayout.CENTER);

		if (!isLoaded) {
			return;
		}

		DataTableModel fieldsTableModel = (DataTableModel) fieldsTable.getModel();

		HashMap<String, FormGroup> groups = model.getTemplate().getGroups();

		for (Entry<String, FormGroup> group : groups.entrySet()) {
			
			String groupName = group.getKey().equals(FormScannerConstants.EMPTY_GROUP_NAME) ? "" : group.getKey();  

			HashMap<String, FormQuestion> fields = group.getValue().getFields();
			for (FormQuestion field : fields.values()) {
				if (model.isGroupsEnabled()) {
					fieldsTableModel.addRow(new Object[] { groupName, field.getName(),
							FormScannerTranslation.getTranslationFor(field.getType().getValue()), field.isMultiple(),
							field.getPoints().size() });
				} else {
					fieldsTableModel.addRow(new Object[] { field.getName(),
							FormScannerTranslation.getTranslationFor(field.getType().getValue()), field.isMultiple(),
							field.getPoints().size() });
				}
			}

			HashMap<String, FormArea> areas = group.getValue().getAreas();
			for (FormArea area : areas.values()) {
				if (model.isGroupsEnabled()) {
					fieldsTableModel.addRow(new Object[] { groupName, area.getName(),
							FormScannerTranslation.getTranslationFor(area.getType().getValue()), null, null });
				} else {
					fieldsTableModel.addRow(new Object[] { area.getName(),
							FormScannerTranslation.getTranslationFor(area.getType().getValue()), null, null });
				}
			}
		}
		enableEditTemplate();
	}

	private JPanel createTemplateToolbar() {
		openTemplateButton = new ButtonBuilder(orientation).withActionCommand(FormScannerConstants.LOAD_TEMPLATE)
				.withActionListener(bottomPanelController)
				.withToolTip(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.LOAD_TEMPLATE_TOOLTIP))
				.withIcon(FormScannerResources.getIconFor(FormScannerResourcesKeys.IMPORT_ICON, 16)).build();

		newTemplateButton = new ButtonBuilder(orientation).withActionCommand(FormScannerConstants.CREATE_TEMPLATE)
				.withActionListener(bottomPanelController)
				.withToolTip(
						FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.CREATE_TEMPLATE_TOOLTIP))
				.withIcon(FormScannerResources.getIconFor(FormScannerResourcesKeys.EDIT_ICON, 16)).build();

		saveTemplateButton = new ButtonBuilder(orientation).withActionCommand(FormScannerConstants.SAVE_TEMPLATE)
				.withActionListener(bottomPanelController)
				.withToolTip(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.SAVE_TEMPLATE_TOOLTIP))
				.withIcon(FormScannerResources.getIconFor(FormScannerResourcesKeys.SAVE_ICON, 16)).setEnabled(false)
				.build();

		openTemplateImage = new ButtonBuilder(orientation).withActionCommand(FormScannerConstants.OPEN_TEMPLATE_IMAGE)
				.withActionListener(bottomPanelController)
				.withToolTip(FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.OPEN_TEMPLATE_IMAGE_TOOLTIP))
				.withIcon(FormScannerResources.getIconFor(FormScannerResourcesKeys.IMAGES_ICON, 16)).setEnabled(false)
				.build();

		JToolBar templateToolBar = new ToolBarBuilder(orientation).withAlignmentY(Component.CENTER_ALIGNMENT)
				.withAlignmentX(Component.LEFT_ALIGNMENT).add(newTemplateButton).add(openTemplateButton)
				.add(saveTemplateButton).add(openTemplateImage).build();

		addFieldButton = new ButtonBuilder(orientation).withActionCommand(FormScannerConstants.ADD_FIELD)
				.withActionListener(bottomPanelController)
				.withToolTip(
						FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.ADD_FIELD_BUTTON_TOOLTIP))
				.withIcon(FormScannerResources.getIconFor(FormScannerResourcesKeys.ADD_FIELD_BUTTON, 16))
				.setEnabled(false).build();

		removeFieldButton = new ButtonBuilder(orientation).withActionCommand(FormScannerConstants.REMOVE_FIELD)
				.withActionListener(bottomPanelController)
				.withToolTip(FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.REMOVE_FIELD_BUTTON_TOOLTIP))
				.withIcon(FormScannerResources.getIconFor(FormScannerResourcesKeys.REMOVE_FIELD_BUTTON, 16))
				.setEnabled(false).build();

		clearTemplateButton = new ButtonBuilder(orientation).withActionCommand(FormScannerConstants.CLEAR_TEMPLATE)
				.withActionListener(bottomPanelController)
				.withToolTip(FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.CLEAR_TEMPLATE_BUTTON_TOOLTIP))
				.withIcon(FormScannerResources.getIconFor(FormScannerResourcesKeys.CLEAR_TEMPLATE_BUTTON, 16))
				.setEnabled(false).build();

		JToolBar editTemplateToolBar = new ToolBarBuilder(orientation).withAlignmentY(Component.CENTER_ALIGNMENT)
				.withAlignmentX(Component.LEFT_ALIGNMENT).add(addFieldButton).add(removeFieldButton)
				.add(clearTemplateButton).build();

		PanelBuilder panel = new PanelBuilder(orientation);

		if (orientation.isLeftToRight()) {
			panel.withLayout(new FlowLayout(FlowLayout.LEFT));
		} else {
			panel.withLayout(new FlowLayout(FlowLayout.RIGHT));
		}

		return panel.add(templateToolBar).add(editTemplateToolBar).build();
	}

	public String getSelectedField() {
		String fieldName = (String) fieldsTable.getValueAt(fieldsTable.getSelectedRow(), 1);
		return fieldName;
	}

	public String getSelectedGroup() {
		String groupName = (String) fieldsTable.getValueAt(fieldsTable.getSelectedRow(), 0);
		return groupName;
	}

	public void removeSelectedField() {
		DataTableModel fieldsTableModel = (DataTableModel) fieldsTable.getModel();

		fieldsTableModel.removeRow(fieldsTable.getSelectedRow());
		enableEditTemplate();
	}

	private JPanel createImagesPanel() {
		// TODO: add toolbar with openImages/saveResults (enabled only if
		// results available)

		JPanel imagesToolBar = createImagesToolbar();
		
		JPanel imagesGridPanel = createImagesGridPanel();

		scrollImagesGrid = new JScrollPane(imagesGridPanel);

		return new PanelBuilder(orientation).withLayout(new BorderLayout()).add(imagesToolBar, BorderLayout.NORTH)
				.add(scrollImagesGrid, BorderLayout.CENTER).build();
	}

	private JPanel createImagesGridPanel() {

		imagesTable = createImagesTable();
		JScrollPane imagesTableScrollPane = new ScrollPaneBuilder(imagesTable, orientation).build();

		return new PanelBuilder(orientation).withLayout(new BorderLayout())
				.addComponent(imagesTableScrollPane, BorderLayout.CENTER).build();
	}

	private JPanel createImagesToolbar() {
		openImagesButton = new ButtonBuilder(orientation).withActionCommand(FormScannerConstants.OPEN_IMAGES)
				.withActionListener(bottomPanelController)
				.withToolTip(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.OPEN_IMAGES))
				.withIcon(FormScannerResources.getIconFor(FormScannerResourcesKeys.OPEN_IMAGES_ICON, 16)).build();

		JToolBar imagesToolBar = new ToolBarBuilder(orientation).withAlignmentY(Component.CENTER_ALIGNMENT)
				.withAlignmentX(Component.LEFT_ALIGNMENT).add(openImagesButton).build();

		clearImagesButton = new ButtonBuilder(orientation).withActionCommand(FormScannerConstants.CLEAR_IMAGES)
				.withActionListener(bottomPanelController)
				.withToolTip(FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.CLEAR_IMAGES_BUTTON_TOOLTIP))
				.withIcon(FormScannerResources.getIconFor(FormScannerResourcesKeys.CLEAR_IMAGES_BUTTON, 16))
				.setEnabled(false).build();

		JToolBar clearToolBar = new ToolBarBuilder(orientation).withAlignmentY(Component.CENTER_ALIGNMENT)
				.withAlignmentX(Component.LEFT_ALIGNMENT).add(clearImagesButton).build();
		
		startButton = new ButtonBuilder(orientation)
				.withActionCommand(FormScannerConstants.ANALYZE_FILES_FIRST)
				.withActionListener(bottomPanelController)
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.ANALYZE_FILES_TOOLTIP))
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.ANALYZE_FILES_ICON, 16))
				.setEnabled(false).build();
		
		startAllButton = new ButtonBuilder(orientation)
				.withActionCommand(FormScannerConstants.ANALYZE_FILES_ALL)
				.withActionListener(bottomPanelController)
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.ANALYZE_FILES_ALL_TOOLTIP))
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.ANALYZE_FILES_ALL_ICON, 16))
				.setEnabled(false).build();
		
		reloadButton = new ButtonBuilder(orientation)
				.withActionCommand(FormScannerConstants.ANALYZE_FILES_CURRENT)
				.withActionListener(bottomPanelController)
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.ANALYZE_FILES_CURRENT_TOOLTIP))
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.ANALYZE_FILES_CURRENT_ICON, 16))
				.setEnabled(false).build();
		
		JToolBar editToolBar = new ToolBarBuilder(orientation).withAlignmentY(Component.CENTER_ALIGNMENT)
				.withAlignmentX(Component.LEFT_ALIGNMENT).add(startButton).add(startAllButton).add(reloadButton).build();

		PanelBuilder panel = new PanelBuilder(orientation);

		if (orientation.isLeftToRight()) {
			panel.withLayout(new FlowLayout(FlowLayout.LEFT));
		} else {
			panel.withLayout(new FlowLayout(FlowLayout.RIGHT));
		}

		return panel.add(imagesToolBar).add(editToolBar).add(clearToolBar).build();
	}

	private JPanel createConsolePanel() {
		JTextArea consoleTextArea = new JTextArea();
		consoleTextArea.setBackground(Color.BLACK);
		consoleTextArea.setForeground(Color.LIGHT_GRAY);
		TextAreaOutputStream consoleOutputStream = new TextAreaOutputStream(consoleTextArea);
		PrintStream con = new PrintStream(consoleOutputStream);
		System.setOut(con);
		System.setErr(con);

		JScrollPane scrollConsolePanel = new JScrollPane(consoleTextArea);

		return new PanelBuilder(orientation).withLayout(new BorderLayout())
				.add(createConsoleToolBar(consoleOutputStream), BorderLayout.NORTH)
				.add(scrollConsolePanel, BorderLayout.CENTER).build();
	}

	private JPanel createConsoleToolBar(TextAreaOutputStream consoleOutputStream) {
		ConsoleController consoleController = new ConsoleController(this.model);
		consoleController.add(consoleOutputStream);

		JButton clearButton = new ButtonBuilder(orientation).withActionCommand(FormScannerConstants.CLEAR_CONSOLE)
				.withActionListener(consoleController)
				.withToolTip(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.CLEAR_CONSOLE_TOOLTIP))
				.withIcon(FormScannerResources.getIconFor(FormScannerResourcesKeys.CLEAR_CONSOLE_ICON, 16)).build();

		JToolBar consoleToolBar = new ToolBarBuilder(orientation).withAlignmentY(Component.CENTER_ALIGNMENT)
				.withAlignmentX(Component.CENTER_ALIGNMENT).add(clearButton).build();

		PanelBuilder panel = new PanelBuilder(orientation);

		if (orientation.isLeftToRight()) {
			panel.withLayout(new FlowLayout(FlowLayout.LEFT));
		} else {
			panel.withLayout(new FlowLayout(FlowLayout.RIGHT));
		}

		return panel.add(consoleToolBar).build();
	}

	public void enableEditTemplate() {
		openTemplateImage.setEnabled(isLoaded);
		addFieldButton.setEnabled(isLoaded);
		saveTemplateButton.setEnabled(isLoaded);
		if (!isLoaded || ((fieldsTable != null) && (fieldsTable.getRowCount() == 0))) {
			removeFieldButton.setEnabled(false);
			clearTemplateButton.setEnabled(false);
		} else {
			removeFieldButton.setEnabled(true);
			clearTemplateButton.setEnabled(true);
		}
	}

	public void setTemplateData() {
		workspace.setTemplateData();
	}

	public void togglePanel() {
		boolean collapsed = getDividerLocation() == getMaximumDividerLocation();
		if (!collapsed) {
			lastDividerLocation = getDividerLocation();
			getBottomComponent().setMinimumSize(new Dimension());
			setDividerLocation(1.0d);
		} else {
			setDividerLocation(lastDividerLocation);
		}
	}

	public void removeAllFields() {
		DataTableModel fieldsTableModel = (DataTableModel) fieldsTable.getModel();

		while (fieldsTable.getRowCount() > 0) {
			fieldsTableModel.removeRow(0);
		}
		enableEditTemplate();
	}

	public void isTemplateLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}

	private JTable createImagesTable() {
		DataTableModel imagesTableModel = new DataTableModel(false);
		
		imagesGridController = ImagesGridController.getInstance(model);
		imagesGridController.add(this);
		imagesTableModel.addTableModelListener(imagesGridController);
		
		JTable table = new JTable(imagesTableModel);
		
		imagesTableModel.addColumn(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.FILENAME));
		
		if (isLoaded) {

			FormTemplate template = model.getTemplate();

			ArrayList<String> groupNames = new ArrayList<>(template.getGroups().keySet());
			Collections.sort(groupNames);

			for (String groupName : groupNames) {
				FormGroup group = template.getGroup(groupName);

				ArrayList<String> fieldNames = new ArrayList<>(group.getFields().keySet());
				Collections.sort(fieldNames);

				for (String fieldName : fieldNames) {
					String columnName = fieldName;
					if (!groupName.equals(FormScannerConstants.EMPTY_GROUP_NAME)) {
						columnName = "[" + groupName + "] " + columnName;
					}
					imagesTableModel.addColumn(columnName);
				}

				ArrayList<String> areaNames = new ArrayList<>(group.getAreas().keySet());
				Collections.sort(areaNames);

				for (String areaName : areaNames) {
					String columnName = areaName;
					if (!groupName.equals(FormScannerConstants.EMPTY_GROUP_NAME)) {
						columnName = "[" + groupName + "] " + columnName;
					}
					imagesTableModel.addColumn(columnName);
				}
			}
		}

		table.setDefaultRenderer(Object.class, new DataTableCellRenderer());
		table.setRowSorter(new TableRowSorter<>(imagesTableModel));
		table.setComponentOrientation(orientation);
		// table.setCellSelectionEnabled(true);
		table.setRowSelectionAllowed(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(imagesGridController);
		return table;
	}

	public void setupImagesTable() {
		imagesPanel.remove(scrollImagesGrid);
		JPanel imagesGridPanel = createImagesGridPanel();
		scrollImagesGrid = new JScrollPane(imagesGridPanel);
		imagesPanel.add(scrollImagesGrid, BorderLayout.CENTER);

		DataTableModel tableModel = (DataTableModel) imagesTable.getModel();

		String[] fileList = model.getOpenedFileList();

		int col = 1;
		if (isLoaded) {
			
			FormTemplate template = model.getTemplate();
			
			ArrayList<String> groupNames = new ArrayList<>(template.getGroups().keySet());
			
			for (String groupName : groupNames) {
				FormGroup group = template.getGroup(groupName);
				
				ArrayList<String> fieldNames = new ArrayList<>(group.getFields().keySet());
				ArrayList<String> areaNames = new ArrayList<>(group.getAreas().keySet());
				col = col + fieldNames.size() + areaNames.size();
			}
		}

		for (String fileName : fileList) {
		
			Object[] values = new Object[col];
		
			values[0] = fileName;
			for (int j=1; j<col; j++) {
				values[j] = null;
			}
			
			tableModel.addRow(values);
		}
	}

	public void removeImages() {
		DataTableModel imagesTableModel = (DataTableModel) imagesTable.getModel();

		while (imagesTable.getRowCount() > 0) {
			imagesTableModel.removeRow(0);
		}
	}

	public String getSelectedFileName() {
		int selectedRow = (imagesTable.getSelectedRow()) < 0 ? 0 : imagesTable.getSelectedRow();
			
		return (String) imagesTable.getValueAt(selectedRow, 0);
	}

	public void setScanControllersEnabled(boolean enable) {
		startButton.setEnabled(enable);
	}

	public void setScanAllControllersEnabled(boolean enable) {
		startAllButton.setEnabled(enable);
	}

	public void setScanCurrentControllersEnabled(boolean enable) {
		reloadButton.setEnabled(enable);
	}

	public boolean selectNextImage() {
		
		int nextRow = imagesTable.getSelectedRow() + 1;
		
//		imagesTable.getSelectionModel().clearSelection();
		imagesTable.getSelectionModel().setSelectionInterval(nextRow+1, nextRow);
		
		return nextRow > imagesTable.getRowCount();
			
	}
}