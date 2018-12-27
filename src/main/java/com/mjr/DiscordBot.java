package com.mjr;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.mjr.discord.MessageManager;
import com.mjr.util.ConsoleUtil;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Channel;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.MessageEditSpec;
import reactor.core.publisher.Mono;

public class DiscordBot {
	public static Snowflake mjrlegends_guild_id = Snowflake.of(304416423147601921L);
	public static Snowflake error_channel_id = Snowflake.of(510208195629809704L);
	public static Snowflake admin_event_log_channel_id = Snowflake.of(512734468029808651L);
	public String channelName;
	public DiscordClient client;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(20);

	public void startBot(String token) {
		if (token.length() == 0)
			return;
		ConsoleUtil.textToConsole("Starting Discord bot");
		client = createClient(token);
		EventDispatcher dispatcher = client.getEventDispatcher();
		dispatcher.on(MessageCreateEvent.class).subscribe(event -> event.getMessage().getContent().ifPresent(c -> MessageManager.onMessageReceivedEvent(event)));
		ConsoleUtil.textToConsole("Finshed starting Discord bot");
	}

	public DiscordClient createClient(String token) {
		try {
			DiscordClient temp = new DiscordClientBuilder(token).build();
			temp.login().subscribe();
			return temp;
		} catch (Exception e) {
			MJRBot.logErrorMessage("Discord: Bot was unable to create a connection, error: " + e.getMessage());
			return null;
		}
	}

	public void sendErrorMessage(String message) {
		if (client == null)
			return;
		sendMessage(client.getChannelById(DiscordBot.error_channel_id), message);
	}

	public void sendAdminEventMessage(String message) {
		if (client == null)
			return;
		sendMessage(client.getChannelById(DiscordBot.admin_event_log_channel_id), message);
	}

	public Mono<Message> sendMessage(Mono<Channel> channel, String message) {
		if (client == null)
			return null;
		if (client.isConnected() == false)
			return null;
		ConsoleUtil.textToConsole("Discord: Attempting to send message to Channel: " + channel.ofType(TextChannel.class).block().getName() + " Message: " + message);
		try {
			Mono<Message> messageReturn = channel.ofType(TextChannel.class).block().createMessage(message);
			messageReturn.subscribe();
			return messageReturn;		
		} catch (Exception e) {
			if (e.getMessage().contains("Discord didn't return a response")) {
				MJRBot.logErrorMessage("Discord: Message could not be sent, retrying to send message");
			} else {
				MJRBot.logErrorMessage("Discord: Message could not be sent, error: " + e.getMessage());
			}
			return null;
		}
	}

	public void sendDirectMessageToUser(Mono<User> user, String message) {
		if (client == null)
			return;
		if (client.isConnected() == false)
			return;
		ConsoleUtil.textToConsole("Discord: Attempting to send message to User: " + user.block().getUsername() + " Message: " + message);
		try {
			user.block().getPrivateChannel().block().createMessage(message).subscribe();
		} catch (Exception e) {
			MJRBot.logErrorMessage("Discord: Private Message could not be sent, error: " + e.getMessage());
			MJRBot.logErrorMessage(":warning: unable to send message of ```" + message + "```" + " to user " + user.block().getUsername());
		}
	}

	public void sendTimedMessage(Mono<Channel> channel, String message, Long delay, TimeUnit timeUnit) {
		if (client == null)
			return;
		if (client.isConnected() == false)
			return;
		ConsoleUtil.textToConsole("Discord: Attempting to send timed message to Channel: " + channel.ofType(TextChannel.class).block().getName() + " Message: " + message);
		Mono<Message> lastMessage = sendMsgToChannelReturnMessageOBJ(channel, message);
		if (lastMessage != null) {
			scheduler.schedule(() -> {
				deleteMessage(channel, lastMessage);
			}, delay, timeUnit);
		}
	}

	public void sendTimedMessage(Mono<Channel> channel, String message) {
		if (client == null)
			return;
		if (client.isConnected() == false)
			return;
		ConsoleUtil.textToConsole("Discord: Attempting to send timed message to Channel: " + channel.ofType(TextChannel.class).block().getName() + " Message: " + message);
		Mono<Message> lastMessage = sendMsgToChannelReturnMessageOBJ(channel, message);
		if (lastMessage != null) {
			scheduler.schedule(() -> {
				deleteMessage(channel, lastMessage);
			}, 1L, TimeUnit.MINUTES);
		}
	}

