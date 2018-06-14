package com.mjr.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import com.mjr.MJRBot;

public class Ranks {

    public static String[] ranks = { "gold", "sliver", "bronze", "none" };

    public static int GoldPrice = 5000;
    public static int SliverPrice = 3500;
    public static int BronzePrice = 2000;

    public static String filename = "UserRanks.properties";
    public static File file;
    public static Properties properties = new Properties();
    protected static InputStream iStream;

    public static void load(String channelName) throws IOException {
	file = new File(MJRBot.filePath + MJRBot.getTwitchBotByChannelName(channelName) + File.separator + filename);
	if (!file.exists()) {
	    file.getParentFile().mkdirs();
	    file.createNewFile();
	}
	FileReader reader = new FileReader(file);
	properties.load(reader);
    }

    public static String getRank(String user) {
	user = user.toLowerCase();
	String value = "";
	user = user.toLowerCase();
	value = properties.getProperty(user, value);
	return value;
    }

    @SuppressWarnings("deprecation")
    public static void setRank(String user, String rank) {
	user = user.toLowerCase();
	rank = rank.toLowerCase();
	if (getRank(user) != rank) {
	    properties.setProperty(user.toLowerCase(), rank);
	    try {
		properties.save(new FileOutputStream(file), null);
	    } catch (FileNotFoundException e) {
		e.printStackTrace();
	    }
	}
    }

    public static void removeRank(String user) {
	user = user.toLowerCase();
	if (getRank(user) != "None") {
	    setRank(user, "None");
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

    public static Boolean isOnList(String user) {
	user = user.toLowerCase();
	if (properties.getProperty(user) != null)
	    return true;
	else
	    return false;
    }

    public static Boolean hasRank(String user, String rank) {
	user = user.toLowerCase();
	rank = rank.toLowerCase();
	if (getRank(user).equalsIgnoreCase(rank))
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
