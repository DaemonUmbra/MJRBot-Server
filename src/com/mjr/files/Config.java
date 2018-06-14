package com.mjr.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.mjr.ConsoleUtil;
import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;

public class Config {
    public static String filename = "Config.properties";
    public static File file;
    public static Properties properties = new Properties();
    protected static InputStream iStream;

    public static void load(BotType type, String channelName) throws IOException {
	if (type == BotType.Twitch)
	    file = new File(MJRBot.filePath + MJRBot.getTwitchBotByChannelName(channelName) + File.separator + filename);
	else {
	    file = new File(MJRBot.filePath + MJRBot.getMixerBotByChannelName(channelName) + File.separator + filename);
	}

	if (!file.exists()) {
	    file.getParentFile().mkdirs();
	    file.createNewFile();
	    iStream = new FileInputStream(file);
	    properties.load(iStream);
	    setSetting("LinkWarning", "you are not allowed to post links with out permission!");
	    setSetting("LanguageWarning", "you are not allowed to use that language in the chat!");
	    setSetting("FollowerMessage", "has followed!");
	    setSetting("SymbolWarning", "you are using to many symbols");
	    setSetting("AnnouncementsDelay", "0");
	    setSetting("GiveawayDelay", "0");
	    setSetting("StartingPoints", "0");
	    setSetting("AutoPointsDelay", "0");
	    setSetting("EmoteWarning", "dont spam emotes!");
	    setSetting("Commands", "false");
	    setSetting("Games", "false");
	    setSetting("Ranks", "false");
	    setSetting("Points", "false");
	    setSetting("Announcements", "false");
	    setSetting("Badwords", "false");
	    setSetting("LinkChecker", "false");
	    setSetting("Emote", "false");
	    setSetting("Symbol", "false");
	    setSetting("SilentJoin", "true");
	    setSetting("FollowerCheck", "false");
	    setSetting("Quotes", "false");
	    setSetting("MaxSymbols", "5");
	    setSetting("MaxEmotes", "5");
	    setSetting("MsgWhenCommandDoesntExist", "true");
	    setSetting("MsgWhenCommandCantBeUsed", "false");
	    setSetting("AnnouncementMessage1", "");
	    setSetting("AnnouncementMessage2", "");
	    setSetting("AnnouncementMessage3", "");
	    setSetting("AnnouncementMessage4", "");
	    setSetting("AnnouncementMessage5", "");

	    properties.store(new FileOutputStream(file), null);
	}
	FileReader reader = new FileReader(file);
	properties.load(reader);
    }

    public static String getSetting(String setting) {
	return properties.getProperty(setting);
    }

    @SuppressWarnings("deprecation")
    public static void setSetting(String setting, String value) {
	if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
	    if (value == "true")
		ConsoleUtil.TextToConsole(setting + " has been has enabled!", "Bot", null);
	    else
		ConsoleUtil.TextToConsole(setting + " has been has disabled!", "Bot", null);

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
