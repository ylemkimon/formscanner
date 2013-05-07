package org.albertoborsetta.formscanner.gui.builder;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.albertoborsetta.formscanner.commons.FormScannerFont;

public class MenuItemBuilder {
	
	private JMenuItem menuItem;
	
	public MenuItemBuilder(String text) {
		menuItem = new JMenuItem(text);
		menuItem.setFont(FormScannerFont.getFont());
	}
	
	public MenuItemBuilder withActionCommand(String action) {
		menuItem.setActionCommand(action);
		return this;
	}
	
	public MenuItemBuilder withActionListener(ActionListener listener) {
		menuItem.addActionListener(listener);
		return this;
	}
	
	public MenuItemBuilder withMnemonic(char mnemonic) {
		menuItem.setMnemonic(mnemonic);
		return this;
	}
	
	public MenuItemBuilder withAccelerator(KeyStroke accelerator) {
		menuItem.setAccelerator(accelerator);
		return this;
	}
	
	public MenuItemBuilder setEnabled(boolean enabled) {
		menuItem.setEnabled(enabled);
		return this;
	}
	
	public JMenuItem build() {
		return menuItem;
	}
}