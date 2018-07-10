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
	if (MySQLConnection.executeQueryNoOutput("SELECT value FROM config WHERE channel = " + "\"" + channelName + "\"" + " AND setting = "
		+ "\"" + "LinkWarning" + "\"") == null) {
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
		    "SELECT value FROM config WHERE channel = " + "\"" + channelName + "\"" + " AND setting = " + "\"" + setting + "\"");
	    if (result == null)
		return null;
	    else
		try {
		    result.beforeFirst();
		    result.next();
		    return result.getString(1);
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
		MySQLConnection.executeUpdate("UPDATE config SET setting=" + "\"" + setting + "\""
			+ ",value=" + "\"" + value + "\"" + " WHERE channel = " + "\"" + channelName + "\"" + " AND setting = " + "\""
			+ setting + "\"");
	}
    }

}
