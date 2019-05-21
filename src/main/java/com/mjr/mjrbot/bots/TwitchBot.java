package com.mjr.mjrbot.bots;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mjr.mjrbot.CrossChatLink;
import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.ConnectionType;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
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
import com.mjr.mjrbot.threads.twitch.GetSubscribersThread;
import com.mjr.mjrbot.threads.twitch.GetViewersThread;
import com.mjr.mjrbot.util.BoolStringPair;
import com.mjr.mjrbot.util.ConsoleUtil;
import com.mjr.mjrbot.util.ConsoleUtil.MessageType;
import com.mjr.mjrbot.util.HTTPConnect;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.TwitchMixerAPICalls;
import com.mjr.twitchframework.irc.TwitchBotBase;
import com.mjr.twitchframework.irc.TwitchIRCManager;

public class TwitchBot extends TwitchBotBase {

	private String ircChannelName;
	private boolean setupComplete;

	private int channelID;
	private String channelName;

	private final CommandManager commands;
	private TwitchData data;

	public GetViewersThread getViewersThread;
	public GetSubscribersThread getSubscribersThread;
	public AutoPointsThread pointsThread;
	public AnnouncementsThread announcementsThread;
	public GetFollowersThread getFollowersThread;
	public BankHeistThread bankHeistThread;
	public RaceStartThread racingThread;

	public MathsGame mathsGame;
	public RacingGame racingGame;

	public TwitchBot() {
		ircChannelName = "";
		setupComplete = false;

		channelID = 0;
		channelName = "";

		commands = new CommandManager();
		data = new TwitchData();

		mathsGame = new MathsGame();
		racingGame = new RacingGame();
	}

	public void init(String channelName, int channelID) {
		this.channelName = channelName.toLowerCase();
		this.channelID = channelID;
		this.ircChannelName = "#" + channelName.toLowerCase();
		ConsoleUtil.textToConsole(this, BotType.Twitch, "Joined " + channelName.substring(channelName.indexOf("#") + 1) + " channel", MessageType.ChatBot, null);
	}

	@Override
	public void onMessage(final String channel, final String sender, final String login, final String hostname, final String userID, final boolean subscriber, final String message) {
		// Outputting messages
		ConsoleUtil.textToConsole(this, BotType.Twitch, message, MessageType.Chat, sender);
		CrossChatLink.sendMessageAcrossPlatforms(BotType.Twitch, this, sender, message);

		// Check user data
		if (subscriber) {
			this.getTwitchData().addSubscriber(sender);
		}
		this.checkFollower(sender);
		this.checkUserProperties(sender);

		// Check/Run command
		if (this.getTwitchData().getModerators() != null)
			if (this.getTwitchData().getModerators().contains(this.getBotName().toLowerCase()))
				ChatModeration.onCommand(BotType.Twitch, this, sender, login, hostname, message);
		if (ChannelConfigManager.getSetting("Commands", this.channelID).equalsIgnoreCase("true")) {
			try {
				commands.onCommand(BotType.Twitch, this, sender.toLowerCase(), login, hostname, message);
			} catch (IOException e) {
				MJRBotUtilities.logErrorMessage(e);
			}
		}
		return;
	}

