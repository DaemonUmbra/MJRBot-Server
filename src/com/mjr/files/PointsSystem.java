package com.mjr.files;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.mjr.ConsoleUtil;
import com.mjr.ConsoleUtil.MessageType;
import com.mjr.MJRBot;
import com.mjr.Utilities;

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
	    // TODO: Add Database Link
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
	    // TODO: Add Database Link
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
	    // TODO: Add Database Link
	}
	return null;
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
}
