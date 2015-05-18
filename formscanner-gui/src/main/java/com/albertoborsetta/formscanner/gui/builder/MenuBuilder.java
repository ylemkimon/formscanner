package com.albertoborsetta.formscanner.gui.builder;

import java.awt.ComponentOrientation;

import javax.swing.JComponent;
import javax.swing.JMenu;

import com.albertoborsetta.formscanner.commons.FormScannerFont;
import javax.swing.ImageIcon;

public class MenuBuilder {

    private final JMenu menu;

    public MenuBuilder(String name, ComponentOrientation orientation) {
        menu = new JMenu(name);
        menu.setFont(FormScannerFont.getFont());
        menu.setComponentOrientation(orientation);
    }

    public MenuBuilder withMnemonic(char mnemonic) {
        menu.setMnemonic(mnemonic);
        return this;
    }

    public MenuBuilder add(JComponent component) {
        menu.add(component);
        return this;
    }

    public MenuBuilder withIcon(ImageIcon icon) {
        menu.setIcon(icon);
        return this;
    }

    public MenuBuilder withSelectedIcon(ImageIcon icon) {
        menu.setSelectedIcon(icon);
        return this;
    }

    public JMenu build() {
        return menu;
    }
}
