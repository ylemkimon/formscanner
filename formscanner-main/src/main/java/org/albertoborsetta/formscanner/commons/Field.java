package org.albertoborsetta.formscanner.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


public class Field {
	char ch;
    int type, subtype;
    String name;
    String[] choices;
    Map<Integer, Integer> positions;
    String[] values;
    int numValues = 0;
    boolean[] singleDone;
    
    public Field(String line) {
        StringTokenizer st = new StringTokenizer(line, " ");
        ch = st.nextToken().charAt(0);
        String typestr = st.nextToken();
        if(typestr.equalsIgnoreCase("row")) {
            type = FormScannerConstants.ROW_CHOICE;
        } else if(typestr.equalsIgnoreCase("column")) {
            type = FormScannerConstants.COLUMN_CHOICE;
        } else if(typestr.equalsIgnoreCase("grid")) {
            type = FormScannerConstants.GRID_CHOICE;
        }
        String subtypestr = st.nextToken();
        if(subtypestr.equalsIgnoreCase("single")) {
            subtype = FormScannerConstants.SINGLE;
        } else if(subtypestr.equalsIgnoreCase("multiple")) {
            subtype = FormScannerConstants.MULTIPLE;
        } else if(subtypestr.equalsIgnoreCase("column")) {
            subtype = FormScannerConstants.COLUMN;
        } else if(subtypestr.equalsIgnoreCase("row")) {
            subtype = FormScannerConstants.ROW;
        }
        name = st.nextToken();
        ArrayList<String> choicearr = new ArrayList<String>();
        while(st.hasMoreTokens()) {
            choicearr.add(st.nextToken());
        }
        choices = new String[choicearr.size()];
        for(int i = 0; i < choicearr.size(); i++) {
            choices[i] = (String)(choicearr.get(i));
        }
        if(type != FormScannerConstants.GRID_CHOICE) {
            values = new String[choices.length];
            singleDone = null;
        } else {
            values = new String[100];
            singleDone = new boolean[100];
        }
        positions = new HashMap<Integer, Integer>();
    }
    
    public char getCh() {
        return ch;
    }
    
    public int getType() {
        return type;
    }
    
    public int getSubtype() {
        return subtype;
    }
    
    public String getName() {
        return name;
    }
    
    public String[] getChoices() {
        return choices;
    }

    int currpos = 0;
    public void addPos(int i) {
        if(type == FormScannerConstants.ROW_CHOICE) {
            positions.put(new Integer(i), new Integer(currpos++));
        } else if(type == FormScannerConstants.COLUMN_CHOICE) {
            positions.put(new Integer(i), new Integer(currpos++));
        } else if(type == FormScannerConstants.GRID_CHOICE && subtype == FormScannerConstants.ROW) {
            positions.put(new Integer(i), new Integer(currpos % choices.length));
            currpos++;
        } else if(type == FormScannerConstants.GRID_CHOICE && subtype == FormScannerConstants.COLUMN) {
            System.out.println("addpos -- " + i + ":" + currpos);
            positions.put(new Integer(i), new Integer(currpos++));
        }
    }
    
    public String getValue(int i) {
        if(type ==FormScannerConstants.GRID_CHOICE && subtype == FormScannerConstants.COLUMN) {
            int mod = currpos / choices.length;
            return choices[((Integer)(positions.get(new Integer(i)))).intValue() / mod];
        } else {
            return choices[((Integer)(positions.get(new Integer(i)))).intValue()];
        }
    }

    public void putValue(int i) {
        if(type == FormScannerConstants.GRID_CHOICE && subtype == FormScannerConstants.COLUMN) {
            int posi = ((Integer)(positions.get(new Integer(i)))).intValue();
            System.out.println("currpos = " + currpos + ":" + choices.length + ":" + i + ":" + posi + ":" + (posi % (currpos / choices.length)) + ":" + singleDone[posi % (currpos / choices.length)]);
            if(!singleDone[posi % (currpos / choices.length)]) {
                values[numValues++] = getValue(i);
                singleDone[posi % (currpos / choices.length)] = true;
            }
        } else if(type == FormScannerConstants.GRID_CHOICE && subtype == FormScannerConstants.ROW) {
            int posi = ((Integer)(positions.get(new Integer(i)))).intValue();
            if(!singleDone[posi / choices.length]) {
                values[numValues++] = getValue(i);
                singleDone[posi / choices.length] = true;
            }
        } else {
            values[numValues++] = getValue(i);
        }
    }
    
    public String getFieldValues() {
        if(subtype == FormScannerConstants.SINGLE) {
            return values[0];
        }
        else if(subtype == FormScannerConstants.MULTIPLE) {
            String retval = "";
            for(int i = 0; i < numValues; i++) {
                retval = retval + " " + values[i];
            }
            if(retval.length() > 0) {
                return retval.substring(1);
            } else {
                return "";
            }
        }
        else if(type == FormScannerConstants.GRID_CHOICE) {
            String retval = "";
            for(int i = 0; i < numValues; i++) {
                retval = retval + values[i];
            }
            if(retval.length() > 0) {
                return retval;
            } else {
                return "";
            }
        }
        return "";
    }
}
