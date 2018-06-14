package com.mjr;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.mjr.MJRBot.BotType;
import com.mjr.commands.CommandManager;
import com.mjr.files.Config;
import com.mjr.files.ConfigMain;
import com.mjr.files.PointsSystem;
import com.mjr.files.Ranks;
import com.mjr.mjrmixer.MJR_MixerBot;
import com.mjr.threads.Announcements;
import com.mjr.threads.CheckFollowers;
import com.mjr.threads.PointsThread;

public class MixerBot extends MJR_MixerBot {

    public String channelName = "";

    public MixerBot() {
	super(ConfigMain.getSetting("MixerClientID"), ConfigMain.getSetting("MixerUsername/BotName"));
    }

    private final CommandManager commands = new CommandManager();

    @Override
    protected void onMessage(String sender, String message) {
	ConsoleUtil.TextToConsole(message, "Chat", sender);
	try {
	    commands.onCommand(BotType.Mixer, this, this.channelName, sender, null, null, message);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @Override
    protected void onJoin(String sender) {
	this.addViewer(sender);
	if (!PointsThread.viewersJoinedTimes.containsKey(sender.toLowerCase()))
	    PointsThread.viewersJoinedTimes.put(sender.toLowerCase(), System.currentTimeMillis());
    }

    @Override
    protected void onPart(String sender) {
	this.removeViewer(sender);
	if (PointsThread.viewersJoinedTimes.containsKey(sender.toLowerCase()))
	    PointsThread.viewersJoinedTimes.remove(sender.toLowerCase());
    }

    public void joinChannel(String channel) throws InterruptedException, ExecutionException, IOException {
	this.setdebug(true);
	this.joinMixerChannel(channel);
	if (this.isConnected() && this.isAuthenticated()) {
	    // Load Config file
	    Config.load(BotType.Mixer, channel);
	    // Load PointsSystem
	    if (Config.getSetting("Points").equalsIgnoreCase("true")) {
		PointsSystem.load(BotType.Mixer, channel);
	    }
	    // Load Ranks
	    if (Config.getSetting("Ranks").equalsIgnoreCase("true")) {
		Ranks.load(channel);
	    }

	    // Start Threads
	    if (Config.getSetting("Points").equalsIgnoreCase("true")) {
		PointsThread pointsThread = new PointsThread(BotType.Mixer, channel);
		pointsThread.start();
	    }
	    if (Config.getSetting("Announcements").equalsIgnoreCase("true")) {
		Announcements announcementsThread = new Announcements(BotType.Mixer, channel);
		announcementsThread.start();
	    }
	    if (Config.getSetting("FollowerCheck").equalsIgnoreCase("true")) {
		CheckFollowers followersThread = new CheckFollowers(BotType.Mixer, channel);
		followersThread.start();
	    }

	    for (String viewer : this.getViewers())
		if (!PointsThread.viewersJoinedTimes.containsKey(viewer.toLowerCase()))
		    PointsThread.viewersJoinedTimes.put(viewer.toLowerCase(), System.currentTimeMillis());

	    ConsoleUtil.TextToConsole("MJRBot is Connected & Authenticated to Mixer!", "Chat", null);
	    if (Config.getSetting("SilentJoin").equalsIgnoreCase("false"))
		this.sendMessage(this.getBotName() + " Connected!");
	} else
	    ConsoleUtil.TextToConsole("Theres been problem, connecting to Mixer, Please check settings are corrrect!", "Chat", null);
    }

    @Override
    protected void onDebugMessage() {
	for (String message : this.getOutputMessages())
	    ConsoleUtil.TextToConsole(message, "Bot", null);
	this.clearOutputMessages();
    }
}
