package com.mjr.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.mjr.ConsoleUtil;
import com.mjr.ConsoleUtil.MessageType;
import com.mjr.MJRBot;
import com.mjr.Utilities;

public class PointsSystem {
    public static String filename = "Points.properties";

    public static Properties load(String channelName) {
	try {
	    FileReader reader;
	    reader = new FileReader(loadFile(channelName));

	    Properties properties = new Properties();
	    properties.load(reader);
	    return properties;
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public static File loadFile(String channelName) {
	try {
	    File file = new File(MJRBot.filePath + MJRBot.getTwitchBotByChannelName(channelName).channelName + File.separator + filename);
	    if (!file.exists()) {
		file.getParentFile().mkdirs();
		file.createNewFile();
	    }
	    return file;
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public static int getPoints(String user, String channelName) {
	user = user.toLowerCase();
	if (!isOnList(user, channelName))
	    return 0;
	String value = null;
	value = load(channelName).getProperty(user);
	return Integer.parseInt(value);
    }

    public static void setPoints(String user, int points, String channelName) {
	user = user.toLowerCase();
	Properties properties = load(channelName);
	properties.setProperty(user, Integer.toString(points));
	try {
	    properties.store(new FileOutputStream(loadFile(channelName)), null);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static Boolean isOnList(String user, String channelName) {
	user = user.toLowerCase();
	if (load(channelName).getProperty(user) != null)
	    return true;
	else
	    return false;
    }

    public static void AddPoints(String user, int points, String channelName) {
	user = user.toLowerCase();
	if (!isOnList(user, channelName))
	    setPoints(user, Integer.parseInt(Config.getSetting("StartingPoints", channelName)), channelName);
	int currentPoints = getPoints(user, channelName);
	currentPoints = currentPoints + points;
	setPoints(user, currentPoints, channelName);
	ConsoleUtil.TextToConsole(null, null, "Added " + points + " points to " + user, channelName, MessageType.Bot, null);
    }

    public static void RemovePoints(String user, int points, String channelName) {
	user = user.toLowerCase();
	if (!isOnList(user, channelName))
	    setPoints(user, 0, channelName);
	int currentPoints = getPoints(user, channelName);
	currentPoints = currentPoints - points;
	setPoints(user, currentPoints, channelName);
	ConsoleUtil.TextToConsole(null, null, "Removed " + points + " points from " + user, channelName, MessageType.Bot, null);
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
