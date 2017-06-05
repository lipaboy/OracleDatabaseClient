package ru.nsu.fit.g14201.lipatkin.model;

import com.sun.istack.internal.NotNull;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;
import java.lang.StringBuilder;

/**
 * Created by castiel on 02.05.2017.
 */
public class SQLCommandExecuter implements SQLCommander {
    private static final Logger log = Logger.getLogger(SQLCommandExecuter.class);
    private Connection connection;

    public SQLCommandExecuter(@NotNull Connection con) {
        connection = con;
    }

    String wrapByQuotes(final String value, final String sqlTypeName) {
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

    String getWhereConditionForSelectedRow(final Entity entity, int rowIndex) {
        StringBuilder whereCondition;
        List<Column> primaryKeys = entity.getPrimaryKeys();

        if (primaryKeys.size() <= 0) {
            //throw new UpdateException("Primary keys is empty into " + entity.getName());
            List<Column> columns = entity.getColumns();
            Column column = columns.get(0);
            whereCondition = new StringBuilder(column.getName() + " = "
                    + wrapByQuotes(column.get(rowIndex), column.getType().getTypeName()));

            //TODO: use lambda to exclude repeating code
            for (int i = 1; i < columns.size(); i++) {
                column = columns.get(i);
                whereCondition.append(" AND ").append(column.getName()).append(" = ")
                        .append(wrapByQuotes(column.get(rowIndex), column.getType().getTypeName()));
            }
        }
        else {
            Column primaryKeyColumn = primaryKeys.get(0);
            whereCondition = new StringBuilder(primaryKeyColumn.getName() + " = "
                    + wrapByQuotes(primaryKeyColumn.get(rowIndex),
                    primaryKeyColumn.getType().getTypeName()));
            //TODO: use lambda to exclude repeating code
            for (int i = 1; i < primaryKeys.size(); i++) {
                Column column = primaryKeys.get(i);
                whereCondition.append(" AND ").append(column.getName()).append(" = ")
                        .append(wrapByQuotes(column.get(rowIndex), column.getType().getTypeName()));
            }
        }

        return whereCondition.toString();
    }

    void executeUpdateQuery(String updateQuery) throws UpdateException {
        try {
            PreparedStatement preStatement = connection.prepareStatement(updateQuery);
            log.info("Execs query: " + updateQuery);
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

    /*-----------------Entries edit----------------*/

    @Override
    public void deleteRow(Entity entity, int rowIndex) throws UpdateException {
        String whereCondition = getWhereConditionForSelectedRow(entity, rowIndex);
        String query = "DELETE FROM " + entity.getName()
                + " WHERE " + whereCondition;
        executeUpdateQuery(query);
    }

    @Override
    public void insertRow(Entity entity, List<String> row) throws UpdateException {
        //TODO: may be need to use reqular expressions ("?") to init PreparedStatement before
        //TODO: calling this method (i.e. in constructor)
        StringBuilder query = new StringBuilder(
                "INSERT INTO " + entity.getName()
                        + " VALUES ("
        );

        List<Column> columns = entity.getColumns();
        for (int i = 0; i < row.size(); i++) {
            query.append(wrapByQuotes(row.get(i), columns.get(i).getType().getTypeName()));
            if (i < row.size() - 1)
                query.append(", ");
        }
        query.append(")");
        executeUpdateQuery(query.toString());
    }

    @Override
    public void update(Entity entity, int rowIndex, String columnName, String newValue)
            throws UpdateException {

        String whereCondition = getWhereConditionForSelectedRow(entity, rowIndex);
        String columnTypeName = entity.getColumn(columnName).getType().getTypeName();
        String wrappedValue = wrapByQuotes(newValue, columnTypeName);

        String query =
                "UPDATE " + entity.getName()
                        + " SET " + columnName + " = " + wrappedValue
                        + " WHERE " + whereCondition;
        //!!!! NO ';' NO ';' NO ';' in query string
        executeUpdateQuery(query);
    }


    /*-----------------Entity construct (destruct)----------------*/

    @Override
    public void createEntity(Entity entity) throws UpdateException {
        StringBuilder query = new StringBuilder("CREATE TABLE ")
                .append(entity.getName()).append("(");
        List<Column> columns = entity.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            //name, type
            query.append(column.getName())
                    .append(" ")
                    .append(column.getType().getSQLFormat());
            //TODO: add constraints
            if (i < columns.size() - 1)
                query.append(", ");
        }
        query.append(")");
        executeUpdateQuery(query.toString());
    }

    @Override
    public void removeEntity(Entity entity) throws UpdateException {
        String query = "DROP TABLE " + entity.getName();
        executeUpdateQuery(query);
    }

    @Override
    public void renameColumn(Entity entity, Column column, String newName) throws UpdateException {
        //"alter table sales rename column order_date to date_of_order"
        //TODO: may be need to use reqular expressions ("?") to init PreparedStatement before
        //TODO: calling this method (i.e. in constructor)
        String query =
                "ALTER TABLE " + entity.getName()
                    + " RENAME COLUMN " + column.getName() +        //unnecessary to wrap by quotes
                        " TO " + newName;
        executeUpdateQuery(query);
    }

    @Override
    public void setColumnType(Entity entity, Column column, SQLType newType) throws UpdateException {
        //TODO: may be need to use reqular expressions ("?") to init PreparedStatement before
        //TODO: calling this method (i.e. in constructor)
        String query =
                "ALTER TABLE " + entity.getName()
                        + " MODIFY " + column.getName()        //unnecessary to wrap by quotes
                          + " " + newType.getSQLFormat();

        executeUpdateQuery(query);
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
        List<Entity> entities = new ArrayList<>(); //I think that "find" operations will be more than "addTableEditorListener" ones.
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
    public DatabaseMetaData getMetaData() throws SQLException {
        return connection.getMetaData();
    }

}
