package com.mjr;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;

public class ConsoleUtil {

    public static void TextToConsole(String message, String MessageType, String sender) {
	TextToConsole(null, "", message, MessageType, sender);
    }

    public static void TextToConsole(BotType type, String channel, String message, String MessageType, String sender) {
	if (MessageType == "Chat") {
	    if (sender != null) {
		if (Permissions.hasPermission(type, channel, sender, PermissionLevel.BotOwner.getName())) {
		    System.out
			    .println("[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Bot Owner]" + sender + ": " + message);
		} else if (Permissions.hasPermission(type, channel, sender, PermissionLevel.Bot.getName())) {
		    System.out.println("[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Bot]" + sender + ": " + message);
		} else if (Permissions.hasPermission(type, channel, sender, PermissionLevel.KnownBot.getName())) {
		    System.out
			    .println("[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Known Bot]" + sender + ": " + message);
		} else if (type == BotType.Twitch && sender.endsWith(channel)) {
		    System.out
			    .println("[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Streamer]" + sender + ": " + message);
		} else if (type == BotType.Mixer && sender.equalsIgnoreCase(channel)) {
		    System.out
			    .println("[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Streamer]" + sender + ": " + message);
		} else if (Permissions.hasPermission(type, channel, sender, PermissionLevel.Moderator.getName())) {
		    System.out.println(
			    "[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " - [Moderator]" + sender + ": " + message);
		} else if (Permissions.hasPermission(type, channel, sender, PermissionLevel.User.getName())) {
		    System.out.println("[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " - [User]" + sender + ": " + message);
		}
	    }
	} else {
	    System.out.println("[MJRBot Info] " + "[Bot Type] " + (type == null ? "Unknown" : type.getTypeName()) + " [Channel] "
		    + (channel == "" ? "Unknown" : channel) + " - " + message);
	}
    }
}
