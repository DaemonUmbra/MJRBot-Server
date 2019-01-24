package com.mjr;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.mixer.api.resource.MixerUser.Role;
import com.mixer.api.resource.constellation.events.LiveEvent;
import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot.ConnectionType;
import com.mjr.chatModeration.ChatModeration;
import com.mjr.commands.CommandManager;
import com.mjr.games.MathsGame;
import com.mjr.games.RacingGame;
import com.mjr.mjrmixer.MJR_MixerBot;
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
import com.mjr.util.ConsoleUtil;
import com.mjr.util.ConsoleUtil.MessageType;
import com.mjr.util.HTTPConnect;
import com.mjr.util.TwitchMixerAPICalls;
import com.mjr.util.Utilities;

public class MixerBot extends MJR_MixerBot {

	public String channelName = "";
	public boolean giveAwayActive = false;

	private final CommandManager commands = new CommandManager();

	public AutoPointsThread pointsThread;
	public AnnouncementsThread announcementsThread;
	public GetFollowersThread getFollowersThread;
	public BankHeistThread bankHeistThread;
	public RaceStartThread racingThread;

	public HashMap<String, Integer> usersCooldowns = new HashMap<String, Integer>();
	public HashMap<String, Long> viewersJoinedTimes = new HashMap<String, Long>();
	public HashMap<String, Integer> bankHeistEnteredUsers = new HashMap<String, Integer>();
	public List<String> linkPermitedUsers = new ArrayList<String>();
	public List<String> giveawayEnteredUsers = new ArrayList<String>();
	public List<String> subscribers = new ArrayList<String>();
	public List<String> followers = new ArrayList<String>();

	public MathsGame mathsGame = new MathsGame();
	public RacingGame racingGame = new RacingGame();

	public MixerBot(String channelName) {
		super(ConfigMain.getSetting("MixerClientID"), ConfigMain.getSetting("MixerAuthCode"), ConfigMain.getSetting("MixerUsername/BotName"));
		this.channelName = channelName;
	}

	@Override
	protected void onMessage(String sender, int userId, List<Role> userRoles, String message) {
		checkFollower(sender, userId);
		if (userRoles.contains(Role.SUBSCRIBER)) {
			if (!this.subscribers.contains(sender.toLowerCase()))
				this.subscribers.add(sender.toLowerCase());
		} else {
			if (this.subscribers.contains(sender.toLowerCase()))
				this.subscribers.remove(sender.toLowerCase());
		}
		ConsoleUtil.textToConsole(this, BotType.Mixer, message, MessageType.Chat, sender);
		CrossChatLink.sendMessageAcrossPlatforms(BotType.Mixer, this, sender, message);
		ChatModeration.onCommand(BotType.Mixer, ChatBotManager.getMixerBotByChannelName(this.channelName), sender, null, null, message);
		try {
			commands.onCommand(BotType.Mixer, this, sender, null, null, message);
		} catch (IOException e) {
			MJRBot.logErrorMessage(e);
		}
	}

	public void checkFollower(final String sender, int userId) {
		String result = "";
		int channel_id = 0;
		try {
			ResultSet set = MySQLConnection.executeQueryNoOutput("SELECT * FROM tokens WHERE channel = '" + this.channelName + "' AND platform = 'Mixer'");
			if (set != null && set.next()) {
				channel_id = set.getInt("channel_id");
			}
			result = HTTPConnect.getRequest(TwitchMixerAPICalls.mixerCheckFollow(channel_id, userId));
			if (!result.contains("null")) {
				if (!this.followers.contains(sender.toLowerCase()))
					this.followers.add(sender.toLowerCase());
			} else {
				if (this.followers.contains(sender.toLowerCase()))
					this.followers.remove(sender.toLowerCase());
			}
		} catch (Exception e) {
			MJRBot.logErrorMessage(e);
		}
	}

	@Override
	protected void onJoin(String sender) {
		if (!this.viewersJoinedTimes.containsKey(sender.toLowerCase()))
			this.viewersJoinedTimes.put(sender.toLowerCase(), System.currentTimeMillis());
		if (Config.getSetting("Points", this.channelName).equalsIgnoreCase("true")) {
			if (!PointsSystem.isOnList(sender, BotType.Mixer, this)) {
				PointsSystem.setPoints(sender, Integer.parseInt(Config.getSetting("StartingPoints", this.channelName)), BotType.Mixer, this, false, false);
			}
		}
		if (Config.getSetting("Ranks", this.channelName).equalsIgnoreCase("true")) {
			if (!RankSystem.isOnList(sender, BotType.Mixer, this)) {
				RankSystem.setRank(sender, "None", BotType.Mixer, this);
			}
		}
		EventLog.addEvent(BotType.Mixer, this, sender, "Joined the channel (Mixer)", EventType.User);
	}

	@Override
	protected void onPart(String sender) {
		if (this.viewersJoinedTimes.containsKey(sender.toLowerCase()))
			this.viewersJoinedTimes.remove(sender.toLowerCase());
		EventLog.addEvent(BotType.Mixer, this, sender, "Left the channel (Mixer)", EventType.User);
	}

