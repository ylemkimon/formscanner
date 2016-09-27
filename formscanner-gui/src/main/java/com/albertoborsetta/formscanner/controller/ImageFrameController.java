package com.albertoborsetta.formscanner.controller;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;

import com.albertoborsetta.formscanner.api.FormPoint;
import com.albertoborsetta.formscanner.api.FormTemplate;
import com.albertoborsetta.formscanner.api.commons.Constants.Corners;
import com.albertoborsetta.formscanner.model.FormScannerModel;
import com.albertoborsetta.formscanner.gui.ImageFrame;

public class ImageFrameController implements MouseMotionListener,
        MouseInputListener, MouseWheelListener, ActionListener, ItemListener {

    private final FormScannerModel model;
    private ImageFrame view;
    private FormPoint p1;
    private FormPoint p2;
    private int buttonPressed;

    public ImageFrameController(FormScannerModel model) {
        this.model = model;
    }

    public void add(ImageFrame view) {
        this.view = view;
    }

    // MouseMotionListener
    @Override
    public void mouseDragged(MouseEvent e) {
        switch (view.getMode()) {
            case SETUP_AREA:
            case SETUP_POINTS:
            case MODIFY_POINTS:
                if (!e.isControlDown() && (buttonPressed == MouseEvent.BUTTON1)) {
                    FormPoint p = showCursorPosition(e);
                    Corners corner = view.getSelectedButton();
                    if (corner != null) {
                        FormTemplate template = view.getTemplate();
                        template.setCorner(corner, p);
                        view.showCornerPosition();
                        view.repaint();
                    } else {
                        view.setTemporaryPoint(p);
                        view.repaint();
                    }
                    break;
                }
            case VIEW:
                if (e.isControlDown()) {
                    showCursorPosition(e);
                    p2 = new FormPoint(e.getPoint());
                    if (p1.dist2(p2) >= 10) {
                        double deltaX = (p1.getX() - p2.getX());
                        double deltaY = (p1.getY() - p2.getY());

                        p1 = p2;

                        view.setScrollBars((int) deltaX, (int) deltaY);
                    }
                }
            default:
                break;
        }

    }

    private FormPoint showCursorPosition(MouseEvent e) {
        FormPoint p = getCursorPoint(e);
        view.showCursorPosition(p);
        return p;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        showCursorPosition(e);
    }

    // MouseInputListener
    @Override
    public void mouseClicked(MouseEvent e) {
        switch (view.getMode()) {
            case MODIFY_POINTS:
                if (e.getButton() == MouseEvent.BUTTON3) {
                    model.deleteNearestPointTo(getCursorPoint(e));
                }
                view.repaint();
                break;
            case VIEW:
            default:
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttonPressed = e.getButton();
        switch (view.getMode()) {
            case SETUP_AREA:
            case SETUP_POINTS:
            case MODIFY_POINTS:
                if (!e.isControlDown() && (buttonPressed == MouseEvent.BUTTON1)) {
                    FormPoint p = getCursorPoint(e);
                    Corners corner = view.getSelectedButton();

                    if (corner != null) {
                        FormTemplate template = view.getTemplate();
                        template.setCorner(corner, p);
                        view.showCornerPosition();
                        view.repaint();
                    } else {
                        view.setTemporaryPoint(p);
                        view.repaint();
                    }
                    break;
                }
            case VIEW:
                if (e.isControlDown()) {
                    p1 = new FormPoint(e.getPoint());
                    view.setImageCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            default:
                break;
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (view.getMode()) {
            case SETUP_AREA:
            case SETUP_POINTS:
            case MODIFY_POINTS:
                if (!e.isControlDown() && (buttonPressed == MouseEvent.BUTTON1)) {
                    FormPoint p = getCursorPoint(e);
                    Corners corner = view.getSelectedButton();
                    if (corner != null) {
                        FormTemplate template = view.getTemplate();
                        template.setCorner(corner, p);
                        view.showCornerPosition();
                        view.repaint();
                        view.resetCornerButtons();
                    } else {
                        view.clearTemporaryPoint();
                        model.addPoint(view, p);
                    }
                    break;
                }
            case VIEW:
                if (e.isControlDown()) {
                    view.setImageCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                }
            default:
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        view.setImageCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        view.setImageCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int delta = e.getUnitsToScroll() * 5;

        if (e.isControlDown()) {
            view.setScrollBars(delta, 0);
        } else {
            view.setScrollBars(0, delta);
        }

        FormPoint p = getCursorPoint(e);
        view.showCursorPosition(p);
    }

    private FormPoint getCursorPoint(MouseEvent e) {
        int dx = view.getHorizontalScrollbarValue();
        int dy = view.getVerticalScrollbarValue();

        int x = (int) (e.getX() / view.getZoom()) + dx;
        int y = (int) (e.getY() / view.getZoom()) + dy;

        FormPoint point = new FormPoint(x, y);
        return point;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Corners corner = Corners.valueOf(e.getActionCommand());
        view.toggleCornerButton(corner);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            view.setZoom();
        }
    }
}
