package com.mjr;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jibble.pircbot.PircBot;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot.ConnectionType;
import com.mjr.MJRBot.StorageType;
import com.mjr.chatModeration.ChatModeration;
import com.mjr.commands.CommandManager;
import com.mjr.games.MathsGame;
import com.mjr.games.RacingGame;
import com.mjr.sql.MySQLConnection;
import com.mjr.storage.Config;
import com.mjr.storage.ConfigMain;
import com.mjr.storage.EventLog;
import com.mjr.storage.EventLog.EventType;
import com.mjr.storage.PointsSystem;
import com.mjr.storage.RankSystem;
import com.mjr.threads.AnnouncementsThread;
import com.mjr.threads.AutoPointsThread;
import com.mjr.threads.BankHeistThread;
import com.mjr.threads.RaceStartThread;
import com.mjr.threads.twitch.GetFollowersThread;
import com.mjr.threads.twitch.GetSubscribersThread;
import com.mjr.threads.twitch.GetViewersThread;
import com.mjr.util.ConsoleUtil;
import com.mjr.util.ConsoleUtil.MessageType;
import com.mjr.util.HTTPConnect;
import com.mjr.util.TwitchMixerAPICalls;
import com.mjr.util.Utilities;

public class TwitchBot extends PircBot {

	private String stream = "";
	public boolean ConnectedToChannel = false;

	public int channelID = 0;
	public String channelName = "";
	public boolean giveAwayActive = false;

	private final CommandManager commands = new CommandManager();

	public GetViewersThread getViewersThread;
	public GetSubscribersThread getSubscribersThread;
	public AutoPointsThread pointsThread;
	public AnnouncementsThread announcementsThread;
	public GetFollowersThread getFollowersThread;
	public BankHeistThread bankHeistThread;
	public RaceStartThread racingThread;

	public List<String> moderators = new ArrayList<String>();
	public List<String> viewers = new ArrayList<String>();
	public List<String> followers = new ArrayList<String>();
	public List<String> subscribers = new ArrayList<String>();
	public List<String> vips = new ArrayList<String>();
	public HashMap<String, Integer> usersCooldowns = new HashMap<String, Integer>();
	public HashMap<String, Long> viewersJoinedTimes = new HashMap<String, Long>();
	public HashMap<String, Integer> bankHeistEnteredUsers = new HashMap<String, Integer>();
	public List<String> linkPermitedUsers = new ArrayList<String>();
	public List<String> giveawayEnteredUsers = new ArrayList<String>();

	public MathsGame mathsGame = new MathsGame();
	public RacingGame racingGame = new RacingGame();

	public void init(String channelName, int channelID) {
		try {
			this.ConnectToTwitch();
		} catch (IOException e) {
			MJRBot.getLogger().info(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
		}
		this.channelName = channelName.toLowerCase();
		this.channelID = channelID;
		this.setChannel("#" + channelName.toLowerCase());
		this.joinChannel(this.getChannel());
		ConsoleUtil.textToConsole(this, BotType.Twitch, "Joined " + channelName.substring(channelName.indexOf("#") + 1) + " channel", MessageType.ChatBot, null);
		if (ConfigMain.getSetting("TwitchVerboseMessages").equalsIgnoreCase("true"))
			this.setVerbose(true);
	}

	@Override
	public void onMessage(final String channel, final String sender, final String login, final String hostname, final String userID, final boolean subscriber, final String message) {
		if (subscriber) {
			if (!this.subscribers.contains(sender.toLowerCase()))
				this.subscribers.add(sender.toLowerCase());
		}
		checkFollower(sender);
		if (!this.viewers.contains(sender.toLowerCase())) {
			this.viewers.add(sender.toLowerCase());
			EventLog.addEvent(BotType.Twitch, this, sender, "Joined the channel (Twitch)", EventType.User);
		}
		if (!this.viewersJoinedTimes.containsKey(sender.toLowerCase()))
			this.viewersJoinedTimes.put(sender.toLowerCase(), System.currentTimeMillis());
		// GUI
		ConsoleUtil.textToConsole(this, BotType.Twitch, message, MessageType.Chat, sender);
		CrossChatLink.sendMessageAcrossPlatforms(BotType.Twitch, this, sender, message);
		if (moderators != null)
			if (Arrays.asList(moderators).toString().toLowerCase().contains(this.getBotName().toLowerCase()))
				ChatModeration.onCommand(BotType.Twitch, this, sender, login, hostname, message);

		if (Config.getSetting("Commands", this.channelID).equalsIgnoreCase("true")) {
			try {
				commands.onCommand(BotType.Twitch, this, sender.toLowerCase(), login, hostname, message);
			} catch (IOException e) {
				MJRBot.logErrorMessage(e);
			}
		}
		return;
	}

	public void checkFollower(final String sender) {
		String result = "";
		try {
			result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchCheckFollow(this.channelID, sender));
			if (result.contains("created_at"))
				if (!this.followers.contains(sender.toLowerCase()))
					this.followers.add(sender.toLowerCase());
				else if (this.followers.contains(sender.toLowerCase()))
					this.followers.remove(sender.toLowerCase());
		} catch (FileNotFoundException e) {
			if (this.followers.contains(sender.toLowerCase()))
				this.followers.remove(sender.toLowerCase());
		} catch (Exception e) {
			MJRBot.logErrorMessage(e);
		}
	}

