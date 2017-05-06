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
    public Entity getAllEntries(@NotNull String tableName) {
        //Map<String, String[]> entries = new HashMap<>();
        Entity entity = new Entity();

        try {
            Statement statement = connection.createStatement();
            //in your table (Oracle XE) can be russian entries
            ResultSet entrySet = statement.executeQuery("SELECT * FROM " + tableName);

            ResultSetMetaData resultSetMetaData = entrySet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

//            resultSetMetaData.get//
            entity.columns = new Column[resultSetMetaData.getColumnCount()];
            for (int i = 0; i < columnCount; i++) {
                entity.columns[i] = new Column();
                Column column = entity.columns[i];
                column.name = resultSetMetaData.getColumnName(i + 1);
                column.type = Class.forName(resultSetMetaData.getColumnClassName(i + 1));
                log.info(resultSetMetaData.getColumnTypeName(i + 1));
                //entries.put(metaData.getColumnName(i), (String[])entrySet.getArray(i).getArray());

            }
//            while(entrySet.next()) {
//                for (int i = 0; i < columnCount; i++) {
//                    Column column = entity.columns[i];
//                    column.elements.add(entrySet.getObject(i + 1, column.type));
//                }
//            }

            statement.close();
        } catch(SQLException exp) {
            log.error(exp.getMessage());
        } catch(ClassNotFoundException exp) {
            log.error(exp.getMessage());
        }

        return entity;
    }

}
