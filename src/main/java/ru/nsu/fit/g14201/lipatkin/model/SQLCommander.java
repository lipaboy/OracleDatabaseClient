package ru.nsu.fit.g14201.lipatkin.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by castiel on 02.05.2017.
 */
public interface SQLCommander {
    public List<String> getAllEntityNames();

    public
        Entity
            getEntity(String tableName);

    public List<Entity> getAllEntities();
}
