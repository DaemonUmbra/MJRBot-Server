package com.mjr.mjrbot.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.PlatformType;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.storage.sql.MySQLConnection;
import com.mjr.mjrbot.util.ConsoleUtil;
import com.mjr.mjrbot.util.MJRBotUtilities;

public class ChannelConfigManager extends FileBase {
	public static String fileName = "Config.properties";

	public static void createSettingIfDoesntExist(String setting, String value, BotType type, String channelMixer, int channelID) {
		try {
			if (type == BotType.Twitch) {
				if (!MySQLConnection.executeQuery("SELECT * FROM config WHERE twitch_channel_id = " + "\"" + channelID + "\"" + " AND setting = " + "\"" + setting + "\"", false).next()) {
					ConsoleUtil.textToConsole("Creating config setting of: " + setting + " with the default value for " + channelID);
					setSetting(setting, value, type, channelMixer, channelID);
				}
			} else if (type == BotType.Mixer) {
				if (!MySQLConnection.executeQuery("SELECT * FROM config WHERE mixer_channel = " + "\"" + channelMixer + "\"" + " AND setting = " + "\"" + setting + "\"", false).next()) {
					ConsoleUtil.textToConsole("Creating config setting of: " + setting + " with the default value for " + channelMixer);
					setSetting(setting, value, type, channelMixer, channelID);
				}
			}
		} catch (SQLException e) {
			MJRBotUtilities.logErrorMessage(e);
		}
	}

	public static void loadDefaultsDatabase(BotType type, String channelMixer, int channelID) throws IOException {
		createSettingIfDoesntExist("LinkWarning", "you are not allowed to post links with out permission!", type, channelMixer, channelID);
		createSettingIfDoesntExist("LanguageWarning", "you are not allowed to use that language in the chat!", type, channelMixer, channelID);
		createSettingIfDoesntExist("FollowerMessage", "has followed!", type, channelMixer, channelID);
		createSettingIfDoesntExist("SymbolWarning", "you are using to many symbols", type, channelMixer, channelID);
		createSettingIfDoesntExist("AnnouncementsDelay", "0", type, channelMixer, channelID);
		createSettingIfDoesntExist("GiveawayDelay", "0", type, channelMixer, channelID);
		createSettingIfDoesntExist("StartingPoints", "0", type, channelMixer, channelID);
		createSettingIfDoesntExist("AutoPointsDelay", "0", type, channelMixer, channelID);
		createSettingIfDoesntExist("EmoteWarning", "dont spam emotes!", type, channelMixer, channelID);
		createSettingIfDoesntExist("Commands", "false", type, channelMixer, channelID);
		createSettingIfDoesntExist("Games", "false", type, channelMixer, channelID);
		createSettingIfDoesntExist("Ranks", "false", type, channelMixer, channelID);
		createSettingIfDoesntExist("Points", "false", type, channelMixer, channelID);
		createSettingIfDoesntExist("Announcements", "false", type, channelMixer, channelID);
		createSettingIfDoesntExist("Badwords", "false", type, channelMixer, channelID);
		createSettingIfDoesntExist("LinkChecker", "false", type, channelMixer, channelID);
		createSettingIfDoesntExist("Emote", "false", type, channelMixer, channelID);
		createSettingIfDoesntExist("Symbol", "false", type, channelMixer, channelID);
		createSettingIfDoesntExist("SilentJoin", "true", type, channelMixer, channelID);
		createSettingIfDoesntExist("FollowerCheck", "false", type, channelMixer, channelID);
		createSettingIfDoesntExist("Quotes", "false", type, channelMixer, channelID);
		createSettingIfDoesntExist("MaxSymbols", "5", type, channelMixer, channelID);
		createSettingIfDoesntExist("MaxEmotes", "5", type, channelMixer, channelID);
		createSettingIfDoesntExist("MsgWhenCommandDoesntExist", "true", type, channelMixer, channelID);
		createSettingIfDoesntExist("MsgWhenCommandCantBeUsed", "false", type, channelMixer, channelID);
		createSettingIfDoesntExist("AnnouncementMessage1", "", type, channelMixer, channelID);
		createSettingIfDoesntExist("AnnouncementMessage2", "", type, channelMixer, channelID);
		createSettingIfDoesntExist("AnnouncementMessage3", "", type, channelMixer, channelID);
		createSettingIfDoesntExist("AnnouncementMessage4", "", type, channelMixer, channelID);
		createSettingIfDoesntExist("AnnouncementMessage5", "", type, channelMixer, channelID);
		createSettingIfDoesntExist("CommandsCooldownAmount", "20", type, channelMixer, channelID);
		createSettingIfDoesntExist("SelectedTimeZone", "Europe/London", type, channelMixer, channelID);
		createSettingIfDoesntExist("SubAlerts", "true", type, channelMixer, channelID);
		createSettingIfDoesntExist("ResubAlerts", "true", type, channelMixer, channelID);
		createSettingIfDoesntExist("GiftSubAlerts", "true", type, channelMixer, channelID);
		createSettingIfDoesntExist("HostingAlerts", "true", type, channelMixer, channelID);
		createSettingIfDoesntExist("RaidAlerts", "true", type, channelMixer, channelID);
		createSettingIfDoesntExist("BitsAlerts", "true", type, channelMixer, channelID);
		createSettingIfDoesntExist("FollowAlerts", "true", type, channelMixer, channelID);
		createSettingIfDoesntExist("AnnouncementsWhenOffline", "false", type, channelMixer, channelID);
		createSettingIfDoesntExist("TwitchChatLink", "false", type, channelMixer, channelID);
		createSettingIfDoesntExist("MixerChatLink", "false", type, channelMixer, channelID);
		createSettingIfDoesntExist("DiscordEnabled", "false", type, channelMixer, channelID);
		createSettingIfDoesntExist("DiscordChatLink", "false", type, channelMixer, channelID);
		createSettingIfDoesntExist("AutoPointsWhenOffline", "false", type, channelMixer, channelID);
		try {
			ResultSet set = null;
			if (type == BotType.Twitch)
				set = MySQLConnection.executeQuery("SELECT * FROM badwords WHERE twitch_channel_id = '" + channelID + "'", false);
			else if (type == BotType.Mixer)
				set = MySQLConnection.executeQuery("SELECT * FROM badwords WHERE mixer_channel = '" + channelMixer + "'", false);
			if (set == null || !set.next()) {
				String[] badwords = { "Fuck", "Shit", "Cunt", "Wanker", "Tosser", "Slag", "Slut", "Penis", "Cock", "Vagina", "Pussy", "Boobs", "Tits", "Ass", "Bastard", "Twat", "Nigger", "Bitch", "***", "Nigga" };
				for (int i = 0; i < badwords.length; i++) {
					if (type == BotType.Twitch)
						MySQLConnection.executeUpdate("INSERT INTO badwords(twitch_channel_id, word) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + badwords[i] + "\"" + ")");
					else if (type == BotType.Mixer)
						MySQLConnection.executeUpdate("INSERT INTO badwords(mixer_channel, word) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + badwords[i] + "\"" + ")");
				}
			}
		} catch (SQLException e) {
			MJRBotUtilities.logErrorMessage(e);
		}
	}

