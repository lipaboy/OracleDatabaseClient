package ru.nsu.fit.g14201.lipatkin.model;

import java.util.List;

/**
 * Created by SPN on 08.05.2017.
 */
public class DBManager {
    private SQLCommander commander;
    private List<Entity> entities;

    public DBManager(SQLCommander sqlCommander) {
        commander = sqlCommander;
        entities = commander.getAllEntities();
    }

    public List<Entity> getEntities() { return entities; }

    //here remove and add entity
}