	@Override
	public void onMessageExtra(final String line, final String channel, final String sender, final String login, final String hostname, final String message) {
		if (line.contains("bits=")) {
			String bitsAmount = line.substring(line.indexOf("bits=") + 5);
			bitsAmount = bitsAmount.substring(0, bitsAmount.indexOf(";"));
			if (Config.getSetting("BitsAlerts", this.channelID).equalsIgnoreCase("true"))
				Utilities.sendMessage(BotType.Twitch, this, sender + " just gave " + bitsAmount + " bit(s) to the channel!");
			ConsoleUtil.textToConsole(this, BotType.Twitch, sender + " just gave " + bitsAmount + " bit(s) to the channel!", MessageType.ChatBot, null);
			EventLog.addEvent(BotType.Twitch, this, sender, "Just gave " + bitsAmount + " bit(s) to the channel!", EventType.Bits);
		}
	}

	@Override
	protected void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
		if (notice.contains("Now hosting")) {
			ConsoleUtil.textToConsole(this, BotType.Twitch, notice.substring(notice.indexOf("Now hosting")), MessageType.ChatBot, null);
		}
		if (notice.contains("Exited host mode")) {
			ConsoleUtil.textToConsole(this, BotType.Twitch, "No longer hosting a channel!", MessageType.ChatBot, null);
		}
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
				ConsoleUtil.textToConsole(this, BotType.Twitch, "There was a problem getting the moderators of this channel!", MessageType.ChatBot, null);
			}
			if (moderators == null) {
				ConsoleUtil.textToConsole(this, BotType.Twitch, "There was a problem getting the moderators of this channel!", MessageType.ChatBot, null);
				return;
			}
			if (moderators.size() > 1)
				ConsoleUtil.textToConsole(this, BotType.Twitch, "Bot has the list of current moderators!", MessageType.ChatBot, null);
		} else if (notice.contains("There are no moderators of this channel"))
			ConsoleUtil.textToConsole(this, BotType.Twitch, "This channel has no moderators!", MessageType.ChatBot, null);
	}

	@Override
	public void onPrivateMessage(String sender, String login, String hostname, String channel, String message) {
		if (channel.equalsIgnoreCase(this.channelName)) {
			if (message.contains("is now hosting you.")) {
				Utilities.sendMessage(BotType.Twitch, channel, message);
				ConsoleUtil.textToConsole(this, BotType.Twitch, message, MessageType.ChatBot, null);
			}
		}
	}

	@Override
	protected void onUnknown(String line) {
		if (line.contains("tmi.twitch.tv RECONNECT")) { // When Twitch tells the bot instance to reconnect
			MJRBot.logErrorMessage(TwitchBot.getChannelNameFromChannelID(channelID) + " has triggered a Reconnect event!");
			this.disconnectTwitch();
			ChatBotManager.removeTwitchBot(this); // ChannelListUpdateThread will add it back as a new bot instance
		} else if (line.contains("msg-id=sub") && !line.contains("msg-param-recipient-display-name=")) {
			String user = line.substring(line.indexOf("display-name=") + 13);
			user = user.substring(0, user.indexOf(';'));
			if (line.contains("msg-param-sub-plan=Prime")) {
				if (Config.getSetting("SubAlerts", this.channelID).equalsIgnoreCase("true"))
					Utilities.sendMessage(BotType.Twitch, this, user + " just subscribed to the channel using Twitch Prime!");
				ConsoleUtil.textToConsole(this, BotType.Twitch, user + " just subscribed to the channel using Twitch Prime!", MessageType.ChatBot, null);
				EventLog.addEvent(BotType.Twitch, this, user, "Just subscribed to the channel using Twitch Prime!", EventType.Sub);
				this.subscribers.add(user);
			} else {
				if (Config.getSetting("SubAlerts", this.channelID).equalsIgnoreCase("true"))
					Utilities.sendMessage(BotType.Twitch, this, user + " just subscribed to the channel!");
				ConsoleUtil.textToConsole(this, BotType.Twitch, user + " just subscribed to the channel!", MessageType.ChatBot, null);
				EventLog.addEvent(BotType.Twitch, this, user, "Just subscribed to the channel!", EventType.Sub);
				this.subscribers.add(user);
			}
		} else if (line.contains("msg-id=resub")) {
			String user = line.substring(line.indexOf("display-name=") + 13);
			user = user.substring(0, user.indexOf(';'));
			String months = line.substring(line.indexOf("msg-param-cumulative-months=") + 28);
			months = months.substring(0, months.indexOf(';'));
			if (line.contains("msg-param-sub-plan=Prime")) {
				if (Config.getSetting("ResubAlerts", this.channelID).equalsIgnoreCase("true"))
					Utilities.sendMessage(BotType.Twitch, this, user + " just resubscribed to the channel using Twitch Prime for " + months + " months in a row!");
				ConsoleUtil.textToConsole(this, BotType.Twitch, user + " just resubscribed to the channel using Twitch Prime for " + months + " months in a row!", MessageType.ChatBot, null);
				EventLog.addEvent(BotType.Twitch, this, user, "Just resubscribed to the channel using Twitch Prime for " + months + " months in a row!", EventType.Sub);
				this.subscribers.add(user);
			} else {
				if (Config.getSetting("ResubAlerts", this.channelID).equalsIgnoreCase("true"))
					Utilities.sendMessage(BotType.Twitch, this, user + " just resubscribed to the channel for " + months + " months in a row!");
				ConsoleUtil.textToConsole(this, BotType.Twitch, user + " just resubscribed to the channel for " + months + " months in a row!", MessageType.ChatBot, null);
				EventLog.addEvent(BotType.Twitch, this, user, "Just resubscribed to the channel for " + months + " months in a row!", EventType.Sub);
				this.subscribers.add(user);
			}
		} else if (line.contains("msg-param-recipient-display-name=")) {
			String gifter = line.substring(line.indexOf("display-name=") + 13);
			gifter = gifter.substring(0, gifter.indexOf(';'));
			String user = line.substring(line.indexOf("msg-param-recipient-display-name=") + 33);
			user = user.substring(0, user.indexOf(';'));
			if (Config.getSetting("GiftSubAlerts", this.channelID).equalsIgnoreCase("true"))
				Utilities.sendMessage(BotType.Twitch, this, gifter + " has gifted a sub to " + user);
			ConsoleUtil.textToConsole(this, BotType.Twitch, gifter + " has gifted a sub to " + user, MessageType.ChatBot, null);
			EventLog.addEvent(BotType.Twitch, this, gifter, "Has gifted a sub to " + user, EventType.Sub);
			this.subscribers.add(user);
		}
	}

	@Override
	protected void onJoin(String channel, String sender, String login, String hostname) {
		if (sender.equalsIgnoreCase(ConfigMain.getSetting("TwitchUsername"))) {
			ConnectedToChannel = true;

			// Start Threads
			pointsThread = new AutoPointsThread(BotType.Twitch, this);
			pointsThread.start();
			announcementsThread = new AnnouncementsThread(BotType.Twitch, this);
			announcementsThread.start();

			this.sendMessage(this.stream, "/mods");
			if (Config.getSetting("SilentJoin", this.channelID).equalsIgnoreCase("false")) {
				this.sendMessage(this.stream, this.getNick() + " Connected!");
			}
			getViewersThread = new GetViewersThread(this);
			getViewersThread.start();
			getFollowersThread = new GetFollowersThread(this, BotType.Twitch);
			getFollowersThread.start();
			if (MJRBot.storageType == StorageType.Database && MJRBot.connectionType == ConnectionType.Database) {
				getSubscribersThread = new GetSubscribersThread(this, BotType.Twitch);
				getSubscribersThread.start();
			} else
				ConsoleUtil.textToConsole("Getting Subscribers Thread for Twtich has been disabled, as it is currently not supported on the file based storage type or/and when manual mode is used!");
		}

		ConsoleUtil.textToConsole(this, BotType.Twitch, sender + " has joined!", MessageType.ChatBot, null);
		if (Config.getSetting("Points", this.channelID).equalsIgnoreCase("true")) {
			if (!PointsSystem.isOnList(sender, BotType.Twitch, this)) {
				PointsSystem.setPoints(sender, Integer.parseInt(Config.getSetting("StartingPoints", BotType.Twitch, this)), BotType.Twitch, this, false, false);
			}
		}
		if (Config.getSetting("Ranks", this.channelID).equalsIgnoreCase("true")) {
			if (!RankSystem.isOnList(sender, BotType.Twitch, this)) {
				RankSystem.setRank(sender, "None", BotType.Twitch, this);
			}
		}
		if (!this.viewers.contains(sender.toLowerCase())) {
			this.viewers.add(sender.toLowerCase());
			EventLog.addEvent(BotType.Twitch, this, sender, "Joined the channel (Twitch)", EventType.User);
		}
		if (!this.viewersJoinedTimes.containsKey(sender.toLowerCase()))
			this.viewersJoinedTimes.put(sender.toLowerCase(), System.currentTimeMillis());
	}

	@Override
	protected void onPart(String channel, String sender, String login, String hostname) {
		ConsoleUtil.textToConsole(this, BotType.Twitch, sender + " has left!", MessageType.ChatBot, null);
		if (this.viewers.contains(sender.toLowerCase())) {
			this.viewers.remove(sender.toLowerCase());
			EventLog.addEvent(BotType.Twitch, this, sender, "Left the channel (Twitch)", EventType.User);
		}
		if (this.viewersJoinedTimes.containsKey(sender.toLowerCase()))
			this.viewersJoinedTimes.remove(sender.toLowerCase());
	}

	@Override
	protected void onDisconnect() {
		MJRBot.bot.sendAdminEventMessage(TwitchBot.getChannelNameFromChannelID(channelID) + " has triggered a onDisconnect event!");
		disconnectTwitch();
		ChatBotManager.removeTwitchBot(this);
	}

	public void ConnectToTwitch() throws IOException {
		if (!ConfigMain.getSetting("TwitchUsername").equals("") && !ConfigMain.getSetting("TwitchPassword").equals("") && !(ConfigMain.getSetting("TwitchUsername") == null) && !(ConfigMain.getSetting("TwitchPassword") == null)) {
			if (!this.ConnectedToChannel) {
				if (this.isConnected()) {
					this.ConnectedToChannel = false;
					this.disconnect();
				}
				this.setName(ConfigMain.getSetting("TwitchUsername"));
				try {
					ConsoleUtil.textToConsole(this, BotType.Twitch, "Connecting to Twitch!", MessageType.ChatBot, null);
					String pass = ConfigMain.getSetting("TwitchPassword");
					this.connect("irc.chat.twitch.tv", 6667, pass);
					this.sendRawLine("CAP REQ :twitch.tv/commands");
					this.sendRawLine("CAP REQ :twitch.tv/membership");
					this.sendRawLine("CAP REQ :twitch.tv/tags");
				} catch (Exception e) {
					MJRBot.logErrorMessage(e);
					ConsoleUtil.textToConsole(this, BotType.Twitch, "Failed to connect to Twitch! Check your internet connection!", MessageType.ChatBot, null);
					return;
				}

			} else {
				ConsoleUtil.textToConsole(this, BotType.Twitch, "Your already connected using these login details!", MessageType.ChatBot, null);
				return;
			}

		} else {
			ConsoleUtil.textToConsole(this, BotType.Twitch, "Error! No Login details were set! Go to settings to enter them! \n Use the Reconnect button when done!", MessageType.ChatBot, null);
			return;
		}
		if (this.isConnected()) {
			ConsoleUtil.textToConsole(this, BotType.Twitch, "Connected to Twitch!", MessageType.ChatBot, null);
		} else
			ConsoleUtil.textToConsole(this, BotType.Twitch, "Connection to Twitch failed, check your login details!", MessageType.ChatBot, null);
	}

	public void sendMessage(String message) {
		// Used to slow down messages if not a moderator due to Twitch's message delay for normal users
		if (!moderators.isEmpty() && !moderators.contains(this.getBotName().toLowerCase())) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				MJRBot.logErrorMessage(e);
			}
		}
		this.sendMessage(this.stream, message);
		ConsoleUtil.textToConsole(this, BotType.Twitch, message, MessageType.Chat, this.getName());
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

	public void disconnectTwitch() {
		String silentJoin = Config.getSetting("SilentJoin", this.channelID);
		if (silentJoin != null && silentJoin.equalsIgnoreCase("false")) {
			this.sendMessage(this.getBotName() + " Disconnected!");
		}
		this.disconnect();
		ConsoleUtil.textToConsole(this, BotType.Twitch, "Left " + this.getChannel() + " channel", MessageType.ChatBot, null);
		this.viewers.clear();
		this.viewersJoinedTimes.clear();
		this.ConnectedToChannel = false;
		this.setChannel("");
	}

	public static int getChannelIDFromChannelName(String channelName) {
		try {
			ResultSet set = MySQLConnection.executeQuery("SELECT * FROM channels WHERE channel = '" + channelName + "'");
			if (set != null && set.next()) {
				return set.getInt("twitch_channel_id");
			}
		} catch (SQLException e) {
			MJRBot.logErrorMessage(e);
		}
		return 0;
	}
	
	public static String getChannelNameFromChannelID(int channelID) {
		try {
			ResultSet set = MySQLConnection.executeQuery("SELECT * FROM channels WHERE twitch_channel_id = '" + channelID + "'");
			if (set != null && set.next()) {
				return set.getString("name");
			}
		} catch (SQLException e) {
			MJRBot.logErrorMessage(e);
		}
		return null;
	}
}
