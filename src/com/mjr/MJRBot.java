package com.mjr;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import com.mjr.commands.CommandManager;
import com.mjr.files.ConfigMain;
import com.mjr.sql.MySQLConnection;
import com.mjr.sql.SQLUtilities;
import com.mjr.threads.ChannelListUpdateThread;
import com.mjr.threads.PointsThread;

public class MJRBot {
    public static final String VERSION = "1.3.0 - Beta, Server Version";

    public static String filePath;

    private static HashMap<String, TwitchBot> twitchBots = new HashMap<String, TwitchBot>();
    private static HashMap<String, MixerBot> mixerBots = new HashMap<String, MixerBot>();

    private static Console console = System.console();
    private static String channel = "";

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
	    ConsoleUtil.TextToConsole("Your Operating System is currently not supported!", "Bot", null);
	    return;
	}
	if (filePath != null) {
	    ConfigMain.load();
	    PointsThread.viewersJoinedTimes.clear();
	    String connectionType = "";
	    do {
		connectionType = console.readLine("Bot Type: Database or Manual?");
		// connectionType = "Database";
		if (connectionType.equalsIgnoreCase("Manual")) {
		    do {
			String botType;
			botType = console.readLine("Connection Type: Twitch or Mixer?");
			channel = console.readLine("Channel Name?");

			// botType = "Twitch";
			// channel = "mjrlegends";
			createBot(channel, botType);

		    } while (twitchBots.isEmpty() && mixerBots.isEmpty());

		} else if (connectionType.equalsIgnoreCase("Database")) {
		    do {
			MySQLConnection.initConnection(ConfigMain.getSetting("DatabaseIPAddress"),
				Integer.parseInt(ConfigMain.getSetting("DatabasePort")), ConfigMain.getSetting("DatabaseDatabaseName"),
				ConfigMain.getSetting("DatabaseUsername"), ConfigMain.getSetting("DatabasePassword"));
		    } while (MySQLConnection.connected == false);
		    SQLUtilities.createDatabaseStructure();
		    System.out.println("Getting list of Channels from Database server");
		    HashMap<String, String> channelList = SQLUtilities.getChannels();
		    for (String channelName : channelList.keySet()) {
			createBot(channelName, channelList.get(channelName));
		    }
		    ChannelListUpdateThread updateThread = new ChannelListUpdateThread();
		    updateThread.start();
		}
		CommandManager.loadCommands();
	    } while (!connectionType.equalsIgnoreCase("Database") && !connectionType.equalsIgnoreCase("Manual"));
	}
    }

    public static void createBot(String channel, String botType) {
	channel = channel.toLowerCase(Locale.ENGLISH);
	if (botType.equalsIgnoreCase("twitch") && channel != "") {
	    TwitchBot bot = new TwitchBot();
	    bot.init(channel);
	    addTwitchBot(channel, bot);
	} else if (botType.equalsIgnoreCase("mixer") && channel != "") {
	    MixerBot bot = new MixerBot(channel);
	    bot.joinChannel(channel);
	    addMixerBot(channel, bot);
	} else if (channel != "")
	    ConsoleUtil.TextToConsole("Unknown Type of Connection!", "Bot", null);
	else
	    ConsoleUtil.TextToConsole("Invalid entry for Channel Name!!", "Bot", null);
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
	ConsoleUtil.TextToConsole("MJRBot has been added to the channel " + channelName, "Bot", null);
	twitchBots.put(channelName, bot);
    }

    public static void removeTwitchBot(TwitchBot bot) {
	ConsoleUtil.TextToConsole("MJRBot has been removed from the channel " + bot.channelName, "Bot", null);
	twitchBots.remove(bot.channelName, bot);
    }

    public static void removeTwitchBot(String channelName) {
	ConsoleUtil.TextToConsole("MJRBot has been removed from the channel " + channelName, "Bot", null);
	twitchBots.remove(channelName, getTwitchBotByChannelName(channelName));
    }

    public static HashMap<String, MixerBot> getMixerBots() {
	return mixerBots;
    }

    public static void setMixerBots(HashMap<String, MixerBot> bots) {
	MJRBot.mixerBots = bots;
    }

    public static void addMixerBot(String channelName, MixerBot bot) {
	ConsoleUtil.TextToConsole("MJRBot has been added to the channel " + channelName, "Bot", null);
	mixerBots.put(channelName, bot);
    }

    public static void removeMixerBot(MixerBot bot) {
	ConsoleUtil.TextToConsole("MJRBot has been removed from the channel " + bot.channelName, "Bot", null);
	twitchBots.remove(bot.channelName, bot);
    }

    public static void removeMixerBot(String channelName) {
	ConsoleUtil.TextToConsole("MJRBot has been removed from the channel " + channelName, "Bot", null);
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