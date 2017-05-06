package ru.nsu.fit.g14201.lipatkin.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by SPN on 06.05.2017.
 */
public class Column {
    public String name;
    public Class type;
    //or public String type;
    public ArrayList<Class> elements;
    {
        elements = new ArrayList<>();
    }
}
