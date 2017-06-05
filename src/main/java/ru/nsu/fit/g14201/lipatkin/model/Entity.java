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

    private ArrayList<EntityListener> listeners;

    {
        name = null;
        columns = new ArrayList<>();
        mapColumn = new TreeMap<>();
        primaryKeys = new ArrayList<>();
        listeners = new ArrayList<>();
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

            /*----------------------Primary keys-----------------------*/

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


    }

    //this method relates to constructor
    void fill(ResultSet resultSet) throws SQLException {
        while(resultSet.next()) {
            for (int i = 0; i < columns.size(); i++) {
                Column column = columns.get(i);
                column.add(resultSet);
            }
        }
    }

    /*---------------Listeners---------------------*/

    public void addEntityListener(EntityListener listener) { listeners.add(listener); }
    public void removeEntityListener(EntityListener listener) { listeners.remove(listener); }
    public void notifyListeners() {
        for (EntityListener listener : listeners)
            listener.dataChanged();
    }

    /*----------------Setters---------------------*/

    //package availability modificator
    void setValueAt(int rowIndex, int columnIndex, String value) {
        columns.get(columnIndex).setValueAt(rowIndex, value);
        notifyListeners();
    }

    void insert(List<String> row) {
        for (int i = 0; i < row.size(); i++) {
            columns.get(i).add(row.get(i));
        }
        notifyListeners();
    }

    void deleteRow(int rowIndex) {
        for (Column column : columns) {
            column.remove(rowIndex);
        }
        notifyListeners();
    }

    /*----------------Getters---------------------*/

    public final String get(int rowIndex, int columnIndex) { return columns.get(columnIndex).get(rowIndex); }
    public String get(int rowIndex, String columnName) { return mapColumn.get(columnName).get(rowIndex); }

    //I can get Column because it is "defended" on modifications from another package
    public final Column getPrimaryKey(int index) {
        return primaryKeys.get(index);
    }
    public int primaryKeysSize() { return primaryKeys.size(); }
    //But List isn't defended on modifications from another package (modificator public and package)
    final List<Column> getPrimaryKeys() { return primaryKeys; }
    public boolean isPrimaryKey(int indexColumn) { return primaryKeys.contains(columns.get(indexColumn)); }
    public boolean isPrimaryKey(Column column) { return primaryKeys.contains(column); }

    public final Column getColumn(int index) { return columns.get(index); }
    public final Column getColumn(String columnName) { return mapColumn.get(columnName); }
    List<Column> getColumns() { return columns; }

    //1) Entity may be empty (it is normal)
    //2) All the columns have the same size
    public int getRowCount() { return (columns.size() > 0) ? columns.get(0).size() : 0; }

    public int getColumnCount() { return columns.size(); }

    public final String getColumnName(int columnIndex) { return columns.get(columnIndex).getName(); }

    public final String getName() { return name; }
}
