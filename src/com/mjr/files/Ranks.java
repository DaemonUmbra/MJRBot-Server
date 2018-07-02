package com.mjr.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import com.mjr.MJRBot;

public class Ranks {

    public static String[] ranks = { "gold", "sliver", "bronze", "none" };

    public static int GoldPrice = 5000;
    public static int SliverPrice = 3500;
    public static int BronzePrice = 2000;

    public static String filename = "UserRanks.properties";

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

    public static String getRank(String user, String channelName) {
	user = user.toLowerCase();
	String value = "";
	user = user.toLowerCase();
	value = load(channelName).getProperty(user, value);
	return value;
    }

    @SuppressWarnings("deprecation")
    public static void setRank(String user, String rank, String channelName) {
	user = user.toLowerCase();
	rank = rank.toLowerCase();
	if (getRank(user, channelName) != rank) {
	    Properties properties = load(channelName);
	    properties.setProperty(user.toLowerCase(), rank);
	    try {
		properties.save(new FileOutputStream(loadFile(channelName)), null);
	    } catch (FileNotFoundException e) {
		e.printStackTrace();
	    }
	}
    }

    public static void removeRank(String user, String channelName) {
	user = user.toLowerCase();
	if (getRank(user, channelName) != "None") {
	    setRank(user, "None", channelName);
	}
    }

    public static int getRankPrice(String rank) {
	rank = rank.toLowerCase();
	if (rank.equalsIgnoreCase(ranks[0]))
	    return GoldPrice;
	else if (rank.equalsIgnoreCase(ranks[1]))
	    return SliverPrice;
	else if (rank.equalsIgnoreCase(ranks[2]))
	    return BronzePrice;
	return 0;
    }

    public static Boolean isOnList(String user, String channelName) {
	user = user.toLowerCase();
	if (load(channelName).getProperty(user) != null)
	    return true;
	else
	    return false;
    }

    public static Boolean hasRank(String user, String rank, String channelName) {
	user = user.toLowerCase();
	rank = rank.toLowerCase();
	if (getRank(user, channelName).equalsIgnoreCase(rank))
	    return true;
	else
	    return false;
    }

    public static Boolean isValidRank(String rank) {
	rank = rank.toLowerCase();
	if (Arrays.asList(ranks).contains(rank))
	    return true;
	else
	    return false;
    }
}
