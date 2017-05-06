package ru.nsu.fit.g14201.lipatkin.model;

import java.util.List;
import java.util.Map;

/**
 * Created by castiel on 02.05.2017.
 */
public interface SQLCommander {
    public List<String> getAllEntities();
    //Map<Column, valuse>
    public
        Entity
            getAllEntries(String tableName);
}
