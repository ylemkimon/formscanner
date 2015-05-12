package com.albertoborsetta.formscanner.gui;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JDesktopPane;
import javax.swing.UIManager;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

import com.albertoborsetta.formscanner.api.FormTemplate;
import com.albertoborsetta.formscanner.commons.FormFileUtils;
import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.gui.controller.FormScannerController;
import com.albertoborsetta.formscanner.gui.model.FormScannerModel;
import com.albertoborsetta.formscanner.gui.view.InternalFrame;
import com.albertoborsetta.formscanner.gui.view.MenuBar;
import com.albertoborsetta.formscanner.gui.view.ToolBar;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.commons.lang3.StringUtils;

public class FormScanner extends JFrame {

    private static final long serialVersionUID = 1L;

    private static FormScannerModel model;
    private static JDesktopPane desktopPane;
    private final ToolBar toolBar;
    private final MenuBar menuBar;
    private final FormScannerController mainFrameController;

    /**
     * Launch the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                        FormScanner window = new FormScanner();
                        window.setIconImage(null);
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            File templateFile = new File(args[0]);
            FormTemplate template = null;
            try {
                template = new FormTemplate(templateFile);
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
            String[] extensions = ImageIO.getReaderFileSuffixes();
            Iterator<?> fileIterator = FileUtils.iterateFiles(new File(args[1]), extensions, false);
            HashMap<String, FormTemplate> filledForms = new HashMap<>();
            while (fileIterator.hasNext()) {
                File imageFile = (File) fileIterator.next();
                BufferedImage image = null;
                try {
                    image = ImageIO.read(imageFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
                FormTemplate filledForm = new FormTemplate(imageFile.getName(), template);
                filledForm.findCorners(image, template.getThreshold(), template.getDensity());
                filledForm.findPoints(image, template.getThreshold(), template.getDensity(), template.getSize());
                filledForm.findAreas(image);
                filledForms.put(FilenameUtils.getName(imageFile.toString()), filledForm);
            }
            Locale locale = Locale.getDefault();
            FormFileUtils fileUtils = FormFileUtils.getInstance(locale);

            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

            File outputFile = new File(args[1] + System.getProperty("file.separator") + "results_" + sdf.format(today) + ".csv");
            fileUtils.saveCsvAs(outputFile, filledForms, false);
            System.exit(0);
        }
    }

    /**
     * Create the application.
     */
    private FormScanner() {
        model = new FormScannerModel(this);
        mainFrameController = FormScannerController.getInstance(model);
        addWindowListener(mainFrameController);

        setName(Frame.DESKTOP_FRAME.name());

        setTitle(StringUtils.replace(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.FORMSCANNER_MAIN_TITLE), FormScannerConstants.VERSION_KEY, FormScannerConstants.VERSION));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setLayout(new BorderLayout(0, 0));
        menuBar = new MenuBar(model);
        setJMenuBar(menuBar);

        toolBar = new ToolBar(model);
        getContentPane().add(toolBar, BorderLayout.NORTH);

        desktopPane = new JDesktopPane();
        getContentPane().add(desktopPane, BorderLayout.CENTER);

        model.setDefaultFramePositions();
        setBounds(model.getLastPosition(Frame.DESKTOP_FRAME));
        setVisible(true);
        setDefaultLookAndFeelDecorated(true);
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    }

    public void arrangeFrame(InternalFrame frame) {
        boolean found = false;

        for (Component component : desktopPane.getComponents()) {
            if (frame.getName().equals(component.getName())) {
                component.setVisible(false);
                desktopPane.remove(component);
                found = true;
                break;
            }
        }

        if (!found) {
            desktopPane.add(frame);
            frame.setVisible(true);
        } else {
            arrangeFrame(frame);
        }
    }

    public void disposeFrame(InternalFrame frame) {
        if (frame != null) {
            model.setLastPosition(Frame.valueOf(frame.getName()), frame.getBounds());
            frame.dispose();
        }
        setDefaultControllersEnabled();
        model.resetFirstPass();
    }

    public void setDefaultControllersEnabled() {
        toolBar.setRenameControllersEnabled(true);
        toolBar.setScanControllersEnabled(true);
        toolBar.setScanAllControllersEnabled(true);
        toolBar.setScanCurrentControllersEnabled(false);
        menuBar.setRenameControllersEnabled(true);
        menuBar.setScanControllersEnabled(true);
        menuBar.setScanAllControllersEnabled(true);
    }

    public void setRenameControllersEnabled(boolean enable) {
        toolBar.setRenameControllersEnabled(enable);
        menuBar.setRenameControllersEnabled(enable);
    }

    public void setScanControllersEnabled(boolean enable) {
        toolBar.setScanControllersEnabled(enable);
        menuBar.setScanControllersEnabled(enable);
    }

    public void setScanAllControllersEnabled(boolean enable) {
        toolBar.setScanAllControllersEnabled(enable);
        menuBar.setScanAllControllersEnabled(enable);
    }

    public void setScanCurrentControllersEnabled(boolean enable) {
        toolBar.setScanCurrentControllersEnabled(enable);
    }
}
