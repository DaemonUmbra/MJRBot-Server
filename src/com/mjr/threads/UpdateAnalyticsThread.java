package com.mjr.threads;

import java.sql.SQLException;

import com.mjr.AnalyticsData;
import com.mjr.MJRBot;
import com.mjr.sql.MySQLConnection;

public class UpdateAnalyticsThread extends Thread {
    @Override
    public void run() {
	while (true) {
	    if(MJRBot.useFileSystem == false) {
		try {
		    if (!MySQLConnection.executeQueryNoOutput("SELECT * FROM analyics").next()) {
			MySQLConnection.executeUpdate("INSERT INTO analyics(name, value) VALUES (" + "\"" + "NumOfCommandsUsed" + "\"" + "," + "\"" + 0 + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO analyics(name, value) VALUES (" + "\"" + "NumOfMessagedModerated" + "\"" + "," + "\"" + 0 + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO analyics(name, value) VALUES (" + "\"" + "NumOfPointsGained" + "\"" + "," + "\"" + 0 + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO analyics(name, value) VALUES (" + "\"" + "NumOfPointsRemoved" + "\"" + "," + "\"" + 0 + "\"" + ")");
		    }
		    else {
			MySQLConnection.executeUpdate("INSERT INTO analyics(name, value) VALUES (" + "\"" + "NumOfCommandsUsed" + "\"" + "," + "\"" + AnalyticsData.getNumOfCommandsUsed() + "\"" + ")");
			AnalyticsData.clearNumOfCommandsUsed();
			MySQLConnection.executeUpdate("INSERT INTO analyics(name, value) VALUES (" + "\"" + "NumOfMessagedModerated" + "\"" + "," + "\"" + AnalyticsData.getNumOfMessagedModerated() + "\"" + ")");
			AnalyticsData.clearNumOfMessagedModerated();
			MySQLConnection.executeUpdate("INSERT INTO analyics(name, value) VALUES (" + "\"" + "NumOfPointsGained" + "\"" + "," + "\"" + AnalyticsData.getNumOfPointsGained() + "\"" + ")");
			AnalyticsData.clearNumOfPointsGained();
			MySQLConnection.executeUpdate("INSERT INTO analyics(name, value) VALUES (" + "\"" + "NumOfPointsRemoved" + "\"" + "," + "\"" + AnalyticsData.getNumOfPointsRemoved() + "\"" + ")");
			AnalyticsData.clearNumOfPointsRemoved();
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
    }
}
