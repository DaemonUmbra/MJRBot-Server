package com.mjr.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import com.mjr.ConsoleUtil;
import com.mjr.MJRBot;

public class PointsSystem {
    public static String filename = "Points.properties";
    public static File file;
    public static Properties properties = new Properties();
    protected static InputStream iStream;

    public static void load() throws IOException {
	if (MJRBot.getTwitchBot() != null)
	    file = new File(MJRBot.filePath + MJRBot.getTwitchBot().getChannel().substring(1) + File.separator + filename);
	else {
	    file = new File(MJRBot.filePath + MJRBot.getChannel() + File.separator + filename);
	}
	if (!file.exists()) {
	    file.getParentFile().mkdirs();
	    file.createNewFile();
	}
	FileReader reader = new FileReader(file);
	properties.load(reader);
    }

    public static int getPoints(String user) {
	user = user.toLowerCase();
	if (!isOnList(user))
	    return 0;
	String value = null;
	value = properties.getProperty(user);
	return Integer.parseInt(value);
    }

    public static void setPoints(String user, int points) {
	user = user.toLowerCase();
	properties.setProperty(user, Integer.toString(points));
	try {
	    properties.store(new FileOutputStream(file), null);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static Boolean isOnList(String user) {
	user = user.toLowerCase();
	if (properties.getProperty(user) != null)
	    return true;
	else
	    return false;
    }

    public static void AddPoints(String user, int points) {
	user = user.toLowerCase();
	if (!isOnList(user))
	    setPoints(user, Integer.parseInt(Config.getSetting("StartingPoints")));
	int currentPoints = getPoints(user);
	currentPoints = currentPoints + points;
	setPoints(user, currentPoints);
	ConsoleUtil.TextToConsole("Added " + points + " points to " + user, "Bot:", null);
    }

    public static void RemovePoints(String user, int points) {
	user = user.toLowerCase();
	if (!isOnList(user))
	    setPoints(user, 0);
	int currentPoints = getPoints(user);
	currentPoints = currentPoints - points;
	setPoints(user, currentPoints);
	ConsoleUtil.TextToConsole("Removed " + points + " points from " + user, "Bot:", null);
    }

    public static Boolean hasPoints(String user, int points) {
	user = user.toLowerCase();
	if (getPoints(user) > points) {
	    return true;
	} else {
	    return false;
	}
    }

    public static int AddRandomPoints(String user) {
	user = user.toLowerCase();
	Random random = new Random();
	int points = random.nextInt((100)) + 1;
	int currentPoints = getPoints(user);
	currentPoints = currentPoints + points;
	setPoints(user, currentPoints);
	return points;
    }
    
    public static int AddRandomPoints(String user, int max, int min) {
	user = user.toLowerCase();
	Random random = new Random();
	int points = random.nextInt((max)) + min;
	int currentPoints = getPoints(user);
	currentPoints = currentPoints + points;
	setPoints(user, currentPoints);
	return points;
    }
}
