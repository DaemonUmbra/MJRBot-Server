package com.mjr;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import com.mjr.commands.CommandManager;
import com.mjr.sql.MySQLConnection;
import com.mjr.sql.SQLUtilities;
import com.mjr.storage.Config;
import com.mjr.storage.ConfigMain;
import com.mjr.storage.PointsSystem;
import com.mjr.storage.QuoteSystem;
import com.mjr.storage.RankSystem;
import com.mjr.threads.ChannelListUpdateThread;
import com.mjr.threads.UserCooldownTickThread;

public class MJRBot {
    public static final String VERSION = "1.7.0 - Beta, Server Version";
    public static final String CLIENT_ID = "it37a0q1pxypsijpd94h6rdhiq3j08";

    public static String filePath;

    private static HashMap<String, TwitchBot> twitchBots = new HashMap<String, TwitchBot>();
    private static HashMap<String, MixerBot> mixerBots = new HashMap<String, MixerBot>();

    private static Console console = System.console();
    private static String channel = "";
    public static boolean useFileSystem = false;
    public static UserCooldownTickThread userCooldownTickThread;

    public enum BotType {
	Twitch("Twitch"), Mixer("Mixer");

	private final String typeName;

	BotType(String typeName) {
	    this.typeName = typeName;
	}

	public String getTypeName() {
	    return typeName;
	}
    }

    public static void main(final String[] args)
	    throws IOException, InterruptedException, ExecutionException, ClassNotFoundException, SQLException {
	if (OSUtilities.isUnix())
	    filePath = "/home/" + File.separator + "MJRBot" + File.separator;
	else if (OSUtilities.isWindows())
	    filePath = "C:" + File.separator + "MJRBot" + File.separator;
	else {
	    ConsoleUtil.TextToConsole("Your Operating System is currently not supported!");
	    return;
	}

	if (filePath != null) {
	    ConfigMain.load();
	    String connectionType = "";
	    do {
		connectionType = console.readLine("Bot Type: Database or Manual or Migrate?");
		// connectionType = "Database";

		if (connectionType.equalsIgnoreCase("Migrate")) {
		    runMirgration();
		}
	    } while (!connectionType.equalsIgnoreCase("Database") && !connectionType.equalsIgnoreCase("Manual"));

	    String fileSystemType = "";
	    do {
		fileSystemType = console.readLine("Storage Type: File or Database?");
		// fileSystemType = "Database";
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
    }

    public static void runMirgration() {
	String channelName = "";
	String type = "";
	channelName = console.readLine("Channel Name?");

	// channelName = "mjrlegends";
	useFileSystem = true;
	try {
	    MySQLConnection.initConnection(ConfigMain.getSetting("DatabaseIPAddress"),
		    Integer.parseInt(ConfigMain.getSetting("DatabasePort")), ConfigMain.getSetting("DatabaseDatabaseName"),
		    ConfigMain.getSetting("DatabaseUsername"), ConfigMain.getSetting("DatabasePassword"));
	} catch (NumberFormatException | ClassNotFoundException | SQLException e) {
	    e.printStackTrace();
	}
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
	do {
	    String botType;
	    botType = console.readLine("Connection Type: Twitch or Mixer?");
	    channel = console.readLine("Channel Name?");

	    // botType = "Twitch";
	    // channel = "mjrlegends";
	    createBot(channel, botType);

	} while (twitchBots.isEmpty() && mixerBots.isEmpty());
    }

    public static void runDatabaseMode() {
	do {
	    try {
		MySQLConnection.initConnection(ConfigMain.getSetting("DatabaseIPAddress"),
			Integer.parseInt(ConfigMain.getSetting("DatabasePort")), ConfigMain.getSetting("DatabaseDatabaseName"),
			ConfigMain.getSetting("DatabaseUsername"), ConfigMain.getSetting("DatabasePassword"));
	    } catch (NumberFormatException | ClassNotFoundException | SQLException e) {
		e.printStackTrace();
	    }
	} while (MySQLConnection.connected == false);
	System.out.println("Getting list of Channels from Database server");
	userCooldownTickThread = new UserCooldownTickThread();
	userCooldownTickThread.start();
	HashMap<String, String> channelList = SQLUtilities.getChannelsTwitch();
	for (String channelName : channelList.keySet()) {
	    createBot(channelName, channelList.get(channelName));
	}

	channelList = SQLUtilities.getChannelsMixer();
	for (String channelName : channelList.keySet()) {
	    createBot(channelName, channelList.get(channelName));
	}

	ChannelListUpdateThread updateThread = new ChannelListUpdateThread();
	updateThread.start();
    }

    public static void createBot(String channel, String botType) {
	channel = channel.toLowerCase(Locale.ENGLISH);
	if (botType.equalsIgnoreCase("twitch") && channel != "") {
	    TwitchBot bot = new TwitchBot();
	    bot.init(channel);
	    addTwitchBot(channel, bot);
	    try {
		if (useFileSystem) {
		    Config.loadDefaults(channel);
		} else {
		    Config.loadDefaultsDatabase(channel);
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} else if (botType.equalsIgnoreCase("mixer") && channel != "") {
	    MixerBot bot = new MixerBot(channel);
	    addMixerBot(channel, bot);
	    bot.joinChannel(channel);
	    try {
		if (useFileSystem) {
		    // Config.loadDefaults(channel);
		} else
		    Config.loadDefaultsDatabase(channel);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} else if (channel != "")
	    ConsoleUtil.TextToConsole("Unknown Type of Connection!");
	else
	    ConsoleUtil.TextToConsole("Invalid entry for Channel Name!!");
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
	ConsoleUtil.TextToConsole("MJRBot has been added to the channel " + channelName);
	twitchBots.put(channelName, bot);
    }

    public static void removeTwitchBot(TwitchBot bot) {
	ConsoleUtil.TextToConsole("MJRBot has been removed from the channel " + bot.channelName);
	twitchBots.remove(bot.channelName, bot);
    }

    public static void removeTwitchBot(String channelName) {
	ConsoleUtil.TextToConsole("MJRBot has been removed from the channel " + channelName);
	twitchBots.remove(channelName, getTwitchBotByChannelName(channelName));
    }

    public static HashMap<String, MixerBot> getMixerBots() {
	return mixerBots;
    }

    public static void setMixerBots(HashMap<String, MixerBot> bots) {
	MJRBot.mixerBots = bots;
    }

    public static void addMixerBot(String channelName, MixerBot bot) {
	ConsoleUtil.TextToConsole("MJRBot has been added to the channel " + channelName);
	mixerBots.put(channelName, bot);
    }

    public static void removeMixerBot(MixerBot bot) {
	ConsoleUtil.TextToConsole("MJRBot has been removed from the channel " + bot.channelName);
	twitchBots.remove(bot.channelName, bot);
    }

    public static void removeMixerBot(String channelName) {
	ConsoleUtil.TextToConsole("MJRBot has been removed from the channel " + channelName);
	twitchBots.remove(channelName, getMixerBotByChannelName(channelName));
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
}