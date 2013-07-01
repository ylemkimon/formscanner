package org.albertoborsetta.formscanner.imageparser;

import java.io.File;

public class ProcessImage {

	public static void main(String args[]) {
        String fileName = args[0];
        File file;
        
        try {
        	file = new File(fileName);
        } catch (Exception e) {
        	file = null;
        }
        
        ScanImage imageScan = new ScanImage(file);
        imageScan.locateCorners();
    }
	
	

}