	public Mono<Message> sendMsgToChannelReturnMessageOBJ(Mono<Channel> channel, String message) {
		if (client == null)
			return null;
		if (client.isConnected() == false)
			return null;
		ConsoleUtil.textToConsole("Discord: Attempting to return obj send message to Channel: " + channel.ofType(TextChannel.class).block().getName() + " Message: " + message);
		try {
			Mono<Message> messageReturn = channel.ofType(TextChannel.class).block().createMessage(message);
			messageReturn.subscribe();
			return messageReturn;
		} catch (Exception e) {
			if (e.getMessage().contains("Discord didn't return a response")) {
				MJRBot.logErrorMessage("Discord: Message could not be sent, retrying to send message");
			} else {
				MJRBot.logErrorMessage("Discord: Message could not be sent, error: " + e.getMessage());
			}
			return null;
		}
	}

	public void nukeChannel(Mono<Channel> channel) {
		if (client == null)
			return;
		if (client.isConnected() == false)
			return;
		try {
			ConsoleUtil.textToConsole("Discord: Attempting to run a nuke of all messages on Channel: " + channel.ofType(TextChannel.class).block().getName());
			// channel.bulkDelete(channel.ofType(TextChannel.class).block().getMessagesBefore(messageId).getFullMessageHistory());
		} catch (Exception e) {
			MJRBot.logErrorMessage("Discord: Channel could not be nuked of messages due to: " + e.getMessage());
			MJRBot.logErrorMessage(":warning: unable to nuke all messages from " + channel.ofType(TextChannel.class).block().getName() + " due to an error, please check the log for details!");
		}
	}

	public void deleteMessage(Mono<Message> message) {
		if (client == null)
			return;
		if (client.isConnected() == false)
			return;
		try {
			ConsoleUtil.textToConsole("Discord: Deleting message with id: " + message.block().getId() + " from " + message.block().getChannel().ofType(TextChannel.class).block().getName());
			message.block().delete().subscribe();
		} catch (Exception e) {
			MJRBot.logErrorMessage("Discord: Message could not be deleted, error: " + e.getMessage());
			MJRBot.logErrorMessage(":warning: unable to delete a message in " + message.block().getChannel().ofType(TextChannel.class).block().getName() + " due to an error, please check the log for details!");
		}
	}

	public void deleteMessage(Mono<Channel> channel, Snowflake messageID) {
		if (client == null)
			return;
		if (client.isConnected() == false)
			return;
		deleteMessage(channel, client.getMessageById(channel.block().getId(), messageID));
	}

	public void deleteMessage(Mono<Channel> channel, Mono<Message> message) {
		if (client == null)
			return;
		if (client.isConnected() == false)
			return;
		try {
			ConsoleUtil.textToConsole("Discord: Deleting message with id: " + message.block().getId() + " from " + channel.ofType(TextChannel.class).block().getName());
			channel.ofType(TextChannel.class).block().getMessageById(message.block().getId()).block().delete().subscribe();
		} catch (Exception e) {
			MJRBot.logErrorMessage("Discord: Message could not be deleted, error: " + e.getMessage());
			MJRBot.logErrorMessage(":warning: unable to delete a message in " + channel.ofType(TextChannel.class).block().getName() + " due to an error, please check the log for details!");
		}
	}

	public Mono<Message> editMessage(Mono<Message> oldMessage, Consumer<MessageEditSpec> newMessage) {
		if (client == null)
			return null;
		if (client.isConnected() == false)
			return null;
		try {
			oldMessage.block().edit(newMessage);
			oldMessage.subscribe();
			return oldMessage;
		} catch (Exception e) {
			if (e.getMessage().contains("Discord didn't return a response")) {
				MJRBot.logErrorMessage("Discord: Message could not be edited, retrying to edit message");
			} else {
				MJRBot.logErrorMessage("Discord: Message could not be edited, error: " + e.getMessage());
			}
			return null;
		}
	}
}
