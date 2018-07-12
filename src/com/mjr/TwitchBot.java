package com.mjr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jibble.pircbot.PircBot;

import com.mjr.ConsoleUtil.MessageType;
import com.mjr.MJRBot.BotType;
import com.mjr.chatModeration.ChatModeration;
import com.mjr.commands.CommandManager;
import com.mjr.files.Config;
import com.mjr.files.ConfigMain;
import com.mjr.files.PointsSystem;
import com.mjr.files.Ranks;
import com.mjr.threads.AnnouncementsThread;
import com.mjr.threads.CheckForNewFollowersThread;
import com.mjr.threads.GetFollowersThread;
import com.mjr.threads.GetViewersThread;
import com.mjr.threads.PointsThread;
import com.mjr.threads.UserCooldownTickThread;

public class TwitchBot extends PircBot {

    private String stream = "";
    public boolean ConnectedToChannel = false;

    public String channelName = "";

    private final CommandManager commands = new CommandManager();

    public GetViewersThread getViewersThread;
    public PointsThread pointsThread;
    public AnnouncementsThread announcementsThread;
    public CheckForNewFollowersThread followersThread;
    public GetFollowersThread getFollowersThread;
    public UserCooldownTickThread userCooldownTickThread;

    public List<String> moderators = new ArrayList<String>();
    public List<String> viewers = new ArrayList<String>();
    public List<String> followers = new ArrayList<String>();
    public HashMap<String, Integer> usersCooldowns = new HashMap<String, Integer>();
    public HashMap<String, Long> viewersJoinedTimes = new HashMap<String, Long>();

