package com.mjr.mjrbot.bots.bases;

import com.mjr.discordframework.DiscordBotBase;
import com.mjr.mjrbot.discord.MessageManager;
import com.mjr.mjrbot.util.ConsoleUtil;
import com.mjr.mjrbot.util.MJRBotUtilities;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.util.Snowflake;

public class DiscordBot extends DiscordBotBase {

	public static Snowflake mjrlegends_guild_id = Snowflake.of(304416423147601921L);
	public static Snowflake error_channel_id = Snowflake.of(510208195629809704L);
	public static Snowflake admin_event_log_channel_id = Snowflake.of(512734468029808651L);
	public String channelName;

	public DiscordBot(String token) {
		super(token);
	}

	@Override
	public void setupEvents() {
		this.getDispatcher().on(MessageCreateEvent.class).doOnError(error -> MJRBotUtilities.logErrorMessage("Please restart MJRBot via console: Error", error))
				.subscribe(event -> event.getMessage().getContent().ifPresent(c -> MessageManager.onMessageReceivedEvent(event)));
	}

	public void sendErrorMessage(String message) {
		if (this.getClient() == null)
			return;
		sendMessage(this.getClient().getChannelById(DiscordBot.error_channel_id), message);
	}

	public void sendAdminEventMessage(String message) {
		if (this.getClient() == null)
			return;
		sendMessage(this.getClient().getChannelById(DiscordBot.admin_event_log_channel_id), message);
	}

	@Override
	public void onOutputMessage(MessageType type, String message) {
		if (type.equals(MessageType.Error))
			MJRBotUtilities.logErrorMessage(message);
		else
			ConsoleUtil.textToConsole(message);
	}
}
