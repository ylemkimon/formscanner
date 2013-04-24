package org.albertoborsetta.formscanner.controller;

import java.awt.event.MouseMotionListener;

import javax.swing.JInternalFrame;
import javax.swing.event.MouseInputListener;

import org.albertoborsetta.formscanner.gui.ImageView;

public interface ImageController extends MouseMotionListener, MouseInputListener  {

	public void add(JInternalFrame view);
	public ImageView getView();
}
