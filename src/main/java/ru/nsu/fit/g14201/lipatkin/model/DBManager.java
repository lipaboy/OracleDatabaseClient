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
//                System.out.println(entity.getName());
//                for (int i = 1; i <= foreignKeySet.getMetaData().getColumnCount(); i++)
//                    System.out.print(foreignKeySet.getMetaData().getColumnName(i) + " ");
//                System.out.println("");
//                PKTABLE_CAT PKTABLE_SCHEM PKTABLE_NAME PKCOLUMN_NAME FKTABLE_CAT
//                FKTABLE_SCHEM FKTABLE_NAME FKCOLUMN_NAME KEY_SEQ UPDATE_RULE DELETE_RULE
//                FK_NAME PK_NAME DEFERRABILITY
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
                    final String constraintName = foreignKeySet.getString("FK_NAME");
                    //columnForeign.setReference(new Reference(entityPrimary, columnPrimary));
                    final Constraint newConstraint = new Constraint(Constraint.Type.FOREIGN_KEY,
                            constraintName);
                    newConstraint.setReference(new Reference(entityPrimary, columnPrimary));
                    columnForeign.addConstraint(newConstraint);
//                    for (int i = 1; i <= foreignKeySet.getMetaData().getColumnCount(); i++)
//                        System.out.print(foreignKeySet.getString(i) + " ");
//                    System.out.println("");
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

    public final Entity createEntity(String entityName, String columnName, String typeName)
            throws UserWrongActionException {
        try {
            final Column column = new Column(columnName, typeName, 1);
            final Entity entity = new Entity(entityName, column);

            commander.createEntity(entity);
            add(entity);

            return entity;
        } catch (UpdateException exp) {
            throw new UserWrongActionException("Cannot create entity");
        }
    }

    public void removeEntity(String entityName) {
        try {
            final Entity entity = mapEntities.get(entityName);
            commander.removeEntity(entity);
            remove(entity);
        } catch (UpdateException exp) {
            throw new UserWrongActionException("Cannot remove entity");
        }
    }

    /*-----------------Columns edit----------------*/

    public final Column addColumn(Entity entity, String columnName, SQLType type) {
        try {
            Column newColumn = new Column(columnName, type, 1);
            commander.addColumn(entity, newColumn);
            return entity.add(columnName, type);
        } catch(UpdateException exp) {
            throw new UserWrongActionException("Cannot add column");
        }
    }

    public void removeColumn(Entity entity, Column column) {
        try {
            commander.dropColumn(entity, column);
            entity.remove(column);
        } catch(UpdateException exp) {
            throw new UserWrongActionException("Cannot remove column");
        }
    }

    public void addConstraint(Entity entity, Column column, Constraint constraint)
            throws UserWrongActionException {
        try {
            constraint.name = entity.getName() + "_" + column.getName() + "_"
                    + constraint.getTypeName().replace(' ', '_');

            Constraint oldCon = column.getConstraint(constraint.getType());
            if (oldCon != null) {
                commander.dropConstraint(entity, column, oldCon);
                entity.removeConstraint(column, constraint);
            }
            commander.addConstraint(entity, column, constraint);
            entity.addConstraint(column, constraint);
        } catch(UpdateException exp) {
            throw new UserWrongActionException("Cannot add constraint");
        }
    }

    public void removeConstraint(Entity entity, Column column, Constraint constraint)
            throws UserWrongActionException {
        try {
            commander.dropConstraint(entity, column, constraint);
            entity.removeConstraint(column, constraint);
        } catch(UpdateException exp) {
            throw new UserWrongActionException("Cannot remove constraint");
        }
    }

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


    /*-------------Setters----------------------------*/

    private void add(Entity entity) {
        entities.add(entity);
        mapEntities.put(entity.getName(), entity);
    }

    private void remove(Entity entity) {
        entities.remove(entity);
        mapEntities.remove(entity.getName());
    }

    /*-------------Getters----------------------------*/

    public final Entity getEntity(String entityName) { return mapEntities.get(entityName.toUpperCase()); }

    public List<Entity> getEntities() { return entities; }

}
