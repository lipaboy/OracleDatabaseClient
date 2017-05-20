package ru.nsu.fit.g14201.lipatkin.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by SPN on 06.05.2017.
 */
public class Column {
    private int number;     //from 1 to ... (number of column)
    private String name;
    private String type;
    private String className;
//    public class Constraints {
//        public boolean isPrimary = false;
//        public boolean isForeign = false;
//        public Entity tableRef = null;
//        public Column columnRef = null;
//    };
//    private Constraints constraint;


    //or public String className;
    private ArrayList<String> elements;         //ArrayList because our Entity can grow

    {
        elements = new ArrayList<>();
    }

    public Column(ResultSetMetaData resultSetMetaData, int columnNumber) throws SQLException {
        number = columnNumber;
        name = resultSetMetaData.getColumnName(columnNumber);
        className = resultSetMetaData.getColumnClassName(columnNumber);
        type = resultSetMetaData.getColumnTypeName(columnNumber);
    }

    //package availability modificator
    void add(ResultSet resultSet) throws SQLException {
//        if (type.equals("LONG"))
//            elements.add(resultSet.getClob());
//        else
        //LONG doesn't work
            elements.add(resultSet.getString(number));
        //resultSet.get
    }

    void setValueAt(int rowIndex, String value) {
        elements.set(rowIndex, value);
    }

    //void setIsPrimaryKey(boolean value) { constraint.isPrimary = value; }

    public int size() { return elements.size(); }

    public String get(int rowIndex) { return elements.get(rowIndex); }

    public String getName() { return name; }

    public String getClassName() { return className; }

    public String getType() { return type; }
}
