package org.albertoborsetta.formscanner.gui;

import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

public class StatusBar extends JLabel {
	
	private static final long serialVersionUID = 1L;
	
	public StatusBar() {
		super("Status label");
		
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
	}
}
