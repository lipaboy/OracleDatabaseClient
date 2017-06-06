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
    //private String className;
    private Reference reference;        //foreign key reference to another entity
    private Set<Constraint> constraintSet;

    private ArrayList<String> elements;         //ArrayList because our Entity can grow

    {
        elements = new ArrayList<>();
        constraintSet = new HashSet<>();
        reference = null;
    }

    public Column(String name1, String type1, int columnNumber) {
        number = columnNumber;
        name = name1;
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

    void setName(String newName) { name = newName; }

    void setType(String newTypeName, int newPrecision, int newScale) {
        type.set(newTypeName, newPrecision, newScale);
    }

    void setType(SQLType newType) { type = newType; }

    //void setReference(Reference ref) { reference = ref; }

    boolean addConstraint(Constraint constraint) {
        return constraintSet.add(constraint);
    }

    void addWithConstraintReplacing(Constraint constraint) {
        if (constraintSet.contains(constraint))
            for (Constraint obj : constraintSet)
                if (obj.equals(constraint))
                    obj.copy(constraint);
        else
            addConstraint(constraint);
    }

    /*----------------Getters---------------------*/

    public int size() { return elements.size(); }

    @Nullable
    public final Reference getReference() { return reference; }

    public final String get(int rowIndex) { return elements.get(rowIndex); }

    public final String getName() { return name; }

    public final SQLType getType() { return type; }
}
