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
    public void update(Entity entity, int rowIndex, String columnName, String newValue)
            throws UpdateException {
        try {
            int primaryKeyIndex = entity.getPrimaryKeyColumnIndex(0);
            String primaryKeyColumn = entity.getColumnName(primaryKeyIndex);
            String columnTypeName = entity.getColumn(columnName).getType();
            String newValueWrapper;
            switch (columnTypeName.toUpperCase()) {
                case "NUMBER":
                case "INTEGER":
                    newValueWrapper = newValue;
                    break;
                case "VARCHAR":
                case "VARCHAR2":
                case "LONG":
                case "DATE":
                default:
                    newValueWrapper = "'" + newValue + "'";
            }

            String query =
                "UPDATE " + entity.getName()
                    + " SET " + columnName + " = " + newValueWrapper
                    + " WHERE " + primaryKeyColumn + " = "
                        + entity.get(rowIndex, primaryKeyIndex);//!!!! NO ';' NO ';' NO ';'

            //System.out.println(query);
            //connection.setAutoCommit(false);
            PreparedStatement preStatement = connection.prepareStatement(query);
            preStatement.executeUpdate();
            log.info("execute Update successful");
            preStatement.close();
        } catch(SQLException exp) {
            log.error(exp.getMessage());
            throw new UpdateException(exp.getMessage());
            //TODO: useful information for exception
//            JDBCTutorialUtilities.printSQLException(e);
//            if (con != null) {
//                try {
//                    System.err.print("Transaction is being rolled back");
//                    con.rollback();
//                } catch(SQLException excep) {
//                    JDBCTutorialUtilities.printSQLException(excep);
//                }
//            }
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
                //if (tableName.toUpperCase() == "SALESORDERS")
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

            //TODO: difficult constructor (make easy)
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

//    @Override
//    public void close() {
//        try {
//            connection.close();
//        } catch(SQLException exp) {
//            log.error(exp.getMessage());
//        }
//    }

}
