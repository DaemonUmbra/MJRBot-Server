package com.mjr.mjrbot.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.storage.sql.MySQLConnection;
import com.mjr.mjrbot.util.ConsoleUtil;
import com.mjr.mjrbot.util.MJRBotUtilities;

public class Config extends FileBase {
	public static String fileName = "Config.properties";

	public static void createSettingIfDoesntExist(String setting, String value, BotType type, String channel, int channelID) {
		try {
			if (type == BotType.Twitch) {
				if (!MySQLConnection.executeQuery("SELECT * FROM config WHERE twitch_channel_id = " + "\"" + channelID + "\"" + " AND setting = " + "\"" + setting + "\"", false).next()) {
					ConsoleUtil.textToConsole("Creating config setting of: " + setting + " with the default value for " + channelID);
					setSetting(setting, value, type, channel, channelID);
				}
			} else if (type == BotType.Mixer) {
				if (!MySQLConnection.executeQuery("SELECT * FROM config WHERE mixer_channel = " + "\"" + channel + "\"" + " AND setting = " + "\"" + setting + "\"", false).next()) {
					ConsoleUtil.textToConsole("Creating config setting of: " + setting + " with the default value for " + channel);
					setSetting(setting, value, type, channel, channelID);
				}
			}
		} catch (SQLException e) {
			MJRBotUtilities.logErrorMessage(e);
		}
	}

	public static void loadDefaultsDatabase(BotType type, String channel, int channelID) throws IOException {
		createSettingIfDoesntExist("LinkWarning", "you are not allowed to post links with out permission!", type, channel, channelID);
		createSettingIfDoesntExist("LanguageWarning", "you are not allowed to use that language in the chat!", type, channel, channelID);
		createSettingIfDoesntExist("FollowerMessage", "has followed!", type, channel, channelID);
		createSettingIfDoesntExist("SymbolWarning", "you are using to many symbols", type, channel, channelID);
		createSettingIfDoesntExist("AnnouncementsDelay", "0", type, channel, channelID);
		createSettingIfDoesntExist("GiveawayDelay", "0", type, channel, channelID);
		createSettingIfDoesntExist("StartingPoints", "0", type, channel, channelID);
		createSettingIfDoesntExist("AutoPointsDelay", "0", type, channel, channelID);
		createSettingIfDoesntExist("EmoteWarning", "dont spam emotes!", type, channel, channelID);
		createSettingIfDoesntExist("Commands", "false", type, channel, channelID);
		createSettingIfDoesntExist("Games", "false", type, channel, channelID);
		createSettingIfDoesntExist("Ranks", "false", type, channel, channelID);
		createSettingIfDoesntExist("Points", "false", type, channel, channelID);
		createSettingIfDoesntExist("Announcements", "false", type, channel, channelID);
		createSettingIfDoesntExist("Badwords", "false", type, channel, channelID);
		createSettingIfDoesntExist("LinkChecker", "false", type, channel, channelID);
		createSettingIfDoesntExist("Emote", "false", type, channel, channelID);
		createSettingIfDoesntExist("Symbol", "false", type, channel, channelID);
		createSettingIfDoesntExist("SilentJoin", "true", type, channel, channelID);
		createSettingIfDoesntExist("FollowerCheck", "false", type, channel, channelID);
		createSettingIfDoesntExist("Quotes", "false", type, channel, channelID);
		createSettingIfDoesntExist("MaxSymbols", "5", type, channel, channelID);
		createSettingIfDoesntExist("MaxEmotes", "5", type, channel, channelID);
		createSettingIfDoesntExist("MsgWhenCommandDoesntExist", "true", type, channel, channelID);
		createSettingIfDoesntExist("MsgWhenCommandCantBeUsed", "false", type, channel, channelID);
		createSettingIfDoesntExist("AnnouncementMessage1", "", type, channel, channelID);
		createSettingIfDoesntExist("AnnouncementMessage2", "", type, channel, channelID);
		createSettingIfDoesntExist("AnnouncementMessage3", "", type, channel, channelID);
		createSettingIfDoesntExist("AnnouncementMessage4", "", type, channel, channelID);
		createSettingIfDoesntExist("AnnouncementMessage5", "", type, channel, channelID);
		createSettingIfDoesntExist("CommandsCooldownAmount", "20", type, channel, channelID);
		createSettingIfDoesntExist("SelectedTimeZone", "Europe/London", type, channel, channelID);
		createSettingIfDoesntExist("SubAlerts", "true", type, channel, channelID);
		createSettingIfDoesntExist("ResubAlerts", "true", type, channel, channelID);
		createSettingIfDoesntExist("GiftSubAlerts", "true", type, channel, channelID);
		createSettingIfDoesntExist("HostingAlerts", "true", type, channel, channelID);
		createSettingIfDoesntExist("RaidAlerts", "true", type, channel, channelID);
		createSettingIfDoesntExist("BitsAlerts", "true", type, channel, channelID);
		createSettingIfDoesntExist("FollowAlerts", "true", type, channel, channelID);
		createSettingIfDoesntExist("AnnouncementsWhenOffline", "false", type, channel, channelID);
		createSettingIfDoesntExist("TwitchChatLink", "false", type, channel, channelID);
		createSettingIfDoesntExist("MixerChatLink", "false", type, channel, channelID);
		createSettingIfDoesntExist("DiscordEnabled", "false", type, channel, channelID);
		createSettingIfDoesntExist("DiscordChatLink", "false", type, channel, channelID);
		createSettingIfDoesntExist("AutoPointsWhenOffline", "false", type, channel, channelID);
		try {
			ResultSet set = null;
			if (type == BotType.Twitch)
				set = MySQLConnection.executeQuery("SELECT * FROM badwords WHERE twitch_channel_id = '" + channelID + "'", false);
			else if (type == BotType.Mixer)
				set = MySQLConnection.executeQuery("SELECT * FROM badwords WHERE mixer_channel = '" + channel + "'", false);
			if (set == null || !set.next()) {
				String[] badwords = { "Fuck", "Shit", "Cunt", "Wanker", "Tosser", "Slag", "Slut", "Penis", "Cock", "Vagina", "Pussy", "Boobs", "Tits", "Ass", "Bastard", "Twat", "Nigger", "Bitch", "***", "Nigga" };
				for (int i = 0; i < badwords.length; i++) {
					if (type == BotType.Twitch)
						MySQLConnection.executeUpdate("INSERT INTO badwords(twitch_channel_id, word) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + badwords[i] + "\"" + ")");
					else if (type == BotType.Mixer)
						MySQLConnection.executeUpdate("INSERT INTO badwords(mixer_channel, word) VALUES (" + "\"" + channel + "\"" + "," + "\"" + badwords[i] + "\"" + ")");
				}
			}
		} catch (SQLException e) {
			MJRBotUtilities.logErrorMessage(e);
		}
	}

