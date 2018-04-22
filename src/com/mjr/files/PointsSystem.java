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

    public static int getPoints(String User) {
	String value = null;
	value = properties.getProperty(User);
	return Integer.parseInt(value);
    }

    public static void setPoints(String User, int Points) {
	User = User.toLowerCase();
	properties.setProperty(User, Integer.toString(Points));
	try {
	    properties.store(new FileOutputStream(file), null);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static Boolean isOnList(String User) {
	User = User.toLowerCase();
	if (properties.getProperty(User) != null)
	    return true;
	else
	    return false;
    }

    public static void AddPoints(String User, int Points) {
	int currentPoints = getPoints(User);
	currentPoints = currentPoints + Points;
	setPoints(User, currentPoints);
	ConsoleUtil.TextToConsole("Added " + Points + " points to " + User, "Bot:", null);
    }

    public static void RemovePoints(String User, int Points) {
	int currentPoints = getPoints(User);
	currentPoints = currentPoints - Points;
	setPoints(User, currentPoints);
	ConsoleUtil.TextToConsole("Removed " + Points + " points from " + User, "Bot:", null);
    }

    public static Boolean hasPoints(String User, int Points) {
	if (getPoints(User) > Points) {
	    return true;
	} else {
	    return false;
	}
    }

    public static int AddRandomPoints(String User) {
	Random random = new Random();
	int Points = random.nextInt((100)) + 1;
	int currentPoints = getPoints(User);
	currentPoints = currentPoints + Points;
	setPoints(User, currentPoints);
	return Points;
    }
}
