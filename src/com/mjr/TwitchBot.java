package com.mjr;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import org.jibble.pircbot.PircBot;

import com.mjr.chatModeration.ChatModeration;
import com.mjr.commands.CommandManager;
import com.mjr.files.Config;
import com.mjr.files.ConfigMain;
import com.mjr.files.PointsSystem;
import com.mjr.files.Ranks;
import com.mjr.threads.Followers;

public class TwitchBot extends PircBot {
    public static String[] mods;
    public String[] viewers;
    public HashMap<String, Long> viewersJoinedTimes = new HashMap<String, Long>();

    private String stream = "";
    public boolean ConnectedToChannel = false;

    private final CommandManager commands = new CommandManager();

    @Override
    public void onMessage(final String channel, final String sender, final String login, final String hostname, final String message) {
	// GUI
	ConsoleUtil.TextToConsole(message, "Chat", sender);
	if (mods != null)
	    if (Arrays.asList(mods).toString().toLowerCase().contains(this.getBotName().toLowerCase()))
		ChatModeration.onCommand(MJRBot.getTwitchBot(), channel, sender, login, hostname, message);

	if (Config.getSetting("Commands").equalsIgnoreCase("true")) {
	    try {
		commands.onCommand(MJRBot.getTwitchBot(), channel, sender.toLowerCase(), login, hostname, message);
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
	System.out.println("HERE" + line);
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
	if (sender.equalsIgnoreCase(ConfigMain.getSetting("TwitchUsername"))) {
	    try {
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
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    MJRBot.getTwitchBot().sendMessage(stream, "/mods");
	    if (Config.getSetting("SilentJoin").equalsIgnoreCase("false")) {
		MJRBot.getTwitchBot().sendMessage(stream, MJRBot.getTwitchBot().getNick() + " Connected!");
	    }
	    ConnectedToChannel = true;
	    Viewers.getViewers();
	    Followers followers = new Followers();
	    followers.start();
	} else {
	    ConsoleUtil.TextToConsole(sender + " has joined!", "Bot", null);
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
	    if (!viewersJoinedTimes.containsKey(sender.toLowerCase()))
		viewersJoinedTimes.put(sender.toLowerCase(), System.currentTimeMillis());
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
	if (viewersJoinedTimes.containsKey(sender.toLowerCase()))
	    viewersJoinedTimes.remove(sender.toLowerCase());

    }

    public void ConnectToTwitch() throws IOException {
	ConfigMain.load();
	if (!ConfigMain.getSetting("TwitchUsername").equals("") && !ConfigMain.getSetting("TwitchPassword").equals("")
		&& !(ConfigMain.getSetting("TwitchUsername") == null) && !(ConfigMain.getSetting("TwitchPassword") == null)) {
	    if (!MJRBot.getTwitchBot().ConnectedToChannel) {
		if (MJRBot.getTwitchBot().isConnected()) {
		    MJRBot.getTwitchBot().ConnectedToChannel = false;
		    MJRBot.getTwitchBot().disconnect();
		}
		MJRBot.getTwitchBot().setName(ConfigMain.getSetting("TwitchUsername"));
		try {
		    ConsoleUtil.TextToConsole("Connecting to Twitch!", "Bot", null);
		    String pass = ConfigMain.getSetting("TwitchPassword");
		    MJRBot.getTwitchBot().connect("irc.chat.twitch.tv", 6667, pass);
		    MJRBot.getTwitchBot().sendRawLine("CAP REQ :twitch.tv/commands");
		    // bot.sendRawLine("CAP REQ :twitch.tv/tags");
		    MJRBot.getTwitchBot().sendRawLine("CAP REQ :twitch.tv/membership");
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
	if (MJRBot.getTwitchBot().isConnected()) {
	    ConsoleUtil.TextToConsole("Connected to Twitch!", "Bot", null);
	} else
	    ConsoleUtil.TextToConsole("Connection to Twitch failed, check your login details!", "Bot", null);
    }

    public void MessageToChat(String message) {
	if (mods != null && !Arrays.asList(mods).contains(MJRBot.getTwitchBot().getBotName().toLowerCase())) {
	    try {
		Thread.sleep(3000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
	MJRBot.getTwitchBot().sendMessage(stream, message);
	ConsoleUtil.TextToConsole(message, "Chat", MJRBot.getTwitchBot().getName());
	System.out.println(message);
    }

    public void MessageToConsole(String message) {
	ConsoleUtil.TextToConsole(message, "Bot: ", null);
    }

    public String getChannel() {
	return stream;
    }

    public String getBotName() {
	return MJRBot.getTwitchBot().getName();
    }

    public void setChannel(String stream) {
	this.stream = stream;
    }

    public void disconnectTwitch() {
	MJRBot.getTwitchBot().disconnect();
	ConsoleUtil.TextToConsole("Left " + MJRBot.getTwitchBot().getChannel() + " channel", "Bot", null);
	MJRBot.getTwitchBot().viewers = new String[0];
	MJRBot.getTwitchBot().ConnectedToChannel = false;
	// GUI_Main.viewers.setText("Viewers: 0");
	// GUI_Main.followers.setText("Followers: 0");
	// GUI_Main.viewerslist.setText(null);
	MJRBot.getTwitchBot().setChannel("");
    }
}
