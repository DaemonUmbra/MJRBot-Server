package com.mjr.files;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Properties;

import com.mjr.MJRBot;

public class Ranks extends FileBase {

    public static String[] ranks = { "gold", "sliver", "bronze", "none" };

    public static int GoldPrice = 5000;
    public static int SliverPrice = 3500;
    public static int BronzePrice = 2000;

    public static String fileName = "UserRanks.properties";

    public static String getRank(String user, String channelName) {
	user = user.toLowerCase();
	String value = "";
	user = user.toLowerCase();
	if (MJRBot.useFileSystem)
	    value = load(channelName, fileName).getProperty(user, value);
	else {
	    // TODO: Add Database Link
	}
	return value;
    }

    @SuppressWarnings("deprecation")
    public static void setRank(String user, String rank, String channelName) {
	user = user.toLowerCase();
	rank = rank.toLowerCase();
	if (getRank(user, channelName) != rank) {
	    if (MJRBot.useFileSystem) {
		Properties properties = load(channelName, fileName);
		properties.setProperty(user.toLowerCase(), rank);
		try {
		    properties.save(new FileOutputStream(loadFile(channelName, fileName)), null);
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		}
	    } else {
		// TODO: Add Database Link
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
