package ru.nsu.fit.g14201.lipatkin.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created by SPN on 06.05.2017.
 */
public class Entity {
    //public String[] fields;
    //public String[][] entries;
    private Column[] columns;

    public Entity(ResultSetMetaData resultSetMetaData) throws SQLException {
        int size = resultSetMetaData.getColumnCount();
        columns = new Column[size];
        for (int i = 0; i < size; i++) {
            columns[i] = new Column(resultSetMetaData, i + 1);
        }
    }

    public void setEntity(ResultSet resultSet) throws SQLException {
        while(resultSet.next()) {
            for (int i = 0; i < columns.length; i++) {
                Column column = columns[i];
                column.add(resultSet);
            }
        }
    }

    /*----------------Selectors---------------------*/

    public String get(int rowIndex, int columnIndex) { return columns[columnIndex].get(rowIndex); }

    //Entity may be empty (it is normal)
    public int getRowCount() { return (columns.length > 0) ? columns[0].size() : 0; }

    public int getColumnCount() { return columns.length; }

    public String getColumnName(int columnIndex) { return columns[columnIndex].getName(); }
}
