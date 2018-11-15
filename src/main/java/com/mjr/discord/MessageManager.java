package com.mjr.discord;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mjr.CrossChatLink;
import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.sql.MySQLConnection;
import com.mjr.storage.Config;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class MessageManager {

	@EventSubscriber
	public void onMessageReceivedEvent(MessageReceivedEvent event) {
		ResultSet channel = MySQLConnection.executeQuery("SELECT channel FROM discord_info WHERE guild_id = '" + event.getGuild().getLongID() + "'");
		try {
			if (channel.next()) {
				String channelName = channel.getString("channel");
				ResultSet channel_id = MySQLConnection.executeQuery("SELECT cross_link_channel_id FROM discord_info WHERE channel = '" + channelName + "'");
				if (channel_id.next()) {
					if (channel_id.getString("cross_link_channel_id") != null) {
						if (Config.getSetting("DiscordChatLink", channelName).equalsIgnoreCase("true") && event.getChannel().getLongID() == Long.parseLong(channel_id.getString("cross_link_channel_id"))) {
							CrossChatLink.sendMessageAcrossPlatforms(BotType.Discord, channelName, event.getAuthor().getDisplayName(event.getGuild()), event.getMessage().getContent().toString());
						}
					}
				}
			}
		} catch (NumberFormatException | SQLException e) {
			MJRBot.logErrorMessage(e);
		}
	}
}
