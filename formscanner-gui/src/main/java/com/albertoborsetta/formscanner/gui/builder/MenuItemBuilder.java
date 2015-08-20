package com.albertoborsetta.formscanner.gui.builder;

import java.awt.ComponentOrientation;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.albertoborsetta.formscanner.commons.FormScannerFont;
import javax.swing.ImageIcon;

public class MenuItemBuilder {

    private final JMenuItem menuItem;

    public MenuItemBuilder(String text, ComponentOrientation orientation) {
        menuItem = new JMenuItem(text);
        menuItem.setFont(FormScannerFont.getFont());
        menuItem.setComponentOrientation(orientation);
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
    
    public MenuItemBuilder withIcon(ImageIcon icon) {
        menuItem.setIcon(icon);
        return this;
    }
    
    public MenuItemBuilder withSelectedIcon(ImageIcon icon) {
        menuItem.setSelectedIcon(icon);
        return this;
    }

    public JMenuItem build() {
        return menuItem;
    }
}
