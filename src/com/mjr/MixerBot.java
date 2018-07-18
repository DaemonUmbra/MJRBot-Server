package com.mjr;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.mjr.ConsoleUtil.MessageType;
import com.mjr.MJRBot.BotType;
import com.mjr.commands.CommandManager;
import com.mjr.files.Config;
import com.mjr.files.ConfigMain;
import com.mjr.mjrmixer.MJR_MixerBot;
import com.mjr.threads.AnnouncementsThread;
import com.mjr.threads.CheckForNewFollowersThread;
import com.mjr.threads.GetFollowersThread;
import com.mjr.threads.GetViewersThread;
import com.mjr.threads.PointsThread;

public class MixerBot extends MJR_MixerBot {

    public String channelName = "";

    public GetViewersThread getViewersThread;
    public PointsThread pointsThread;
    public AnnouncementsThread announcementsThread;
    public CheckForNewFollowersThread followersThread;
    public GetFollowersThread getFollowersThread;

    public HashMap<String, Integer> usersCooldowns = new HashMap<String, Integer>();
    public HashMap<String, Long> viewersJoinedTimes = new HashMap<String, Long>();

    public MixerBot(String channelName) {
	super(ConfigMain.getSetting("MixerClientID"), ConfigMain.getSetting("MixerAuthCode"),
		ConfigMain.getSetting("MixerUsername/BotName"));
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
	if (!this.viewersJoinedTimes.containsKey(sender.toLowerCase()))
	    this.viewersJoinedTimes.put(sender.toLowerCase(), System.currentTimeMillis());
    }

    @Override
    protected void onPart(String sender) {
	if (this.viewersJoinedTimes.containsKey(sender.toLowerCase()))
	    this.viewersJoinedTimes.remove(sender.toLowerCase());
    }

    public void joinChannel(String channel) {
	try {
	    this.setdebug(true);
	    this.joinMixerChannel(channel);
	    if (this.isConnected() && this.isAuthenticated()) {
		// Start Threads
		if (Config.getSetting("Points", channel).equalsIgnoreCase("true")) {
		    pointsThread = new PointsThread(BotType.Mixer, channel);
		    pointsThread.start();
		}
		if (Config.getSetting("Announcements", channel).equalsIgnoreCase("true")) {
		    announcementsThread = new AnnouncementsThread(BotType.Mixer, channel);
		    announcementsThread.start();
		}
		if (Config.getSetting("FollowerCheck", channel).equalsIgnoreCase("true")) {
		    // CheckForNewFollowersThread followersThread = new CheckForNewFollowersThread(BotType.Mixer, channel); TODO Add for Mixer
		    // followersThread.start();
		}

		for (String viewer : this.getViewers())
		    if (!this.viewersJoinedTimes.containsKey(viewer.toLowerCase()))
			this.viewersJoinedTimes.put(viewer.toLowerCase(), System.currentTimeMillis());

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
    
    public void disconnectMixer() {
	if (Config.getSetting("SilentJoin", this.channelName).equalsIgnoreCase("false")) {
	    this.sendMessage(this.getBotName() + " Disconnected!");
	}
	this.disconnect();
	ConsoleUtil.TextToConsole(this, BotType.Twitch, this.channelName, "Left " + this.channelName + " channel", MessageType.Bot, null);
	this.viewersJoinedTimes.clear();
    }
}
