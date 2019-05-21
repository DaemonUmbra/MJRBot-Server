package com.mjr.mjrbot.bots.bases;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.mixer.api.resource.MixerUser.Role;
import com.mixer.api.resource.constellation.events.LiveEvent;
import com.mjr.mjrbot.CrossChatLink;
import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.ConnectionType;
import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.bases.dataStorage.MixerData;
import com.mjr.mjrbot.chatModeration.ChatModeration;
import com.mjr.mjrbot.commands.CommandManager;
import com.mjr.mjrbot.games.MathsGame;
import com.mjr.mjrbot.games.RacingGame;
import com.mjr.mjrbot.storage.BotConfigManager;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.storage.EventLogManager;
import com.mjr.mjrbot.storage.EventLogManager.EventType;
import com.mjr.mjrbot.storage.PointsSystemManager;
import com.mjr.mjrbot.storage.RankSystemManager;
import com.mjr.mjrbot.storage.sql.MySQLConnection;
import com.mjr.mjrbot.threads.AnnouncementsThread;
import com.mjr.mjrbot.threads.AutoPointsThread;
import com.mjr.mjrbot.threads.BankHeistThread;
import com.mjr.mjrbot.threads.RaceStartThread;
import com.mjr.mjrbot.threads.twitch.GetFollowersThread;
import com.mjr.mjrbot.util.ConsoleUtil;
import com.mjr.mjrbot.util.ConsoleUtil.MessageType;
import com.mjr.mjrbot.util.HTTPConnect;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.TwitchMixerAPICalls;
import com.mjr.mjrmixer.MJR_MixerBot;

public class MixerBot extends MJR_MixerBot {

	private String channelName = "";

	private final CommandManager commands;

	public AutoPointsThread pointsThread;
	public AnnouncementsThread announcementsThread;
	public GetFollowersThread getFollowersThread;
	public BankHeistThread bankHeistThread;
	public RaceStartThread racingThread;

	private MixerData data;

	public MathsGame mathsGame;
	public RacingGame racingGame;

	public MixerBot(String channelName) {
		super(BotConfigManager.getSetting("MixerClientID"), BotConfigManager.getSetting("MixerAuthCode"), BotConfigManager.getSetting("MixerUsername/BotName"));
		commands = new CommandManager();
		data = new MixerData();

		mathsGame = new MathsGame();
		racingGame = new RacingGame();
		this.channelName = channelName;
	}

	@Override
	protected void onMessage(String sender, int userId, List<Role> userRoles, String message) {
		// Outputting messages
		ConsoleUtil.textToConsole(this, BotType.Mixer, message, MessageType.Chat, sender);
		CrossChatLink.sendMessageAcrossPlatforms(BotType.Mixer, this, sender, message);

		// Check user data
		if (userRoles.contains(Role.SUBSCRIBER))
			this.getMixerData().addSubscriber(sender);
		else
			this.getMixerData().removeSubscriber(sender);
		this.checkUserProperties(sender);
		this.checkFollower(sender, userId);

		// Check/Run command
		if (this.getModerators() != null)
			if (this.getModerators().contains(this.getBotName().toLowerCase()))
				ChatModeration.onCommand(BotType.Mixer, ChatBotManager.getMixerBotByChannelName(this.channelName), sender, null, null, message);
		try {
			commands.onCommand(BotType.Mixer, this, sender, null, null, message);
		} catch (IOException e) {
			MJRBotUtilities.logErrorMessage(e);
		}
	}

	@Override
	protected void onJoin(String sender) {
		ConsoleUtil.textToConsole(this, BotType.Mixer, sender + " has joined!", MessageType.ChatBot, null);
		this.checkUserProperties(sender);
	}

	@Override
	protected void onPart(String sender) {
		ConsoleUtil.textToConsole(this, BotType.Mixer, sender + " has left!", MessageType.ChatBot, null);
		this.removeUserProperties(sender);
	}

