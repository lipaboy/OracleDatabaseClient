package ru.nsu.fit.g14201.lipatkin.model;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by castiel on 02.05.2017.
 */
public interface SQLCommander {

    public Entity executeQuery(String query) throws UpdateException;

    /*-----------------Entries edit----------------*/

    public void deleteRow(Entity entity, int rowIndex) throws UpdateException;

    public void insertRow(Entity entity, List<String> row) throws UpdateException;

    public void update(Entity entity, int rowIndex, String columnName, String newValue) throws UpdateException;

    /*-----------------Entity construct (destruct)----------------*/

    public void createEntity(Entity entity) throws UpdateException;

    public void removeEntity(Entity entity) throws UpdateException;

        /*-----------------Column modifications----------------*/

    public void addColumn(Entity entity, Column column) throws UpdateException;

    public void dropColumn(Entity entity, Column column) throws UpdateException;

    public void addConstraint(Entity entity, Column column, Constraint constraint) throws UpdateException;

    public void dropConstraint(Entity entity, Column column, Constraint constraint)
            throws UpdateException;

    public void renameColumn(Entity entity, Column column, String newName) throws UpdateException;

    public void setColumnType(Entity entity, Column column, SQLType newType) throws UpdateException;

    /*-----------------Getters----------------*/

    public List<String> getAllEntityNames();

    public Entity getEntity(String tableName);

    public List<Entity> getAllEntities();

    public DatabaseMetaData getMetaData() throws SQLException;

//    public void close();
}
