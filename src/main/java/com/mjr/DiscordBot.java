package com.mjr;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

public class DiscordBot {
	public static long mjrlegends_guild_id = 304416423147601921L;
	public static long error_channel_id = 510208195629809704L;
	public String channelName;
	public IDiscordClient client;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(20);

	public void startBot(String token) {
		MJRBot.getLogger().info("Starting Discord bot");
		client = createClient(token, true);
		EventDispatcher dispatcher = client.getDispatcher();
		MJRBot.getLogger().info("Finshed starting Discord bot");
	}

	public IDiscordClient createClient(String token, boolean login) {
		ClientBuilder clientBuilder = new ClientBuilder();
		clientBuilder.withToken(token);
		try {
			if (login) {
				return clientBuilder.login();
			} else {
				return clientBuilder.build();
			}
		} catch (DiscordException e) {
			MJRBot.getLogger().info("Discord: Bot was unable to create a connection, error: " + e.getMessage());
			return null;
		}
	}

	public void sendMessage(IChannel channel, String message) {
		MJRBot.getLogger().info("Discord: Attempting to send message to Channel: " + channel.getName() + " Message: " + message);
		IMessage lastMessage = null;
		int numAttempts = 0;
		do { // Do While loop to fix Discord4j Discord didn't return a response error (Apache Httpclient issue) & to make sure the return is the message object not null
			lastMessage = RequestBuffer.request(() -> {
				try {
					return channel.sendMessage(message);
				} catch (DiscordException e) {
					if (e.getMessage().contains("Discord didn't return a response")) {
						MJRBot.getLogger().info("Discord: Message could not be sent, retrying to send message");
					} else {
						MJRBot.getLogger().info("Discord: Message could not be sent, error: " + e.getMessage());
					}
					return null;
				} catch (MissingPermissionsException e) {
					MJRBot.getLogger().info("Discord: Private Message could not be sent, error: " + e.getMessage());
					return null;
				}
			}).get();
			numAttempts++;
		} while (lastMessage == null && numAttempts < 10);
		if (numAttempts >= 9) {
			this.sendMessage(client.getGuildByID(DiscordBot.mjrlegends_guild_id).getChannelByID(DiscordBot.error_channel_id),
					":warning: unable to send message of ```" + message + "```" + " to channel " + channel.getName() + " after 10 attempts due to an error, please check the log for details!");
		}
	}

	public void sendDirectMessageToUser(IUser user, String message) {
		MJRBot.getLogger().info("Discord: Attempting to send message to User: " + user.getName() + " Message: " + message);
		RequestBuffer.request(() -> {
			try {
				user.getOrCreatePMChannel().sendMessage(message);
			} catch (DiscordException e) {
				MJRBot.getLogger().info("Discord: Private Message could not be sent, error: " + e.getMessage());
				this.sendMessage(client.getGuildByID(DiscordBot.mjrlegends_guild_id).getChannelByID(DiscordBot.error_channel_id),
						":warning: unable to send message of ```" + message + "```" + " to user " + user.getName() + " after 10 attempts due to an error, please check the log for details!");
			} catch (MissingPermissionsException e) {
				MJRBot.getLogger().info("Discord: Private Message could not be sent, error: " + e.getMessage());
				this.sendMessage(client.getGuildByID(DiscordBot.mjrlegends_guild_id).getChannelByID(DiscordBot.error_channel_id),
						":warning: unable to send message of ```" + message + "```" + " to user " + user.getName() + " after 10 attempts due to an error, please check the log for details!");
			}
		});
	}

	public void sendTimedMessage(IChannel channel, String message, Long delay, TimeUnit timeUnit) {
		MJRBot.getLogger().info("Discord: Attempting to send timed message to Channel: " + channel.getName() + " Message: " + message);
		IMessage lastMessage = sendMsgToChannelReturnMessageOBJ(channel, message);
		if (lastMessage != null) {
			scheduler.schedule(() -> {
				deleteMessage(channel, lastMessage);
			}, delay, timeUnit);
		}
	}

	public void sendTimedMessage(IChannel channel, String message) {
		MJRBot.getLogger().info("Discord: Attempting to send timed message to Channel: " + channel.getName() + " Message: " + message);
		IMessage lastMessage = sendMsgToChannelReturnMessageOBJ(channel, message);
		if (lastMessage != null) {
			scheduler.schedule(() -> {
				deleteMessage(channel, lastMessage);
			}, 1L, TimeUnit.MINUTES);
		}
	}

	public IMessage sendMsgToChannelReturnMessageOBJ(IChannel channel, String message) {
		MJRBot.getLogger().info("Discord: Attempting to return obj send message to Channel: " + channel.getName() + " Message: " + message);
		IMessage lastMessage = null;
		int numAttempts = 0;
		do { // Do While loop to fix Discord4j Discord didn't return a response error (Apache Httpclient issue) & to make sure the return is the message object not null
			lastMessage = RequestBuffer.request(() -> {
				try {
					return channel.sendMessage(message);
				} catch (DiscordException e) {
					if (e.getMessage().contains("Discord didn't return a response")) {
						MJRBot.getLogger().info("Discord: Message could not be sent, retrying to send message");
					} else {
						MJRBot.getLogger().info("Discord: Message could not be sent, error: " + e.getMessage());
					}
					return null;
				} catch (MissingPermissionsException e) {
					MJRBot.getLogger().info("Discord: Message could not be sent, error: " + e.getMessage());
					return null;
				}
			}).get();
		} while (lastMessage == null && numAttempts < 10);
		if (numAttempts >= 9) {
			this.sendMessage(client.getGuildByID(DiscordBot.mjrlegends_guild_id).getChannelByID(DiscordBot.error_channel_id),
					":warning: unable to send message of ```" + message + "```" + " to channel " + channel.getName() + " after 10 attempts due to an error, please check the log for details!");
		}
		return lastMessage;
	}