	public static void loadDefaults(BotType type, String channel, int channelID) throws IOException {
		File file = null;
		if (type == BotType.Twitch)
			file = new File(MJRBot.filePath + channelID + File.separator + fileName);
		else if (type == BotType.Mixer)
			file = new File(MJRBot.filePath + channel + File.separator + fileName);
		if (!file.exists()) {
			setSetting("LinkWarning", "you are not allowed to post links with out permission!", type, channel, channelID);
			setSetting("LanguageWarning", "you are not allowed to use that language in the chat!", type, channel, channelID);
			setSetting("FollowerMessage", "has followed!", type, channel, channelID);
			setSetting("SymbolWarning", "you are using to many symbols", type, channel, channelID);
			setSetting("AnnouncementsDelay", "0", type, channel, channelID);
			setSetting("GiveawayDelay", "0", type, channel, channelID);
			setSetting("StartingPoints", "0", type, channel, channelID);
			setSetting("AutoPointsDelay", "0", type, channel, channelID);
			setSetting("EmoteWarning", "dont spam emotes!", type, channel, channelID);
			setSetting("Commands", "false", type, channel, channelID);
			setSetting("Games", "false", type, channel, channelID);
			setSetting("Ranks", "false", type, channel, channelID);
			setSetting("Points", "false", type, channel, channelID);
			setSetting("Announcements", "false", type, channel, channelID);
			setSetting("Badwords", "false", type, channel, channelID);
			setSetting("LinkChecker", "false", type, channel, channelID);
			setSetting("Emote", "false", type, channel, channelID);
			setSetting("Symbol", "false", type, channel, channelID);
			setSetting("SilentJoin", "true", type, channel, channelID);
			setSetting("FollowerCheck", "false", type, channel, channelID);
			setSetting("Quotes", "false", type, channel, channelID);
			setSetting("MaxSymbols", "5", type, channel, channelID);
			setSetting("MaxEmotes", "5", type, channel, channelID);
			setSetting("MsgWhenCommandDoesntExist", "true", type, channel, channelID);
			setSetting("MsgWhenCommandCantBeUsed", "false", type, channel, channelID);
			setSetting("AnnouncementMessage1", "", type, channel, channelID);
			setSetting("AnnouncementMessage2", "", type, channel, channelID);
			setSetting("AnnouncementMessage3", "", type, channel, channelID);
			setSetting("AnnouncementMessage4", "", type, channel, channelID);
			setSetting("AnnouncementMessage5", "", type, channel, channelID);
			setSetting("CommandsCooldownAmount", "20", type, channel, channelID);
			setSetting("SelectedTimeZone", "Europe/London", type, channel, channelID);
			setSetting("SubAlerts", "true", type, channel, channelID);
			setSetting("ResubAlerts", "true", type, channel, channelID);
			setSetting("GiftSubAlerts", "true", type, channel, channelID);
			setSetting("HostingAlerts", "true", type, channel, channelID);
			setSetting("RaidAlerts", "true", type, channel, channelID);
			setSetting("BitsAlerts", "true", type, channel, channelID);
			setSetting("FollowAlerts", "true", type, channel, channelID);
			setSetting("AnnouncementsWhenOffline", "false", type, channel, channelID);
			setSetting("TwitchChatLink", "false", type, channel, channelID);
			setSetting("MixerChatLink", "false", type, channel, channelID);
			setSetting("DiscordEnabled", "false", type, channel, channelID);
			setSetting("DiscordChatLink", "false", type, channel, channelID);
			setSetting("AutoPointsWhenOffline", "false", type, channel, channelID);
		}
	}

