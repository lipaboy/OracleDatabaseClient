package ru.nsu.fit.g14201.lipatkin.model;

import com.sun.istack.internal.NotNull;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;

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
    public List<String> getAllEntityNames() {
        List<String> entities = new ArrayList<>();
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
    public Entity getEntity(@NotNull String tableName) {
        //Map<String, String[]> entries = new HashMap<>();
        Entity entity = null;

        try {
            Statement statement = connection.createStatement();
            //in your table (Oracle XE) can be russian entries
            ResultSet entrySet = statement.executeQuery("SELECT * FROM " + tableName);

            //what about Foreign Key
            //connection.getMetaData().getExportedKeys()
            //connection.getMetaData().getPrimaryKeys()
            entity = new Entity(tableName, entrySet.getMetaData());
            entity.fill(entrySet);


            statement.close();
            //TODO: I think that we need to throw exception up the method
        } catch(SQLException exp) {
            log.error(exp.getMessage());
        }

        return entity;
    }

    public List<Entity> getAllEntities() {
        List<Entity> entities = new ArrayList<>(); //I think that "find" operations will be more than "add" ones.
        try {

            List<String> tableNames = getAllEntityNames();

            //TODO: rewrite code to
            //DatabaseMetaData meta = connection.getMetaData();
            //ResultSet tablesRs = meta.getTables(null, null, "table_name", new String[]{"TABLE"});
            for (String name : tableNames) {
                entities.add(getEntity(name));
            }

                //TODO: you need to paste login variable
            ResultSet primaryKeys = connection.getMetaData().getPrimaryKeys(null, null,
                    tableNames.get(tableNames.size() - 1));
//            ResultSet primaryKeys = connection.getMetaData().getIndexInfo(null, null,
//                    tableNames.get(0), true, true);
            for (int i = 1; i <= primaryKeys.getMetaData().getColumnCount(); i++)
                System.out.println(primaryKeys.getMetaData().getColumnName(i));
            System.out.println("");
            while (primaryKeys.next()) {
                System.out.println("Primary key: " + primaryKeys.getString("COLUMN_NAME"));
            }
        } catch(SQLException exp) {
            log.error(exp.getMessage());
            //TODO: throw another exception (business)
        }
        return entities;
    }

    public void close() {
        try {
            connection.close();
        } catch(SQLException exp) {
            log.error(exp.getMessage());
        }
    }

}
