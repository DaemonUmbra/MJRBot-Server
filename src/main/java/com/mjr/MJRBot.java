package com.mjr;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.mjr.ChatBotManager.BotType;
import com.mjr.commands.CommandManager;
import com.mjr.console.ConsoleCommandManager;
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

	public static final String VERSION = "1.8.7, Server Edition";
	public static final String CLIENT_ID = "it37a0q1pxypsijpd94h6rdhiq3j08";

	public static String filePath;
	private static Logger logger = LogManager.getLogger();

	public static StorageType storageType = StorageType.Database;
	public static ConnectionType connectionType = ConnectionType.Database;
	public static UserCooldownTickThread userCooldownTickThread;
	public static UpdateAnalyticsThread updateAnalyticsThread;
	public static ChannelListUpdateThread updateThread;
	public static DiscordBot bot = null;

	// Manual Mode
	private static String channel = "";
	public static String id = "";

	// Development
	public static boolean developmentModeDatabase = false;
	public static boolean developmentModeManual = false;
	public static boolean developmentStorageFileMode = false;
	public static boolean developmentStorageDatabaseMode = false;
	public static boolean developmentDisableSendMessage = false;
	public static String developmentChannel = "MJRBotTest";
	public static String developmentPlatform = "Mixer";
	public static String developmentID = "57804636";

	public enum ConnectionType {
		Database(), Manual();
	}

	public enum StorageType {
		Database(), File();
	}

	public enum MirgrationType {
		All(), Config(), Points(), Ranks(), Quotes();
	}

	public static void main(final String[] args) {
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
			ConsoleCommandManager.loadCommands();
            System.out.println("  __  __       _   ____    ____            _   \r\n" + 
                    " |  \\/  |     | | |  _ \\  | __ )    ___   | |_ \r\n" + 
                    " | |\\/| |  _  | | | |_) | |  _ \\   / _ \\  | __|\r\n" + 
                    " | |  | | | |_| | |  _ <  | |_) | | (_) | | |_ \r\n" + 
                    " |_|  |_|  \\___/  |_| \\_\\ |____/   \\___/   \\__|\r\n" + 
                    "                                               ");
			System.out.println("Welcome to MJRBot!");
			System.out.println("v" + VERSION);
			System.out.println("Use 'connect <type>' to connect");
			System.out.println("\n" + "You can use 'help' at any time to see possible commands");
			Thread thread = new Thread("Server console handler") {
				@Override
				public void run() {
					BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in));
					String s4;

					try {
						while ((s4 = bufferedreader.readLine()) != null) {
							ConsoleCommandManager.onCommand(s4);
						}
					} catch (IOException ioexception1) {
						ConsoleUtil.textToConsole("Exception handling console input" + ioexception1);
					}
				}
			};
			thread.setDaemon(true);
			thread.start();
			while (true) {
			}
		}
	}

	public static void connectBot(ConnectionType type) {
		connectBot(type, null, null, 0);
	}

	public static void discordConnect() {
		if (bot == null) {
			try {
				ConfigMain.load();
			} catch (IOException e) {
				MJRBot.logErrorMessage(e);
			}
			bot = new DiscordBot(ConfigMain.getSetting("DiscordToken"));
			if (MJRBot.connectionType == ConnectionType.Database)
				bot.setupEvents();
			else
				ConsoleUtil.textToConsole("Discord Crosslink is disabled, as it is currently not supported on the file based storage type!");
		}
	}

	public static void connectBot(ConnectionType type, BotType botType, String channelName, int channelId) {
		try {
			ConfigMain.load();
			if (MJRBot.connectionType == ConnectionType.Database) {
				do {
					MySQLConnection.connect();
					Thread.sleep(5000);
				} while (MySQLConnection.connected == false);
			}
			discordConnect();
			Thread.sleep(2000);

			if (type == ConnectionType.Manual) {
				runManualMode(botType, channelName, channelId);
			} else if (type == ConnectionType.Database) {
				runDatabaseMode();
			}
			CommandManager.loadCommands();
		} catch (Exception e) {
			MJRBot.logErrorMessage(e);
		}
	}

	public static void runMirgration(String channelName, MirgrationType type) {
		if (type == MirgrationType.All) {
			Config.migrateFile(channelName);
			PointsSystem.migrateFile(channelName);
			RankSystem.migrateFile(channelName);
			QuoteSystem.migrateFile(channelName);
		} else if (type == MirgrationType.Config) {
			Config.migrateFile(channelName);
		}
		if (type == MirgrationType.Points) {
			PointsSystem.migrateFile(channelName);
		}
		if (type == MirgrationType.Ranks) {
			RankSystem.migrateFile(channelName);
		}
		if (type == MirgrationType.Quotes) {
			QuoteSystem.migrateFile(channelName);
		}
	}

	public static void runManualMode(BotType type, String channelName, int channelId) {
		ConsoleUtil.textToConsole("Analytics Recording has been disabled, as it is currently not supported on the file based storage type!");
		if (developmentModeManual) {
			channel = developmentChannel;
			id = developmentID;
		} else {
			channel = channelName;
			id = "" + channelId;
		}
		userCooldownTickThread = new UserCooldownTickThread();
		userCooldownTickThread.start();
		ChatBotManager.createBot(channel, developmentModeManual == true ? developmentPlatform : type.getTypeName());
	}

	public static void runDatabaseMode() {
		ConsoleUtil.textToConsole("Getting list of Channels from Database server");
		userCooldownTickThread = new UserCooldownTickThread();
		userCooldownTickThread.start();
		if (storageType == StorageType.Database) {
			updateAnalyticsThread = new UpdateAnalyticsThread();
			updateAnalyticsThread.start();
		} else {
			ConsoleUtil.textToConsole("Analytics Recording has been disabled, as it is currently not supported on the file based storage type!");
		}
		HashMap<String, String> channelList = SQLUtilities.getChannelsTwitch();
		for (String channelName : channelList.keySet()) {
			ChatBotManager.createBot(channelName, channelList.get(channelName));
		}

		channelList = SQLUtilities.getChannelsMixer();
		for (String channelName : channelList.keySet()) {
			ChatBotManager.createBot(channelName, channelList.get(channelName));
		}

		updateThread = new ChannelListUpdateThread();
		updateThread.start();
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		MJRBot.logger = logger;
	}

	public static void logErrorMessage(String error, final Throwable throwable) {
		String stackTrace = Utilities.getStackTraceString(throwable);
		logErrorMessage(error + " - " + stackTrace);
	}

	public static void logErrorMessage(final Throwable throwable, String channelName) {
		String stackTrace = Utilities.getStackTraceString(throwable);
		logErrorMessage(channelName + " - " + stackTrace);
	}

	public static void logErrorMessage(final Throwable throwable, BotType type, String channelName) {
		String stackTrace = Utilities.getStackTraceString(throwable);
		logErrorMessage(type.getTypeName() + ": " + channelName + " - " + stackTrace);
	}

	public static void logErrorMessage(final Throwable throwable) {
		String stackTrace = Utilities.getStackTraceString(throwable);
		logErrorMessage(stackTrace);
	}

	public static void logErrorMessage(String stackTrace) {
		getLogger().info(stackTrace);
		if (MJRBot.bot != null)
			bot.sendErrorMessage((developmentModeDatabase || developmentModeManual ? "**Dev:** " : "") + stackTrace);
	}
}