	public static String getSetting(String setting, BotType type, Object bot) {
		if (type == BotType.Twitch)
			return getSetting(setting, MJRBotUtilities.getChannelIDFromBotType(type, bot), false);
		else if (type == BotType.Mixer)
			return getSetting(setting, MJRBotUtilities.getChannelNameFromBotType(type, bot), false);
		return null;
	}

	public static String getSetting(String setting, int channelID) {
		return getSetting(setting, channelID, false);
	}

	public static String getSetting(String setting, int channelID, boolean overrideStorageType) {
		if (MJRBot.storageType == StorageType.File || overrideStorageType) {
			return load(channelID, fileName).getProperty(setting);
		} else {
			ResultSet result = MySQLConnection.executeQuery("SELECT value FROM config WHERE twitch_channel_id = " + "\"" + channelID + "\"" + " AND setting = " + "\"" + setting + "\"", false);
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
				MJRBotUtilities.logErrorMessage(e);
			}
		}
		return null;
	}

	public static String getSetting(String setting, String channelName) {
		return getSetting(setting, channelName, false);
	}

	public static String getSetting(String setting, String channelName, boolean overrideStorageType) {
		if (MJRBot.storageType == StorageType.File || overrideStorageType) {
			return load(channelName, fileName).getProperty(setting);
		} else {
			ResultSet result = MySQLConnection.executeQuery("SELECT value FROM config WHERE mixer_channel = " + "\"" + channelName + "\"" + " AND setting = " + "\"" + setting + "\"", false);
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
				MJRBotUtilities.logErrorMessage(e);
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static void setSetting(String setting, String value, BotType type, String channel, int channelID) {
		if (MJRBot.storageType == StorageType.File) {
			File file = null;
			if (type == BotType.Twitch)
				file = loadFile(channelID, fileName);
			else if (type == BotType.Mixer)
				file = loadFile(channel, fileName);
			Properties properties = null;
			if (type == BotType.Twitch)
				properties = load(channelID, fileName);
			else if (type == BotType.Mixer)
				properties = load(channel, fileName);
			if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
				if (value == "true")
					ConsoleUtil.textToConsole(setting + " has been has enabled!");
				else
					ConsoleUtil.textToConsole(setting + " has been has disabled!");

				properties.setProperty(setting, value);
				try {
					properties.save(new FileOutputStream(file), null);
				} catch (IOException e) {
					MJRBotUtilities.logErrorMessage(e);
				}
			} else {
				properties.setProperty(setting, value);
				try {
					properties.store(new FileOutputStream(file), null);
				} catch (IOException e) {
					MJRBotUtilities.logErrorMessage(e);
				}
			}
		} else {
			if (type == BotType.Twitch) {
				if (getSetting(setting, channelID) == null)
					MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + setting + "\"" + "," + "\"" + value + "\"" + ")");
				else
					MySQLConnection.executeUpdate("UPDATE config SET setting=" + "\"" + setting + "\"" + ",value=" + "\"" + value + "\"" + " WHERE twitch_channel_id = " + "\"" + channelID + "\"" + " AND setting = " + "\"" + setting + "\"");
			}
			if (type == BotType.Mixer) {
				if (getSetting(setting, channel) == null)
					MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channel + "\"" + "," + "\"" + setting + "\"" + "," + "\"" + value + "\"" + ")");
				else
					MySQLConnection.executeUpdate("UPDATE config SET setting=" + "\"" + setting + "\"" + ",value=" + "\"" + value + "\"" + " WHERE mixer_channel = " + "\"" + channel + "\"" + " AND setting = " + "\"" + setting + "\"");
			}

		}
	}

	public static void migrateFile(String channelName) {
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "LinkWarning" + "\"" + "," + "\"" + getSetting("LinkWarning", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "LanguageWarning" + "\"" + "," + "\"" + getSetting("LanguageWarning", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "FollowerMessage" + "\"" + "," + "\"" + getSetting("FollowerMessage", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "SymbolWarning" + "\"" + "," + "\"" + getSetting("SymbolWarning", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "AnnouncementsDelay" + "\"" + "," + "\"" + getSetting("AnnouncementsDelay", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "GiveawayDelay" + "\"" + "," + "\"" + getSetting("GiveawayDelay", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "StartingPoints" + "\"" + "," + "\"" + getSetting("StartingPoints", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "AutoPointsDelay" + "\"" + "," + "\"" + getSetting("AutoPointsDelay", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "EmoteWarning" + "\"" + "," + "\"" + getSetting("EmoteWarning", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "Commands" + "\"" + "," + "\"" + getSetting("Commands", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "Games" + "\"" + "," + "\"" + getSetting("Games", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "Ranks" + "\"" + "," + "\"" + getSetting("Ranks", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "Points" + "\"" + "," + "\"" + getSetting("Points", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "Announcements" + "\"" + "," + "\"" + getSetting("Announcements", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "Badwords" + "\"" + "," + "\"" + getSetting("Badwords", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "LinkChecker" + "\"" + "," + "\"" + getSetting("LinkChecker", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "Emote" + "\"" + "," + "\"" + getSetting("Emote", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "Symbol" + "\"" + "," + "\"" + getSetting("Symbol", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "SilentJoin" + "\"" + "," + "\"" + getSetting("SilentJoin", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "FollowerCheck" + "\"" + "," + "\"" + getSetting("FollowerCheck", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "Quotes" + "\"" + "," + "\"" + getSetting("Quotes", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "MaxSymbols" + "\"" + "," + "\"" + getSetting("MaxSymbols", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "MaxEmotes" + "\"" + "," + "\"" + getSetting("MaxEmotes", channelName) + "\"" + ")");
		MySQLConnection
				.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "MsgWhenCommandDoesntExist" + "\"" + "," + "\"" + getSetting("MsgWhenCommandDoesntExist", channelName) + "\"" + ")");
		MySQLConnection
				.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "MsgWhenCommandCantBeUsed" + "\"" + "," + "\"" + getSetting("MsgWhenCommandCantBeUsed", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "AnnouncementMessage1" + "\"" + "," + "\"" + getSetting("AnnouncementMessage1", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "AnnouncementMessage2" + "\"" + "," + "\"" + getSetting("AnnouncementMessage2", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "AnnouncementMessage3" + "\"" + "," + "\"" + getSetting("AnnouncementMessage3", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "AnnouncementMessage4" + "\"" + "," + "\"" + getSetting("AnnouncementMessage4", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "AnnouncementMessage5" + "\"" + "," + "\"" + getSetting("AnnouncementMessage5", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "CommandsCooldownAmount" + "\"" + "," + "\"" + getSetting("CommandsCooldownAmount", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "SelectedTimeZone" + "\"" + "," + "\"" + getSetting("SelectedTimeZone", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "SubAlerts" + "\"" + "," + "\"" + getSetting("SubAlerts", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "ResubAlerts" + "\"" + "," + "\"" + getSetting("ResubAlerts", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "GiftSubAlerts" + "\"" + "," + "\"" + getSetting("GiftSubAlerts", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "HostingAlerts" + "\"" + "," + "\"" + getSetting("HostingAlerts", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "RaidAlerts" + "\"" + "," + "\"" + getSetting("RaidAlerts", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "BitsAlerts" + "\"" + "," + "\"" + getSetting("BitsAlerts", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "FollowAlerts" + "\"" + "," + "\"" + getSetting("FollowAlerts", channelName) + "\"" + ")");
		MySQLConnection
				.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "AnnouncementsWhenOffline" + "\"" + "," + "\"" + getSetting("AnnouncementsWhenOffline", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "TwitchChatLink" + "\"" + "," + "\"" + getSetting("TwitchChatLink", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "MixerChatLink" + "\"" + "," + "\"" + getSetting("MixerChatLink", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "DiscordEnabled" + "\"" + "," + "\"" + getSetting("DiscordEnabled", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "DiscordChatLink" + "\"" + "," + "\"" + getSetting("DiscordChatLink", channelName) + "\"" + ")");
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + "AutoPointsWhenOffline" + "\"" + "," + "\"" + getSetting("AutoPointsWhenOffline", channelName) + "\"" + ")");
	}
}
