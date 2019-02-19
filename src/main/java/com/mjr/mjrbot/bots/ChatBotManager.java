package com.mjr.mjrbot.bots;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.storage.Config;
import com.mjr.mjrbot.storage.ConfigMain;
import com.mjr.mjrbot.util.ConsoleUtil;
import com.mjr.mjrbot.util.MJRBotUtilities;

public class ChatBotManager {

	public enum BotType {
		Twitch("Twitch"), Mixer("Mixer"), Discord("Discord");

		private final String typeName;

		BotType(String typeName) {
			this.typeName = typeName;
		}

		public String getTypeName() {
			return typeName;
		}

		public static BotType getTypeByName(String permission) {
			for (BotType type : BotType.values())
				if (type.getTypeName().equalsIgnoreCase(permission))
					return type;
			return null;
		}
	}

	private static HashMap<Integer, TwitchBot> twitchBots = new HashMap<Integer, TwitchBot>();
	private static HashMap<String, MixerBot> mixerBots = new HashMap<String, MixerBot>();

	public static void createBot(String channel, int channelID, String botType) {
		if (channel != null)
			channel = channel.toLowerCase(Locale.ENGLISH);
		if (botType.equalsIgnoreCase("twitch") && channelID != 0) {
			try {
				if (MJRBot.storageType == StorageType.File) {
					Config.loadDefaults(BotType.Twitch, channel, channelID);
				} else {
					Config.loadDefaultsDatabase(BotType.Twitch, channel, channelID);
				}
			} catch (IOException e) {
				MJRBotUtilities.logErrorMessage(e);
			}
			TwitchBot bot = new TwitchBot();
			ChatBotManager.addTwitchBot(channelID, bot);
			bot.init(TwitchBot.getChannelNameFromChannelID(channelID), channelID);
		} else if (botType.equalsIgnoreCase("mixer") && channel != "") {
			try {
				if (MJRBot.storageType == StorageType.File) {
					Config.loadDefaults(BotType.Mixer, channel, channelID);
				} else
					Config.loadDefaultsDatabase(BotType.Mixer, channel, channelID);
			} catch (IOException e) {
				MJRBotUtilities.logErrorMessage(e);
			}
			MixerBot bot = new MixerBot(channel);
			ChatBotManager.addMixerBot(channel, bot);
			try {
				bot.joinChannel(channel);
			} catch (SQLException e) {
				MJRBotUtilities.logErrorMessage(e);
			}
		} else if (channel != "")
			ConsoleUtil.textToConsole("Unknown Type of Connection!");
		else
			ConsoleUtil.textToConsole("Invalid entry for Channel Name!!");
	}

	public static HashMap<Integer, TwitchBot> getTwitchBots() {
		return twitchBots;
	}

	public static void setTwitchBots(HashMap<Integer, TwitchBot> bots) {
		twitchBots = bots;
	}

	public static void addTwitchBot(int channelID, TwitchBot bot) {
		ConsoleUtil.textToConsole("[Twitch] " + ConfigMain.getSetting("TwitchUsername") + " has been added to the channel " + TwitchBot.getChannelNameFromChannelID(channelID));
		MJRBot.getDiscordBot().sendAdminEventMessage("[Twitch] " + ConfigMain.getSetting("TwitchUsername") + " has been added to the channel " + TwitchBot.getChannelNameFromChannelID(channelID));
		twitchBots.put(channelID, bot);
	}

	public static void removeTwitchBot(TwitchBot bot) {
		ConsoleUtil.textToConsole("[Twitch] " + ConfigMain.getSetting("TwitchUsername") + " has been removed from the channel " + MJRBotUtilities.getChannelNameFromBotType(BotType.Twitch, bot));
		MJRBot.getDiscordBot().sendAdminEventMessage("[Twitch] " + ConfigMain.getSetting("TwitchUsername") + " has been removed from the channel " + MJRBotUtilities.getChannelNameFromBotType(BotType.Twitch, bot));
		twitchBots.remove(bot.getChannelID(), bot);
	}

	public static void removeTwitchBot(int channelID) {
		ConsoleUtil.textToConsole("[Twitch] " + ConfigMain.getSetting("TwitchUsername") + " has been removed from the channel " + TwitchBot.getChannelNameFromChannelID(channelID));
		MJRBot.getDiscordBot().sendAdminEventMessage("[Twitch] " + ConfigMain.getSetting("TwitchUsername") + " has been removed from the channel " + TwitchBot.getChannelNameFromChannelID(channelID));
		twitchBots.remove(channelID, getTwitchBotByChannelID(channelID));
	}

	public static HashMap<String, MixerBot> getMixerBots() {
		return mixerBots;
	}

	public static void setMixerBots(HashMap<String, MixerBot> bots) {
		mixerBots = bots;
	}

	public static void addMixerBot(String channelName, MixerBot bot) {
		ConsoleUtil.textToConsole("[Mixer] " + ConfigMain.getSetting("MixerUsername/BotName") + " has been added to the channel " + channelName);
		MJRBot.getDiscordBot().sendAdminEventMessage("[Mixer] " + ConfigMain.getSetting("MixerUsername/BotName") + " has been added to the channel " + channelName);
		mixerBots.put(channelName, bot);
	}

	public static void removeMixerBot(MixerBot bot) {
		ConsoleUtil.textToConsole("[Mixer] " + ConfigMain.getSetting("MixerUsername/BotName") + " has been removed from the channel " + bot.getChannelName());
		MJRBot.getDiscordBot().sendAdminEventMessage("[Mixer] " + ConfigMain.getSetting("MixerUsername/BotName") + " has been removed from the channel " + bot.getChannelName());
		mixerBots.remove(bot.getChannelName(), bot);
	}

	public static void removeMixerBot(String channelName) {
		ConsoleUtil.textToConsole("[Mixer] " + ConfigMain.getSetting("MixerUsername/BotName") + " has been removed from the channel " + channelName);
		MJRBot.getDiscordBot().sendAdminEventMessage("[Mixer] " + ConfigMain.getSetting("MixerUsername/BotName") + " has been removed from the channel " + channelName);
		mixerBots.remove(channelName, getMixerBotByChannelName(channelName));
	}

	public static TwitchBot getTwitchBotByChannelID(int channelID) {
		for (Integer bot : twitchBots.keySet()) {
			if (bot == channelID)
				return twitchBots.get(bot);
		}
		return null;
	}

	public static MixerBot getMixerBotByChannelName(String channelName) {
		for (String bot : mixerBots.keySet()) {
			if (bot.equalsIgnoreCase(channelName))
				return mixerBots.get(bot);
		}
		return null;
	}
}
