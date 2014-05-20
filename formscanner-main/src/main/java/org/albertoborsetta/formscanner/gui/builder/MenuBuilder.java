package org.albertoborsetta.formscanner.gui.builder;

import javax.swing.JComponent;
import javax.swing.JMenu;

import org.albertoborsetta.formscanner.commons.FormScannerFont;

public class MenuBuilder {
	
	private JMenu menu;
	
	public MenuBuilder(String name) {
		menu = new JMenu(name);
		menu.setFont(FormScannerFont.getFont());
	}
	
	public MenuBuilder withMnemonic(char mnemonic) {
		menu.setMnemonic(mnemonic);
		return this;
	}
	
	public MenuBuilder add(JComponent component) {
		menu.add(component);
		return this;
	}
	
	public JMenu build() {
		return menu;
	}
}
