package com.mjr.storage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

import com.mjr.ConsoleUtil;
import com.mjr.MJRBot;
import com.mjr.ConsoleUtil.MessageType;
import com.mjr.sql.MySQLConnection;

public class RankSystem extends FileBase {

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
	    ResultSet result = MySQLConnection.executeQueryNoOutput(
		    "SELECT rank FROM ranks WHERE channel = " + "\"" + channelName + "\"" + " AND name = " + "\"" + user + "\"");
	    try {
		if (result == null)
		    return null;
		else if (!result.next())
		    return null;
		else {
		    result.beforeFirst();
		    result.next();
		    return result.getString(1);
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
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
		if (isOnList(user, channelName) == false)
		    MySQLConnection.executeUpdate("INSERT INTO ranks(name, channel, rank) VALUES (" + "\"" + user + "\"" + "," + "\""
			    + channelName + "\"" + "," + "\"" + rank + "\"" + ")");
		else
		    MySQLConnection.executeUpdate("UPDATE ranks SET rank=" + "\"" + rank + "\"" + " WHERE channel = " + "\"" + channelName
			    + "\"" + " AND name = " + "\"" + user + "\"");
	    }
	    ConsoleUtil.TextToConsole(null, null, channelName, "Set " + user + " rank to " + rank, MessageType.Bot, null);
	    EventLog.addEvent(channelName, user, "Set rank to" + rank);
	}
    }

    public static void removeRank(String user, String channelName) {
	user = user.toLowerCase();
	if (getRank(user, channelName) != "None") {
	    ConsoleUtil.TextToConsole(null, null, channelName, "Removed rank from " + user, MessageType.Bot, null);
	    EventLog.addEvent(channelName, user, "Removed rank");
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
	    ResultSet result = MySQLConnection.executeQueryNoOutput(
		    "SELECT * FROM ranks WHERE channel = " + "\"" + channelName + "\"" + " AND name = " + "\"" + user + "\"");
	    try {
		if (result == null)
		    return false;
		else if (!result.next())
		    return false;
		else
		    return true;
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
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

    public static void migrateFile(String channelName) {
	Properties file = load(channelName, fileName);
	for (Object user : file.keySet()) {
	    MySQLConnection.executeUpdate("INSERT INTO ranks(name, channel, rank) VALUES (" + "\"" + ((String) user) + "\"" + "," + "\""
		    + channelName + "\"" + "," + "\"" + file.getProperty((String) user) + "\"" + ")");
	}
    }
}
