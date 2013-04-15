package org.albertoborsetta.formscanner.gui.builder;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.JTabbedPane;

public class TabbedPane extends JTabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static class Builder {
		
		private int tabPosition = 1;
		private Integer i = 0;
		private HashMap<Integer, String> titles = new HashMap<Integer, String>();
		private HashMap<Integer, Component> components = new HashMap<Integer, Component>();
		
		public Builder(int tabPosition) {
			this.tabPosition = tabPosition;
		}
		
		public Builder addTab(String title, Component component) {
			titles.put(i, title);
			components.put(i, component);
			i++;
			return this;
		}
		
		public TabbedPane build() {
			return new TabbedPane(this);
		}
	}
	
	public TabbedPane(Builder builder) {
		super(builder.tabPosition);
		for (int i=0; i<builder.titles.size(); i++) {
			addTab(builder.titles.get(i), builder.components.get(i));
		}
	}

}
