package com.mjr;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;

import com.mjr.MJRBot.StorageType;
import com.mjr.storage.Config;
import com.mjr.storage.ConfigMain;
import com.mjr.util.ConsoleUtil;

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

	private static HashMap<String, TwitchBot> twitchBots = new HashMap<String, TwitchBot>();
	private static HashMap<String, MixerBot> mixerBots = new HashMap<String, MixerBot>();

	public static void createBot(String channel, String botType) {
		channel = channel.toLowerCase(Locale.ENGLISH);
		if (botType.equalsIgnoreCase("twitch") && channel != "") {
			try {
				if (MJRBot.storageType == StorageType.File) {
					Config.loadDefaults(channel);
				} else {
					Config.loadDefaultsDatabase(channel);
				}
			} catch (IOException e) {
				MJRBot.logErrorMessage(e);
			}
			TwitchBot bot = new TwitchBot();
			ChatBotManager.addTwitchBot(channel, bot);
			bot.init(channel);
		} else if (botType.equalsIgnoreCase("mixer") && channel != "") {
			try {
				if (MJRBot.storageType == StorageType.File) {
					Config.loadDefaults(channel);
				} else
					Config.loadDefaultsDatabase(channel);
			} catch (IOException e) {
				MJRBot.logErrorMessage(e);
			}
			MixerBot bot = new MixerBot(channel);
			ChatBotManager.addMixerBot(channel, bot);
			try {
				bot.joinChannel(channel);
			} catch (SQLException e) {
				MJRBot.logErrorMessage(e);
			}
		} else if (channel != "")
			ConsoleUtil.textToConsole("Unknown Type of Connection!");
		else
			ConsoleUtil.textToConsole("Invalid entry for Channel Name!!");
	}

	public static HashMap<String, TwitchBot> getTwitchBots() {
		return twitchBots;
	}

	public static void setTwitchBots(HashMap<String, TwitchBot> bots) {
		twitchBots = bots;
	}

	public static void addTwitchBot(String channelName, TwitchBot bot) {
		ConsoleUtil.textToConsole("[Twitch] " + ConfigMain.getSetting("TwitchUsername") + " has been added to the channel " + channelName);
		MJRBot.bot.sendAdminEventMessage("[Twitch] " + ConfigMain.getSetting("TwitchUsername") + " has been added to the channel " + channelName);
		twitchBots.put(channelName, bot);
	}

	public static void removeTwitchBot(TwitchBot bot) {
		ConsoleUtil.textToConsole("[Twitch] " + ConfigMain.getSetting("TwitchUsername") + " has been removed from the channel " + bot.channelName);
		MJRBot.bot.sendAdminEventMessage("[Twitch] " + ConfigMain.getSetting("TwitchUsername") + " has been removed from the channel " + bot.channelName);
		twitchBots.remove(bot.channelName, bot);
	}

	public static void removeTwitchBot(String channelName) {
		ConsoleUtil.textToConsole("[Twitch] " + ConfigMain.getSetting("TwitchUsername") + " has been removed from the channel " + channelName);
		MJRBot.bot.sendAdminEventMessage("[Twitch] " + ConfigMain.getSetting("TwitchUsername") + " has been removed from the channel " + channelName);
		twitchBots.remove(channelName, getTwitchBotByChannelName(channelName));
	}

	public static HashMap<String, MixerBot> getMixerBots() {
		return mixerBots;
	}

	public static void setMixerBots(HashMap<String, MixerBot> bots) {
		mixerBots = bots;
	}

	public static void addMixerBot(String channelName, MixerBot bot) {
		ConsoleUtil.textToConsole("[Mixer] " + ConfigMain.getSetting("MixerUsername/BotName") + " has been added to the channel " + channelName);
		MJRBot.bot.sendAdminEventMessage("[Mixer] " + ConfigMain.getSetting("MixerUsername/BotName") + " has been added to the channel " + channelName);
		mixerBots.put(channelName, bot);
	}

	public static void removeMixerBot(MixerBot bot) {
		ConsoleUtil.textToConsole("[Mixer] " + ConfigMain.getSetting("MixerUsername/BotName") + " has been removed from the channel " + bot.channelName);
		MJRBot.bot.sendAdminEventMessage("[Mixer] " + ConfigMain.getSetting("MixerUsername/BotName") + " has been removed from the channel " + bot.channelName);
		mixerBots.remove(bot.channelName, bot);
	}

	public static void removeMixerBot(String channelName) {
		ConsoleUtil.textToConsole("[Mixer] " + ConfigMain.getSetting("MixerUsername/BotName") + " has been removed from the channel " + channelName);
		MJRBot.bot.sendAdminEventMessage("[Mixer] " + ConfigMain.getSetting("MixerUsername/BotName") + " has been removed from the channel " + channelName);
		mixerBots.remove(channelName, getMixerBotByChannelName(channelName));
	}

	public static TwitchBot getTwitchBotByChannelName(String channelName) {
		for (String bot : twitchBots.keySet()) {
			if (bot.equalsIgnoreCase(channelName))
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
