package com.mjr.threads;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mjr.AnalyticsData;
import com.mjr.ConsoleUtil;
import com.mjr.MJRBot;
import com.mjr.sql.MySQLConnection;

public class UpdateAnalyticsThread extends Thread {
    @Override
    public void run() {
	while (true) {
	    if (MJRBot.useFileSystem == false) {
		try {
		    if (!MySQLConnection.executeQueryNoOutput("SELECT * FROM analytics").next()) {
			MySQLConnection.executeUpdate("INSERT INTO analytics(name, value) VALUES (" + "\"" + "NumOfCommandsUsed" + "\""
				+ "," + "\"" + 0 + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO analytics(name, value) VALUES (" + "\"" + "NumOfMessagedModerated" + "\""
				+ "," + "\"" + 0 + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO analytics(name, value) VALUES (" + "\"" + "NumOfPointsGained" + "\""
				+ "," + "\"" + 0 + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO analytics(name, value) VALUES (" + "\"" + "NumOfPointsRemoved" + "\""
				+ "," + "\"" + 0 + "\"" + ")");
		    } else {
			ResultSet result = MySQLConnection
				.executeQueryNoOutput("SELECT value FROM analytics WHERE name = " + "\"" + "NumOfCommandsUsed" + "\"");
			if (result.next()) {
			    MySQLConnection.executeUpdate("UPDATE analytics SET value=" + (result.getInt(1)
				    + AnalyticsData.getNumOfCommandsUsed()) + " WHERE name=" + "\"" + "NumOfCommandsUsed" + "\"");
			    AnalyticsData.clearNumOfCommandsUsed();
			}
			if (result.next()) {
			    result = MySQLConnection.executeQueryNoOutput(
				    "SELECT value FROM analytics WHERE name = " + "\"" + "NumOfMessagedModerated" + "\"");
			    MySQLConnection.executeUpdate("UPDATE analytics SET value=" + (result.getInt(1)
				    + AnalyticsData.getNumOfMessagedModerated()) + " WHERE name=" + "\"" + "NumOfMessagedModerated" + "\"");
			    AnalyticsData.clearNumOfMessagedModerated();
			}
			if (result.next()) {
			    result = MySQLConnection
				    .executeQueryNoOutput("SELECT value FROM analytics WHERE name = " + "\"" + "NumOfPointsGained" + "\"");
			    MySQLConnection.executeUpdate("UPDATE analytics SET value=" + (result.getInt(1)
				    + AnalyticsData.getNumOfPointsGained()) + " WHERE name=" + "\"" + "NumOfPointsGained" + "\"");
			    AnalyticsData.clearNumOfPointsGained();
			}
			if (result.next()) {
			    result = MySQLConnection
				    .executeQueryNoOutput("SELECT value FROM analytics WHERE name = " + "\"" + "NumOfPointsRemoved" + "\"");
			    MySQLConnection.executeUpdate("UPDATE analytics SET value=" + (result.getInt(1)
				    + AnalyticsData.getNumOfPointsRemoved()) + " WHERE name=" + "\"" + "NumOfPointsRemoved" + "\"");
			    AnalyticsData.clearNumOfPointsRemoved();
			}
			ConsoleUtil.TextToConsole("Sent Analytics Data to Database & reset local Analytics Data!");
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	    try {
		Thread.sleep(300000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }
}