	@Override
	public void onMessageExtra(final String line, final String channel, final String sender, final String login, final String hostname, final String message) {
		if (line.contains("bits=")) {
			String bitsAmount = line.substring(line.indexOf("bits=") + 5);
			bitsAmount = bitsAmount.substring(0, bitsAmount.indexOf(";"));
			if (ChannelConfigManager.getSetting("BitsAlerts", this.channelID).equalsIgnoreCase("true"))
				MJRBotUtilities.sendMessage(BotType.Twitch, this, sender + " just gave " + bitsAmount + " bit(s) to the channel!");
			ConsoleUtil.textToConsole(this, BotType.Twitch, sender + " just gave " + bitsAmount + " bit(s) to the channel!", MessageType.ChatBot, null);
			EventLogManager.addEvent(BotType.Twitch, this, sender, "Just gave " + bitsAmount + " bit(s) to the channel!", EventType.Bits);
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
				notice += ", " + this.ircChannelName.substring(this.ircChannelName.indexOf("#") + 1);
				for (String moderator : notice.split(", ")) {
					if (!this.getTwitchData().getModerators().contains(moderator.toLowerCase())) {
						this.getTwitchData().addModerator(moderator);
					}
				}
			} catch (Exception e) {
				ConsoleUtil.textToConsole(this, BotType.Twitch, "There was a problem getting the moderators of this channel!", MessageType.ChatBot, null);
			}
			if (this.getTwitchData().getModerators() == null) {
				ConsoleUtil.textToConsole(this, BotType.Twitch, "There was a problem getting the moderators of this channel!", MessageType.ChatBot, null);
				return;
			}
			if (this.getTwitchData().getModerators().size() > 1)
				ConsoleUtil.textToConsole(this, BotType.Twitch, "Bot has the list of current moderators!", MessageType.ChatBot, null);
		} else if (notice.contains("There are no moderators of this channel"))
			ConsoleUtil.textToConsole(this, BotType.Twitch, "This channel has no moderators!", MessageType.ChatBot, null);
	}

	@Override
	public void onPrivateMessage(String sender, String login, String hostname, String channel, String message) {
		if (channel.equalsIgnoreCase(this.channelName)) {
			if (message.contains("is now hosting you.")) {
				MJRBotUtilities.sendMessage(BotType.Twitch, channel, message);
				ConsoleUtil.textToConsole(this, BotType.Twitch, message, MessageType.ChatBot, null);
			}
		}
	}

	@Override
	protected void onUnknown(String line) {
		if (line.contains("msg-id=subgift") && line.contains("msg-param-recipient-display-name=")) {
			String gifter = line.substring(line.indexOf("display-name=") + 13);
			gifter = gifter.substring(0, gifter.indexOf(';'));
			String user = line.substring(line.indexOf("msg-param-recipient-display-name=") + 33);
			user = user.substring(0, user.indexOf(';'));
			if (ChannelConfigManager.getSetting("GiftSubAlerts", this.channelID).equalsIgnoreCase("true"))
				MJRBotUtilities.sendMessage(BotType.Twitch, this, gifter + " has gifted a sub to " + user);
			ConsoleUtil.textToConsole(this, BotType.Twitch, gifter + " has gifted a sub to " + user, MessageType.ChatBot, null);
			EventLogManager.addEvent(BotType.Twitch, this, gifter, "Has gifted a sub to " + user, EventType.Sub);
			this.getTwitchData().addSubscriber(user);
		}

		// User Gifting x amount of subs to the channel
		else if (line.contains("msg-id=submysterygift") && line.contains("msg-param-recipient-display-name=")) {
			String gifter = line.substring(line.indexOf("system-msg=") + 11);
			gifter = gifter.substring(0, gifter.indexOf("\\"));
			String amount = line.substring(line.indexOf("msg-param-mass-gift-count=") + 26);
			amount = amount.substring(0, amount.indexOf(';'));
			if (ChannelConfigManager.getSetting("GiftSubAlerts", this.channelID).equalsIgnoreCase("true"))
				MJRBotUtilities.sendMessage(BotType.Twitch, this, gifter + " Thanks for gifting " + amount + " subs to the community");
			ConsoleUtil.textToConsole(this, BotType.Twitch, gifter + "Gifted " + amount + " subs to the community", MessageType.ChatBot, null);
			EventLogManager.addEvent(BotType.Twitch, this, gifter, "Gifted " + amount + " subs to the community", EventType.Sub);
		}

		// Anonymous Gift Sub to User
		else if (line.contains("msg-id=anonsubgift") && line.contains("msg-param-recipient-display-name=")) {
			String gifter = "AnAnonymousGifter";
			String user = line.substring(line.indexOf("msg-param-recipient-display-name=") + 33);
			user = user.substring(0, user.indexOf(';'));
			if (ChannelConfigManager.getSetting("GiftSubAlerts", this.channelID).equalsIgnoreCase("true"))
				MJRBotUtilities.sendMessage(BotType.Twitch, this, gifter + " has gifted a sub to " + user);
			ConsoleUtil.textToConsole(this, BotType.Twitch, gifter + " has gifted a sub to " + user, MessageType.ChatBot, null);
			EventLogManager.addEvent(BotType.Twitch, this, gifter, "Has gifted a sub to " + user, EventType.Sub);
			this.getTwitchData().addSubscriber(user);
		}

		// New Subscriber
		else if (line.contains("msg-id=sub") && !line.contains("msg-param-recipient-display-name=")) {
			String user = line.substring(line.indexOf("display-name=") + 13);
			user = user.substring(0, user.indexOf(';'));
			if (line.contains("msg-param-sub-plan=Prime")) {
				if (ChannelConfigManager.getSetting("SubAlerts", this.channelID).equalsIgnoreCase("true"))
					MJRBotUtilities.sendMessage(BotType.Twitch, this, user + " just subscribed to the channel using Twitch Prime!");
				ConsoleUtil.textToConsole(this, BotType.Twitch, user + " just subscribed to the channel using Twitch Prime!", MessageType.ChatBot, null);
				EventLogManager.addEvent(BotType.Twitch, this, user, "Just subscribed to the channel using Twitch Prime!", EventType.Sub);
				this.getTwitchData().addSubscriber(user);
			} else {
				if (ChannelConfigManager.getSetting("SubAlerts", this.channelID).equalsIgnoreCase("true"))
					MJRBotUtilities.sendMessage(BotType.Twitch, this, user + " just subscribed to the channel!");
				ConsoleUtil.textToConsole(this, BotType.Twitch, user + " just subscribed to the channel!", MessageType.ChatBot, null);
				EventLogManager.addEvent(BotType.Twitch, this, user, "Just subscribed to the channel!", EventType.Sub);
				this.getTwitchData().addSubscriber(user);
			}
		}

		// ReSubscriber
		else if (line.contains("msg-id=resub")) {
			String user = line.substring(line.indexOf("display-name=") + 13);
			user = user.substring(0, user.indexOf(';'));
			String totalMonths = line.substring(line.indexOf("msg-param-cumulative-months=") + 28);
			totalMonths = totalMonths.substring(0, totalMonths.indexOf(';'));
			String currentMonths = line.substring(line.indexOf("msg-param-streak-months=") + 24);
			currentMonths = currentMonths.substring(0, currentMonths.indexOf(';'));
			String showCurrentStreak = line.substring(line.indexOf("msg-param-should-share-streak=") + 30);
			showCurrentStreak = showCurrentStreak.substring(0, showCurrentStreak.indexOf(';'));
			String resubMessage = line.substring(line.indexOf("USERNOTICE"));
			if (resubMessage.contains(":"))
				resubMessage = resubMessage.substring(resubMessage.indexOf(":") + 1);
			else
				resubMessage = null;
			String endOfMsg = "";
			if (showCurrentStreak.equals("0"))
				endOfMsg = "for a Total of " + totalMonths + " months!";
			else
				endOfMsg = "for " + currentMonths + " months in a row, with a Total of " + totalMonths + " months!";

			if (line.contains("msg-param-sub-plan=Prime")) {
				if (ChannelConfigManager.getSetting("ResubAlerts", this.channelID).equalsIgnoreCase("true"))
					MJRBotUtilities.sendMessage(BotType.Twitch, this, user + " just resubscribed to the channel using Twitch Prime " + endOfMsg);
				ConsoleUtil.textToConsole(this, BotType.Twitch, user + " just resubscribed to the channel using Twitch Prime " + endOfMsg + (resubMessage != null ? " Message: " + resubMessage : ""), MessageType.ChatBot, null);
				EventLogManager.addEvent(BotType.Twitch, this, user, "Just resubscribed to the channel using Twitch Prime " + endOfMsg + (resubMessage != null ? " Message: " + resubMessage : ""), EventType.Sub);
				this.getTwitchData().addSubscriber(user);
			} else {
				if (ChannelConfigManager.getSetting("ResubAlerts", this.channelID).equalsIgnoreCase("true"))
					MJRBotUtilities.sendMessage(BotType.Twitch, this, user + " just resubscribed to the channel " + endOfMsg);
				ConsoleUtil.textToConsole(this, BotType.Twitch, user + " just resubscribed to the channel " + endOfMsg + (resubMessage != null ? " Message: " + resubMessage : ""), MessageType.ChatBot, null);
				EventLogManager.addEvent(BotType.Twitch, this, user, "Just resubscribed to the channel " + endOfMsg + (resubMessage != null ? " Message: " + resubMessage : ""), EventType.Sub);
				this.getTwitchData().addSubscriber(user);
			}
		}
	}

	@Override
	protected void onJoin(String channel, String sender, String login, String hostname) {
		if (sender.equalsIgnoreCase(BotConfigManager.getSetting("TwitchUsername"))) {
			setupBot(channel);
		}
		ConsoleUtil.textToConsole(this, BotType.Twitch, sender + " has joined!", MessageType.ChatBot, null);
		this.checkUserProperties(sender);
	}

	@Override
	protected void onPart(String channel, String sender, String login, String hostname) {
		ConsoleUtil.textToConsole(this, BotType.Twitch, sender + " has left!", MessageType.ChatBot, null);
		this.removeUserProperties(sender);
	}

	public void disconnectTwitch() {
		TwitchIRCManager.removeChannel(getChannelName(), 50);
		String silentJoin = ChannelConfigManager.getSetting("SilentJoin", this.channelID);
		if (silentJoin != null && silentJoin.equalsIgnoreCase("false")) {
			this.sendMessage(this.getBotName() + " Disconnected!");
		}
		ConsoleUtil.textToConsole(this, BotType.Twitch, "Left " + this.channelName + " channel", MessageType.ChatBot, null);
		data = new TwitchData();
		this.setupComplete = false;
		this.ircChannelName = "";
	}

	public void sendMessage(String message) {
		// Used to slow down messages if not a moderator due to Twitch's message delay for normal users
		if (!this.getTwitchData().getModerators().isEmpty() && !this.getTwitchData().getModerators().contains(this.getBotName().toLowerCase())) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				MJRBotUtilities.logErrorMessage(e);
			}
		}
		TwitchIRCManager.sendMessageByChannelName(this.channelName, message);
		// ConsoleUtil.textToConsole(this, BotType.Twitch, message, MessageType.Chat, this.getName()); TODO Fix Twitch FrameworkPort
	}

	public void setupBot(String channel) {
		setupComplete = true;

		// Start Threads
		pointsThread = new AutoPointsThread(BotType.Twitch, this, channel);
		pointsThread.start();
		announcementsThread = new AnnouncementsThread(BotType.Twitch, this, channel);
		announcementsThread.start();

		TwitchIRCManager.sendMessageByChannelName(this.channelName, "/mods");
		if (ChannelConfigManager.getSetting("SilentJoin", this.channelID).equalsIgnoreCase("false")) {
			TwitchIRCManager.sendMessageByChannelName(this.channelName, BotConfigManager.getSetting("TwitchUsername") + " Connected!");
		}
		getViewersThread = new GetViewersThread(this);
		getViewersThread.start();
		getFollowersThread = new GetFollowersThread(this);
		getFollowersThread.start();
		if (MJRBot.storageType == StorageType.Database && MJRBot.connectionType == ConnectionType.Database) {
			getSubscribersThread = new GetSubscribersThread(this);
			getSubscribersThread.start();
		} else
			ConsoleUtil.textToConsole("Getting Subscribers Thread for Twtich has been disabled, as it is currently not supported on the file based storage type or/and when manual mode is used!");
	}

	public void checkFollower(final String sender) {
		String result = "";
		try {
			result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchCheckFollow(this.channelID, sender));
			if (result.contains("created_at")) {
				if (!this.getTwitchData().getFollowers().contains(sender.toLowerCase()))
					this.getTwitchData().addFollower(sender);
			} else
				this.getTwitchData().removeFollower(sender);
		} catch (FileNotFoundException e) {
			this.getTwitchData().removeFollower(sender);
		} catch (Exception e) {
			MJRBotUtilities.logErrorMessage(e);
		}
	}

	public void checkUserProperties(String user) {
		if (ChannelConfigManager.getSetting("Points", this.channelID).equalsIgnoreCase("true")) {
			if (!PointsSystemManager.isOnList(user, BotType.Twitch, this)) {
				PointsSystemManager.setPoints(user, Integer.parseInt(ChannelConfigManager.getSetting("StartingPoints", BotType.Twitch, this)), BotType.Twitch, this, false, false);
			}
		}
		if (ChannelConfigManager.getSetting("Ranks", this.channelID).equalsIgnoreCase("true")) {
			if (!RankSystemManager.isOnList(user, BotType.Twitch, this)) {
				RankSystemManager.setRank(user, "None", BotType.Twitch, this);
			}
		}
		if (!this.getTwitchData().usersCooldowns.containsKey(user.toLowerCase())) {
			this.getTwitchData().usersCooldowns.put(user.toLowerCase(), 0);
		}
		if (!this.getTwitchData().getViewers().contains(user.toLowerCase())) {
			this.getTwitchData().addViewer(user);
			EventLogManager.addEvent(BotType.Twitch, this, user, "Joined the channel", EventType.User);
		}
		if (!this.getTwitchData().viewersJoinedTimes.containsKey(user.toLowerCase()))
			this.getTwitchData().viewersJoinedTimes.put(user.toLowerCase(), System.currentTimeMillis());
	}

	public void removeUserProperties(String user) {
		if (this.getTwitchData().usersCooldowns.containsKey(user.toLowerCase())) {
			this.getTwitchData().usersCooldowns.remove(user.toLowerCase(), 0);
		}
		if (this.getTwitchData().getViewers().contains(user.toLowerCase())) {
			this.getTwitchData().removeViewer(user);
			EventLogManager.addEvent(BotType.Twitch, this, user, "Left the channel", EventType.User);
		}
		if (this.getTwitchData().viewersJoinedTimes.containsKey(user.toLowerCase()))
			this.getTwitchData().viewersJoinedTimes.remove(user.toLowerCase());
	}

	public String getBotName() {
		return "MJRBot";// this.getName(); TODO Fix Twitch FrameworkPort
	}

	public boolean isBotSetupCompleted() {
		return setupComplete;
	}

	public void setBotSetupCompleted(boolean setupComplete) {
		this.setupComplete = setupComplete;
	}

	public TwitchData getTwitchData() {
		return data;
	}

	public int getChannelID() {
		return channelID;
	}

	public void setChannelID(int channelID) {
		this.channelID = channelID;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	/*
	 * Static Utilities Methods
	 */

	public static int getChannelIDFromChannelName(String channelName) {
		if (MJRBot.connectionType == ConnectionType.Database) {
			try {
				ResultSet set = MySQLConnection.executeQuery("SELECT * FROM channels WHERE name = '" + channelName + "' AND bot_type = 'Twitch'");
				if (set != null && set.next()) {
					return set.getInt("twitch_channel_id");
				}
			} catch (SQLException e) {
				MJRBotUtilities.logErrorMessage(e);
			}
		} else
			return MJRBot.manualChannelID;
		return 0;
	}

	public static String getChannelNameFromChannelID(int channelID) {
		if (MJRBot.connectionType == ConnectionType.Database) {
			try {
				ResultSet set = MySQLConnection.executeQuery("SELECT * FROM channels WHERE twitch_channel_id = '" + channelID + "'");
				if (set != null && set.next()) {
					return set.getString("name");
				}
			} catch (SQLException e) {
				MJRBotUtilities.logErrorMessage(e);
			}
		} else
			return MJRBot.manualChannelName;
		return null;
	}

	public static BoolStringPair checkforUsernameChange(TwitchBot bot) {
		String newChannel = "";
		try {
			ResultSet set = MySQLConnection.executeQuery("SELECT * FROM tokens WHERE channel_id = '" + bot.getChannelID() + "' AND platform = 'Twitch'", false);
			if (set.next() && set.getString("access_token") != null && set.getString("access_token") != "") {
				String result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetUserAPI(set.getString("access_token")));
				newChannel = result.substring(result.indexOf("name") + 7);
				newChannel = newChannel.substring(0, newChannel.indexOf(",") - 1).toLowerCase();
				if (!newChannel.equalsIgnoreCase(bot.channelName))
					return new BoolStringPair(newChannel, true);
			}
		} catch (Exception e) {
			if (!e.getMessage().contains("401"))
				MJRBotUtilities.logErrorMessage(e);
			else {
				MJRBotUtilities.logErrorMessage("Unable to check for new channel username for " + bot.getChannelName() + " due to invalid oauth token!");
				OAuthManager.refreshTokenTwitch(5, bot);
			}
		}
		return new BoolStringPair(newChannel, false);
	}

	public static void performUsernameChange(TwitchBot bot, String newChannel) {
		try {
			ConsoleUtil.textToConsole("[Twitch] " + MJRBotUtilities.getChannelNameFromBotType(BotType.Twitch, bot) + " has changed their username to " + newChannel);
			MJRBot.getDiscordBot().sendAdminEventMessage("[Twitch] " + MJRBotUtilities.getChannelNameFromBotType(BotType.Twitch, bot) + " has changed their username to " + newChannel);
			bot.disconnectTwitch();
			MySQLConnection.executeUpdate("DELETE from channels where twitch_channel_id = " + "\"" + bot.getChannelID() + "\"" + " AND bot_type = " + "\"" + "Twitch" + "\"");
			ChatBotManager.removeTwitchBot(bot);
			MySQLConnection.executeUpdate("UPDATE tokens set channel = '" + newChannel + "' where channel = " + "\"" + bot.channelName + "\"" + " AND platform = " + "\"" + "Twitch" + "\"");
			MySQLConnection.executeUpdate("UPDATE moderation_actions set channel = '" + newChannel + "' where channel = " + "\"" + bot.channelName + "\"" + " AND platform = " + "\"" + "Twitch" + "\"");
			MySQLConnection.executeUpdate("UPDATE events set channel = '" + newChannel + "' where channel = " + "\"" + bot.channelName + "\"" + " AND platform = " + "\"" + "Twitch" + "\"");
			MySQLConnection.executeUpdate("UPDATE discord_info set channel = '" + newChannel + "' where channel = " + "\"" + bot.channelName + "\"");
			MySQLConnection.executeUpdate("INSERT INTO `channels`(`name`, `twitch_channel_id`, `bot_type`) VALUES ('" + newChannel + "','" + bot.getChannelID() + "','Twitch')");
		} catch (Exception e) {
			MJRBotUtilities.logErrorMessage(e);
		}
	}
}