	public static void loadDefaults(BotType type, String channelMixer, int channelID) throws IOException {
		File file = null;
		if (type == BotType.Twitch)
			file = new File(MJRBot.filePath + channelID + File.separator + fileName);
		else if (type == BotType.Mixer)
			file = new File(MJRBot.filePath + channelMixer + File.separator + fileName);
		if (!file.exists()) {
			setSetting("LinkWarning", "you are not allowed to post links with out permission!", type, channelMixer, channelID);
			setSetting("LanguageWarning", "you are not allowed to use that language in the chat!", type, channelMixer, channelID);
			setSetting("FollowerMessage", "has followed!", type, channelMixer, channelID);
			setSetting("SymbolWarning", "you are using to many symbols", type, channelMixer, channelID);
			setSetting("AnnouncementsDelay", "0", type, channelMixer, channelID);
			setSetting("GiveawayDelay", "0", type, channelMixer, channelID);
			setSetting("StartingPoints", "0", type, channelMixer, channelID);
			setSetting("AutoPointsDelay", "0", type, channelMixer, channelID);
			setSetting("EmoteWarning", "dont spam emotes!", type, channelMixer, channelID);
			setSetting("Commands", "false", type, channelMixer, channelID);
			setSetting("Games", "false", type, channelMixer, channelID);
			setSetting("Ranks", "false", type, channelMixer, channelID);
			setSetting("Points", "false", type, channelMixer, channelID);
			setSetting("Announcements", "false", type, channelMixer, channelID);
			setSetting("Badwords", "false", type, channelMixer, channelID);
			setSetting("LinkChecker", "false", type, channelMixer, channelID);
			setSetting("Emote", "false", type, channelMixer, channelID);
			setSetting("Symbol", "false", type, channelMixer, channelID);
			setSetting("SilentJoin", "true", type, channelMixer, channelID);
			setSetting("FollowerCheck", "false", type, channelMixer, channelID);
			setSetting("Quotes", "false", type, channelMixer, channelID);
			setSetting("MaxSymbols", "5", type, channelMixer, channelID);
			setSetting("MaxEmotes", "5", type, channelMixer, channelID);
			setSetting("MsgWhenCommandDoesntExist", "true", type, channelMixer, channelID);
			setSetting("MsgWhenCommandCantBeUsed", "false", type, channelMixer, channelID);
			setSetting("AnnouncementMessage1", "", type, channelMixer, channelID);
			setSetting("AnnouncementMessage2", "", type, channelMixer, channelID);
			setSetting("AnnouncementMessage3", "", type, channelMixer, channelID);
			setSetting("AnnouncementMessage4", "", type, channelMixer, channelID);
			setSetting("AnnouncementMessage5", "", type, channelMixer, channelID);
			setSetting("CommandsCooldownAmount", "20", type, channelMixer, channelID);
			setSetting("SelectedTimeZone", "Europe/London", type, channelMixer, channelID);
			setSetting("SubAlerts", "true", type, channelMixer, channelID);
			setSetting("ResubAlerts", "true", type, channelMixer, channelID);
			setSetting("GiftSubAlerts", "true", type, channelMixer, channelID);
			setSetting("HostingAlerts", "true", type, channelMixer, channelID);
			setSetting("RaidAlerts", "true", type, channelMixer, channelID);
			setSetting("BitsAlerts", "true", type, channelMixer, channelID);
			setSetting("FollowAlerts", "true", type, channelMixer, channelID);
			setSetting("AnnouncementsWhenOffline", "false", type, channelMixer, channelID);
			setSetting("TwitchChatLink", "false", type, channelMixer, channelID);
			setSetting("MixerChatLink", "false", type, channelMixer, channelID);
			setSetting("DiscordEnabled", "false", type, channelMixer, channelID);
			setSetting("DiscordChatLink", "false", type, channelMixer, channelID);
			setSetting("AutoPointsWhenOffline", "false", type, channelMixer, channelID);
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
	public static void setSetting(String setting, String value, BotType type, String channelMixer, int channelID) {
		if (MJRBot.storageType == StorageType.File) {
			File file = null;
			if (type == BotType.Twitch)
				file = loadFile(channelID, fileName);
			else if (type == BotType.Mixer)
				file = loadFile(channelMixer, fileName);
			Properties properties = null;
			if (type == BotType.Twitch)
				properties = load(channelID, fileName);
			else if (type == BotType.Mixer)
				properties = load(channelMixer, fileName);
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
				if (getSetting(setting, channelMixer) == null)
					MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + setting + "\"" + "," + "\"" + value + "\"" + ")");
				else
					MySQLConnection.executeUpdate("UPDATE config SET setting=" + "\"" + setting + "\"" + ",value=" + "\"" + value + "\"" + " WHERE mixer_channel = " + "\"" + channelMixer + "\"" + " AND setting = " + "\"" + setting + "\"");
			}

		}
	}

