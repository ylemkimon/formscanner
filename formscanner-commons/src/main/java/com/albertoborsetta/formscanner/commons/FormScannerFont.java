package com.albertoborsetta.formscanner.commons;

import java.awt.Font;

public class FormScannerFont extends Font {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static Font font = null;
	
	public FormScannerFont(String name, int style, int size) {
		super(name, style, size);
		// TODO Auto-generated constructor stub
	}
	
	public static Font getFont() {
		if (font == null) {
			font = new FormScannerFont(Font.SANS_SERIF, Font.PLAIN, 12); 
		}
		return (Font) font;
		
	}

}