package com.mjr;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

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

    public MixerBot() {
	super(ConfigMain.getSetting("MixerClientID"), ConfigMain.getSetting("MixerUsername/BotName"));
    }

    private final CommandManager commands = new CommandManager();

    @Override
    protected void onMessage(String sender, String message) {
	ConsoleUtil.TextToConsole(message, "Chat", sender);
	try {
	    commands.onCommand(this, MJRBot.getChannel(), sender, null, null, message);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @Override
    protected void onJoin(String sender) {
	MJRBot.getMixerBot().addViewer(sender);
	if (!PointsThread.viewersJoinedTimes.containsKey(sender.toLowerCase()))
	    PointsThread.viewersJoinedTimes.put(sender.toLowerCase(), System.currentTimeMillis());
    }

    @Override
    protected void onPart(String sender) {
	MJRBot.getMixerBot().removeViewer(sender);
	if (PointsThread.viewersJoinedTimes.containsKey(sender.toLowerCase()))
	    PointsThread.viewersJoinedTimes.remove(sender.toLowerCase());
    }

    public void joinChannel(String channel) throws InterruptedException, ExecutionException, IOException {
	MJRBot.getMixerBot().setdebug(true);
	MJRBot.getMixerBot().joinMixerChannel(channel);
	if (MJRBot.getMixerBot().isConnected() && MJRBot.getMixerBot().isAuthenticated()) {
	    // Load Config file
	    Config.load();
	    // Load PointsSystem
	    if (Config.getSetting("Points").equalsIgnoreCase("true")) {
		PointsSystem.load();
	    }
	    // Load Ranks
	    if (Config.getSetting("Ranks").equalsIgnoreCase("true")) {
		Ranks.load();
	    }

	    // Start Threads
	    if (Config.getSetting("Points").equalsIgnoreCase("true")) {
		PointsThread pointsThread = new PointsThread();
		pointsThread.start();
	    }
	    if (Config.getSetting("Announcements").equalsIgnoreCase("true")) {
		Announcements announcementsThread = new Announcements();
		announcementsThread.start();
	    }
	    if (Config.getSetting("FollowerCheck").equalsIgnoreCase("true")) {
		CheckFollowers followersThread = new CheckFollowers();
		followersThread.start();
	    }

	    for (String viewer : this.getViewers())
		if (!PointsThread.viewersJoinedTimes.containsKey(viewer.toLowerCase()))
		    PointsThread.viewersJoinedTimes.put(viewer.toLowerCase(), System.currentTimeMillis());

	    ConsoleUtil.TextToConsole("MJRBot is Connected & Authenticated to Mixer!", "Chat", null);
	    if (Config.getSetting("SilentJoin").equalsIgnoreCase("false"))
		MJRBot.getMixerBot().sendMessage(MJRBot.getMixerBot().getBotName() + " Connected!");
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