	@Override
	protected void onLiveEvent(LiveEvent event) {
		String line = event.data.payload.toString();
		String type = event.data.channel;
		type = type.substring(type.lastIndexOf(":") + 1);
		if (type.equalsIgnoreCase("subscribed")) {
			String user = line.substring(line.indexOf("username") + 12);
			user = user.substring(0, user.indexOf(';'));
			if (ChannelConfigManager.getSetting("SubAlerts", this.channelName).equalsIgnoreCase("true"))
				ChatBotManager.sendMessage(BotType.Mixer, this.channelName, user + " just subscribed to the channel!");
			ConsoleUtil.textToConsole(this, BotType.Mixer, user + " just subscribed to the channel!", MessageType.ChatBot, null);
			EventLogManager.addEvent(BotType.Mixer, this, user, "Just subscribed to the channel!", EventType.Sub);
			this.getMixerData().addSubscriber(user);
		} else if (type.equalsIgnoreCase("resubscribed")) {
			String user = line.substring(line.indexOf("username") + 12);
			user = user.substring(0, user.indexOf(';'));
			String months = line.substring(line.indexOf("totalMonths") + 15);
			months = months.substring(0, months.indexOf(';'));
			if (ChannelConfigManager.getSetting("ResubAlerts", this.channelName).equalsIgnoreCase("true"))
				ChatBotManager.sendMessage(BotType.Mixer, this.channelName, user + " just resubscribed to the channel for " + months + " months in a row!");
			ConsoleUtil.textToConsole(this, BotType.Mixer, user + " just resubscribed to the channel for " + months + " months in a row!", MessageType.ChatBot, null);
			EventLogManager.addEvent(BotType.Mixer, this, user, "Just resubscribed to the channel for " + months + " months in a row!", EventType.Sub);
			this.getMixerData().addSubscriber(user);
		} else if (type.equalsIgnoreCase("hosted")) {
			String user = line.substring(line.indexOf("name") + 7);
			user = user.substring(0, user.indexOf("'"));
			if (ChannelConfigManager.getSetting("HostingAlerts", this.channelName).equalsIgnoreCase("true"))
				ChatBotManager.sendMessage(BotType.Mixer, this.channelName, user + " is now hosting you!");
			ConsoleUtil.textToConsole(this, BotType.Mixer, user + " is now hosting you!", MessageType.ChatBot, null);
		} else if (type.equalsIgnoreCase("followed")) {
			String user = line.substring(line.indexOf("username") + 11);
			user = user.substring(0, user.indexOf("\""));
			String following = line.substring(line.indexOf("following") + 11);
			following = following.substring(0, following.indexOf("}"));
			if (following.trim().equals("true")) {
				if (ChannelConfigManager.getSetting("FollowAlerts", this.channelName).equalsIgnoreCase("true"))
					ChatBotManager.sendMessage(BotType.Mixer, this.channelName, user + " is now following you!");
				ConsoleUtil.textToConsole(this, BotType.Mixer, user + " is now following you!", MessageType.ChatBot, null);
				this.getMixerData().addFollower(user);
			} else {
				if (ChannelConfigManager.getSetting("FollowAlerts", this.channelName).equalsIgnoreCase("true"))
					ChatBotManager.sendMessage(BotType.Mixer, this.channelName, user + " is now unfollowing you!");
				ConsoleUtil.textToConsole(this, BotType.Mixer, user + " is now unfollowing you!", MessageType.ChatBot, null);
				this.getMixerData().removeFollower(user);
			}
		}
	}

	@Override
	protected void onDebugMessage() {
		for (String message : this.getOutputMessages())
			ConsoleUtil.textToConsole(this, BotType.Mixer, message, MessageType.ChatBot, null);
		this.clearOutputMessages();
	}

