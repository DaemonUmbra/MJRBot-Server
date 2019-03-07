package com.mjr.mjrbot.storage.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mjr.mjrbot.storage.BotConfigManager;
import com.mjr.mjrbot.util.MJRBotUtilities;

public class MySQLConnection {

	private static Connection connection;
	private static boolean connected = false;

	public static void connect(String ipAddress, int port, String databaseName, String user, String password) {
		connected = false;
		try {
			MySQLConnection.initConnection(ipAddress, port, databaseName, user, password);
		} catch (NumberFormatException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static void reconnect() {
		connect(BotConfigManager.getSetting("DatabaseIPAddress"), Integer.parseInt(BotConfigManager.getSetting("DatabasePort")), BotConfigManager.getSetting("DatabaseDatabaseName"), BotConfigManager.getSetting("DatabaseUsername"),
				BotConfigManager.getSetting("DatabasePassword"));
	}

	private static void initConnection(String ipAddress, int port, String databaseName, String user, String password) throws SQLException, ClassNotFoundException {
		try {
			do {
				System.out.println("MySQl getConnection() initializing!");
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + ipAddress + ":" + port + "/" + databaseName + "?serverTimezone=GMT", user, password);
				connected = getConnection().isValid(5);
				if (!connected)
					System.out.println("MySQL Connection Failed!");
			} while (!connected);
			System.out.println("MySQl getConnection() has been initialized!");
		} catch (ClassNotFoundException | SQLException e) {
			if (e.getMessage().contains("Unknown database")) {
				createDatabase(ipAddress, port, databaseName, user, password);
			} else {
				if (!e.getMessage().contains("Communications link failure"))
					e.printStackTrace();
				else
					System.out.println("MySQL Connection Failed! Error: " + e.getMessage().substring(0, e.getMessage().indexOf("\n")));
			}
		}
	}

	private static void createDatabase(String ipAddress, int port, String databaseName, String user, String password) {
		try {
			System.out.println("MySQL New Database detected!");
			do {
				System.out.println("MySQl getConnection() initializing!");
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + ipAddress + ":" + port + "?serverTimezone=GMT", user, password);
				connected = getConnection().isValid(5);
				if (!connected)
					System.out.println("MySQL Connection Failed!");
			} while (!connected);
			System.out.println("MySQl getConnection() has been initialized!");
			System.out.println("MySQL Settings up Database!");
			SQLUtilities.createDatabaseStructure();
			connected = false;
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("MySQL Connection Failed! Error: " + ex.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static ResultSet executeQuery(String statement) {
		return executeQuery(statement, true);
	}

	public static ResultSet executeQuery(String statement, boolean logError) {
		try {
			if (!connected || !connection.isValid(30)) {
				reconnect();
				MJRBotUtilities.logErrorMessage("MySQL is disconnected! Attempting to reconnect!");
			}
			if (connected) {
				Statement myStmt;
				try {
					myStmt = getConnection().createStatement();
					ResultSet myRs = myStmt.executeQuery(statement);
					return myRs;
				} catch (SQLException e) {
					if (logError)
						e.printStackTrace();
				}
			} else
				MJRBotUtilities.logErrorMessage("MySQL is disconnected even after a reconnect attempt! Failed to run query: " + statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void executeUpdate(String statement) {
		try {
			if (!connected || !connection.isValid(30)) {
				reconnect();
				MJRBotUtilities.logErrorMessage("MySQL is disconnected! Attempting to reconnect!");
			}
			if (connected) {
				Statement myStmt;
				try {
					myStmt = getConnection().createStatement();
					myStmt.executeUpdate(statement);
					myStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else
				MJRBotUtilities.logErrorMessage("MySQL is disconnected even after a reconnect attempt! Failed to run query: " + statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		return connection;
	}

	public static boolean isConnected() {
		return connected;
	}

}
