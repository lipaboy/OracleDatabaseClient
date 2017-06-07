package ru.nsu.fit.g14201.lipatkin.model;

import com.sun.istack.internal.Nullable;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by SPN on 06.05.2017.
 */
public class Column {
    private static final Logger log = Logger.getLogger(Column.class);

    private int number;     //from 1 to ... (number of column)
    private String name;
    private SQLType type;
    private Set<Constraint> constraintSet;

    private ArrayList<String> elements;         //ArrayList because our Entity can grow

    {
        elements = new ArrayList<>();
        constraintSet = new HashSet<>();
    }

    public Column(String name1, String type1, int columnNumber) {
        number = columnNumber;
        name = name1.toUpperCase();
        type = new SQLType(type1);
    }

    public Column(ResultSetMetaData rsmd, int columnNumber) throws SQLException {
        number = columnNumber;
        name = rsmd.getColumnName(columnNumber);
        type = new SQLType(rsmd.getColumnTypeName(columnNumber),
                           rsmd.getPrecision(columnNumber),
                           rsmd.getScale(columnNumber));
    }

    //package availability modificator
    void add(ResultSet resultSet) throws SQLException {

        if (type.typeName.equals("LONG")) {         //TODO: for LONG wrong
            //byte[] bufLong = resultSet.getBytes(number);
            //elements.add(new String(bufLong));
            //resultSet.get
            //log.info(bufLong);
            elements.add("");
            //may be getBinaryStream
        }
//        else
        //LONG doesn't work
            elements.add(resultSet.getString(number));
    }

    void add(String newElement) {
        elements.add(newElement);
    }

    /*----------------Element Setters---------------------*/

    void setValueAt(int rowIndex, String value) {
        elements.set(rowIndex, value);
    }

    void remove(int index) { elements.remove(index); }

    /*----------------Column Setters---------------------*/

    void setName(String newName) { name = newName.toUpperCase(); }

    void setType(String newTypeName, int newPrecision, int newScale) {
        type.set(newTypeName, newPrecision, newScale);
    }

    void setType(SQLType newType) { type = newType; }

    boolean addConstraint(Constraint constraint) {
        return constraintSet.add(constraint);
    }

    boolean removeConstraint(Constraint constraint) {
        for (Constraint obj : constraintSet)
            if (obj.getType() == constraint.getType())
                return constraintSet.remove(obj);
        return false;
    }

    public boolean containsConstraint(Constraint constraint) {
        return constraintSet.contains(constraint);
    }

    @Nullable
    public final Constraint getConstraint(Constraint.Type type) {
        for (Constraint obj : constraintSet)
            if (obj.getType() == type)
                return obj;
        return null;
    }

    /*----------------Getters---------------------*/

    public int size() { return elements.size(); }

    public final String get(int rowIndex) { return elements.get(rowIndex); }

    public final String getName() { return name; }

    public final SQLType getType() { return type; }
}
