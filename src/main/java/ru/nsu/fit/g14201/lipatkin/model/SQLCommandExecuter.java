package ru.nsu.fit.g14201.lipatkin.model;

import com.sun.istack.internal.NotNull;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by castiel on 02.05.2017.
 */
public class SQLCommandExecuter implements SQLCommander {
    private static final Logger log = Logger.getLogger(SQLCommandExecuter.class);
    private Connection connection;

    public SQLCommandExecuter(@NotNull Connection con) {
        connection = con;
    }

    @Override
    public List<String> getAllEntities() {
        List<String> entities = new ArrayList<String>();
        try {
            Statement statement = connection.createStatement();
            //in your table (Oracle XE) can be russian entries
            ResultSet entrySet = statement.executeQuery("SELECT * FROM user_tables");

            //I think rs - it is entry
            while (entrySet.next()) {
                String tableName = entrySet.getString(1);
                if (!tableName.contains("APEX$"))
                    entities.add(tableName);
            }

            statement.close();
        } catch(SQLException exp) {
            log.error(exp.getMessage());
        }
        return entities;
    }

    @Override
    public Map<String, String[]> getAllEntries(@NotNull String tableName) {
        Map<String, String[]> entries = new HashMap<>();

        try {
            Statement statement = connection.createStatement();
            //in your table (Oracle XE) can be russian entries
            ResultSet entrySet = statement.executeQuery("SELECT * FROM " + tableName);

            ResultSetMetaData metaData = entrySet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                entries.put(metaData.getColumnName(i), (String[])entrySet.getArray(i).getArray());
            }

            statement.close();
        } catch(SQLException exp) {
            log.error(exp.getMessage());
        }

        return entries;
    }

}
