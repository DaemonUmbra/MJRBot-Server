package com.mjr.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.mjr.ConsoleUtil;
import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.Utilities;

public class PointsSystem {
    public static String filename = "Points.properties";
    public static File file;
    public static Properties properties = new Properties();
    protected static InputStream iStream;

    public static void load(BotType type, String channelName) throws IOException {
	if (type == BotType.Twitch)
	    file = new File(MJRBot.filePath + MJRBot.getTwitchBotByChannelName(channelName).channelName + File.separator + filename);
	else {
	    file = new File(MJRBot.filePath + MJRBot.getMixerBotByChannelName(channelName).channelName + File.separator + filename);
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

    public static void AddPoints(String user, int points, String channelName) {
	user = user.toLowerCase();
	if (!isOnList(user))
	    setPoints(user, Integer.parseInt(Config.getSetting("StartingPoints")));
	int currentPoints = getPoints(user);
	currentPoints = currentPoints + points;
	setPoints(user, currentPoints);
	ConsoleUtil.TextToConsole("[Channel] " + channelName + " - Added " + points + " points to " + user, "Bot:", null);
    }

    public static void RemovePoints(String user, int points, String channelName) {
	user = user.toLowerCase();
	if (!isOnList(user))
	    setPoints(user, 0);
	int currentPoints = getPoints(user);
	currentPoints = currentPoints - points;
	setPoints(user, currentPoints);
	ConsoleUtil.TextToConsole("[Channel] \" + channelName + \" - Removed " + points + " points from " + user, "Bot:", null);
    }

    public static Boolean hasPoints(String user, int points) {
	user = user.toLowerCase();
	if (getPoints(user) >= points) {
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