    public void init(String channelName) {
	try {
	    this.ConnectToTwitch();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	this.channelName = channelName.toLowerCase();
	this.setChannel("#" + channelName.toLowerCase());
	this.joinChannel(this.getChannel());
	ConsoleUtil.TextToConsole(this, BotType.Twitch, this.channelName,
		"Joined " + this.channelName.substring(this.channelName.indexOf("#") + 1) + " channel", MessageType.Bot, null);
	if (ConfigMain.getSetting("TwitchVerboseMessages").equalsIgnoreCase("true"))
	    this.setVerbose(true);
    }

    @Override
    public void onMessage(final String channel, final String sender, final String login, final String hostname, final String message) {
	if (!this.viewers.contains(sender.toLowerCase())) {
	    this.viewers.add(sender.toLowerCase());
	}
	if (!this.viewersJoinedTimes.containsKey(sender.toLowerCase()))
	    this.viewersJoinedTimes.put(sender.toLowerCase(), System.currentTimeMillis());
	// GUI
	ConsoleUtil.TextToConsole(this, BotType.Twitch, this.channelName, message, MessageType.Chat, sender);
	if (moderators != null)
	    if (Arrays.asList(moderators).toString().toLowerCase().contains(this.getBotName().toLowerCase()))
		ChatModeration.onCommand(BotType.Twitch, MJRBot.getTwitchBotByChannelName(this.channelName), this.channelName, sender,
			login, hostname, message);

	if (Config.getSetting("Commands", this.channelName).equalsIgnoreCase("true")) {
	    try {
		commands.onCommand(BotType.Twitch, MJRBot.getTwitchBotByChannelName(this.channelName), this.channelName,
			sender.toLowerCase(), login, hostname, message);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	return;
    }

    @Override
    protected void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
	if (notice.contains("The moderators of this channel are:")) {
	    try {
		notice = notice.substring(notice.indexOf(":") + 2);
		notice += ", " + this.stream.substring(this.stream.indexOf("#") + 1);
		for (String moderator : notice.split(", ")) {
		    if (!moderators.contains(moderator.toLowerCase())) {
			moderators.add(moderator.toLowerCase());
		    }
		}
	    } catch (Exception e) {
		ConsoleUtil.TextToConsole(this, BotType.Twitch, this.channelName,
			"There was a problem getting the moderators of this channel!", MessageType.Bot, null);
	    }
	    if (moderators == null) {
		ConsoleUtil.TextToConsole(this, BotType.Twitch, this.channelName,
			"There was a problem getting the moderators of this channel!", MessageType.Bot, null);
		return;
	    }
	    if (moderators.size() > 1)
		ConsoleUtil.TextToConsole(this, BotType.Twitch, this.channelName, "Bot has the list of current moderators!",
			MessageType.Bot, null);
	} else if (notice.contains("There are no moderators of this channel"))
	    ConsoleUtil.TextToConsole(this, BotType.Twitch, this.channelName, "This channel has no moderators!", MessageType.Bot, null);
    }

    @Override
    public void onPrivateMessage(String sender, String login, String hostname, String message) {
    }

    @Override
    protected void onUnknown(String line) {
	if (line.contains("tmi.twitch.tv RECONNECT")) {
	    this.disconnectTwitch();
	    MJRBot.removeTwitchBot(this);
	}
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
	if (sender.equalsIgnoreCase(ConfigMain.getSetting("TwitchUsername"))) {
	    // Start Threads
	    if (Config.getSetting("Points", this.channelName).equalsIgnoreCase("true")) {
		pointsThread = new PointsThread(BotType.Twitch, this.channelName);
		pointsThread.start();
	    }
	    if (Config.getSetting("Announcements", this.channelName).equalsIgnoreCase("true")) {
		announcementsThread = new AnnouncementsThread(BotType.Twitch, this.channelName);
		announcementsThread.start();
	    }
	    if (Config.getSetting("FollowerCheck", this.channelName).equalsIgnoreCase("true")) {
		followersThread = new CheckForNewFollowersThread(this, BotType.Twitch);
		followersThread.start();
	    }
	    this.sendMessage(this.stream, "/mods");
	    if (Config.getSetting("SilentJoin", this.channelName).equalsIgnoreCase("false")) {
		this.sendMessage(this.stream, this.getNick() + " Connected!");
	    }
	    ConnectedToChannel = true;
	    getViewersThread = new GetViewersThread(this);
	    getViewersThread.start();
	    getFollowersThread = new GetFollowersThread(this, BotType.Twitch);
	    getFollowersThread.start();

	    userCooldownTickThread = new UserCooldownTickThread();
	    userCooldownTickThread.start();
	} else {
	    ConsoleUtil.TextToConsole(this, BotType.Twitch, this.channelName, sender + " has joined!", MessageType.Bot, null);
	    if (Config.getSetting("Points", this.channelName).equalsIgnoreCase("true")) {
		if (!PointsSystem.isOnList(sender, this.channelName)) {
		    PointsSystem.setPoints(sender, Integer.parseInt(Config.getSetting("StartingPoints", this.channelName)),
			    this.channelName);
		}
	    }
	    if (Config.getSetting("Ranks", this.channelName).equalsIgnoreCase("true")) {
		if (!Ranks.isOnList(sender, this.channelName)) {
		    Ranks.setRank(sender, "None", this.channelName);
		}
	    }
	    if (!this.viewers.contains(sender.toLowerCase())) {
		this.viewers.add(sender.toLowerCase());
	    }
	    if (!this.viewersJoinedTimes.containsKey(sender.toLowerCase()))
		this.viewersJoinedTimes.put(sender.toLowerCase(), System.currentTimeMillis());
	}
    }

    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
	ConsoleUtil.TextToConsole(this, BotType.Twitch, this.channelName, sender + " has left!", MessageType.Bot, null);
	if (this.viewers.contains(sender.toLowerCase())) {
	    this.viewers.remove(sender.toLowerCase());
	}
	if (this.viewersJoinedTimes.containsKey(sender.toLowerCase()))
	    this.viewersJoinedTimes.remove(sender.toLowerCase());
    }

    public void ConnectToTwitch() throws IOException {
	if (!ConfigMain.getSetting("TwitchUsername").equals("") && !ConfigMain.getSetting("TwitchPassword").equals("")
		&& !(ConfigMain.getSetting("TwitchUsername") == null) && !(ConfigMain.getSetting("TwitchPassword") == null)) {
	    if (!this.ConnectedToChannel) {
		if (this.isConnected()) {
		    this.ConnectedToChannel = false;
		    this.disconnect();
		}
		this.setName(ConfigMain.getSetting("TwitchUsername"));
		try {
		    ConsoleUtil.TextToConsole(this, BotType.Twitch, this.channelName, "Connecting to Twitch!", MessageType.Bot, null);
		    String pass = ConfigMain.getSetting("TwitchPassword");
		    this.connect("irc.chat.twitch.tv", 6667, pass);
		    this.sendRawLine("CAP REQ :twitch.tv/commands");
		    this.sendRawLine("CAP REQ :twitch.tv/membership");
		} catch (Exception e1) {
		    e1.printStackTrace();
		    ConsoleUtil.TextToConsole(this, BotType.Twitch, this.channelName,
			    "Failed to connect to Twitch! Check your internet connection!", MessageType.Bot, null);
		    return;
		}

	    } else {
		ConsoleUtil.TextToConsole(this, BotType.Twitch, this.channelName, "Your already connected using these login details!",
			MessageType.Bot, null);
		return;
	    }

	} else {
	    ConsoleUtil.TextToConsole(this, BotType.Twitch, this.channelName,
		    "Error! No Login details were set! Go to settings to enter them! \n Use the Reconnect button when done!",
		    MessageType.Bot, null);
	    return;
	}
	if (this.isConnected()) {
	    ConsoleUtil.TextToConsole(this, BotType.Twitch, this.channelName, "Connected to Twitch!", MessageType.Bot, null);
	} else
	    ConsoleUtil.TextToConsole(this, BotType.Twitch, this.channelName, "Connection to Twitch failed, check your login details!",
		    MessageType.Bot, null);
    }

    public void MessageToChat(String message) {
	if (!moderators.isEmpty() && !moderators.contains(this.getBotName().toLowerCase())) {
	    try {
		Thread.sleep(3000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
	this.sendMessage(this.stream, message);
	ConsoleUtil.TextToConsole(this, BotType.Twitch, this.channelName, message, MessageType.Chat, this.getName());
    }

    public String getChannel() {
	return this.stream;
    }

    public String getBotName() {
	return this.getName();
    }

    public void setChannel(String stream) {
	this.stream = stream;
    }

    @SuppressWarnings("deprecation")
    public void disconnectTwitch() {
	this.MessageToChat(this.getBotName() + " Disconnected!");
	this.disconnect();
	ConsoleUtil.TextToConsole(this, BotType.Twitch, this.channelName, "Left " + this.getChannel() + " channel", MessageType.Bot, null);
	this.viewers.clear();
	this.viewersJoinedTimes.clear();
	this.ConnectedToChannel = false;
	this.setChannel("");
	if (this.getViewersThread != null)
	    this.getViewersThread.destroy();
	if (this.pointsThread != null)
	    this.pointsThread.destroy();
	if (this.announcementsThread != null)
	    this.announcementsThread.destroy();
	if (this.followersThread != null)
	    this.followersThread.destroy();
	if (this.getFollowersThread != null)
	    this.getFollowersThread.destroy();
	if (this.userCooldownTickThread != null)
	    this.userCooldownTickThread.destroy();
    }
}
