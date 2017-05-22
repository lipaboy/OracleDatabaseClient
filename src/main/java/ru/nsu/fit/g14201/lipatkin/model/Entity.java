package ru.nsu.fit.g14201.lipatkin.model;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by SPN on 06.05.2017.
 */
public class Entity {
    //TODO: may be I need Map<String, Column>

    private Map<String, Column> mapColumn;
    private ArrayList<Column> columns;
    private ArrayList<Column> primaryKeys;     //column index (from 0.to.n-1)
    private String name;

    {
        name = null;
        columns = new ArrayList<>();
        mapColumn = new TreeMap<>();    //TODO: may TreeMap?
        primaryKeys = new ArrayList<>();
    }

    //TODO:(advice) at table columns have numeration from 1.to.n but other objects (like Arrays) have from 0.to.n-1
    //TODO:(advice) you need to use for first "number" name variables for indexing and "index" for second ones

    public Entity(String name1) {
        name = name1;
    }

    public Entity(String name1, ResultSetMetaData tableMetaData, DatabaseMetaData dbMetaData)
            throws SQLException {
        this(name1);
        int size = tableMetaData.getColumnCount();
        for (int i = 0; i < size; i++) {
            Column column = new Column(tableMetaData, i + 1);
            columns.add(column);
            mapColumn.put(column.getName(), column);
        }

        ResultSet primaryKeysSet = dbMetaData.getPrimaryKeys(null, null, name1);
        while (primaryKeysSet.next()) {
            primaryKeys.add(
                mapColumn.get(
                                primaryKeysSet.getString(
                                        "COLUMN_NAME" //label only for primary keys
                                )
                             )
            );
        }
        //for foreign keys use label: PKCOLUMN_NAME
    }

    public void fill(ResultSet resultSet) throws SQLException {
        while(resultSet.next()) {
            for (int i = 0; i < columns.size(); i++) {
                Column column = columns.get(i);
                column.add(resultSet);
            }
        }
    }

    /*----------------Setters---------------------*/

    //package availability modificator
    void setValueAt(int rowIndex, int columnIndex, String value) {
        columns.get(columnIndex).setValueAt(rowIndex, value);
    }

    /*----------------Getters---------------------*/

    public String get(int rowIndex, int columnIndex) { return columns.get(columnIndex).get(rowIndex); }
    public String get(int rowIndex, String columnName) { return mapColumn.get(columnName).get(rowIndex); }

    //I can get Column because it is "defended" on modifications from another package
    public Column getPrimaryKey(int index) {
        return primaryKeys.get(index);
    }
    //But List isn't defended on modifications from another package (modificator public and package)
    List<Column> getPrimaryKeys() {
        return primaryKeys;
    }
    public boolean isPrimaryKey(int indexColumn) { return primaryKeys.contains(columns.get(indexColumn)); }
    public boolean isPrimaryKey(Column column) { return primaryKeys.contains(column); }

    public Column getColumn(int index) { return columns.get(index); }
    public Column getColumn(String columnName) { return mapColumn.get(columnName); }
    List<Column> getColumns() { return columns; }

    //1) Entity may be empty (it is normal)
    //2) All the columns have the same size
    public int getRowCount() { return (columns.size() > 0) ? columns.get(0).size() : 0; }

    public int getColumnCount() { return columns.size(); }

    public String getColumnName(int columnIndex) { return columns.get(columnIndex).getName(); }

    public String getName() { return name; }
}
