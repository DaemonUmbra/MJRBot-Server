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
	initConnection("jdbc:mysql://" + ipAddress + ":" + port + "/" + databaseName + "?serverTimezone=GMT", user, password);
    }

    public static void initConnection(String url, String user, String password) throws SQLException, ClassNotFoundException {
	try {
	    System.out.println("MySQl connection initializing!");
	    Class.forName("com.mysql.cj.jdbc.Driver");
	    connection = DriverManager.getConnection(url, user, password);
	    connected = true;
	    System.out.println("MySQl connection has been initialized!");
	} catch (ClassNotFoundException | SQLException e) {
	    System.out.println("MySQL Connection Failed! Error: " + e.getMessage());
	    connected = false;
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
}
