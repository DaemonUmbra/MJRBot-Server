package com.mjr.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mjr.AnalyticsData;
import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.MJRBot.StorageType;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.sql.MySQLConnection;
import com.mjr.storage.EventLog.EventType;
import com.mjr.util.ConsoleUtil;
import com.mjr.util.ConsoleUtil.MessageType;
import com.mjr.util.Utilities;

public class PointsSystem extends FileBase {
	public static String fileName = "Points.properties";

	public static int getPoints(String user, BotType type, Object bot) {
		user = user.toLowerCase();
		if (!isOnList(user, type, bot))
			return 0;
		String value = null;
		if (MJRBot.storageType == StorageType.File) {
			if (type == BotType.Twitch)
				value = load(((TwitchBot) bot).channelID, fileName).getProperty(user);
			else if (type == BotType.Mixer)
				value = load(((MixerBot) bot).channelName, fileName).getProperty(user);
		} else {
			ResultSet result = null;
			if (type == BotType.Twitch)
				result = MySQLConnection.executeQuery("SELECT amount FROM points WHERE twitch_channel_id = " + "\"" + Utilities.getChannelIDFromBotType(type, bot) + "\"" + " AND name = " + "\"" + user + "\"");
			else if (type == BotType.Mixer)
				result = MySQLConnection.executeQuery("SELECT amount FROM points WHERE mixer_channel = " + "\"" + Utilities.getChannelNameFromBotType(type, bot) + "\"" + " AND name = " + "\"" + user + "\"");
			try {
				if (result == null)
					return 0;
				else if (!result.next())
					return 0;
				else {
					result.beforeFirst();
					result.next();
					return Integer.parseInt(result.getString(1));
				}
			} catch (SQLException e) {
				MJRBot.logErrorMessage(e);
			}
		}
		return Integer.parseInt(value);
	}

	public static void setPoints(String user, int points, BotType type, Object bot, boolean outputEvent, boolean outputConsole) {
		user = user.toLowerCase();
		if (MJRBot.storageType == StorageType.File) {
			Properties properties = null;
			if (type == BotType.Twitch)
				properties = load(((TwitchBot) bot).channelID, fileName);
			else if (type == BotType.Mixer)
				properties = load(((MixerBot) bot).channelName, fileName);
			properties.setProperty(user, Integer.toString(points));
			try {
				File file = null;
				if (type == BotType.Twitch)
					file = loadFile(((TwitchBot) bot).channelID, fileName);
				else if (type == BotType.Mixer)
					file = loadFile(((MixerBot) bot).channelName, fileName);
				properties.store(new FileOutputStream(file), null);
			} catch (IOException e) {
				MJRBot.logErrorMessage(e);
			}
		} else {
			if (isOnList(user, type, bot) == false) {
				if (type == BotType.Twitch)
					MySQLConnection.executeUpdate("INSERT INTO points(name, twitch_channel_id, amount) VALUES (" + "\"" + user + "\"" + "," + "\"" + Utilities.getChannelIDFromBotType(type, bot) + "\"" + "," + "\"" + points + "\"" + ")");
				else if (type == BotType.Mixer)
					MySQLConnection.executeUpdate("INSERT INTO points(name, mixer_channel, amount) VALUES (" + "\"" + user + "\"" + "," + "\"" + Utilities.getChannelNameFromBotType(type, bot) + "\"" + "," + "\"" + points + "\"" + ")");
			} else {
				if (type == BotType.Twitch)
					MySQLConnection.executeUpdate("UPDATE points SET amount=" + "\"" + points + "\"" + " WHERE twitch_channel_id = " + "\"" + Utilities.getChannelIDFromBotType(type, bot) + "\"" + " AND name = " + "\"" + user + "\"");
				else if (type == BotType.Mixer)
					MySQLConnection.executeUpdate("UPDATE points SET amount=" + "\"" + points + "\"" + " WHERE mixer_channel = " + "\"" + Utilities.getChannelNameFromBotType(type, bot) + "\"" + " AND name = " + "\"" + user + "\"");
			}
		}
		if (outputConsole)
			ConsoleUtil.textToConsole(bot, type, "Set " + user + " point(s) to " + points, MessageType.ChatBot, null);
		if (outputEvent)
			EventLog.addEvent(type, bot, user, "Set point(s) to " + points, EventType.Points);
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
				result = MySQLConnection.executeQuery("SELECT amount FROM points WHERE twitch_channel_id = " + "\"" + Utilities.getChannelIDFromBotType(type, bot) + "\"" + " AND name = " + "\"" + user + "\"");
			else if (type == BotType.Mixer)
				result = MySQLConnection.executeQuery("SELECT amount FROM points WHERE mixer_channel = " + "\"" + Utilities.getChannelNameFromBotType(type, bot) + "\"" + " AND name = " + "\"" + user + "\"");
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
		return false;
	}

