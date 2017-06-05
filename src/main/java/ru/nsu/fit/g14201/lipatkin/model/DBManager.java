package ru.nsu.fit.g14201.lipatkin.model;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by SPN on 08.05.2017.
 */
public class DBManager {
    private static final Logger log = Logger.getLogger(SQLCommandExecuter.class);
    private SQLCommander commander;
    private List<Entity> entities;      //TODO: may be I need Map<String, Entity>
    private Map<String, Entity> mapEntities;

    public DBManager(Connection connection) {
        try {
            commander = new SQLCommandExecuter(connection);
            entities = commander.getAllEntities();
            mapEntities = new TreeMap<>();
            for (Entity entity : entities) {
                mapEntities.put(entity.getName(), entity);
            }

        /*----------------------Foreign keys (references)-----------------------*/

            for (Entity entity : entities) {
                ResultSet foreignKeySet = commander.getMetaData().getImportedKeys(null, null,
                        entity.getName());
                while (foreignKeySet.next()) {
                    final Entity entityForeign = mapEntities.get(
                            foreignKeySet.getString("FKTABLE_NAME")
                    );
                    final Column columnForeign = entityForeign.getColumn(
                            foreignKeySet.getString("FKCOLUMN_NAME")
                    );
                    final Entity entityPrimary = mapEntities.get(
                            foreignKeySet.getString("PKTABLE_NAME")
                    );
                    final Column columnPrimary = entityPrimary.getColumn(
                            foreignKeySet.getString("PKCOLUMN_NAME")
                    );
                    columnForeign.setReference(new Reference(entityPrimary, columnPrimary));
                }
            }
        } catch (SQLException exp) {
            log.error(exp.getMessage());
        }
        //for foreign keys use label:
        // getImportedKeys
        //PKTABLE_NAME PKCOLUMN_NAME <- to
        //FKTABLE_NAME FKCOLUMN_NAME <- FROM
    }

    /*-----------------Entries edit----------------*/

    public void setValueAt(Entity entity, int rowIndex, int columnIndex, String value)
            throws UserWrongActionException {
        try {
                //commander doesn't update entity in Client
            commander.update(entity, rowIndex, entity.getColumnName(columnIndex), value);
            entity.setValueAt(rowIndex, columnIndex, value);
        } catch(UpdateException exp) {
            throw new UserWrongActionException("Wrong setting value in field");
        }
    }

    public void insert(Entity entity, List<String> row)
            throws UserWrongActionException {
        try {
            commander.insertRow(entity, row);
            entity.insert(row);
        } catch(UpdateException exp) {
            throw new UserWrongActionException("Wrong insertion row");
        }
    }

    public void removeRow(Entity entity, int rowIndex)
            throws UserWrongActionException {
        try {
            commander.deleteRow(entity, rowIndex);
            entity.deleteRow(rowIndex);
        } catch(UpdateException exp) {
            throw new UserWrongActionException("Wrong removing row");
        }
    }

    /*-----------------Entity construct (destruct)----------------*/

    public void setColumnName(Entity entity, Column column, String newName)
            throws UserWrongActionException {
        try {
            commander.renameColumn(entity, column, newName);
            column.setName(newName);
        } catch(UpdateException exp) {
            throw new UserWrongActionException("Wrong setting column name");
        }
    }

    public void setColumnType(Entity entity, Column column, String typeFormat)
            throws UserWrongActionException {
        try {
            SQLType type = new SQLType(typeFormat);     //if wrong format then need exception
            commander.setColumnType(entity, column, type);
            column.setType(type);
        } catch(UpdateException exp) {
            throw new UserWrongActionException("Wrong setting column type");
        }
    }



    /*-------------Getters----------------------------*/

    public List<Entity> getEntities() { return entities; }

}
