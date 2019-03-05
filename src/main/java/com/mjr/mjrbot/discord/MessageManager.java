package com.mjr.mjrbot.discord;

import java.sql.ResultSet;

import com.mjr.mjrbot.CrossChatLink;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.storage.sql.MySQLConnection;
import com.mjr.mjrbot.util.MJRBotUtilities;

import discord4j.core.event.domain.message.MessageCreateEvent;

public class MessageManager {

	public static void onMessageReceivedEvent(MessageCreateEvent event) {
		try {
			ResultSet channel = MySQLConnection.executeQuery("SELECT channel FROM discord_info WHERE guild_id = '" + event.getGuildId().get().asLong() + "'");
			if (channel.next()) {
				String channelName = channel.getString("channel");
				ResultSet channel_id = MySQLConnection.executeQuery("SELECT cross_link_channel_id FROM discord_info WHERE channel = '" + channelName + "'");
				if (channel_id.next()) {
					if (!channel_id.getString("cross_link_channel_id").equals("")) {
						if (ChannelConfigManager.getSetting("DiscordChatLink", channelName).equalsIgnoreCase("true") && event.getMessage().getChannelId().asLong() == Long.parseLong(channel_id.getString("cross_link_channel_id"))) {
							CrossChatLink.sendMessageAcrossPlatforms(BotType.Discord, null, event.getMember().get().getDisplayName(), event.getMessage().getContent().get().toString(), channelName);
						}
					}
				}
			}
		} catch (Exception e) {
			MJRBotUtilities.logErrorMessage("onMessageReceivedEvent Error", e);
		}
	}
}