	public static void AddPointsWithEventMsg(String user, int points, BotType type, Object bot) {
		user = user.toLowerCase();
		if (!isOnList(user, type, bot))
			setPoints(user, Integer.parseInt(Config.getSetting("StartingPoints", type, bot)), type, bot, false, false);
		int currentPoints = getPoints(user, type, bot);
		currentPoints = currentPoints + points;
		setPoints(user, currentPoints, type, bot, false, false);
		ConsoleUtil.textToConsole(bot, type, "Added " + points + " point(s) to " + user, MessageType.ChatBot, null);
		EventLog.addEvent(type, bot, user, "Added " + points + " point(s)", EventType.Points);
		AnalyticsData.addNumOfPointsGained(points);
	}

	public static void AddPoints(String user, int points, BotType type, Object bot) {
		user = user.toLowerCase();
		if (!isOnList(user, type, bot))
			setPoints(user, Integer.parseInt(Config.getSetting("StartingPoints", type, bot)), type, bot, false, false);
		int currentPoints = getPoints(user, type, bot);
		currentPoints = currentPoints + points;
		setPoints(user, currentPoints, type, bot, false, false);
		ConsoleUtil.textToConsole(bot, type, "Added " + points + " point(s) to " + user, MessageType.ChatBot, null);
		AnalyticsData.addNumOfPointsGained(points);
	}

	public static void RemovePoints(String user, int points, BotType type, Object bot) {
		user = user.toLowerCase();
		if (!isOnList(user, type, bot))
			setPoints(user, 0, type, bot, false, false);
		int currentPoints = getPoints(user, type, bot);
		currentPoints = currentPoints - points;
		setPoints(user, currentPoints, type, bot, false, false);
		ConsoleUtil.textToConsole(bot, type, "Removed " + points + " point(s) from " + user, MessageType.ChatBot, null);
		EventLog.addEvent(type, bot, user, "Removed " + points + " point(s)", EventType.Points);
		AnalyticsData.addNumOfPointsRemoved(points);
	}

	public static Boolean hasPoints(String user, int points, BotType type, Object bot) {
		user = user.toLowerCase();
		if (getPoints(user, type, bot) >= points) {
			return true;
		} else {
			return false;
		}
	}

	public static int AddRandomPoints(String user, BotType type, Object bot) {
		return AddRandomPoints(user, 100, 1, type, bot);
	}

	public static int AddRandomPoints(String user, int max, int min, BotType type, Object bot) {
		user = user.toLowerCase();
		int points = Utilities.getRandom(min, max);
		AddPointsWithEventMsg(user, points, type, bot);
		return points;
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
						"INSERT INTO points(name, twitch_channel_id, amount) VALUES (" + "\"" + ((String) user) + "\"" + "," + "\"" + Utilities.getChannelIDFromBotType(type, bot) + "\"" + "," + "\"" + properties.getProperty((String) user) + "\"" + ")");
			else if (type == BotType.Mixer)
				MySQLConnection.executeUpdate(
						"INSERT INTO points(name, mixer_channel, amount) VALUES (" + "\"" + ((String) user) + "\"" + "," + "\"" + Utilities.getChannelNameFromBotType(type, bot) + "\"" + "," + "\"" + properties.getProperty((String) user) + "\"" + ")");
		}
	}
}