	public void nukeChannel(IChannel channel) {
		try {
			MJRBot.getLogger().info("Discord: Attempting to run a nuke of all messages on Channel: " + channel.getName());
			channel.bulkDelete(channel.getFullMessageHistory());
		} catch (DiscordException e) {
			MJRBot.getLogger().info("Discord: Channel could not be nuked of messages due to: " + e.getMessage());
			this.sendMessage(client.getGuildByID(DiscordBot.mjrlegends_guild_id).getChannelByID(DiscordBot.error_channel_id), ":warning: unable to nuke all messages from " + channel.getName() + " due to an error, please check the log for details!");
		} catch (MissingPermissionsException e) {
			MJRBot.getLogger().info("Discord: Channel could not be nuked of messages due, error: " + e.getMessage());
			this.sendMessage(client.getGuildByID(DiscordBot.mjrlegends_guild_id).getChannelByID(DiscordBot.error_channel_id), ":warning: unable to nuke all messages from " + channel.getName() + " due to an error, please check the log for details!");
		}
	}

	public void deleteMessage(IMessage message) {
		RequestBuffer.request(() -> {
			try {
				MJRBot.getLogger().info("Discord: Deleting message with id: " + message.getLongID() + " from " + message.getChannel().getName());
				message.delete();
			} catch (DiscordException e) {
				MJRBot.getLogger().info("Discord: Message could not be deleted, error: " + e.getMessage());
				this.sendMessage(client.getGuildByID(DiscordBot.mjrlegends_guild_id).getChannelByID(DiscordBot.error_channel_id),
						":warning: unable to delete a message in " + message.getChannel().getName() + " due to an error, please check the log for details!");
			} catch (MissingPermissionsException e) {
				MJRBot.getLogger().info("Discord: Message could not be deleted, error: " + e.getMessage());
				this.sendMessage(client.getGuildByID(DiscordBot.mjrlegends_guild_id).getChannelByID(DiscordBot.error_channel_id),
						":warning: unable to delete a message in " + message.getChannel().getName() + " due to an error, please check the log for details!");
			}
		}).get();
	}

	public void deleteMessage(Long channelID, Long messageID) {
		IChannel channel = client.getChannelByID(channelID);
		deleteMessage(channel, messageID);
	}

	public void deleteMessage(IChannel channel, Long messageID) {
		deleteMessage(channel, channel.getMessageByID(messageID));
	}

	public void deleteMessage(IChannel channel, IMessage message) {
		RequestBuffer.request(() -> {
			try {
				MJRBot.getLogger().info("Discord: Deleting message with id: " + message.getLongID() + " from " + channel.getName());
				channel.getMessageByID(message.getLongID()).delete();
			} catch (DiscordException e) {
				MJRBot.getLogger().info("Discord: Message could not be deleted, error: " + e.getMessage());
				this.sendMessage(client.getGuildByID(DiscordBot.mjrlegends_guild_id).getChannelByID(DiscordBot.error_channel_id), ":warning: unable to delete a message in " + channel.getName() + " due to an error, please check the log for details!");
			} catch (MissingPermissionsException e) {
				MJRBot.getLogger().info("Discord: Message could not be deleted, error: " + e.getMessage());
				this.sendMessage(client.getGuildByID(DiscordBot.mjrlegends_guild_id).getChannelByID(DiscordBot.error_channel_id), ":warning: unable to delete a message in " + channel.getName() + " due to an error, please check the log for details!");
			}
		}).get();
	}

	public void editMessage(IMessage oldMessage, String newMessage) {
		IMessage lastMessage = null;
		int numAttempts = 0;
		do { // Do While loop to fix Discord4j Discord didn't return a response error (Apache Httpclient issue)
			lastMessage = RequestBuffer.request(() -> {
				try {
					return oldMessage.edit(newMessage);
				} catch (DiscordException e) {
					if (e.getMessage().contains("Discord didn't return a response")) {
						MJRBot.getLogger().info("Discord: Message could not be edited, retrying to edit message");
					} else {
						MJRBot.getLogger().info("Discord: Message could not be edited, error: " + e.getMessage());
					}
					return null;
				} catch (MissingPermissionsException e) {
					MJRBot.getLogger().info("Discord: Message could not be edited, error: " + e.getMessage());
					return null;
				}
			}).get();
		} while (lastMessage == null || !lastMessage.getContent().replaceAll("[^A-Za-z0-9]", "").equalsIgnoreCase(newMessage.replaceAll("[^A-Za-z0-9]", "")) && numAttempts < 10);
		if (numAttempts >= 9) {
			this.sendMessage(client.getGuildByID(DiscordBot.mjrlegends_guild_id).getChannelByID(DiscordBot.error_channel_id),
					":warning: unable to edit an message in " + oldMessage.getChannel().getName() + " due to an error, please check the log for details!");
		}
	}
}
