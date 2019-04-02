package com.mjr.mjrbot;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.DiscordBot;
import com.mjr.mjrbot.commands.CommandManager;
import com.mjr.mjrbot.console.ConsoleCommandManager;
import com.mjr.mjrbot.storage.BotConfigManager;
import com.mjr.mjrbot.storage.sql.MySQLConnection;
import com.mjr.mjrbot.storage.sql.SQLUtilities;
import com.mjr.mjrbot.threads.ChannelListUpdateThread;
import com.mjr.mjrbot.threads.UpdateAnalyticsThread;
import com.mjr.mjrbot.threads.UserCooldownTickThread;
import com.mjr.mjrbot.util.ConsoleUtil;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.OSUtilities;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public class MJRBot {

	public static final String VERSION = "1.9.6, Server Edition";
	public static final String CLIENT_ID = "it37a0q1pxypsijpd94h6rdhiq3j08";

	public static String filePath;
	private static Logger logger = LogManager.getLogger();

	public static StorageType storageType = StorageType.Database;
	public static ConnectionType connectionType = ConnectionType.Database;
	public static UserCooldownTickThread userCooldownTickThread;
	public static UpdateAnalyticsThread updateAnalyticsThread;
	public static ChannelListUpdateThread updateThread;
	private static DiscordBot discordBot = null;

	// Manual Mode
	public static String manualChannelName = "";
	public static int manualChannelID = 0;

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
			System.out.println("  __  __       _   ____    ____            _   \r\n" + " |  \\/  |     | | |  _ \\  | __ )    ___   | |_ \r\n" + " | |\\/| |  _  | | | |_) | |  _ \\   / _ \\  | __|\r\n"
					+ " | |  | | | |_| | |  _ <  | |_) | | (_) | | |_ \r\n" + " |_|  |_|  \\___/  |_| \\_\\ |____/   \\___/   \\__|\r\n" + "                                               ");
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
							if (s4.length() != 0)
								ConsoleCommandManager.onCommand(s4);
							else
								System.out.println("Invalid command, You can use 'help' at any time to see possible commands");
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
		if (discordBot == null) {
			try {
				BotConfigManager.load();
			} catch (IOException e) {
				MJRBotUtilities.logErrorMessage(e);
			}
			discordBot = new DiscordBot(BotConfigManager.getSetting("DiscordToken"));
			if (MJRBot.connectionType == ConnectionType.Database)
				discordBot.setupEvents();
			else
				ConsoleUtil.textToConsole("Discord Crosslink is disabled, as it is currently not supported on the file based storage type!");
		}
	}

	public static void connectBot(ConnectionType type, BotType botType, String channelName, int channelId) {
		try {
			BotConfigManager.load();
			if (MJRBot.connectionType == ConnectionType.Database) {
				do {
					MySQLConnection.connect(BotConfigManager.getSetting("DatabaseIPAddress"), Integer.parseInt(BotConfigManager.getSetting("DatabasePort")), BotConfigManager.getSetting("DatabaseDatabaseName"), BotConfigManager.getSetting("DatabaseUsername"),
							BotConfigManager.getSetting("DatabasePassword"));
					Thread.sleep(5000);
				} while (MySQLConnection.isConnected() == false);
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
			MJRBotUtilities.logErrorMessage(e);
		}
	}

	public static void runMirgration(String channelName, MirgrationType type) {
		// if (type == MirgrationType.All) { TODO Fix
		// Config.migrateFile(channelName);
		// PointsSystem.migrateFile(channelName);
		// RankSystem.migrateFile(channelName);
		// QuoteSystem.migrateFile(channelName);
		// } else if (type == MirgrationType.Config) {
		// Config.migrateFile(channelName);
		// }
		// if (type == MirgrationType.Points) {
		// PointsSystem.migrateFile(channelName);
		// }
		// if (type == MirgrationType.Ranks) {
		// RankSystem.migrateFile(channelName);
		// }
		// if (type == MirgrationType.Quotes) {
		// QuoteSystem.migrateFile(channelName);
		// }
	}

	public static void runManualMode(BotType type, String channelName, int channelId) {
		ConsoleUtil.textToConsole("Analytics Recording has been disabled, as it is currently not supported on the file based storage type!");
		manualChannelName = channelName;
		manualChannelID = channelId;
		userCooldownTickThread = new UserCooldownTickThread();
		userCooldownTickThread.start();
		ChatBotManager.createBot(manualChannelName, manualChannelID, type.getTypeName());
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
		HashMap<Integer, String> channelList = SQLUtilities.getChannelsTwitch();
		for (Integer channelID : channelList.keySet()) {
			ChatBotManager.createBot(null, channelID, channelList.get(channelID));
		}

		HashMap<String, String> channelListMixer = SQLUtilities.getChannelsMixer();
		for (String channelName : channelListMixer.keySet()) {
			ChatBotManager.createBot(channelName, 0, channelListMixer.get(channelName));
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

	public static DiscordBot getDiscordBot() {
		return discordBot;
	}

	public static void setDiscordBot(DiscordBot discordBot) {
		MJRBot.discordBot = discordBot;
	}
}