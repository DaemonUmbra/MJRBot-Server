package com.mjr;

import java.sql.ResultSet;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot.ConnectionType;
import com.mjr.sql.MySQLConnection;
import com.mjr.storage.Config;
import com.mjr.storage.ConfigMain;
import com.mjr.util.ConsoleUtil;
import com.mjr.util.Utilities;

import discord4j.core.object.util.Snowflake;

public class CrossChatLink {

	public static void sendMessageAcrossPlatforms(BotType type, String channelName, String sender, String message) {
		try {
			// Exclude commands & bots messages
			if (message.startsWith("!") || sender.equalsIgnoreCase(ConfigMain.getSetting("TwitchUsername")) || sender.equalsIgnoreCase(ConfigMain.getSetting("MixerUsername/BotName")))
				return;
			boolean twitch = type == BotType.Twitch ? false : true;
			boolean mixer = type == BotType.Mixer ? false : true;
			boolean discord = Config.getSetting("DiscordEnabled", channelName).equalsIgnoreCase("true") ? true : false;
			String platformPrefex = null;
			if(type == BotType.Discord)
				platformPrefex = "[Discord]";
			else
				platformPrefex = type == BotType.Twitch ? "[Twitch]" : "[Mixer]";
			String senderPrefex = " " + sender + ": ";
	
			if (twitch && Config.getSetting("TwitchChatLink", channelName).equalsIgnoreCase("true"))
				Utilities.sendMessage(BotType.Twitch, channelName, platformPrefex + senderPrefex + message);
			if (mixer && Config.getSetting("MixerChatLink", channelName).equalsIgnoreCase("true"))
				Utilities.sendMessage(BotType.Mixer, channelName, platformPrefex + senderPrefex + message);
			if (discord && Config.getSetting("DiscordChatLink", channelName).equalsIgnoreCase("true") && type != BotType.Discord) {
				if(MJRBot.connectionType == ConnectionType.Database) {
					ResultSet channel_id = MySQLConnection.executeQuery("SELECT cross_link_channel_id FROM discord_info WHERE channel = '" + channelName + "'");
					if (channel_id.next()) {
						if(channel_id.getString("cross_link_channel_id") != null) {
							Snowflake channel = Snowflake.of(Long.parseLong(channel_id.getString("cross_link_channel_id")));
							MJRBot.bot.sendMessage(MJRBot.bot.getClient().getChannelById(channel), platformPrefex + senderPrefex + message);
						}
					}
				}
				else {
					ConsoleUtil.textToConsole("Discord Crosslink is disabled, as it is currently not supported on the file based storage type!");
				}
			}
		} catch (Exception e) {
			MJRBot.logErrorMessage(e);
		}
	}
}
