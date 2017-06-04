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
    private SQLType type;
    private String className;

    private ArrayList<String> elements;         //ArrayList because our Entity can grow

    {
        elements = new ArrayList<>();
    }

    public Column(ResultSetMetaData resultSetMetaData, int columnNumber) throws SQLException {
        number = columnNumber;
        ResultSetMetaData rsmd = resultSetMetaData;
        name = rsmd.getColumnName(columnNumber);
        className = rsmd.getColumnClassName(columnNumber);
        type = new SQLType(rsmd.getColumnTypeName(columnNumber),
                           rsmd.getPrecision(columnNumber),
                           rsmd.getScale(columnNumber));
//        System.out.println(     //for varchar2 good
//                "Display size " + name + " column = " + resultSetMetaData.getColumnDisplaySize(number));
//        System.out.println(     //the best
//                "Precision " + name + " column = " + resultSetMetaData.getPrecision(number) +
//        ", " + resultSetMetaData.getScale(number));
//        System.out.println(
//                "Catalog name " + number + " column = " + resultSetMetaData.getCatalogName(number));
//        System.out.println(
//                "Column label " + number + " column = " + resultSetMetaData.getColumnLabel(number));
    }

    //package availability modificator
    void add(ResultSet resultSet) throws SQLException {
//        if (type.equals("LONG"))          //TODO: for LONG wrong
//            elements.addTableEditorListener(resultSet.getClob());
//        else
        //LONG doesn't work
            elements.add(resultSet.getString(number));
    }

    void add(String newElement) {
        elements.add(newElement);
    }

    /*----------------Setters---------------------*/

    void setValueAt(int rowIndex, String value) {
        elements.set(rowIndex, value);
    }

    void setName(String newName) { name = newName; }

    void setType(String newTypeName, int newPrecision, int newScale) {
        type.set(newTypeName, newPrecision, newScale);
    }

    void setType(SQLType newType) { type = newType; }

    void remove(int index) { elements.remove(index); }

    /*----------------Getters---------------------*/

    public int size() { return elements.size(); }

    public final String get(int rowIndex) { return elements.get(rowIndex); }

    public final String getName() { return name; }

    public final String getClassName() { return className; }

    public final SQLType getType() { return type; }
}
