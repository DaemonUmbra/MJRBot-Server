package com.mjr.mjrbot.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.MixerBot;
import com.mjr.mjrbot.TwitchBot;
import com.mjr.mjrbot.sql.MySQLConnection;
import com.mjr.mjrbot.storage.EventLog.EventType;
import com.mjr.mjrbot.util.ConsoleUtil;
import com.mjr.mjrbot.util.ConsoleUtil.MessageType;
import com.mjr.mjrbot.util.Utilities;

public class RankSystem extends FileBase {

	public static String[] ranks = { "gold", "sliver", "bronze", "none" };

	public static int GoldPrice = 5000;
	public static int SliverPrice = 3500;
	public static int BronzePrice = 2000;

	public static String fileName = "UserRanks.properties";

	public static String getRank(String user, BotType type, Object bot) {
		user = user.toLowerCase();
		String value = "";
		user = user.toLowerCase();
		if (MJRBot.storageType == StorageType.File) {
			if (type == BotType.Twitch)
				value = load(((TwitchBot) bot).channelID, fileName).getProperty(user, value);
			else if (type == BotType.Mixer)
				value = load(((MixerBot) bot).channelName, fileName).getProperty(user, value);
		} else {
			ResultSet result = null;
			if (type == BotType.Twitch)
				result = MySQLConnection.executeQuery("SELECT rank FROM ranks WHERE twitch_channel_id = " + "\"" + Utilities.getChannelIDFromBotType(type, bot) + "\"" + " AND name = " + "\"" + user + "\"");
			else if (type == BotType.Mixer)
				result = MySQLConnection.executeQuery("SELECT rank FROM ranks WHERE mixer_channel = " + "\"" + Utilities.getChannelNameFromBotType(type, bot) + "\"" + " AND name = " + "\"" + user + "\"");
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
				MJRBot.logErrorMessage(e);
			}
		}
		return value;
	}

	@SuppressWarnings("deprecation")
	public static void setRank(String user, String rank, BotType type, Object bot) {
		user = user.toLowerCase();
		rank = rank.toLowerCase();
		if (getRank(user, type, bot) != rank) {
			if (MJRBot.storageType == StorageType.File) {
				Properties properties = null;
				if (type == BotType.Twitch)
					properties = load(((TwitchBot) bot).channelID, fileName);
				else if (type == BotType.Mixer)
					properties = load(((MixerBot) bot).channelName, fileName);
				properties.setProperty(user.toLowerCase(), rank);
				try {
					File file = null;
					if (type == BotType.Twitch)
						file = loadFile(((TwitchBot) bot).channelID, fileName);
					else if (type == BotType.Mixer)
						file = loadFile(((MixerBot) bot).channelName, fileName);
					properties.save(new FileOutputStream(file), null);
				} catch (FileNotFoundException e) {
					MJRBot.logErrorMessage(e);
				}
			} else {
				if (isOnList(user, type, bot) == false) {
					if (type == BotType.Twitch)
						MySQLConnection.executeUpdate("INSERT INTO ranks(name, twitch_channel_id, rank) VALUES (" + "\"" + user + "\"" + "," + "\"" + Utilities.getChannelIDFromBotType(type, bot) + "\"" + "," + "\"" + rank + "\"" + ")");
					else if (type == BotType.Mixer)
						MySQLConnection.executeUpdate("INSERT INTO ranks(name, mixer_channel, rank) VALUES (" + "\"" + user + "\"" + "," + "\"" + Utilities.getChannelNameFromBotType(type, bot) + "\"" + "," + "\"" + rank + "\"" + ")");
				} else {
					if (type == BotType.Twitch)
						MySQLConnection.executeUpdate("UPDATE ranks SET rank=" + "\"" + rank + "\"" + " WHERE twitch_channel_id = " + "\"" + Utilities.getChannelIDFromBotType(type, bot) + "\"" + " AND name = " + "\"" + user + "\"");
					else if (type == BotType.Mixer)
						MySQLConnection.executeUpdate("UPDATE ranks SET rank=" + "\"" + rank + "\"" + " WHERE mixer_channel = " + "\"" + Utilities.getChannelNameFromBotType(type, bot) + "\"" + " AND name = " + "\"" + user + "\"");
				}
			}
			ConsoleUtil.textToConsole(bot, type, "Set " + user + " rank to " + rank, MessageType.ChatBot, null);
			EventLog.addEvent(type, bot, user, "Set rank to " + rank, EventType.Rank);
		}
	}

	public static void removeRank(String user, BotType type, Object bot) {
		user = user.toLowerCase();
		if (getRank(user, type, bot) != "None") {
			ConsoleUtil.textToConsole(bot, type, "Removed rank from " + user, MessageType.ChatBot, null);
			EventLog.addEvent(type, bot, user, "Removed rank", EventType.Rank);
			setRank(user, "None", type, bot);
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

	public static Boolean isOnList(String user, BotType type, Object bot) {
		user = user.toLowerCase();
		if (MJRBot.storageType == StorageType.File) {
			Properties properties = null;
			if (type == BotType.Twitch)
				properties = load(((TwitchBot) bot).channelID, fileName);
			else if (type == BotType.Mixer)
				properties = load(((MixerBot) bot).channelName, fileName);
			if (properties.getProperty(user) != null)
				return true;
			else
				return false;
		} else {
			ResultSet result = null;
			if (type == BotType.Twitch)
				result = MySQLConnection.executeQuery("SELECT * FROM ranks WHERE twitch_channel_id = " + "\"" + Utilities.getChannelIDFromBotType(type, bot) + "\"" + " AND name = " + "\"" + user + "\"");
			else if (type == BotType.Mixer)
				result = MySQLConnection.executeQuery("SELECT * FROM ranks WHERE mixer_channel = " + "\"" + Utilities.getChannelNameFromBotType(type, bot) + "\"" + " AND name = " + "\"" + user + "\"");
			try {
				if (result == null)
					return false;
				else if (!result.next())
					return false;
				else
					return true;
			} catch (SQLException e) {
				MJRBot.logErrorMessage(e);
			}
		}
		return null;
	}

	public static Boolean hasRank(String user, String rank, BotType type, Object bot) {
		user = user.toLowerCase();
		rank = rank.toLowerCase();
		if (getRank(user, type, bot).equalsIgnoreCase(rank))
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

	public static void migrateFile(BotType type, Object bot) {
		Properties properties = null;
		if (type == BotType.Twitch)
			properties = load(((TwitchBot) bot).channelID, fileName);
		else if (type == BotType.Mixer)
			properties = load(((MixerBot) bot).channelName, fileName);
		for (Object user : properties.keySet()) {
			if (type == BotType.Twitch)
				MySQLConnection.executeUpdate(
						"INSERT INTO ranks(name, twitch_channel_id, rank) VALUES (" + "\"" + ((String) user) + "\"" + "," + "\"" + Utilities.getChannelIDFromBotType(type, bot) + "\"" + "," + "\"" + properties.getProperty((String) user) + "\"" + ")");
			else if (type == BotType.Mixer)
				MySQLConnection.executeUpdate(
						"INSERT INTO ranks(name, mixer_channel, rank) VALUES (" + "\"" + ((String) user) + "\"" + "," + "\"" + Utilities.getChannelNameFromBotType(type, bot) + "\"" + "," + "\"" + properties.getProperty((String) user) + "\"" + ")");
		}
	}
}
