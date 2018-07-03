package com.mjr.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.mjr.ConsoleUtil;

public class Config extends FileBase {
    public static String fileName = "Config.properties";

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
	return load(channelName, fileName).getProperty(setting);
    }

    @SuppressWarnings("deprecation")
    public static void setSetting(String setting, String value, String channelName) {
	Properties properties = load(channelName, fileName);
	File file = loadFile(channelName, fileName);
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

    }

}
