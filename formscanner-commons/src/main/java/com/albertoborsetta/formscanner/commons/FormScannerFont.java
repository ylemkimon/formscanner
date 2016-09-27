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
    }

    public static Font getFont() {
        if (font == null) {
            font = new FormScannerFont(Font.SANS_SERIF, Font.PLAIN, 12);
        }
        return font;

    }

    public static Font getFont(String fontType, Integer fontSize) {
        font = new FormScannerFont(fontType, Font.PLAIN, fontSize);
        return font;
    }

    public static Font getImageFont(Integer fontSize) {
        return new FormScannerFont(font.getFamily(), Font.PLAIN, fontSize);
    }
}
