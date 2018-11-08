package com.mjr.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mjr.MJRBot;
import com.mjr.storage.ConfigMain;

public class MySQLConnection {

    private static Connection connection;
    public static boolean connected = false;

    public static void connect() {
	connected = false;
	try {
	    MySQLConnection.initConnection(ConfigMain.getSetting("DatabaseIPAddress"),
		    Integer.parseInt(ConfigMain.getSetting("DatabasePort")), ConfigMain.getSetting("DatabaseDatabaseName"),
		    ConfigMain.getSetting("DatabaseUsername"), ConfigMain.getSetting("DatabasePassword"));
	} catch (NumberFormatException | ClassNotFoundException | SQLException e) {
	    MJRBot.getLogger().info(e.getMessage() + " " + e.getCause()); e.printStackTrace();
	}
    }

    private static void initConnection(String ipAddress, int port, String databaseName, String user, String password)
	    throws SQLException, ClassNotFoundException {
	try {
	    do {
		System.out.println("MySQl getConnection() initializing!");
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(
			"jdbc:mysql://" + ipAddress + ":" + port + "/" + databaseName + "?serverTimezone=GMT", user, password);
		connected = getConnection().isValid(5);
		if (!connected)
		    System.out.println("MySQL Connection Failed!");
	    } while (!connected);
	    System.out.println("MySQl getConnection() has been initialized!");
	} catch (ClassNotFoundException | SQLException e) {
	    if (e.getMessage().contains("Unknown database")) {
		try {
		    System.out.println("MySQL New Database detected!");
		    do {
			System.out.println("MySQl getConnection() initializing!");
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(
				"jdbc:mysql://" + ipAddress + ":" + port + "/" + databaseName + "?serverTimezone=GMT", user, password);
			connected = getConnection().isValid(5);
			if (!connected)
			    System.out.println("MySQL Connection Failed!");
		    } while (!connected);
		    System.out.println("MySQl getConnection() has been initialized!");
		    System.out.println("MySQL Settings up Database!");
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
	if (connected) {
	    try {
		if (!getConnection().isValid(5))
		    connect();
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	    Statement myStmt;
	    try {
		myStmt = getConnection().createStatement();
		ResultSet myRs = myStmt.executeQuery(statement);
		return myRs;
	    } catch (SQLException e) {
		MJRBot.getLogger().info(e.getMessage() + " " + e.getCause()); e.printStackTrace();
	    }
	} else
	    System.out.println("MySQL is disconnected! Please restart bot!");
	return null;
    }

    public static ResultSet executeQueryNoOutput(String statement) {
	if (connected) {
	    try {
		if (!getConnection().isValid(5))
		    connect();
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	    Statement myStmt;
	    try {
		myStmt = getConnection().createStatement();
		ResultSet myRs = myStmt.executeQuery(statement);
		return myRs;
	    } catch (SQLException e) {
		return null;
	    }
	} else
	    System.out.println("MySQL is disconnected! Please restart bot!");
	return null;
    }

    public static void executeUpdate(String statement) {
	if (connected) {
	    try {
		if (!getConnection().isValid(5))
		    connect();
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	    Statement myStmt;
	    try {
		myStmt = getConnection().createStatement();
		myStmt.executeUpdate(statement);
		myStmt.close();
	    } catch (SQLException e) {
		MJRBot.getLogger().info(e.getMessage() + " " + e.getCause()); e.printStackTrace();
	    }
	} else
	    System.out.println("MySQL is disconnected! Please restart bot!");
    }
}
