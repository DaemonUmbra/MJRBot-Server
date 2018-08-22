package com.mjr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.mjr.ConsoleUtil.MessageType;
import com.mjr.MJRBot.BotType;
import com.mjr.commands.CommandManager;
import com.mjr.games.MathsGame;
import com.mjr.games.RacingGame;
import com.mjr.mjrmixer.MJR_MixerBot;
import com.mjr.storage.Config;
import com.mjr.storage.ConfigMain;
import com.mjr.storage.EventLog;
import com.mjr.storage.PointsSystem;
import com.mjr.storage.RankSystem;
import com.mjr.storage.EventLog.EventType;
import com.mjr.threads.AnnouncementsThread;
import com.mjr.threads.BankHeistThread;
import com.mjr.threads.CheckForNewFollowersThread;
import com.mjr.threads.GetFollowersThread;
import com.mjr.threads.GetViewersThread;
import com.mjr.threads.PointsThread;
import com.mjr.threads.RaceStartThread;

public class MixerBot extends MJR_MixerBot {

    public String channelName = "";
    public boolean giveAwayActive = false;

    private final CommandManager commands = new CommandManager();

    public GetViewersThread getViewersThread;
    public PointsThread pointsThread;
    public AnnouncementsThread announcementsThread;
    public CheckForNewFollowersThread followersThread;
    public GetFollowersThread getFollowersThread;
    public BankHeistThread bankHeistThread;
    public RaceStartThread racingThread;

    public HashMap<String, Integer> usersCooldowns = new HashMap<String, Integer>();
    public HashMap<String, Long> viewersJoinedTimes = new HashMap<String, Long>();
    public HashMap<String, Integer> bankHeistEnteredUsers = new HashMap<String, Integer>();
    public List<String> linkPermitedUsers = new ArrayList<String>();
    public List<String> giveawayEnteredUsers = new ArrayList<String>();

    public MathsGame mathsGame = new MathsGame();
    public RacingGame racingGame = new RacingGame();

    public MixerBot(String channelName) {
	super(ConfigMain.getSetting("MixerClientID"), ConfigMain.getSetting("MixerAuthCode"),
		ConfigMain.getSetting("MixerUsername/BotName"));
	this.channelName = channelName;
    }

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
	if (Config.getSetting("Points", this.channelName).equalsIgnoreCase("true")) {
	    if (!PointsSystem.isOnList(sender, this.channelName)) {
		PointsSystem.setPoints(sender, Integer.parseInt(Config.getSetting("StartingPoints", this.channelName)), this.channelName,
			false, false);
	    }
	}
	if (Config.getSetting("Ranks", this.channelName).equalsIgnoreCase("true")) {
	    if (!RankSystem.isOnList(sender, this.channelName)) {
		RankSystem.setRank(sender, "None", this.channelName);
	    }
	}
	EventLog.addEvent(this.channelName, sender, "Joined the channel (Mixer)", EventType.User);
    }

    @Override
    protected void onPart(String sender) {
	if (this.viewersJoinedTimes.containsKey(sender.toLowerCase()))
	    this.viewersJoinedTimes.remove(sender.toLowerCase());
	EventLog.addEvent(this.channelName, sender, "Left the channel (Mixer)", EventType.User);
    }

    public void joinChannel(String channel) {
	try {
	    this.setdebug(true);
	    this.joinMixerChannel(channel);
	    if (this.isConnected() && this.isAuthenticated()) {
		// Start Threads
		pointsThread = new PointsThread(BotType.Mixer, channel);
		pointsThread.start();
		announcementsThread = new AnnouncementsThread(BotType.Mixer, channel);
		announcementsThread.start();
		// CheckForNewFollowersThread followersThread = new CheckForNewFollowersThread(BotType.Mixer, channel); TODO Add for Mixer
		// followersThread.start();

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
