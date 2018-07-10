package com.mjr.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mjr.ConsoleUtil;
import com.mjr.MJRBot;
import com.mjr.sql.MySQLConnection;

public class Config extends FileBase {
    public static String fileName = "Config.properties";

    public static void loadDefaultsDatabase(String channelName) throws IOException {
	try {
	    if (MySQLConnection.executeQueryNoOutput("SELECT value FROM config WHERE channel = " + "\"" + channelName + "\""
		    + " AND setting = " + "\"" + "LinkWarning" + "\"").getFetchSize() < 1) {
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
	    }
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public static void loadDefaults(String channelName) throws IOException {
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
    }

    public static String getSetting(String setting, String channelName) {
	if (MJRBot.useFileSystem) {
	    File file = new File(MJRBot.filePath + channelName + File.separator + fileName);
	    if (!file.exists()) {
		file.getParentFile().mkdirs();
		try {
		    file.createNewFile();
		    loadDefaults(channelName);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    return load(channelName, fileName).getProperty(setting);
	} else {
	    ResultSet result = MySQLConnection.executeQueryNoOutput(
		    "SELECT * FROM config WHERE channel = " + "\"" + channelName + "\"" + " AND setting = " + "\"" + setting + "\"");
	    try {
		if (result == null)
		    return null;
		else if (result.getFetchSize() > 1)
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
	return null;
    }

    @SuppressWarnings("deprecation")
    public static void setSetting(String setting, String value, String channelName) {
	if (MJRBot.useFileSystem) {
	    File file = new File(MJRBot.filePath + channelName + File.separator + fileName);
	    if (!file.exists()) {
		file.getParentFile().mkdirs();
		try {
		    file.createNewFile();
		    loadDefaults(channelName);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    Properties properties = load(channelName, fileName);
	    file = loadFile(channelName, fileName);
	    if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
		if (value == "true")
		    ConsoleUtil.TextToConsole(setting + " has been has enabled!");
		else
		    ConsoleUtil.TextToConsole(setting + " has been has disabled!");

		properties.setProperty(setting, value);
		try {
		    properties.save(new FileOutputStream(file), null);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    } else {
		properties.setProperty(setting, value);
		try {
		    properties.store(new FileOutputStream(file), null);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	} else {
	    if (getSetting(setting, channelName) == null)
		MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + ","
			+ "\"" + setting + "\"" + "," + "\"" + value + "\"" + ")");
	    else
		MySQLConnection.executeUpdate("UPDATE config SET setting=" + "\"" + setting + "\"" + ",value=" + "\"" + value + "\""
			+ " WHERE channel = " + "\"" + channelName + "\"" + " AND setting = " + "\"" + setting + "\"");
	}
    }

    public static void migrateFile(String channelName) {
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "LinkWarning" + "\"" + "," + "\"" + getSetting("LinkWarning", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "LanguageWarning" + "\"" + "," + "\"" + getSetting("LanguageWarning", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "FollowerMessage" + "\"" + "," + "\"" + getSetting("FollowerMessage", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "SymbolWarning" + "\"" + "," + "\"" + getSetting("SymbolWarning", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "AnnouncementsDelay" + "\"" + "," + "\"" + getSetting("AnnouncementsDelay", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "GiveawayDelay" + "\"" + "," + "\"" + getSetting("GiveawayDelay", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "StartingPoints" + "\"" + "," + "\"" + getSetting("StartingPoints", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "AutoPointsDelay" + "\"" + "," + "\"" + getSetting("AutoPointsDelay", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "EmoteWarning" + "\"" + "," + "\"" + getSetting("EmoteWarning", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "Commands" + "\"" + "," + "\"" + getSetting("Commands", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "Games" + "\"" + "," + "\"" + getSetting("Games", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "Ranks" + "\"" + "," + "\"" + getSetting("Ranks", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "Points" + "\"" + "," + "\"" + getSetting("Points", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "Announcements" + "\"" + "," + "\"" + getSetting("Announcements", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "Badwords" + "\"" + "," + "\"" + getSetting("Badwords", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "LinkChecker" + "\"" + "," + "\"" + getSetting("LinkChecker", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "Emote" + "\"" + "," + "\"" + getSetting("Emote", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "Symbol" + "\"" + "," + "\"" + getSetting("Symbol", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "SilentJoin" + "\"" + "," + "\"" + getSetting("SilentJoin", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "FollowerCheck" + "\"" + "," + "\"" + getSetting("FollowerCheck", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "Quotes" + "\"" + "," + "\"" + getSetting("Quotes", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "MaxSymbols" + "\"" + "," + "\"" + getSetting("MaxSymbols", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "MaxEmotes" + "\"" + "," + "\"" + getSetting("MaxEmotes", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "MsgWhenCommandDoesntExist" + "\"" + "," + "\"" + getSetting("MsgWhenCommandDoesntExist", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "MsgWhenCommandCantBeUsed" + "\"" + "," + "\"" + getSetting("MsgWhenCommandCantBeUsed", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "AnnouncementMessage1" + "\"" + "," + "\"" + getSetting("AnnouncementMessage1", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "AnnouncementMessage2" + "\"" + "," + "\"" + getSetting("AnnouncementMessage2", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "AnnouncementMessage3" + "\"" + "," + "\"" + getSetting("AnnouncementMessage3", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "AnnouncementMessage4" + "\"" + "," + "\"" + getSetting("AnnouncementMessage4", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "AnnouncementMessage5" + "\"" + "," + "\"" + getSetting("AnnouncementMessage5", channelName) + "\"" + ")");
	MySQLConnection.executeUpdate("INSERT INTO config(channel, setting, value) VALUES (" + "\"" + channelName + "\"" + "," + "\""
		+ "CommandsCooldownAmount" + "\"" + "," + "\"" + getSetting("CommandsCooldownAmount", channelName) + "\"" + ")");
    }

}
