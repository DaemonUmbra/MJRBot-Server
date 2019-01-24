package com.mjr.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.MJRBot.StorageType;
import com.mjr.sql.MySQLConnection;
import com.mjr.util.ConsoleUtil;
import com.mjr.util.Utilities;

public class Config extends FileBase {
	public static String fileName = "Config.properties";

	public static void createSettingIfDoesntExist(String setting, String value, String channelName) {
		try {
			if (!MySQLConnection.executeQueryNoOutput("SELECT * FROM config WHERE channel = " + "\"" + channelName + "\"" + " AND setting = " + "\"" + setting + "\"").next()) {
				ConsoleUtil.textToConsole("Creating config setting of: " + setting + " with the default value for " + channelName);
				setSetting(setting, value, channelName);
			}
		} catch (SQLException e) {
			MJRBot.logErrorMessage(e);
		}
	}

	public static void loadDefaultsDatabase(String channelName) throws IOException {
		createSettingIfDoesntExist("LinkWarning", "you are not allowed to post links with out permission!", channelName);
		createSettingIfDoesntExist("LanguageWarning", "you are not allowed to use that language in the chat!", channelName);
		createSettingIfDoesntExist("FollowerMessage", "has followed!", channelName);
		createSettingIfDoesntExist("SymbolWarning", "you are using to many symbols", channelName);
		createSettingIfDoesntExist("AnnouncementsDelay", "0", channelName);
		createSettingIfDoesntExist("GiveawayDelay", "0", channelName);
		createSettingIfDoesntExist("StartingPoints", "0", channelName);
		createSettingIfDoesntExist("AutoPointsDelay", "0", channelName);
		createSettingIfDoesntExist("EmoteWarning", "dont spam emotes!", channelName);
		createSettingIfDoesntExist("Commands", "false", channelName);
		createSettingIfDoesntExist("Games", "false", channelName);
		createSettingIfDoesntExist("Ranks", "false", channelName);
		createSettingIfDoesntExist("Points", "false", channelName);
		createSettingIfDoesntExist("Announcements", "false", channelName);
		createSettingIfDoesntExist("Badwords", "false", channelName);
		createSettingIfDoesntExist("LinkChecker", "false", channelName);
		createSettingIfDoesntExist("Emote", "false", channelName);
		createSettingIfDoesntExist("Symbol", "false", channelName);
		createSettingIfDoesntExist("SilentJoin", "true", channelName);
		createSettingIfDoesntExist("FollowerCheck", "false", channelName);
		createSettingIfDoesntExist("Quotes", "false", channelName);
		createSettingIfDoesntExist("MaxSymbols", "5", channelName);
		createSettingIfDoesntExist("MaxEmotes", "5", channelName);
		createSettingIfDoesntExist("MsgWhenCommandDoesntExist", "true", channelName);
		createSettingIfDoesntExist("MsgWhenCommandCantBeUsed", "false", channelName);
		createSettingIfDoesntExist("AnnouncementMessage1", "", channelName);
		createSettingIfDoesntExist("AnnouncementMessage2", "", channelName);
		createSettingIfDoesntExist("AnnouncementMessage3", "", channelName);
		createSettingIfDoesntExist("AnnouncementMessage4", "", channelName);
		createSettingIfDoesntExist("AnnouncementMessage5", "", channelName);
		createSettingIfDoesntExist("CommandsCooldownAmount", "20", channelName);
		createSettingIfDoesntExist("SelectedTimeZone", "Europe/London", channelName);
		createSettingIfDoesntExist("SubAlerts", "true", channelName);
		createSettingIfDoesntExist("ResubAlerts", "true", channelName);
		createSettingIfDoesntExist("GiftSubAlerts", "true", channelName);
		createSettingIfDoesntExist("HostingAlerts", "true", channelName);
		createSettingIfDoesntExist("RaidAlerts", "true", channelName);
		createSettingIfDoesntExist("BitsAlerts", "true", channelName);
		createSettingIfDoesntExist("FollowAlerts", "true", channelName);
		createSettingIfDoesntExist("AnnouncementsWhenOffline", "false", channelName);
		createSettingIfDoesntExist("TwitchChatLink", "false", channelName);
		createSettingIfDoesntExist("MixerChatLink", "false", channelName);
		createSettingIfDoesntExist("DiscordEnabled", "false", channelName);
		createSettingIfDoesntExist("DiscordChatLink", "false", channelName);
		createSettingIfDoesntExist("AutoPointsWhenOffline", "false", channelName);
		try {
			ResultSet set = MySQLConnection.executeQueryNoOutput("SELECT * FROM badwords WHERE channel = '" + channelName + "'");
			if (set == null || !set.next()) {
				String[] badwords = { "Fuck", "Shit", "Cunt", "Wanker", "Tosser", "Slag", "Slut", "Penis", "Cock", "Vagina", "Pussy", "Boobs", "Tits", "Ass", "Bastard", "Twat", "Nigger", "Bitch", "***", "Nigga" };
				for (int i = 0; i < badwords.length; i++) {
					MySQLConnection.executeUpdate("INSERT INTO badwords(channel, word) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + badwords[i] + "\"" + ")");
				}
			}
		} catch (SQLException e) {
			MJRBot.logErrorMessage(e);
		}
	}

