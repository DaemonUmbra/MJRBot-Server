package com.mjr;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;

public class ConsoleUtil {

    public enum MessageType {
	Chat("Chat"), Bot("Bot"), Console("Console");

	private final String name;

	MessageType(String name) {
	    this.name = name;
	}

	public String getName() {
	    return name;
	}
    }

    public static void TextToConsole(String message) {
	TextToConsole(null, null, "", message, MessageType.Console, null);
    }

    public static void TextToConsole(Object bot, BotType type, String channel, String message, MessageType messageType, String sender) {
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	if (messageType == MessageType.Chat) {
	    if (sender != null) {
		if (Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.BotOwner.getName())) {
		    MJRBot.getLogger().info(dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel
			    + " [Bot Owner]" + sender + ": " + message);
		} else if (Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.Bot.getName())) {
		    MJRBot.getLogger().info(dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Bot]"
			    + sender + ": " + message);
		} else if (Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.KnownBot.getName())) {
		    MJRBot.getLogger().info(dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel
			    + " [Known Bot]" + sender + ": " + message);
		} else if (type == BotType.Twitch && sender.endsWith(channel)) {
		    MJRBot.getLogger().info(dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel
			    + " [Streamer]" + sender + ": " + message);
		} else if (type == BotType.Mixer && sender.equalsIgnoreCase(channel)) {
		    MJRBot.getLogger().info(dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel
			    + " [Streamer]" + sender + ": " + message);
		} else if (Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.Moderator.getName())) {
		    MJRBot.getLogger().info(dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel
			    + " - [Moderator]" + sender + ": " + message);
		} else if (Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.User.getName())) {
		    MJRBot.getLogger().info(dateFormat.format(date) + "[Bot Type]" + type.getTypeName() + " [Channel]" + channel
			    + " - [User]" + sender + ": " + message);
		}
	    }
	} else if (messageType == MessageType.Bot) {
	    MJRBot.getLogger()
		    .info(dateFormat.format(date) + " [MJRBot Info] " + "[Bot Type] " + (type == null ? "Unknown" : type.getTypeName())
			    + " [Channel] " + (channel == "" ? "Unknown" : channel) + " - " + message);
	} else if (messageType == MessageType.Console) {
	    MJRBot.getLogger().info(dateFormat.format(date) + " [MJRBot Info]" + " - " + message);
	}
    }
}
