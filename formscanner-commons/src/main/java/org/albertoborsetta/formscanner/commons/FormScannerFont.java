package org.albertoborsetta.formscanner.commons;

import java.awt.Font;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Map;

public class FormScannerFont extends Font {
	
	static Font instance = null;
	
	public FormScannerFont(String name, int style, int size) {
		super(name, style, size);
		// TODO Auto-generated constructor stub
	}
	
	public static Font getFont() {
		if (instance == null) {
			instance = new FormScannerFont(Font.SANS_SERIF, Font.PLAIN, 12); 
		}
		return (Font) instance;
		
	}

}
