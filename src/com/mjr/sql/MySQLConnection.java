package com.mjr.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnection {

    private static Connection connection;
    public static boolean connected = false;

    public static void initConnection(String ipAddress, int port, String databaseName, String user, String password)
	    throws SQLException, ClassNotFoundException {
	try {
	    System.out.println("MySQl connection initializing!");
	    Class.forName("com.mysql.cj.jdbc.Driver");
	    connection = DriverManager.getConnection("jdbc:mysql://" + ipAddress + ":" + port + "/" + databaseName + "?serverTimezone=GMT",
		    user, password);
	    connected = true;
	    System.out.println("MySQl connection has been initialized!");
	} catch (ClassNotFoundException | SQLException e) {
	    if (e.getMessage().contains("Unknown database")) {
		try {
		    System.out.println("MySQL New Database detected!");
		    System.out.println("MySQL Settings up Database!");
		    Class.forName("com.mysql.cj.jdbc.Driver");
		    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql?serverTimezone=GMT", user, password);
		    connected = true;
		    SQLUtilities.createDatabaseStructure();
		    connected = false;
		} catch (SQLException ex) {
		    System.out.println("MySQL Connection Failed! Error: " + ex.getMessage());
		}
	    } else
		System.out.println("MySQL Connection Failed! Error: " + e.getMessage());
	}
    }

    public static Connection getConnection() {
	return connection;
    }

    public static ResultSet executeQuery(String statement) {
	Statement myStmt;
	try {
	    myStmt = connection.createStatement();
	    ResultSet myRs = myStmt.executeQuery(statement);
	    return myRs;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public static ResultSet executeQueryNoOutput(String statement) {
	Statement myStmt;
	try {
	    myStmt = connection.createStatement();
	    ResultSet myRs = myStmt.executeQuery(statement);
	    return myRs;
	} catch (SQLException e) {
	    return null;
	}
    }

    public static void executeUpdate(String statement) {
	Statement myStmt;
	try {
	    myStmt = connection.createStatement();
	    myStmt.executeUpdate(statement);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }
}
