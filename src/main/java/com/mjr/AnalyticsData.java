package com.mjr;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mjr.sql.MySQLConnection;
import com.mjr.util.ConsoleUtil;

public class AnalyticsData {
    private static int numOfCommandsUsed = 0;
    private static int numOfMessagedModerated = 0;
    private static int numOfPointsGained = 0;
    private static int numOfPointsRemoved = 0;

    public static int getNumOfCommandsUsed() {
	return numOfCommandsUsed;
    }

    public static void addNumOfCommandsUsed(int number) {
	numOfCommandsUsed += number;
    }

    public static int getNumOfMessagedModerated() {
	return numOfMessagedModerated;
    }

    public static void addNumOfMessagedModerated(int number) {
	numOfMessagedModerated += number;
    }

    public static int getNumOfPointsGained() {
	return numOfPointsGained;
    }

    public static void addNumOfPointsGained(int number) {
	numOfPointsGained += number;
    }

    public static int getNumOfPointsRemoved() {
	return numOfPointsRemoved;
    }

    public static void addNumOfPointsRemoved(int number) {
	numOfPointsRemoved += number;
    }

    public static void clearNumOfCommandsUsed() {
	numOfCommandsUsed = 0;
    }

    public static void clearNumOfMessagedModerated() {
	numOfMessagedModerated = 0;
    }

    public static void clearNumOfPointsGained() {
	numOfPointsGained = 0;
    }

    public static void clearNumOfPointsRemoved() {
	numOfPointsRemoved = 0;
    }

    public static void sendData() {
	try {
	    if (!MySQLConnection.executeQueryNoOutput("SELECT * FROM analytics").next()) {
		MySQLConnection.executeUpdate(
			"INSERT INTO analytics(name, value) VALUES (" + "\"" + "NumOfCommandsUsed" + "\"" + "," + "\"" + 0 + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO analytics(name, value) VALUES (" + "\"" + "NumOfMessagedModerated" + "\"" + ","
			+ "\"" + 0 + "\"" + ")");
		MySQLConnection.executeUpdate(
			"INSERT INTO analytics(name, value) VALUES (" + "\"" + "NumOfPointsGained" + "\"" + "," + "\"" + 0 + "\"" + ")");
		MySQLConnection.executeUpdate(
			"INSERT INTO analytics(name, value) VALUES (" + "\"" + "NumOfPointsRemoved" + "\"" + "," + "\"" + 0 + "\"" + ")");
	    } else {
		ResultSet result = MySQLConnection
			.executeQuery("SELECT value FROM analytics WHERE name = " + "\"" + "NumOfCommandsUsed" + "\"");
		if (result.next()) {
		    MySQLConnection.executeUpdate("UPDATE analytics SET value=" + (result.getInt(1) + AnalyticsData.getNumOfCommandsUsed())
			    + " WHERE name=" + "\"" + "NumOfCommandsUsed" + "\"");
		    AnalyticsData.clearNumOfCommandsUsed();
		}
		result = MySQLConnection.executeQuery("SELECT value FROM analytics WHERE name = " + "\"" + "NumOfMessagedModerated" + "\"");
		if (result.next()) {
		    MySQLConnection
			    .executeUpdate("UPDATE analytics SET value=" + (result.getInt(1) + AnalyticsData.getNumOfMessagedModerated())
				    + " WHERE name=" + "\"" + "NumOfMessagedModerated" + "\"");
		    AnalyticsData.clearNumOfMessagedModerated();
		}
		result = MySQLConnection.executeQuery("SELECT value FROM analytics WHERE name = " + "\"" + "NumOfPointsGained" + "\"");
		if (result.next()) {
		    MySQLConnection.executeUpdate("UPDATE analytics SET value=" + (result.getInt(1) + AnalyticsData.getNumOfPointsGained())
			    + " WHERE name=" + "\"" + "NumOfPointsGained" + "\"");
		    AnalyticsData.clearNumOfPointsGained();
		}
		result = MySQLConnection.executeQuery("SELECT value FROM analytics WHERE name = " + "\"" + "NumOfPointsRemoved" + "\"");
		if (result.next()) {
		    MySQLConnection.executeUpdate("UPDATE analytics SET value=" + (result.getInt(1) + AnalyticsData.getNumOfPointsRemoved())
			    + " WHERE name=" + "\"" + "NumOfPointsRemoved" + "\"");
		    AnalyticsData.clearNumOfPointsRemoved();
		}
		ConsoleUtil.textToConsole("Sent Analytics Data to Database & reset local Analytics Data!");
	    }
	} catch (SQLException e) {
	    MJRBot.getLogger().info(e.getMessage() + " " + e.getCause()); e.printStackTrace();
	}
    }
}
