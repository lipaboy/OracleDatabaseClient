package ru.nsu.fit.g14201.lipatkin.model;

import java.util.Arrays;
import java.util.InputMismatchException;

/**
 * Created by SPN on 22.05.2017.
 */
public class SQLType {
    String typeName;
    int precision;
    int scale;

    //If your constructor is public that you need to return final SQLType to presenter or presenters
    public SQLType(String typeName1, int precision1, int scale1) {
        typeName = typeName1;
        precision = precision1;
        scale = scale1;
    }

    public SQLType(String typeToParse) {
        set(typeToParse);
    }

    void set(String typeName1, int precision1, int scale1) {
        typeName = typeName1;
        precision = precision1;
        scale = scale1;
    }

    //TODO: may be need to use regex
    //TODO: need exceptions on wrong pattern
    void set(String type) {         //type like "NUMBER(2, 16)"
        //StringBuilder wrapper = new StringBuilder(type);
        int bracketOpen = type.indexOf("(");
        if (bracketOpen < 0) {
            typeName = type.toUpperCase();
            precision = 0;      //????
            scale = 0;       //????
        }
        else {
            typeName = type.substring(0, bracketOpen).toUpperCase();
            int comma = type.indexOf(",");
            if (comma < 0) {
                precision = Integer.parseInt(type.substring(bracketOpen + 1, type.length() - 1));
                scale = 0;
            }
            else {
                precision = Integer.parseInt(type.substring(bracketOpen + 1, comma));
                String str = type.substring(comma + 1, type.length() - 1);
                scale = Integer.parseInt(str.trim());
            }
        }
    }

    public final String getTypeName() { return typeName; }

    public String getSQLFormat() {
        return typeName + "(" + Integer.toString(precision) + ", " + Integer.toString(scale) + ")";
    }
}