	public static void migrateFile(int channelID, String channelMixer, PlatformType platform) {
		if(platform == PlatformType.TWITCH) {
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "LinkWarning" + "\"" + "," + "\"" + getSetting("LinkWarning", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "LanguageWarning" + "\"" + "," + "\"" + getSetting("LanguageWarning", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "FollowerMessage" + "\"" + "," + "\"" + getSetting("FollowerMessage", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "SymbolWarning" + "\"" + "," + "\"" + getSetting("SymbolWarning", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "AnnouncementsDelay" + "\"" + "," + "\"" + getSetting("AnnouncementsDelay", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "GiveawayDelay" + "\"" + "," + "\"" + getSetting("GiveawayDelay", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "StartingPoints" + "\"" + "," + "\"" + getSetting("StartingPoints", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "AutoPointsDelay" + "\"" + "," + "\"" + getSetting("AutoPointsDelay", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "EmoteWarning" + "\"" + "," + "\"" + getSetting("EmoteWarning", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "Commands" + "\"" + "," + "\"" + getSetting("Commands", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "Games" + "\"" + "," + "\"" + getSetting("Games", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "Ranks" + "\"" + "," + "\"" + getSetting("Ranks", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "Points" + "\"" + "," + "\"" + getSetting("Points", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "Announcements" + "\"" + "," + "\"" + getSetting("Announcements", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "Badwords" + "\"" + "," + "\"" + getSetting("Badwords", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "LinkChecker" + "\"" + "," + "\"" + getSetting("LinkChecker", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "Emote" + "\"" + "," + "\"" + getSetting("Emote", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "Symbol" + "\"" + "," + "\"" + getSetting("Symbol", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "SilentJoin" + "\"" + "," + "\"" + getSetting("SilentJoin", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "FollowerCheck" + "\"" + "," + "\"" + getSetting("FollowerCheck", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "Quotes" + "\"" + "," + "\"" + getSetting("Quotes", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "MaxSymbols" + "\"" + "," + "\"" + getSetting("MaxSymbols", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "MaxEmotes" + "\"" + "," + "\"" + getSetting("MaxEmotes", channelID) + "\"" + ")");
			MySQLConnection
					.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "MsgWhenCommandDoesntExist" + "\"" + "," + "\"" + getSetting("MsgWhenCommandDoesntExist", channelID) + "\"" + ")");
			MySQLConnection
					.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "MsgWhenCommandCantBeUsed" + "\"" + "," + "\"" + getSetting("MsgWhenCommandCantBeUsed", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "AnnouncementMessage1" + "\"" + "," + "\"" + getSetting("AnnouncementMessage1", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "AnnouncementMessage2" + "\"" + "," + "\"" + getSetting("AnnouncementMessage2", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "AnnouncementMessage3" + "\"" + "," + "\"" + getSetting("AnnouncementMessage3", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "AnnouncementMessage4" + "\"" + "," + "\"" + getSetting("AnnouncementMessage4", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "AnnouncementMessage5" + "\"" + "," + "\"" + getSetting("AnnouncementMessage5", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "CommandsCooldownAmount" + "\"" + "," + "\"" + getSetting("CommandsCooldownAmount", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "SelectedTimeZone" + "\"" + "," + "\"" + getSetting("SelectedTimeZone", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "SubAlerts" + "\"" + "," + "\"" + getSetting("SubAlerts", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "ResubAlerts" + "\"" + "," + "\"" + getSetting("ResubAlerts", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "GiftSubAlerts" + "\"" + "," + "\"" + getSetting("GiftSubAlerts", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "HostingAlerts" + "\"" + "," + "\"" + getSetting("HostingAlerts", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "RaidAlerts" + "\"" + "," + "\"" + getSetting("RaidAlerts", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "BitsAlerts" + "\"" + "," + "\"" + getSetting("BitsAlerts", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "FollowAlerts" + "\"" + "," + "\"" + getSetting("FollowAlerts", channelID) + "\"" + ")");
			MySQLConnection
					.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "AnnouncementsWhenOffline" + "\"" + "," + "\"" + getSetting("AnnouncementsWhenOffline", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "TwitchChatLink" + "\"" + "," + "\"" + getSetting("TwitchChatLink", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "MixerChatLink" + "\"" + "," + "\"" + getSetting("MixerChatLink", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "DiscordEnabled" + "\"" + "," + "\"" + getSetting("DiscordEnabled", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "DiscordChatLink" + "\"" + "," + "\"" + getSetting("DiscordChatLink", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "AutoPointsWhenOffline" + "\"" + "," + "\"" + getSetting("AutoPointsWhenOffline", channelID) + "\"" + ")");
		}		if(platform == PlatformType.TWITCH) {
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "LinkWarning" + "\"" + "," + "\"" + getSetting("LinkWarning", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "LanguageWarning" + "\"" + "," + "\"" + getSetting("LanguageWarning", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "FollowerMessage" + "\"" + "," + "\"" + getSetting("FollowerMessage", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "SymbolWarning" + "\"" + "," + "\"" + getSetting("SymbolWarning", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "AnnouncementsDelay" + "\"" + "," + "\"" + getSetting("AnnouncementsDelay", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "GiveawayDelay" + "\"" + "," + "\"" + getSetting("GiveawayDelay", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "StartingPoints" + "\"" + "," + "\"" + getSetting("StartingPoints", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "AutoPointsDelay" + "\"" + "," + "\"" + getSetting("AutoPointsDelay", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "EmoteWarning" + "\"" + "," + "\"" + getSetting("EmoteWarning", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "Commands" + "\"" + "," + "\"" + getSetting("Commands", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "Games" + "\"" + "," + "\"" + getSetting("Games", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "Ranks" + "\"" + "," + "\"" + getSetting("Ranks", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "Points" + "\"" + "," + "\"" + getSetting("Points", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "Announcements" + "\"" + "," + "\"" + getSetting("Announcements", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "Badwords" + "\"" + "," + "\"" + getSetting("Badwords", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "LinkChecker" + "\"" + "," + "\"" + getSetting("LinkChecker", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "Emote" + "\"" + "," + "\"" + getSetting("Emote", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "Symbol" + "\"" + "," + "\"" + getSetting("Symbol", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "SilentJoin" + "\"" + "," + "\"" + getSetting("SilentJoin", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "FollowerCheck" + "\"" + "," + "\"" + getSetting("FollowerCheck", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "Quotes" + "\"" + "," + "\"" + getSetting("Quotes", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "MaxSymbols" + "\"" + "," + "\"" + getSetting("MaxSymbols", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "MaxEmotes" + "\"" + "," + "\"" + getSetting("MaxEmotes", channelID) + "\"" + ")");
			MySQLConnection
					.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "MsgWhenCommandDoesntExist" + "\"" + "," + "\"" + getSetting("MsgWhenCommandDoesntExist", channelID) + "\"" + ")");
			MySQLConnection
					.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "MsgWhenCommandCantBeUsed" + "\"" + "," + "\"" + getSetting("MsgWhenCommandCantBeUsed", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "AnnouncementMessage1" + "\"" + "," + "\"" + getSetting("AnnouncementMessage1", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "AnnouncementMessage2" + "\"" + "," + "\"" + getSetting("AnnouncementMessage2", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "AnnouncementMessage3" + "\"" + "," + "\"" + getSetting("AnnouncementMessage3", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "AnnouncementMessage4" + "\"" + "," + "\"" + getSetting("AnnouncementMessage4", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "AnnouncementMessage5" + "\"" + "," + "\"" + getSetting("AnnouncementMessage5", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "CommandsCooldownAmount" + "\"" + "," + "\"" + getSetting("CommandsCooldownAmount", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "SelectedTimeZone" + "\"" + "," + "\"" + getSetting("SelectedTimeZone", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "SubAlerts" + "\"" + "," + "\"" + getSetting("SubAlerts", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "ResubAlerts" + "\"" + "," + "\"" + getSetting("ResubAlerts", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "GiftSubAlerts" + "\"" + "," + "\"" + getSetting("GiftSubAlerts", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "HostingAlerts" + "\"" + "," + "\"" + getSetting("HostingAlerts", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "RaidAlerts" + "\"" + "," + "\"" + getSetting("RaidAlerts", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "BitsAlerts" + "\"" + "," + "\"" + getSetting("BitsAlerts", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "FollowAlerts" + "\"" + "," + "\"" + getSetting("FollowAlerts", channelID) + "\"" + ")");
			MySQLConnection
					.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "AnnouncementsWhenOffline" + "\"" + "," + "\"" + getSetting("AnnouncementsWhenOffline", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "TwitchChatLink" + "\"" + "," + "\"" + getSetting("TwitchChatLink", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "MixerChatLink" + "\"" + "," + "\"" + getSetting("MixerChatLink", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "DiscordEnabled" + "\"" + "," + "\"" + getSetting("DiscordEnabled", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "DiscordChatLink" + "\"" + "," + "\"" + getSetting("DiscordChatLink", channelID) + "\"" + ")");
			MySQLConnection.executeUpdate("INSERT INTO config(twitch_channel_id, setting, value) VALUES (" + "\"" + channelID + "\"" + "," + "\"" + "AutoPointsWhenOffline" + "\"" + "," + "\"" + getSetting("AutoPointsWhenOffline", channelID) + "\"" + ")");
		}
		else if(platform == PlatformType.MIXER) {
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "LinkWarning" + "\"" + "," + "\"" + getSetting("LinkWarning", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "LanguageWarning" + "\"" + "," + "\"" + getSetting("LanguageWarning", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "FollowerMessage" + "\"" + "," + "\"" + getSetting("FollowerMessage", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "SymbolWarning" + "\"" + "," + "\"" + getSetting("SymbolWarning", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "AnnouncementsDelay" + "\"" + "," + "\"" + getSetting("AnnouncementsDelay", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "GiveawayDelay" + "\"" + "," + "\"" + getSetting("GiveawayDelay", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "StartingPoints" + "\"" + "," + "\"" + getSetting("StartingPoints", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "AutoPointsDelay" + "\"" + "," + "\"" + getSetting("AutoPointsDelay", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "EmoteWarning" + "\"" + "," + "\"" + getSetting("EmoteWarning", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "Commands" + "\"" + "," + "\"" + getSetting("Commands", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "Games" + "\"" + "," + "\"" + getSetting("Games", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "Ranks" + "\"" + "," + "\"" + getSetting("Ranks", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "Points" + "\"" + "," + "\"" + getSetting("Points", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "Announcements" + "\"" + "," + "\"" + getSetting("Announcements", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "Badwords" + "\"" + "," + "\"" + getSetting("Badwords", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "LinkChecker" + "\"" + "," + "\"" + getSetting("LinkChecker", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "Emote" + "\"" + "," + "\"" + getSetting("Emote", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "Symbol" + "\"" + "," + "\"" + getSetting("Symbol", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "SilentJoin" + "\"" + "," + "\"" + getSetting("SilentJoin", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "FollowerCheck" + "\"" + "," + "\"" + getSetting("FollowerCheck", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "Quotes" + "\"" + "," + "\"" + getSetting("Quotes", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "MaxSymbols" + "\"" + "," + "\"" + getSetting("MaxSymbols", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "MaxEmotes" + "\"" + "," + "\"" + getSetting("MaxEmotes", channelMixer) + "\"" + ")");
				MySQLConnection
						.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "MsgWhenCommandDoesntExist" + "\"" + "," + "\"" + getSetting("MsgWhenCommandDoesntExist", channelMixer) + "\"" + ")");
				MySQLConnection
						.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "MsgWhenCommandCantBeUsed" + "\"" + "," + "\"" + getSetting("MsgWhenCommandCantBeUsed", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "AnnouncementMessage1" + "\"" + "," + "\"" + getSetting("AnnouncementMessage1", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "AnnouncementMessage2" + "\"" + "," + "\"" + getSetting("AnnouncementMessage2", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "AnnouncementMessage3" + "\"" + "," + "\"" + getSetting("AnnouncementMessage3", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "AnnouncementMessage4" + "\"" + "," + "\"" + getSetting("AnnouncementMessage4", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "AnnouncementMessage5" + "\"" + "," + "\"" + getSetting("AnnouncementMessage5", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "CommandsCooldownAmount" + "\"" + "," + "\"" + getSetting("CommandsCooldownAmount", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "SelectedTimeZone" + "\"" + "," + "\"" + getSetting("SelectedTimeZone", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "SubAlerts" + "\"" + "," + "\"" + getSetting("SubAlerts", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "ResubAlerts" + "\"" + "," + "\"" + getSetting("ResubAlerts", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "GiftSubAlerts" + "\"" + "," + "\"" + getSetting("GiftSubAlerts", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "HostingAlerts" + "\"" + "," + "\"" + getSetting("HostingAlerts", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "RaidAlerts" + "\"" + "," + "\"" + getSetting("RaidAlerts", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "BitsAlerts" + "\"" + "," + "\"" + getSetting("BitsAlerts", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "FollowAlerts" + "\"" + "," + "\"" + getSetting("FollowAlerts", channelMixer) + "\"" + ")");
				MySQLConnection
						.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "AnnouncementsWhenOffline" + "\"" + "," + "\"" + getSetting("AnnouncementsWhenOffline", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "TwitchChatLink" + "\"" + "," + "\"" + getSetting("TwitchChatLink", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "MixerChatLink" + "\"" + "," + "\"" + getSetting("MixerChatLink", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "DiscordEnabled" + "\"" + "," + "\"" + getSetting("DiscordEnabled", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "DiscordChatLink" + "\"" + "," + "\"" + getSetting("DiscordChatLink", channelMixer) + "\"" + ")");
				MySQLConnection.executeUpdate("INSERT INTO config(mixer_channel, setting, value) VALUES (" + "\"" + channelMixer + "\"" + "," + "\"" + "AutoPointsWhenOffline" + "\"" + "," + "\"" + getSetting("AutoPointsWhenOffline", channelMixer) + "\"" + ")");
			}
	}
}
