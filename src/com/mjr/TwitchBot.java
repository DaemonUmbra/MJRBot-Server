package com.mjr;

import java.io.IOException;
import java.util.Arrays;

import org.jibble.pircbot.PircBot;

import com.mjr.MJRBot.BotType;
import com.mjr.chatModeration.ChatModeration;
import com.mjr.commands.CommandManager;
import com.mjr.files.Config;
import com.mjr.files.ConfigMain;
import com.mjr.files.PointsSystem;
import com.mjr.files.Ranks;
import com.mjr.threads.Announcements;
import com.mjr.threads.CheckFollowers;
import com.mjr.threads.Followers;
import com.mjr.threads.PointsThread;

public class TwitchBot extends PircBot {
    public static String[] mods;
    public String[] viewers;

    private String stream = "";
    public boolean ConnectedToChannel = false;

    public String channelName = "";

    public TwitchBot(String channelName) {
	super();
	this.channelName = channelName;
    }

    private final CommandManager commands = new CommandManager();

    public void init() {
	try {
	    this.ConnectToTwitch();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	this.setChannel("#" + channelName);
	this.joinChannel(channelName);
	ConsoleUtil.TextToConsole("Joined " + channelName.substring(channelName.indexOf("#") + 1) + " channel", "Bot", null);
	this.setVerbose(true);
    }

    @Override
    public void onMessage(final String channel, final String sender, final String login, final String hostname, final String message) {
	// GUI
	ConsoleUtil.TextToConsole(message, "Chat", sender);
	if (mods != null)
	    if (Arrays.asList(mods).toString().toLowerCase().contains(this.getBotName().toLowerCase()))
		ChatModeration.onCommand(BotType.Twitch, MJRBot.getTwitchBotByChannelName(channel), channel, sender, login, hostname,
			message);

	if (Config.getSetting("Commands").equalsIgnoreCase("true")) {
	    try {
		commands.onCommand(BotType.Twitch, MJRBot.getTwitchBotByChannelName(channel), channel, sender.toLowerCase(), login,
			hostname, message);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	return;
    }

    @Override
    protected void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
	if (notice.contains("The moderators of this room are:")) {
	    notice = notice.substring(notice.indexOf(":") + 2);
	    notice += ", " + stream.substring(stream.indexOf("#") + 1);
	    mods = notice.split(", ");
	    if (mods == null) {
		ConsoleUtil.TextToConsole("There was a problem getting the moderators of this channel!", "Bot", null);
		return;
	    }
	    if (mods.length < 1)
		ConsoleUtil.TextToConsole("This channel has no moderators!", "Bot", null);
	    else
		ConsoleUtil.TextToConsole("Bot has the Moderators!", "Bot", null);
	}
    }

    @Override
    public void onPrivateMessage(String sender, String login, String hostname, String message) {
    }

    @Override
    protected void onUnknown(String line) {
	if (line.contains("tmi.twitch.tv RECONNECT")) {
	    try {
		this.viewers = new String[0];
		this.ConnectedToChannel = false;
		this.disconnect();
		Thread.sleep(60000);
		this.ConnectToTwitch();
		this.joinChannel(this.getChannel());
	    } catch (IOException | InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
	if (sender.equalsIgnoreCase(ConfigMain.getSetting("TwitchUsername"))) {
	    try {
		// Load Config file
		Config.load(BotType.Twitch, channel);
		// Load PointsSystem
		if (Config.getSetting("Points").equalsIgnoreCase("true")) {
		    PointsSystem.load(BotType.Twitch, channel);
		}
		// Load Ranks
		if (Config.getSetting("Ranks").equalsIgnoreCase("true")) {
		    Ranks.load(channel);
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
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    this.sendMessage(stream, "/mods");
	    if (Config.getSetting("SilentJoin").equalsIgnoreCase("false")) {
		this.sendMessage(stream, this.getNick() + " Connected!");
	    }
	    ConnectedToChannel = true;
	    Viewers.getViewers();
	    Followers followers = new Followers();
	    followers.start();
	} else {
	    ConsoleUtil.TextToConsole(sender + " has joined!", "Bot", null);
	    if (Config.getSetting("Points").equalsIgnoreCase("true")) {
		if (!PointsSystem.isOnList(sender)) {
		    PointsSystem.setPoints(sender, Integer.parseInt(Config.getSetting("StartingPoints")));
		}
	    }
	    if (Config.getSetting("Ranks").equalsIgnoreCase("true")) {
		if (!Ranks.isOnList(sender)) {
		    Ranks.setRank(sender, "None");
		}
	    }
	    if (viewers != null) {
		if (!Arrays.asList(viewers).toString().contains(sender)) {
		    String newviewers = Arrays.asList(viewers).toString();
		    newviewers = newviewers + "," + sender;
		    newviewers = newviewers.replace(" ", "");
		    newviewers = newviewers.replace("[", "");
		    newviewers = newviewers.replace("]", "");
		    Arrays.fill(this.viewers, "");
		    viewers = newviewers.split(",");
		}
	    } else {
		viewers = new String[1];
		viewers[0] = sender;
	    }
	    if (!PointsThread.viewersJoinedTimes.containsKey(sender.toLowerCase()))
		PointsThread.viewersJoinedTimes.put(sender.toLowerCase(), System.currentTimeMillis());
	}
    }

    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
	ConsoleUtil.TextToConsole(sender + " has left!", "Bot", null);
	if (Arrays.asList(viewers).toString().contains(sender)) {
	    String newviewers = Arrays.asList(viewers).toString();
	    newviewers = newviewers.replace(", " + sender, "");
	    newviewers = newviewers.replace(" ", "");
	    newviewers = newviewers.replace("[", "");
	    newviewers = newviewers.replace("]", "");
	    viewers = new String[0];
	    if (newviewers != "") {
		viewers = newviewers.split(",");
	    }
	}
	if (PointsThread.viewersJoinedTimes.containsKey(sender.toLowerCase()))
	    PointsThread.viewersJoinedTimes.remove(sender.toLowerCase());

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
		    ConsoleUtil.TextToConsole("Connecting to Twitch!", "Bot", null);
		    String pass = ConfigMain.getSetting("TwitchPassword");
		    this.connect("irc.chat.twitch.tv", 6667, pass);
		    this.sendRawLine("CAP REQ :twitch.tv/commands");
		    // bot.sendRawLine("CAP REQ :twitch.tv/tags");
		    this.sendRawLine("CAP REQ :twitch.tv/membership");
		} catch (Exception e1) {
		    e1.printStackTrace();
		    ConsoleUtil.TextToConsole("Failed to connect to Twitch! Check your internet connection!", "Bot", null);
		    return;
		}

	    } else {
		ConsoleUtil.TextToConsole("Your already connected using these login details!", "Bot", null);
		return;
	    }

	} else {
	    ConsoleUtil.TextToConsole(
		    "Error! No Login details were set! Go to settings to enter them! \n Use the Reconnect button when done!", "Bot", null);
	    return;
	}
	if (this.isConnected()) {
	    ConsoleUtil.TextToConsole("Connected to Twitch!", "Bot", null);
	} else
	    ConsoleUtil.TextToConsole("Connection to Twitch failed, check your login details!", "Bot", null);
    }

    public void MessageToChat(String message) {
	if (mods != null && !Arrays.asList(mods).contains(this.getBotName().toLowerCase())) {
	    try {
		Thread.sleep(3000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
	this.sendMessage(stream, message);
	ConsoleUtil.TextToConsole(message, "Chat", this.getName());
	System.out.println(message);
    }

    public void MessageToConsole(String message) {
	ConsoleUtil.TextToConsole(message, "Bot: ", null);
    }

    public String getChannel() {
	return stream;
    }

    public String getBotName() {
	return this.getName();
    }

    public void setChannel(String stream) {
	this.stream = stream;
    }

    public void disconnectTwitch() {
	this.disconnect();
	ConsoleUtil.TextToConsole("Left " + this.getChannel() + " channel", "Bot", null);
	this.viewers = new String[0];
	this.ConnectedToChannel = false;
	this.setChannel("");
    }
}
