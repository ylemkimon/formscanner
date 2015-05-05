package com.albertoborsetta.formscanner.commons.translation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class FormScannerTranslation extends Properties {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected static FormScannerTranslation translations = null;

    private FormScannerTranslation(String path, String language) {
        super();
        try {
            String translationFile = path + "/language/formscanner-" + language
                    + ".lang";
            final FileInputStream translationInputStream = new FileInputStream(
                    translationFile);

            load(translationInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setTranslation(String path, String language) {
        translations = new FormScannerTranslation(path, language);
    }

    public static String getTranslationFor(String key) {
        String value = translations.getProperty(key, key);
        try {
            value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return value;
    }

    public static char getMnemonicFor(String key) {
        String value = translations.getProperty(key, key);
        try {
            value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return value.charAt(0);
    }
}