	public void joinChannel(String channel) throws SQLException {
		try {
			this.setdebug(true);
			int channel_id = 0;
			if (MJRBot.connectionType == ConnectionType.Manual) {
				channel_id = MJRBot.manualChannelID;
			} else {
				ResultSet set = MySQLConnection.executeQuery("SELECT * FROM tokens WHERE channel = '" + channel + "' AND platform = 'Mixer'", false);
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
					pointsThread = new AutoPointsThread(BotType.Mixer, this, channel);
					pointsThread.start();
					announcementsThread = new AnnouncementsThread(BotType.Mixer, this, channel);
					announcementsThread.start();

					for (String viewer : this.getViewers())
						if (!this.getMixerData().viewersJoinedTimes.containsKey(viewer.toLowerCase()))
							this.getMixerData().viewersJoinedTimes.put(viewer.toLowerCase(), System.currentTimeMillis());

					ConsoleUtil.textToConsole(this, BotType.Mixer, "MJRBot is Connected & Authenticated to Mixer!", MessageType.Chat, null);
					if (ChannelConfigManager.getSetting("SilentJoin", channel).equalsIgnoreCase("false"))
						this.sendMessage(this.getBotName() + " Connected!");
				} else
					ConsoleUtil.textToConsole(this, BotType.Mixer, "Theres been problem, connecting to Mixer, Please check settings are corrrect!", MessageType.Chat, null);
			}
			ConsoleUtil.textToConsole(this, BotType.Mixer, "Theres been problem, connecting to Mixer, Please check settings are corrrect!", MessageType.Chat, null);
		} catch (InterruptedException | ExecutionException | IOException e) {
			MJRBotUtilities.logErrorMessage(e);
		}
	}

	public void disconnectMixer() {
		String silentJoin = ChannelConfigManager.getSetting("SilentJoin", this.channelName);
		if (silentJoin != null && silentJoin.equalsIgnoreCase("false")) {
			this.sendMessage(this.getBotName() + " Disconnected!");
		}
		this.disconnectAll();
		ConsoleUtil.textToConsole(this, BotType.Mixer, "Left " + this.channelName + " channel", MessageType.ChatBot, null);
		data = new MixerData();
	}

	public void checkFollower(final String sender, int userId) {
		String result = "";
		int channel_id = 0;
		try {
			ResultSet set = MySQLConnection.executeQuery("SELECT * FROM tokens WHERE channel = '" + this.channelName + "' AND platform = 'Mixer'", false);
			if (set != null && set.next()) {
				channel_id = set.getInt("channel_id");
			}
			result = HTTPConnect.getRequest(TwitchMixerAPICalls.mixerCheckFollow(channel_id, userId));
			if (!result.contains("null")) {
				this.getMixerData().addFollower(sender);
			} else {
				this.getMixerData().removeFollower(sender);
			}
		} catch (Exception e) {
			MJRBotUtilities.logErrorMessage(e);
		}
	}

	public void checkUserProperties(String user) {
		if (ChannelConfigManager.getSetting("Points", this.channelName).equalsIgnoreCase("true")) {
			if (!PointsSystemManager.isOnList(user, BotType.Mixer, this)) {
				PointsSystemManager.setPoints(user, Integer.parseInt(ChannelConfigManager.getSetting("StartingPoints", BotType.Mixer, this)), BotType.Mixer, this, false, false);
			}
		}
		if (ChannelConfigManager.getSetting("Ranks", this.channelName).equalsIgnoreCase("true")) {
			if (!RankSystemManager.isOnList(user, BotType.Mixer, this)) {
				RankSystemManager.setRank(user, "None", BotType.Mixer, this);
			}
		}
		if (!this.getMixerData().usersCooldowns.containsKey(user.toLowerCase())) {
			this.getMixerData().usersCooldowns.put(user.toLowerCase(), 0);
		}
		if (!this.getViewers().contains(user.toLowerCase()))
			EventLogManager.addEvent(BotType.Mixer, this, user, "Joined the channel", EventType.User);
		if (!this.getMixerData().viewersJoinedTimes.containsKey(user.toLowerCase()))
			this.getMixerData().viewersJoinedTimes.put(user.toLowerCase(), System.currentTimeMillis());
	}

	public void removeUserProperties(String user) {
		if (this.getMixerData().usersCooldowns.containsKey(user.toLowerCase())) {
			this.getMixerData().usersCooldowns.remove(user.toLowerCase(), 0);
		}
		EventLogManager.addEvent(BotType.Mixer, this, user, "Left the channel", EventType.User);
		if (this.getMixerData().viewersJoinedTimes.containsKey(user.toLowerCase()))
			this.getMixerData().viewersJoinedTimes.remove(user.toLowerCase());
	}

	public MixerData getMixerData() {
		return data;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
}
