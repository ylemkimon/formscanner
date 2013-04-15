package org.albertoborsetta.formscanner.gui.builder;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerFont;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;

public class MenuItem extends JMenuItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static class Builder {
		
		private String text = null;
		private String action = null;
		private ActionListener listener = null;
		private char mnemonic = ' ';
		private KeyStroke accelerator = null;
		
		public Builder(String text) {
			this.text = text;
		}
		
		public Builder withActionCommand(String action) {
			this.action = action;
			return this;
		}
		
		public Builder withActionListener(ActionListener listener) {
			this.listener = listener;
			return this;
		}
		
		public Builder withMnemonic(char mnemonic) {
			this.mnemonic = mnemonic;
			return this;
		}
		
		public Builder withAccelerator(KeyStroke accelerator) {
			this.accelerator = accelerator;
			return this;
		}
		
		public JMenuItem build() {
			return new MenuItem(this);
		}
	}

	public MenuItem(Builder builder) {
		super(builder.text);
		setActionCommand(builder.action);
		addActionListener(builder.listener);
		setMnemonic(builder.mnemonic);
		setAccelerator(builder.accelerator);
		setFont(FormScannerFont.getFont());
	}
}
