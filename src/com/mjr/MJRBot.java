package com.mjr;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.mjr.commands.CommandManager;
import com.mjr.files.ConfigMain;
import com.mjr.threads.PointsThread;

public class MJRBot {
    public static final String VERSION = "1.0.8 - Beta, Server Version";

    public static String filePath;

    private static TwitchBot bot;
    private static MixerBot botMixer;

    private static Console console = System.console();
    private static String channel = "";

    public static void main(final String[] args) throws IOException, InterruptedException, ExecutionException {
	filePath = "/home/" + File.separator + "MJRBot" + File.separator;
	ConfigMain.load();
	PointsThread.viewersJoinedTimes.clear();
	String botType;
	botType = console.readLine("Twitch/Mixer?");
	channel = console.readLine("Channel?");
	if (botType.equalsIgnoreCase("twitch")) {
	    setTwitchBot(new TwitchBot());
	    bot.ConnectToTwitch();
	    bot.setChannel("#" + channel);
	    bot.joinChannel(MJRBot.getTwitchBot().getChannel());
	    ConsoleUtil.TextToConsole(
		    "Joined " + MJRBot.getTwitchBot().getChannel().substring(MJRBot.getTwitchBot().getChannel().indexOf("#") + 1)
			    + " channel", "Bot", null);
	    bot.setVerbose(true);
	} else {
	    botMixer = new MixerBot();
	    botMixer.joinChannel(getChannel());
	}
	CommandManager.loadCommands();
    }

    public static TwitchBot getTwitchBot() {
	return bot;
    }

    public static void setTwitchBot(TwitchBot bot) {
	MJRBot.bot = bot;
    }

    public static MixerBot getMixerBot() {
	return botMixer;
    }

    public static void setMixerBot(MixerBot botMixer) {
	MJRBot.botMixer = botMixer;
    }

    public static String getChannel() {
	return channel;
    }

    public static void setChannel(String channel) {
	MJRBot.channel = channel;
    }

    public static Console getConsole() {
	return console;
    }
}