	public static void loadDefaults(String channelName) throws IOException {
		File file = new File(MJRBot.filePath + channelName + File.separator + fileName);
		if (!file.exists()) {
			setSetting("LinkWarning", "you are not allowed to post links with out permission!", channelName);
			setSetting("LanguageWarning", "you are not allowed to use that language in the chat!", channelName);
			setSetting("FollowerMessage", "has followed!", channelName);
			setSetting("SymbolWarning", "you are using to many symbols", channelName);
			setSetting("AnnouncementsDelay", "0", channelName);
			setSetting("GiveawayDelay", "0", channelName);
			setSetting("StartingPoints", "0", channelName);
			setSetting("AutoPointsDelay", "0", channelName);
			setSetting("EmoteWarning", "dont spam emotes!", channelName);
			setSetting("Commands", "false", channelName);
			setSetting("Games", "false", channelName);
			setSetting("Ranks", "false", channelName);
			setSetting("Points", "false", channelName);
			setSetting("Announcements", "false", channelName);
			setSetting("Badwords", "false", channelName);
			setSetting("LinkChecker", "false", channelName);
			setSetting("Emote", "false", channelName);
			setSetting("Symbol", "false", channelName);
			setSetting("SilentJoin", "true", channelName);
			setSetting("FollowerCheck", "false", channelName);
			setSetting("Quotes", "false", channelName);
			setSetting("MaxSymbols", "5", channelName);
			setSetting("MaxEmotes", "5", channelName);
			setSetting("MsgWhenCommandDoesntExist", "true", channelName);
			setSetting("MsgWhenCommandCantBeUsed", "false", channelName);
			setSetting("AnnouncementMessage1", "", channelName);
			setSetting("AnnouncementMessage2", "", channelName);
			setSetting("AnnouncementMessage3", "", channelName);
			setSetting("AnnouncementMessage4", "", channelName);
			setSetting("AnnouncementMessage5", "", channelName);
			setSetting("CommandsCooldownAmount", "20", channelName);
			setSetting("SelectedTimeZone", "Europe/London", channelName);
			setSetting("SubAlerts", "true", channelName);
			setSetting("ResubAlerts", "true", channelName);
			setSetting("GiftSubAlerts", "true", channelName);
			setSetting("HostingAlerts", "true", channelName);
			setSetting("RaidAlerts", "true", channelName);
			setSetting("BitsAlerts", "true", channelName);
			setSetting("FollowAlerts", "true", channelName);
			setSetting("AnnouncementsWhenOffline", "false", channelName);
			setSetting("TwitchChatLink", "false", channelName);
			setSetting("MixerChatLink", "false", channelName);
			setSetting("DiscordEnabled", "false", channelName);
			setSetting("DiscordChatLink", "false", channelName);
			setSetting("AutoPointsWhenOffline", "false", channelName);
		}
	}

	public static String getSetting(String setting, BotType type, Object bot) {
		if(type == BotType.Twitch)
			return getSetting(setting, Utilities.getChannelIDFromBotType(type, bot), false);
		else if(type == BotType.Mixer)
			return getSetting(setting, Utilities.getChannelNameFromBotType(type, bot), false);
		return null;
	}
	
	public static String getSetting(String setting, int channelID) {
		return getSetting(setting, channelID, false);
	}

	public static String getSetting(String setting, int channelID, boolean overrideStorageType) {
		if (MJRBot.storageType == StorageType.File || overrideStorageType) {
			return load(channelID, fileName).getProperty(setting);
		} else {
			ResultSet result = MySQLConnection.executeQueryNoOutput("SELECT value FROM config WHERE twitch_channel_id = " + "\"" + channelID + "\"" + " AND setting = " + "\"" + setting + "\"");
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
		return null;
	}
	
	public static String getSetting(String setting, String channelName) {
		return getSetting(setting, channelName, false);
	}

	public static String getSetting(String setting, String channelName, boolean overrideStorageType) {
		if (MJRBot.storageType == StorageType.File || overrideStorageType) {
			return load(channelName, fileName).getProperty(setting);
		} else {
			ResultSet result = MySQLConnection.executeQueryNoOutput("SELECT value FROM config WHERE channel = " + "\"" + channelName + "\"" + " AND setting = " + "\"" + setting + "\"");
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
		return null;
	}

	@SuppressWarnings("deprecation")
	public static void setSetting(String setting, String value, String channelName) {
		if (MJRBot.storageType == StorageType.File) {
			File file = loadFile(channelName, fileName);
			Properties properties = load(channelName, fileName);
			file = loadFile(channelName, fileName);
			if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
				if (value == "true")
					ConsoleUtil.textToConsole(setting + " has been has enabled!");
				else
					ConsoleUtil.textToConsole(setting + " has been has disabled!");

				properties.setProperty(setting, value);
				try {
					properties.save(new FileOutputStream(file), null);
				} catch (IOException e) {
					MJRBot.logErrorMessage(e);
				}
			} else {
				properties.setProperty(setting, value);
				try {
					properties.store(new FileOutputStream(file), null);
				} catch (IOException e) {
					MJRBot.logErrorMessage(e);
				}
			}
		} else {
			if (getSetting(setting, channelName) == null)
				MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + setting + "\"" + "," + "\"" + value + "\"" + ")");
			else
				MySQLConnection.executeUpdate("UPDATE config SET setting=" + "\"" + setting + "\"" + ",value=" + "\"" + value + "\"" + " WHERE channel = " + "\"" + channelName + "\"" + " AND setting = " + "\"" + setting + "\"");
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
