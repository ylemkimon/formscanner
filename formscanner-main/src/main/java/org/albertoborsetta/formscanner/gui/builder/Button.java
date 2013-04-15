package org.albertoborsetta.formscanner.gui.builder;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Button extends JButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static class Builder {
		
		private String tooltip = null;
		private boolean enabled = true;
		private String action = null;
		private ActionListener listener = null;
		private ImageIcon icon = null;
		private String text = null;
		
		public Builder() {}
		
		public Builder withActionListener(ActionListener listener) {
			this.listener = listener;
			return this;
		}
		
		public Builder withActionCommand(String action) {
			this.action = action;
			return this;
		}
		
		public Builder withToolTip(String tooltip) {
			this.tooltip = tooltip;
			return this;
		}
		
		public Builder withIcon(ImageIcon icon) {
			this.icon = icon;
			return this;
		}
		
		public Builder withText(String text) {
			this.text = text;
			return this;
		}
		
		public Builder isEnabled(boolean enabled) {
			this.enabled = enabled;
			return this;
		}		
		
		public Button build() {
			return new Button(this);
		}
	} 
	
	public Button(Builder builder) {
		super();
		setIcon(builder.icon);
		setText(builder.text);
		setToolTipText(builder.tooltip);
		setEnabled(builder.enabled);
		setActionCommand(builder.action);
		addActionListener(builder.listener);
	}
}
