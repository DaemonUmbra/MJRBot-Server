package com.mjr;

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
	if (messageType == MessageType.Chat) {
	    if (sender != null) {
		if (Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.BotOwner.getName())) {
		    System.out
			    .println("[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Bot Owner]" + sender + ": " + message);
		} else if (Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.Bot.getName())) {
		    System.out.println("[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Bot]" + sender + ": " + message);
		} else if (Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.KnownBot.getName())) {
		    System.out
			    .println("[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Known Bot]" + sender + ": " + message);
		} else if (type == BotType.Twitch && sender.endsWith(channel)) {
		    System.out
			    .println("[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Streamer]" + sender + ": " + message);
		} else if (type == BotType.Mixer && sender.equalsIgnoreCase(channel)) {
		    System.out
			    .println("[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Streamer]" + sender + ": " + message);
		} else if (Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.Moderator.getName())) {
		    System.out.println(
			    "[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " - [Moderator]" + sender + ": " + message);
		} else if (Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.User.getName())) {
		    System.out.println("[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " - [User]" + sender + ": " + message);
		}
	    }
	} else if (messageType == MessageType.Bot) {
	    System.out.println("[MJRBot Info] " + "[Bot Type] " + (type == null ? "Unknown" : type.getTypeName()) + " [Channel] "
		    + (channel == "" ? "Unknown" : channel) + " - " + message);
	} else if (messageType == MessageType.Console) {
	    System.out.println("[MJRBot Info]" + " - " + message);
	}
    }
}