	public void joinChannel(String channel) throws SQLException {
		try {
			this.setdebug(true);
			int channel_id = 0;
			if (MJRBot.connectionType == ConnectionType.Manual) {
				channel_id = MJRBot.id;
			} else {
				ResultSet set = MySQLConnection.executeQueryNoOutput("SELECT * FROM tokens WHERE channel = '" + channel + "' AND platform = 'Mixer'");
				if (set != null && set.next()) {
					channel_id = set.getInt("channel_id");
				}
			}
			if (channel_id != 0) {
				ArrayList<String> events = new ArrayList<String>();
				events.add("channel:" + channel_id + ":followed");
				events.add("channel:" + channel_id + ":hosted");
				events.add("channel:" + channel_id + ":unhosted");
				events.add("channel:" + channel_id + ":subscribed");
				events.add("channel:" + channel_id + ":resubscribed");
				this.joinMixerChannel(channel, events);
				if (this.isConnected() && this.isAuthenticated()) {
					// Start Threads
					pointsThread = new AutoPointsThread(BotType.Mixer, this);
					pointsThread.start();
					announcementsThread = new AnnouncementsThread(BotType.Mixer, this);
					announcementsThread.start();

					for (String viewer : this.getViewers())
						if (!this.viewersJoinedTimes.containsKey(viewer.toLowerCase()))
							this.viewersJoinedTimes.put(viewer.toLowerCase(), System.currentTimeMillis());

					ConsoleUtil.textToConsole(this, BotType.Mixer, "MJRBot is Connected & Authenticated to Mixer!", MessageType.Chat, null);
					if (Config.getSetting("SilentJoin", channel).equalsIgnoreCase("false"))
						this.sendMessage(this.getBotName() + " Connected!");
				} else
					ConsoleUtil.textToConsole(this, BotType.Mixer, "Theres been problem, connecting to Mixer, Please check settings are corrrect!", MessageType.Chat, null);
			}
			ConsoleUtil.textToConsole(this, BotType.Mixer, "Theres been problem, connecting to Mixer, Please check settings are corrrect!", MessageType.Chat, null);
		} catch (InterruptedException | ExecutionException | IOException e) {
			MJRBot.logErrorMessage(e);
		}
	}

	@Override
	protected void onDebugMessage() {
		for (String message : this.getOutputMessages())
			ConsoleUtil.textToConsole(this, BotType.Mixer, message, MessageType.ChatBot, null);
		this.clearOutputMessages();
	}

	public void disconnectMixer() {
		String silentJoin = Config.getSetting("SilentJoin", this.channelName);
		if (silentJoin != null && silentJoin.equalsIgnoreCase("false")) {
			this.sendMessage(this.getBotName() + " Disconnected!");
		}
		this.disconnect();
		ConsoleUtil.textToConsole(this, BotType.Mixer, "Left " + this.channelName + " channel", MessageType.ChatBot, null);
		this.viewersJoinedTimes.clear();
	}

	@Override
	protected void onLiveEvent(LiveEvent event) {
		String line = event.data.payload.toString();
		String type = event.data.channel;
		type = type.substring(type.lastIndexOf(":") + 1);
		if (type.equalsIgnoreCase("subscribed")) {
			String user = line.substring(line.indexOf("username") + 12);
			user = user.substring(0, user.indexOf(';'));
			if (Config.getSetting("SubAlerts", this.channelName).equalsIgnoreCase("true"))
				Utilities.sendMessage(BotType.Mixer, this.channelName, user + " just subscribed to the channel!");
			ConsoleUtil.textToConsole(this, BotType.Mixer, user + " just subscribed to the channel!", MessageType.ChatBot, null);
			EventLog.addEvent(BotType.Mixer, this, user, "Just subscribed to the channel!", EventType.Sub);
			this.subscribers.add(user);
		} else if (type.equalsIgnoreCase("resubscribed")) {
			String user = line.substring(line.indexOf("username") + 12);
			user = user.substring(0, user.indexOf(';'));
			String months = line.substring(line.indexOf("totalMonths") + 15);
			months = months.substring(0, months.indexOf(';'));
			if (Config.getSetting("ResubAlerts", this.channelName).equalsIgnoreCase("true"))
				Utilities.sendMessage(BotType.Mixer, this.channelName, user + " just resubscribed to the channel for " + months + " months in a row!");
			ConsoleUtil.textToConsole(this, BotType.Mixer, user + " just resubscribed to the channel for " + months + " months in a row!", MessageType.ChatBot, null);
			EventLog.addEvent(BotType.Mixer, this, user, "Just resubscribed to the channel for " + months + " months in a row!", EventType.Sub);
			this.subscribers.add(user);
		} else if (type.equalsIgnoreCase("hosted")) {
			String user = line.substring(line.indexOf("name") + 7);
			user = user.substring(0, user.indexOf("'"));
			if (Config.getSetting("HostingAlerts", this.channelName).equalsIgnoreCase("true"))
				Utilities.sendMessage(BotType.Mixer, this.channelName, user + " is now hosting you!");
			ConsoleUtil.textToConsole(this, BotType.Mixer, user + " is now hosting you!", MessageType.ChatBot, null);
		} else if (type.equalsIgnoreCase("followed")) {
			String user = line.substring(line.indexOf("username") + 11);
			user = user.substring(0, user.indexOf("\""));
			String following = line.substring(line.indexOf("following") + 11);
			following = following.substring(0, following.indexOf("}"));
			if (following.trim().equals("true")) {
				if (Config.getSetting("FollowAlerts", this.channelName).equalsIgnoreCase("true"))
					Utilities.sendMessage(BotType.Mixer, this.channelName, user + " is now following you!");
				ConsoleUtil.textToConsole(this, BotType.Mixer, user + " is now following you!", MessageType.ChatBot, null);
				if (!this.followers.contains(user.toLowerCase()))
					this.followers.add(user.toLowerCase());
			} else {
				if (Config.getSetting("FollowAlerts", this.channelName).equalsIgnoreCase("true"))
					Utilities.sendMessage(BotType.Mixer, this.channelName, user + " is now unfollowing you!");
				ConsoleUtil.textToConsole(this, BotType.Mixer, user + " is now unfollowing you!", MessageType.ChatBot, null);
				if (this.followers.contains(user.toLowerCase()))
					this.followers.remove(user.toLowerCase());
			}
		}
	}
}
