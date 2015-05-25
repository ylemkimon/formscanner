package com.albertoborsetta.formscanner.commons.resources;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import javax.swing.ImageIcon;

public class FormScannerResources {

    private static String iconsPath;
    private static String licensePath;
    private static String template;

    public static void setResources(String path) {
        iconsPath = path + "/icons/";
        licensePath = path + "/license/";
    }

    public static ImageIcon getIconFor(String key) {
        ImageIcon icon = new ImageIcon(iconsPath + key);
        return icon;
    }

    public static void setTemplate(String tpl) {
        template = tpl;
    }

    public static File getTemplate() {
        return new File(template);
    }

    public static File getLicense() {
        return new File(licensePath + "license.txt");
    }

    public static Image getFormScannerIcon() {
        try {
            Image icon = ImageIO.read(new File(iconsPath + "formscanner.png"));
            return icon;
        } catch (IOException ex) {
            Logger.getLogger(FormScannerResources.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
