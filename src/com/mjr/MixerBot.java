package com.mjr;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.mjr.ConsoleUtil.MessageType;
import com.mjr.MJRBot.BotType;
import com.mjr.commands.CommandManager;
import com.mjr.files.Config;
import com.mjr.files.ConfigMain;
import com.mjr.mjrmixer.MJR_MixerBot;
import com.mjr.threads.AnnouncementsThread;
import com.mjr.threads.PointsThread;

public class MixerBot extends MJR_MixerBot {

    public String channelName = "";

    public MixerBot(String channelName) {
	super(ConfigMain.getSetting("MixerClientID"), ConfigMain.getSetting("MixerUsername/BotName"));
	this.channelName = channelName;
    }

    private final CommandManager commands = new CommandManager();

    @Override
    protected void onMessage(String sender, String message) {
	ConsoleUtil.TextToConsole(this, BotType.Mixer, this.channelName, message, MessageType.Chat, sender);
	try {
	    commands.onCommand(BotType.Mixer, this, this.channelName, sender, null, null, message);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @Override
    protected void onJoin(String sender) {
	this.addViewer(sender);
	// if
	// (!PointsThread.viewersJoinedTimes.containsKey(sender.toLowerCase()))
	// PointsThread.viewersJoinedTimes.put(sender.toLowerCase(),
	// System.currentTimeMillis());
    }

    @Override
    protected void onPart(String sender) {
	this.removeViewer(sender);
	// if
	// (PointsThread.viewersJoinedTimes.containsKey(sender.toLowerCase()))
	// PointsThread.viewersJoinedTimes.remove(sender.toLowerCase());
    }

    public void joinChannel(String channel) {
	try {
	    this.setdebug(true);
	    this.joinMixerChannel(channel);
	    if (this.isConnected() && this.isAuthenticated()) {
		// Start Threads
		if (Config.getSetting("Points", channel).equalsIgnoreCase("true")) {
		    PointsThread pointsThread = new PointsThread(BotType.Mixer, channel);
		    pointsThread.start();
		}
		if (Config.getSetting("Announcements", channel).equalsIgnoreCase("true")) {
		    AnnouncementsThread announcementsThread = new AnnouncementsThread(BotType.Mixer, channel);
		    announcementsThread.start();
		}
		if (Config.getSetting("FollowerCheck", channel).equalsIgnoreCase("true")) {
		    // CheckFollowers followersThread = new
		    // CheckFollowers(BotType.Mixer, channel); TODO Add for
		    // Mixer
		    // followersThread.start();
		}

		// for (String viewer : this.getViewers())
		// if
		// (!PointsThread.viewersJoinedTimes.containsKey(viewer.toLowerCase()))
		// PointsThread.viewersJoinedTimes.put(viewer.toLowerCase(),
		// System.currentTimeMillis());

		ConsoleUtil.TextToConsole(this, BotType.Mixer, this.channelName, "MJRBot is Connected & Authenticated to Mixer!",
			MessageType.Chat, null);
		if (Config.getSetting("SilentJoin", channel).equalsIgnoreCase("false"))
		    this.sendMessage(this.getBotName() + " Connected!");
	    } else
		ConsoleUtil.TextToConsole(this, BotType.Mixer, this.channelName,
			"Theres been problem, connecting to Mixer, Please check settings are corrrect!", MessageType.Chat, null);
	} catch (InterruptedException | ExecutionException | IOException e) {
	    e.printStackTrace();
	}
    }

    @Override
    protected void onDebugMessage() {
	for (String message : this.getOutputMessages())
	    ConsoleUtil.TextToConsole(this, BotType.Mixer, this.channelName, message, MessageType.Bot, null);
	this.clearOutputMessages();
    }
}
