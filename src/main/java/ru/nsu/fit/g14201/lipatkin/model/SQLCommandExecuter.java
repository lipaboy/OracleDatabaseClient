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

    String wrapByQuotes(String value, String sqlTypeName) {
        String wrappedValue;
        //TODO: may be need check on NULL, DOUBLE, TIMESTAMP
        switch (sqlTypeName.toUpperCase()) {
            case "NUMBER":
            case "INTEGER":
                wrappedValue = value;
                break;
            case "VARCHAR":
            case "VARCHAR2":
            case "LONG":
            case "DATE":
            default:
                wrappedValue = "'" + value + "'";
        }
        return wrappedValue;
    }

        /*-----------------Setters----------------*/

    @Override
    public void update(Entity entity, int rowIndex, String columnName, String newValue)
            throws UpdateException {
        try {
            String whereCondition;
            List<Column> primaryKeys = entity.getPrimaryKeys();

            if (primaryKeys.size() <= 0) {
                //throw new UpdateException("Primary keys is empty into " + entity.getName());
                List<Column> columns = entity.getColumns();
                Column column = columns.get(0);
                whereCondition = column.getName() + " = "
                        + wrapByQuotes(column.get(rowIndex), column.getType());

                for (int i = 1; i < columns.size(); i++) {
                    column = columns.get(i);
                    whereCondition = whereCondition + " AND "
                            + column.getName() + " = "
                            + wrapByQuotes(column.get(rowIndex), column.getType());
                }
            }
            else {
                Column primaryKeyColumn = primaryKeys.get(0);
                whereCondition = primaryKeyColumn.getName() + " = "
                        + wrapByQuotes(primaryKeyColumn.get(rowIndex), primaryKeyColumn.getType());

                for (int i = 1; i < primaryKeys.size(); i++) {
                    Column column = primaryKeys.get(i);
                    whereCondition = whereCondition + " AND "
                            + column.getName() + " = "
                            + wrapByQuotes(column.get(rowIndex), column.getType());
                }
            }

            String columnTypeName = entity.getColumn(columnName).getType();
            String wrappedValue = wrapByQuotes(newValue, columnTypeName);

            String query =
                    "UPDATE " + entity.getName()
                            + " SET " + columnName + " = " + wrappedValue
                            + " WHERE " + whereCondition;
            //!!!! NO ';' NO ';' NO ';' in query string

            PreparedStatement preStatement = connection.prepareStatement(query);
            preStatement.executeUpdate();
            //log.info("execute Update successful");
            preStatement.close();
        } catch(UpdateException exp) {
            log.error(exp.getMessage());
            throw exp;
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

    @Override
    public void renameColumn(Entity entity, Column column, String newName) throws UpdateException {
        //"alter table sales rename column order_date to date_of_order"

        try {
            String type = column.getType();
            //TODO: may be need to use reqular expressions ("?") to init PreparedStatement before
            //TODO: calling this method (i.e. in constructor)
            String query =
                    "ALTER TABLE " + entity.getName()
                        + " RENAME COLUMN " + column.getName() +        //unnecessary to wrap by quotes
                            " TO " + newName;

            PreparedStatement preStatement = connection.prepareStatement(query);
            //log.info("Rename column: " + query);
            preStatement.executeUpdate();
            preStatement.close();
        } catch(UpdateException exp) {
            log.error(exp.getMessage());
            throw exp;
        } catch(SQLException exp) {
            log.error(exp.getMessage());
            throw new UpdateException(exp.getMessage());
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

            //if (!statement.isClosed())
                statement.close();      //why it is already closed
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
