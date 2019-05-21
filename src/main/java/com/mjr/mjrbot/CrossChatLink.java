package com.mjr.mjrbot;

import java.sql.ResultSet;

import com.mjr.mjrbot.MJRBot.ConnectionType;
import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.storage.BotConfigManager;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.storage.sql.MySQLConnection;
import com.mjr.mjrbot.util.ConsoleUtil;
import com.mjr.mjrbot.util.MJRBotUtilities;

import discord4j.core.object.util.Snowflake;

public class CrossChatLink {

	public static void sendMessageAcrossPlatforms(BotType type, Object bot, String sender, String message) {
		sendMessageAcrossPlatforms(type, bot, sender, message, null);
	}

	public static void sendMessageAcrossPlatforms(BotType type, Object bot, String sender, String message, String channel) {
		try {
			// Exclude commands & bots messages
			if (message.startsWith("!") || sender.equalsIgnoreCase(BotConfigManager.getSetting("TwitchUsername")) || sender.equalsIgnoreCase(BotConfigManager.getSetting("MixerUsername/BotName")))
				return;
			boolean twitch = type == BotType.Twitch ? false : true;
			boolean mixer = type == BotType.Mixer ? false : true;
			boolean discord = bot == null ? false : ChannelConfigManager.getSetting("DiscordEnabled", type, bot).equalsIgnoreCase("true") ? true : false;
			String platformPrefex = null;
			if (type == BotType.Discord)
				platformPrefex = "[Discord]";
			else
				platformPrefex = type == BotType.Twitch ? "[Twitch]" : "[Mixer]";
			String senderPrefex = " " + sender + ": ";
			if (twitch && ChannelConfigManager.getSetting("TwitchChatLink", BotType.Mixer, type == BotType.Discord ? ChatBotManager.getMixerBotByChannelName(channel) : bot).equalsIgnoreCase("true")) {
				String channelName = "";
				if (bot != null)
					channelName = ((MixerBot) bot).getChannelName();
				else
					channelName = channel;
				ResultSet set = MySQLConnection.executeQuery("SELECT * FROM channel_cross_link WHERE mixer_channel = '" + channelName + "'");
				int channelID = set.next() ? set.getInt("twitch_channel_id") : 0;
				if (channelID != 0)
					ChatBotManager.sendMessage(BotType.Twitch, ChatBotManager.getTwitchBotByChannelID(channelID), platformPrefex + senderPrefex + message);
			}
			if (mixer && ChannelConfigManager.getSetting("MixerChatLink", BotType.Twitch, type == BotType.Discord ? ChatBotManager.getTwitchBotByChannelID(TwitchBot.getChannelIDFromChannelName(channel)) : bot).equalsIgnoreCase("true")) {
				int channelID = 0;
				if (bot != null)
					channelID = ((TwitchBot) bot).getChannelID();
				else
					channelID = TwitchBot.getChannelIDFromChannelName(channel);
				ResultSet set = MySQLConnection.executeQuery("SELECT * FROM channel_cross_link WHERE twitch_channel_id = '" + channelID + "'");
				String targetChannel = set.next() ? set.getString("mixer_channel") : "";
				if (targetChannel != "")
					ChatBotManager.sendMessage(BotType.Mixer, ChatBotManager.getMixerBotByChannelName(targetChannel), platformPrefex + senderPrefex + message);
			}
			if (discord && ChannelConfigManager.getSetting("DiscordChatLink", type, bot).equalsIgnoreCase("true") && type != BotType.Discord) {
				if (MJRBot.connectionType == ConnectionType.Database) {
					ResultSet channel_id = MySQLConnection.executeQuery("SELECT cross_link_channel_id FROM discord_info WHERE channel = '" + ChatBotManager.getChannelNameFromBotType(type, bot) + "'");
					if (channel_id.next()) {
						if (!channel_id.getString("cross_link_channel_id").equals("")) {
							Snowflake targetChannel = Snowflake.of(Long.parseLong(channel_id.getString("cross_link_channel_id")));
							MJRBot.getDiscordBot().sendMessage(MJRBot.getDiscordBot().getClient().getChannelById(targetChannel), platformPrefex + senderPrefex + message);
						}
					}
				} else {
					ConsoleUtil.textToConsole("Discord Crosslink is disabled, as it is currently not supported on the file based storage type!");
				}
			}
		} catch (Exception e) {
			MJRBotUtilities.logErrorMessage(e);
		}
	}
}
