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

        /*-----------------Setters----------------*/

    @Override
    public void update(Entity entity, int rowIndex, String columnName, String newValue) {
        try {
            //Statement statement = connection.createStatement();
            //in your table (Oracle XE) can be russian entries
            //ResultSet entrySet = statement.executeQuery(
            int primaryKeyNumber = entity.getPrimaryKeyColumnNumber(0);
            String primaryKeyColumn = entity.getColumnName(primaryKeyNumber - 1);   //offset
            String str =
                "UPDATE " + entity.getName()
                    + " SET " + columnName + " = " + newValue
                    + " WHERE " + primaryKeyColumn + " = "
                        + entity.get(rowIndex, primaryKeyNumber - 1);
//                + ";");

            PreparedStatement preStatement = connection.prepareStatement(
                    "UPDATE ? SET ? = ? WHERE ? = ?;"
            );

            preStatement.setString(1, entity.getName());
            preStatement.setString(2, columnName);
            preStatement.setString(3, newValue);
            preStatement.setString(4, primaryKeyColumn);
            preStatement.setString(5, entity.get(rowIndex, primaryKeyNumber - 1));

            System.out.println(str);

            preStatement.executeUpdate();
            log.info("execute Update");
            preStatement.close();
        } catch(SQLException exp) {
            log.error(exp.getMessage());
        }
    }

        /*-----------------Getters----------------*/

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

            entity = new Entity(tableName, entrySet.getMetaData(), connection.getMetaData());
            entity.fill(entrySet);

            statement.close();
            //TODO: I think that we need to throw exception up the method
        } catch(SQLException exp) {
            log.error(exp.getMessage());     //TODO: throw another exception (business)
        }

        return entity;
    }

    @Override
    public List<Entity> getAllEntities() {
        List<Entity> entities = new ArrayList<>(); //I think that "find" operations will be more than "add" ones.
        List<String> tableNames = getAllEntityNames();

        //TODO: rewrite code to
        //DatabaseMetaData meta = connection.getMetaData();
        //ResultSet tablesRs = meta.getTables(null, null, "table_name", new String[]{"TABLE"});
        for (String name : tableNames) {
            entities.add(getEntity(name));
        }

        return entities;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch(SQLException exp) {
            log.error(exp.getMessage());
        }
    }

}
