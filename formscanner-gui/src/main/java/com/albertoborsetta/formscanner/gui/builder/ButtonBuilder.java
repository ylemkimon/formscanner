package com.albertoborsetta.formscanner.gui.builder;

import java.awt.ComponentOrientation;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import org.jdesktop.swingx.JXButton;

import com.albertoborsetta.formscanner.commons.CompoundIcon;
import com.albertoborsetta.formscanner.commons.FormScannerFont;
import com.albertoborsetta.formscanner.commons.RotatedIcon;
import com.albertoborsetta.formscanner.commons.RotatedIcon.Rotate;
import com.albertoborsetta.formscanner.commons.TextIcon;

public class ButtonBuilder {

	private final JXButton button;
	private final ComponentOrientation orientation;
	private String text;
	private ImageIcon icon;
	private Rotate rotation;
	private double degrees;
	private Integer fontSize;

	public ButtonBuilder(ComponentOrientation orientation) {
		button = new JXButton();
		button.setComponentOrientation(orientation);
		this.orientation = orientation;
	}

	public ButtonBuilder withActionListener(ActionListener listener) {
		button.addActionListener(listener);
		return this;
	}

	public ButtonBuilder withActionCommand(String action) {
		button.setActionCommand(action);
		return this;
	}

	public ButtonBuilder withToolTip(String tooltip) {
		button.setToolTipText(tooltip);
		return this;
	}

	public ButtonBuilder withIcon(ImageIcon icon) {
		this.icon = icon;
		return this;
	}

	public ButtonBuilder withBorder(Border border) {
		button.setBorder(border);
		return this;
	}

	public ButtonBuilder withSelectedIcon(ImageIcon icon) {
		button.setSelectedIcon(icon);
		return this;
	}

	public ButtonBuilder withText(String text) {
		this.text = text;
		return this;
	}

	public ButtonBuilder setEnabled(boolean enabled) {
		button.setEnabled(enabled);
		return this;
	}

	public ButtonBuilder withFontSize(Integer fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	public ButtonBuilder setSelected(boolean selected) {
		button.setSelected(selected);
		return this;
	}

	public ButtonBuilder setBorder(EtchedBorder border) {
		button.setBorder(border);
		return this;
	}

	public ButtonBuilder withRotation(Rotate rotation) {
		this.rotation = rotation;
		return this;
	}

	public ButtonBuilder withRotation(Rotate rotation, double degrees) {
		this.rotation = rotation;
		this.degrees = degrees;
		return this;
	}

	public JXButton build() {
		if (fontSize != null)
			button.setFont(FormScannerFont.getFont(fontSize));
		else
			button.setFont(FormScannerFont.getFont());
		
		if (rotation != null) {
			TextIcon ti = new TextIcon(button, text);
			CompoundIcon ci = null;
			if (icon != null) {
				ImageIcon ii = icon;
				ci = new CompoundIcon(ii, ti);
			} else {
				ci = new CompoundIcon(ti);
			}
			RotatedIcon ri = null;
			if (rotation == Rotate.ABOUT_CENTER) {
				ri = new RotatedIcon(ci, degrees);
			} else {
				ri = new RotatedIcon(ci, rotation);
			}
			button.setIcon(ri);
		} else {
			if (icon != null) {
				button.setIcon(icon);
			}
			if (text != null) {
				button.setText(text);
			}
		}

		return button;
	}

	public ButtonBuilder setAlignment() {
		if (orientation.isLeftToRight()) {
			button.setHorizontalAlignment(SwingConstants.LEFT);
		} else {
			button.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return this;
	}

}
