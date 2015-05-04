package com.albertoborsetta.formscanner.api;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;

import com.albertoborsetta.formscanner.api.commons.Constants;
import com.albertoborsetta.formscanner.api.commons.Constants.Corners;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.Result;

public class BarcodeDetector implements Callable<HashMap<String, FormArea>> {

    private final FormTemplate template;
    private final FormArea barcodeArea;
    private final FormTemplate parent;
    private final HashMap<String, FormArea> barcodes;
    private final BufferedImage image;

    public BarcodeDetector(FormTemplate template, FormArea barcodeArea,
            BufferedImage image) {
        this.template = template;
        this.barcodeArea = barcodeArea;
        parent = template.getParentTemplate();
        this.image = image;
        barcodes = new HashMap<>();
    }

    @Override
    public HashMap<String, FormArea> call() throws Exception {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(
                source));

        try {

            Reader reader = new MultiFormatReader();
            Result resultBarcode = null;

            int attempts = 0;
            boolean lastAttempt = false;
            while ((resultBarcode == null) && !lastAttempt) {
                try {
                    HashMap<DecodeHintType, Object> hints;
                    switch (attempts) {
                        case 2:
                            // Try again with other binarizer
                            bitmap = new BinaryBitmap(new HybridBinarizer(source));
                            lastAttempt = true;
                        case 1:
                            // Look for normal barcode in photo
                            hints = Constants.HINTS;
                            break;
                        default:
                            // Look for pure barcode
                            hints = Constants.HINTS_PURE;
                            break;
                    }
                    resultBarcode = reader.decode(bitmap, hints);
                } catch (ReaderException re) {
                    // Do nothing
                }
                attempts++;
            }

            FormArea resultArea = calcResultArea();
            resultArea.setText(resultBarcode != null ? resultBarcode.getText() : StringUtils.EMPTY);
            barcodes.put(barcodeArea.getName(), resultArea);
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
        return barcodes;
    }

    private FormArea calcResultArea() {
        FormArea responseArea = new FormArea(barcodeArea.getName());

        for (Corners corner : Corners.values()) {
            responseArea.setCorner(corner,
                    calcResponsePoint(barcodeArea.getCorner(corner)));
        }

        responseArea.setType(barcodeArea.getType());
        return responseArea;
    }

    private FormPoint calcResponsePoint(FormPoint responsePoint) {
        FormPoint point = responsePoint.clone();

        FormPoint templateOrigin = parent.getCorner(Corners.TOP_LEFT);
        double templateRotation = parent.getRotation();
        double scale = Math.sqrt(template.getDiagonal() / parent.getDiagonal());

        point.rotoTranslate(templateOrigin, templateRotation, true);
        point.scale(scale);
        point.rotoTranslate(template.getCorners().get(Corners.TOP_LEFT),
                template.getRotation(), false);
        return point;
    }
}
