package com.mjr;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import com.mjr.commands.CommandManager;
import com.mjr.files.ConfigMain;
import com.mjr.threads.PointsThread;

public class MJRBot {
    public static final String VERSION = "1.1.7 - Beta, Server Version";

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

    public static void main(final String[] args) throws IOException, InterruptedException, ExecutionException {
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
	    do {
		String botType;
		botType = console.readLine("Connection Type: Twitch/Mixer?");
		channel = console.readLine("Channel Name?");
		channel = channel.toLowerCase(Locale.ENGLISH);
		if (botType.equalsIgnoreCase("twitch") && channel != "") {
		    TwitchBot bot = new TwitchBot(channel);
		    bot.init();
		    addTwitchBot(channel, bot);
		} else if (botType.equalsIgnoreCase("mixer") && channel != "") {
		    // botMixer = new MixerBot(); TODO Finish
		    // botMixer.joinChannel(channel);
		} else if (channel != "")
		    ConsoleUtil.TextToConsole("Unknown Type of Connection!", "Bot", null);
		else
		    ConsoleUtil.TextToConsole("Invalid entry for Channel Name!!", "Bot", null);
	    } while (twitchBots.isEmpty() && mixerBots.isEmpty());
	    CommandManager.loadCommands();
	}
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
	twitchBots.put(channelName, bot);
    }

    public static HashMap<String, MixerBot> getMixerBots() {
	return mixerBots;
    }

    public static void setMixerBots(HashMap<String, MixerBot> bots) {
	MJRBot.mixerBots = bots;
    }

    public static void addMixerBot(String channelName, MixerBot bot) {
	mixerBots.put(channelName, bot);
    }
    
    public static TwitchBot getTwitchBotByChannelName(String channelName) {
	for(String bot : twitchBots.keySet()) {
	    if(bot.equalsIgnoreCase(channelName))
		return twitchBots.get(bot);
	}
	return null;
    }
    
    public static MixerBot getMixerBotByChannelName(String channelName) {
	for(String bot : mixerBots.keySet()) {
	    if(bot.equalsIgnoreCase(channelName))
		return mixerBots.get(bot);
	}
	return null;
    }
}