package com.photoserviceproject;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataHandler {
// DB details

    //private static final String dbURL = "jdbc:ucanaccess://guiDB1.accdb;sysSchema=true";
    private static String msAccDB = "photo_serviceDB.accdb"; // path to the DB file
    private static String dbURL = "jdbc:ucanaccess://" + msAccDB;
    private static java.sql.Connection connection;
    private static java.sql.Statement statement;
    private static java.sql.PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private static java.sql.ResultSetMetaData rsMeta;
    private static int columnCount;
    
    public static Object[][] getRows(String table, String columnNames, String condition) {
        String sqlQuery = "SELECT " + columnNames + " FROM " + table;
        if(condition != null){
            sqlQuery += " " + condition;
        }
        Object[][] content = null;
        try {
            connection = DriverManager.getConnection(dbURL);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(sqlQuery);
            resultSet.last();
            int columnCount = resultSet.getMetaData().getColumnCount();
            content = new Object[resultSet.getRow()][columnCount];
            resultSet.beforeFirst();
            int r = 0;
            while (resultSet.next()) {
                for (int col = 1; col <= columnCount; col++) {
                    content[r][col - 1] = resultSet.getObject(col);
//                    System.out.println((String)content[r][col - 1]);
                }
                r++;
            }
        } catch (SQLException sqlex) {
            System.err.println("getRows()::SQL statement issue " + sqlex.getMessage());
        } finally {

            // Step 3: Closing database connection
            try {
                if (null != resultSet) {
                    // cleanup resources, once after processing
                    resultSet.close();
                }
                if (null != statement) {
                    // cleanup resources, once after processing
                    statement.close();
                }
                if (null != connection) {
                    // and then finally close connection
                    connection.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
//        String sqlQuery = "SELECT ";
        return content;
    }
    
    //This is useful when extracting a single coloumn from the database as the method getRows() returns an array of arrays that have a single item each
    public static Object[] convertDoubleArrayToSingle(Object[][] doubleArray) {
        Object[] singleArray = new Object[doubleArray.length];
        for(int r = 0; r < doubleArray.length; r++){
            singleArray[r] = doubleArray[r][0];
        }
        return singleArray;
    }
    
    public static int insertData(String table, String columnNames, String values) {
        String sqlQuery = "INSERT INTO " + table + " (" + columnNames + ") VALUES (" + values + ")";
//        String query = "INSERT INTO " + table + "(username, password) VALUES(?, ?)";
        System.out.println(sqlQuery);
        int successfulInsert = -1;
        try {
            connection = DriverManager.getConnection(dbURL);
            statement = connection.createStatement();
            successfulInsert = statement.executeUpdate(sqlQuery);
        } catch (SQLException sqlex) {
            System.err.println("insertData()::SQL statement issue " + sqlex.getMessage());
        } finally {

            // Step 3: Closing database connection
            try {
                if (null != resultSet) {
                    // cleanup resources, once after processing
                    resultSet.close();
                }
                if (null != statement) {
                    // cleanup resources, once after processing
                    statement.close();
                }
                if (null != connection) {
                    // and then finally close connection
                    connection.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
        return successfulInsert;
    }
    //*********************************************************************************************************************

    
}
