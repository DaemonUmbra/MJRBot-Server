package com.mjr.files;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mjr.ConsoleUtil;
import com.mjr.ConsoleUtil.MessageType;
import com.mjr.MJRBot;
import com.mjr.Utilities;
import com.mjr.sql.MySQLConnection;

public class PointsSystem extends FileBase {
    public static String fileName = "Points.properties";

    public static int getPoints(String user, String channelName) {
	user = user.toLowerCase();
	if (!isOnList(user, channelName))
	    return 0;
	String value = null;
	if (MJRBot.useFileSystem)
	    value = load(channelName, fileName).getProperty(user);
	else {
	    ResultSet result = MySQLConnection.executeQueryNoOutput(
		    "SELECT amount FROM points WHERE channel = " + "\"" + channelName + "\"" + " AND name = " + "\"" + user + "\"");
	    if (result == null)
		return 0;
	    else
		try {
		    result.beforeFirst();
		    result.next();
		    return Integer.parseInt(result.getString(1));
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	}
	return Integer.parseInt(value);
    }

    public static void setPoints(String user, int points, String channelName) {
	user = user.toLowerCase();
	if (MJRBot.useFileSystem) {
	    Properties properties = load(channelName, fileName);
	    properties.setProperty(user, Integer.toString(points));
	    try {
		properties.store(new FileOutputStream(loadFile(channelName, fileName)), null);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} else {
	    if (isOnList(user, channelName) == false)
		MySQLConnection.executeUpdate("INSERT INTO points(name, channel, amount) VALUES (" + "\"" + user + "\"" + "," + "\""
			+ channelName + "\"" + "," + "\"" + points + "\"" + ")");
	    else
		MySQLConnection.executeUpdate("UPDATE points SET amount=" + "\"" + points + "\"" + " WHERE channel = " + "\"" + channelName
			+ "\"" + " AND name = " + "\"" + user + "\"");
	}
    }

    public static Boolean isOnList(String user, String channelName) {
	user = user.toLowerCase();
	if (MJRBot.useFileSystem) {
	    if (load(channelName, fileName).getProperty(user) != null)
		return true;
	    else
		return false;
	} else {
	    ResultSet result = MySQLConnection.executeQueryNoOutput(
		    "SELECT * FROM points WHERE channel = " + "\"" + channelName + "\"" + " AND name = " + "\"" + user + "\"");
	    try {
		if (result == null)
		    return false;
		else if (result.getFetchSize() > 1)
		    return false;
		else
		    return true;
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return false;
    }

    public static void AddPoints(String user, int points, String channelName) {
	user = user.toLowerCase();
	if (!isOnList(user, channelName))
	    setPoints(user, Integer.parseInt(Config.getSetting("StartingPoints", channelName)), channelName);
	int currentPoints = getPoints(user, channelName);
	currentPoints = currentPoints + points;
	setPoints(user, currentPoints, channelName);
	ConsoleUtil.TextToConsole(null, null, channelName, "Added " + points + " points to " + user, MessageType.Bot, null);
    }

    public static void RemovePoints(String user, int points, String channelName) {
	user = user.toLowerCase();
	if (!isOnList(user, channelName))
	    setPoints(user, 0, channelName);
	int currentPoints = getPoints(user, channelName);
	currentPoints = currentPoints - points;
	setPoints(user, currentPoints, channelName);
	ConsoleUtil.TextToConsole(null, null, channelName, "Removed " + points + " points from " + user, MessageType.Bot, null);
    }

    public static Boolean hasPoints(String user, int points, String channelName) {
	user = user.toLowerCase();
	if (getPoints(user, channelName) >= points) {
	    return true;
	} else {
	    return false;
	}
    }

    public static int AddRandomPoints(String user, String channelName) {
	return AddRandomPoints(user, 100, 1, channelName);
    }

    public static int AddRandomPoints(String user, int max, int min, String channelName) {
	user = user.toLowerCase();
	int points = Utilities.getRandom(min, max);
	AddPoints(user, points, channelName);
	return points;
    }

    public static void migrateFile(String channelName) {
	Properties file = load(channelName, fileName);
	for (Object user : file.keySet()) {
	    MySQLConnection.executeUpdate("INSERT INTO points(name, channel, amount) VALUES (" + "\"" + ((String) user) + "\"" + "," + "\""
		    + channelName + "\"" + "," + "\"" + file.getProperty((String) user) + "\"" + ")");
	}
    }
}
