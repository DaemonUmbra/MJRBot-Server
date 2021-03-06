package com.mjr.mjrbot.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.util.ConsoleUtil;
import com.mjr.mjrbot.util.MJRBotUtilities;

public class BotConfigManager {
	public static String filename = "Settings.properties";
	public static File file = new File(MJRBot.filePath + filename);
	public static Properties properties = new Properties();
	protected static InputStream iStream;

	public static void load() throws IOException {
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
			iStream = new FileInputStream(file);
			properties.load(iStream);
			properties.store(new FileOutputStream(file), null);
			setSetting("TwitchUsername", "");
			setSetting("TwitchPassword", "");
			setSetting("TwitchVerboseMessages", "false");
			setSetting("TwitchClientSecret", "");
			
			setSetting("MixerUsername/BotName", "");
			setSetting("MixerAuthCode", "");

			setSetting("DatabaseIPAddress", "");
			setSetting("DatabaseDatabaseName", "");
			setSetting("DatabasePort", "");
			setSetting("DatabaseUsername", "");
			setSetting("DatabasePassword", "");
			
			setSetting("UpdateChannelFromDatabaseTime(Seconds)", "30");

			setSetting("DiscordToken", "");
			setSetting("PUBGToken", "");
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

	}

}
