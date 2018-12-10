package com.mjr;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.mjr.commands.CommandManager;
import com.mjr.sql.MySQLConnection;
import com.mjr.sql.SQLUtilities;
import com.mjr.storage.Config;
import com.mjr.storage.ConfigMain;
import com.mjr.storage.PointsSystem;
import com.mjr.storage.QuoteSystem;
import com.mjr.storage.RankSystem;
import com.mjr.threads.ChannelListUpdateThread;
import com.mjr.threads.UpdateAnalyticsThread;
import com.mjr.threads.UserCooldownTickThread;
import com.mjr.util.ConsoleUtil;
import com.mjr.util.OSUtilities;
import com.mjr.util.Utilities;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public class MJRBot {

	public static boolean developmentModeDatabase = false;
	public static boolean developmentModeManual = true;
	public static boolean developmentStorageFileMode = true;
	public static boolean developmentStorageDatabaseMode = false;
	public static boolean developmentDisableSendMessage = false;
	public static String developmentChannel = "mjrlegends";
	public static String developmentPlatform = "Mixer";
	public static String developmentID = "176426";

	public static final String VERSION = "1.8.2, Server Version";
	public static final String CLIENT_ID = "it37a0q1pxypsijpd94h6rdhiq3j08";

	public static String filePath;

	private static HashMap<String, TwitchBot> twitchBots = new HashMap<String, TwitchBot>();
	private static HashMap<String, MixerBot> mixerBots = new HashMap<String, MixerBot>();

	private static Console console = System.console();
	private static String channel = "";
	public static String id = "";
	public static boolean useFileSystem = false;
	public static boolean useManualMode = false;
	public static UserCooldownTickThread userCooldownTickThread;
	public static UpdateAnalyticsThread updateAnalyticsThread;
	public static ChannelListUpdateThread updateThread;
	public static DiscordBot bot = null;

	private static Logger logger = LogManager.getLogger();

	public enum BotType {
		Twitch("Twitch"), Mixer("Mixer"), Discord("Discord");

		private final String typeName;

		BotType(String typeName) {
			this.typeName = typeName;
		}

		public String getTypeName() {
			return typeName;
		}
	}

	public static void main(final String[] args) throws IOException, InterruptedException, ExecutionException, ClassNotFoundException, SQLException {
		// Disable logging from dependency packages
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		for (ch.qos.logback.classic.Logger l : lc.getLoggerList()) {
			l.setLevel(Level.OFF);
		}
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

		if (OSUtilities.isUnix())
			filePath = "/home/" + File.separator + "MJRBot" + File.separator;
		else if (OSUtilities.isWindows())
			filePath = "C:" + File.separator + "MJRBot" + File.separator;
		else {
			ConsoleUtil.textToConsole("Your Operating System is currently not supported!");
			return;
		}

		if (filePath != null) {
			ConfigMain.load();
			String connectionType = "";
			do {
				if (developmentModeDatabase)
					connectionType = "Database";
				else if (developmentModeManual)
					connectionType = "Manual";
				else
					connectionType = console.readLine("Bot Type: Database or Manual or Migrate?");

				if (connectionType.equalsIgnoreCase("Migrate")) {
					runMirgration();
				}
			} while (!connectionType.equalsIgnoreCase("Database") && !connectionType.equalsIgnoreCase("Manual"));

			String fileSystemType = "";
			do {
				if (developmentStorageDatabaseMode)
					fileSystemType = "Database";
				else if (developmentStorageFileMode)
					fileSystemType = "File";
				else
					fileSystemType = console.readLine("Storage Type: File or Database?");
			} while (!fileSystemType.equalsIgnoreCase("File") && !fileSystemType.equalsIgnoreCase("Database"));

			if (fileSystemType.equalsIgnoreCase("File"))
				useFileSystem = true;
			else
				useFileSystem = false;

			if (connectionType.equalsIgnoreCase("Manual")) {
				runManualMode();
			} else if (connectionType.equalsIgnoreCase("Database")) {
				runDatabaseMode();
			}
			CommandManager.loadCommands();
		}
		if(!MJRBot.useFileSystem && !MJRBot.useManualMode) {
			bot = new DiscordBot();
			bot.startBot(ConfigMain.getSetting("DiscordToken"));
		}
		else {
			ConsoleUtil.textToConsole("Discord Integration has been disabled, as it is currently not supported on the file based storage type or/and when manual mode is used!");
		}
	}

	public static void runMirgration() {
		String channelName = "";
		String type = "";
		channelName = console.readLine("Channel Name?");
		useFileSystem = true;
		MySQLConnection.connect();
		type = console.readLine("What would you like to migrate? 1 - All, 2 - Config, 3 - Points, 4 - Ranks, 5 - Quotes");

		if (type.equalsIgnoreCase("1")) {
			Config.migrateFile(channelName);
			PointsSystem.migrateFile(channelName);
			RankSystem.migrateFile(channelName);
			QuoteSystem.migrateFile(channelName);
		} else if (type.equalsIgnoreCase("2")) {
			Config.migrateFile(channelName);
		}
		if (type.equalsIgnoreCase("3")) {
			PointsSystem.migrateFile(channelName);
		}
		if (type.equalsIgnoreCase("4")) {
			RankSystem.migrateFile(channelName);
		}
		if (type.equalsIgnoreCase("5")) {
			QuoteSystem.migrateFile(channelName);
		}
		useFileSystem = false;
		// Thread.sleep(100000);
	}

	public static void runManualMode() {
		ConsoleUtil.textToConsole("Analytics Recording has been disabled, as it is currently not supported on the file based storage type!");
		do {
			String botType;
			if (developmentModeManual) {
				botType = developmentPlatform;
				channel = developmentChannel;
				id = developmentID;
			} else {
				botType = console.readLine("Connection Type: Twitch or Mixer?");
				channel = console.readLine("Channel Name?");
				id = console.readLine("Channel ID?");
			}
			useManualMode = true;
			createBot(channel, botType);

		} while (twitchBots.isEmpty() && mixerBots.isEmpty());
	}

	public static void runDatabaseMode() {
		do {
			MySQLConnection.connect();
		} while (MySQLConnection.connected == false);
		ConsoleUtil.textToConsole("Getting list of Channels from Database server");
		userCooldownTickThread = new UserCooldownTickThread();
		userCooldownTickThread.start();
		if (!useFileSystem) {
			updateAnalyticsThread = new UpdateAnalyticsThread();
			updateAnalyticsThread.start();
		}
		else {
			ConsoleUtil.textToConsole("Analytics Recording has been disabled, as it is currently not supported on the file based storage type!");
		}
		HashMap<String, String> channelList = SQLUtilities.getChannelsTwitch();
		for (String channelName : channelList.keySet()) {
			createBot(channelName, channelList.get(channelName));
		}

		channelList = SQLUtilities.getChannelsMixer();
		for (String channelName : channelList.keySet()) {
			createBot(channelName, channelList.get(channelName));
		}

		updateThread = new ChannelListUpdateThread();
		updateThread.start();
	}

	public static void createBot(String channel, String botType) {
		channel = channel.toLowerCase(Locale.ENGLISH);
		if (botType.equalsIgnoreCase("twitch") && channel != "") {
			try {
				if (useFileSystem) {
					Config.loadDefaults(channel);
				} else {
					Config.loadDefaultsDatabase(channel);
				}
			} catch (IOException e) {
				MJRBot.logErrorMessage(e);
			}
			TwitchBot bot = new TwitchBot();
			bot.init(channel);
			addTwitchBot(channel, bot);
		} else if (botType.equalsIgnoreCase("mixer") && channel != "") {
			try {
				if (useFileSystem) {
					Config.loadDefaults(channel);
				} else
					Config.loadDefaultsDatabase(channel);
			} catch (IOException e) {
				MJRBot.logErrorMessage(e);
			}
			MixerBot bot = new MixerBot(channel);
			addMixerBot(channel, bot);
			try {
				bot.joinChannel(channel);
			} catch (SQLException e) {
				MJRBot.logErrorMessage(e);
			}
		} else if (channel != "")
			ConsoleUtil.textToConsole("Unknown Type of Connection!");
		else
			ConsoleUtil.textToConsole("Invalid entry for Channel Name!!");
	}

	public static Console getConsole() {
		return console;
	}

	public static HashMap<String, TwitchBot> getTwitchBots() {
		return twitchBots;
	}

	public static void setTwitchBots(HashMap<String, TwitchBot> bots) {
		MJRBot.twitchBots = bots;
	}

	public static void addTwitchBot(String channelName, TwitchBot bot) {
		ConsoleUtil.textToConsole("MJRBot has been added to the channel " + channelName);
		twitchBots.put(channelName, bot);
	}

	public static void removeTwitchBot(TwitchBot bot) {
		ConsoleUtil.textToConsole("MJRBot has been removed from the channel " + bot.channelName);
		twitchBots.remove(bot.channelName, bot);
	}

	public static void removeTwitchBot(String channelName) {
		ConsoleUtil.textToConsole("MJRBot has been removed from the channel " + channelName);
		twitchBots.remove(channelName, getTwitchBotByChannelName(channelName));
	}

	public static HashMap<String, MixerBot> getMixerBots() {
		return mixerBots;
	}

	public static void setMixerBots(HashMap<String, MixerBot> bots) {
		MJRBot.mixerBots = bots;
	}

	public static void addMixerBot(String channelName, MixerBot bot) {
		ConsoleUtil.textToConsole("MJRBot has been added to the channel " + channelName);
		mixerBots.put(channelName, bot);
	}

	public static void removeMixerBot(MixerBot bot) {
		ConsoleUtil.textToConsole("MJRBot has been removed from the channel " + bot.channelName);
		twitchBots.remove(bot.channelName, bot);
	}

	public static void removeMixerBot(String channelName) {
		ConsoleUtil.textToConsole("MJRBot has been removed from the channel " + channelName);
		mixerBots.remove(channelName, getMixerBotByChannelName(channelName));
	}

	public static TwitchBot getTwitchBotByChannelName(String channelName) {
		for (String bot : twitchBots.keySet()) {
			if (bot.equalsIgnoreCase(channelName))
				return twitchBots.get(bot);
		}
		return null;
	}

	public static MixerBot getMixerBotByChannelName(String channelName) {
		for (String bot : mixerBots.keySet()) {
			if (bot.equalsIgnoreCase(channelName))
				return mixerBots.get(bot);
		}
		return null;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		MJRBot.logger = logger;
	}
	
	public static void logErrorMessage(final Throwable throwable) {
		String stackTrace = Utilities.getStackTraceString(throwable);
		logErrorMessage(stackTrace);
	}
	
	public static void logErrorMessage(String stackTrace) {
		getLogger().info(stackTrace);
		if(MJRBot.bot != null)
			bot.sendErrorMessage(stackTrace);
	}
}