package ru.nsu.fit.g14201.lipatkin.model;

import org.apache.log4j.Logger;

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

    public DBManager(SQLCommander sqlCommander) {
        commander = sqlCommander;
        entities = commander.getAllEntities();
        mapEntities = new TreeMap<>();
        for (Entity entity : entities) {
            mapEntities.put(entity.getName(), entity);
        }

        //commander.update(mapEntities.get("DEPT"), 0, "DNAME", "12");
    }

    /*-------------Setters----------------------------*/

    public void setValueAt(Entity entity, int rowIndex, int columnIndex, String value)
            throws UpdateException {
        try {
                //commander doesn't update entity in Client
            commander.update(entity, rowIndex, entity.getColumnName(columnIndex), value);
            entity.setValueAt(rowIndex, columnIndex, value);
        } catch(UpdateException exp) {
            throw exp;
        }
    }

    public void setColumnName(Entity entity, Column column, String newName)
            throws UpdateException {
        try {
            commander.renameColumn(entity, column, newName);
            column.setName(newName);
        } catch(UpdateException exp) {
            throw exp;
        }
    }

    public void setColumnType(Entity entity, Column column, String type)
            throws UpdateException {
        try {
//            commander.renameColumn(entity, column, newName);
//            column.setName(newName);
        } catch(UpdateException exp) {
            throw exp;
        }
    }

    /*-------------Getters----------------------------*/

    public List<Entity> getEntities() { return entities; }

}
