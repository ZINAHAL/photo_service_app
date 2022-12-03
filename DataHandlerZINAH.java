package com.photoserviceproject;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

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

//    public static Vector<String> getTables() {
//        Vector<String> l = new Vector<>();
//        /*l.add("Employee");
//        l.add("Dependant");
//        l.add("Department");
//        l.add("Project");
//        l.add("Workson");*/
//        String sqlQuery = "SELECT Name FROM sys.MSysObjects WHERE Type=1 AND Flags=0";
//        try {
//            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
//            con = DriverManager.getConnection(dbURL, "", "");
//            stm = con.createStatement();
//            rs = stm.executeQuery(sqlQuery);
//            while (rs.next()) {
//            // each row is an array of objects
//                    l.add((String) rs.getObject(1));
//            }
//        } catch (ClassNotFoundException cnfex) {
//            System.err.println("Issue with the JDBC driver.");
//            System.exit(1); // terminate program - cannot recover
//        } catch (java.sql.SQLException sqlex) {
//            System.err.println(sqlex);
//        } catch (Exception ex) {
//            System.err.println(ex);
//            //ex.printStackTrace();
//        } finally {
//            try {
//                if (null != con) {
//                    // cleanup resources, once after processing
//                    rs.close();
//                    stm.close();
//                    // and then finally close connection
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return l;
//    }

//    public static void searchRecords(String table) {
//        String sqlQuery = "SELECT * FROM " + table;
//        try {
//            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
//            con = DriverManager.getConnection(dbURL, "", "");
//            stm = con.createStatement(
//                    java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,
//                    java.sql.ResultSet.CONCUR_READ_ONLY);
//            rs = stm.executeQuery(sqlQuery);
//            rsMeta = rs.getMetaData();
//            columnCount = rsMeta.getColumnCount();
//        } catch (ClassNotFoundException cnfex) {
//            System.err.println("Issue with the JDBC driver.");
//            System.exit(1); // terminate program - cannot recover
//        } catch (java.sql.SQLException sqlex) {
//            System.err.println(sqlex);
//        } catch (Exception ex) {
//            System.err.println(ex);
//            //ex.printStackTrace();
//        }
//    }

//    public static Object[] getTitles(String table) {
//        Object[] columnNames = new Object[columnCount];
//        try {
//            for (int col = columnCount; col > 0; col--) {
//                columnNames[col - 1]
//                        = rsMeta.getColumnName(col);
//            }
//        } catch (java.sql.SQLException sqlex) {
//            System.err.println(sqlex);
//        } finally {
//            try {
//                if (null != con) {
//                    // cleanup resources, once after processing
//                    rs.close();
//                    stm.close();
//                    // and then finally close connection
//                    con.close();
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return columnNames;
//    }
    //****************************************************************************************************************
//    public static Vector<String> getColumnValues(String table, String columnName, String condition) {
//        String sqlQuery = "SELECT " + columnName + " FROM " + table;
//        Vector<String> returnedResult = new Vector<>();
//        if(condition != null){
//            sqlQuery += " " + condition;
//        }
//        try {
//            connection = DriverManager.getConnection(dbURL);
//            statement = connection.createStatement();
//            resultSet = statement.executeQuery(sqlQuery);
//            while (resultSet.next()) {
//                returnedResult.add(resultSet.getString(1));
////                System.out.println(resultSet.getString(1) + "\t");
////                System.out.println("------------------------------");
////                        + resultSet.getString(2) + "\t\t"
////                        + resultSet.getString(3) + "\t"
////                        + resultSet.getString(4));
//            }
//        } catch (SQLException sqlex) {
//            System.err.println("SQL statement issue " + sqlex.getMessage());
//        } finally {
//
//            // Step 3: Closing database connection
//            try {
//                if (null != resultSet) {
//                    // cleanup resources, once after processing
//                    resultSet.close();
//                }
//                if (null != statement) {
//                    // cleanup resources, once after processing
//                    statement.close();
//                }
//                if (null != connection) {
//                    // and then finally close connection
//                    connection.close();
//                }
//            } catch (SQLException sqlex) {
//                System.err.println(sqlex.getMessage());
//            }
//        }
////        String sqlQuery = "SELECT ";
//        return returnedResult;
//    }
    
    
//    public static Object[][] getRowTTs(String table) {
//        searchRecords(table);
//        Object[][] content;
//        try {
//// determine the number of rows
//            rs.last();
//            int number = rs.getRow();
//            content = new Object[number][columnCount];
//            rs.beforeFirst();
//            int i = 0;
//            while (rs.next()) {
//// each row is an array of objects
//                for (int col = 1; col <= columnCount; col++) {
//                    content[i][col - 1] = rs.getObject(col);
//                }
//                i++;
//            }
//            return content;
//        } catch (java.sql.SQLException sqlex) {
//            System.err.println(sqlex);
//        }
//        return null;
//    }
    
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
