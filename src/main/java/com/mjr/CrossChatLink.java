package com.mjr;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mjr.MJRBot.BotType;
import com.mjr.sql.MySQLConnection;
import com.mjr.storage.Config;
import com.mjr.storage.ConfigMain;
import com.mjr.util.Utilities;

public class CrossChatLink {

	public static void sendMessageAcrossPlatforms(BotType type, String channelName, String sender, String message) {
		// Exclude commands & bots messages
		if (message.startsWith("!") || sender.equalsIgnoreCase(ConfigMain.getSetting("TwitchUsername")) || sender.equalsIgnoreCase(ConfigMain.getSetting("MixerUsername/BotName")))
			return;
		boolean twitch = type == BotType.Twitch ? false : true;
		boolean mixer = type == BotType.Mixer ? false : true;
		boolean discord = Config.getSetting("DiscordEnabled", channelName).equalsIgnoreCase("true") ? true : false;
		String platformPrefex = type == BotType.Twitch ? "[Twitch]" : "[Mixer]";
		String senderPrefex = " " + sender + ": ";

		if (twitch && Config.getSetting("TwitchChatLink", channelName).equalsIgnoreCase("true"))
			Utilities.sendMessage(BotType.Twitch, channelName, platformPrefex + senderPrefex + message);
		if (mixer && Config.getSetting("MixerChatLink", channelName).equalsIgnoreCase("true"))
			Utilities.sendMessage(BotType.Mixer, channelName, platformPrefex + senderPrefex + message);
		if (discord && Config.getSetting("DiscordChatLink", channelName).equalsIgnoreCase("true") && MJRBot.useMannalMode == false) {
			ResultSet channel_id = MySQLConnection.executeQuery("SELECT cross_link_channel_id FROM discord_info WHERE channel = '" + channelName + "'");
			try {
				if (channel_id.next()) {
					if(channel_id.getString("cross_link_channel_id") != null)
						MJRBot.bot.sendMessage(MJRBot.bot.client.getChannelByID(Long.parseLong(channel_id.getString("cross_link_channel_id"))), platformPrefex + senderPrefex + message);
				}
			} catch (NumberFormatException | SQLException e) {
				MJRBot.logErrorMessage(e);
			}
		}
	}
}
