package com.mjr;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.files.Config;

public class ConsoleUtil {

    public static void TextToConsole(String message, String MessageType, String sender) {
	TextToConsole(null, "", message, MessageType, sender);
    }

    public static void TextToConsole(BotType type, String channel, String message, String MessageType, String sender) {
	if (MessageType == "Chat") {
	    if (sender != null) {
		if (Permissions.hasPermission(type, channel, sender, PermissionLevel.Bot.getName())) {
		    System.out.println(
			    "\n" + "[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Bot]" + sender + ": " + message);
		} else if (type == BotType.Twitch && sender.endsWith(channel)) {
		    System.out.println(
			    "\n" + "[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Streamer]" + sender + ": " + message);
		} else if (type == BotType.Mixer && sender.equalsIgnoreCase(channel)) {
		    System.out.println(
			    "\n" + "[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Streamer]" + sender + ": " + message);
		} else if (Permissions.hasPermission(type, channel, sender, PermissionLevel.BotOwner.getName())) {
		    System.out.println(
			    "\n" + "[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Bot Owner]" + sender + ": " + message);
		} else if (Permissions.hasPermission(type, channel, sender, PermissionLevel.Moderator.getName())) {
		    System.out.println(
			    "\n" + "[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " - [Moderator]" + sender + ": " + message);
		} else if (Permissions.hasPermission(type, channel, sender, PermissionLevel.User.getName())) {
		    System.out.println(
			    "\n" + "[Bot Type]" + type.getTypeName() + " [Channel]" + channel + " - [User]" + sender + ": " + message);
		}
	    }
	} else {
	    System.out.println("[MJRBot Info] " + "[Bot Type] " +(type == null ? "Unknown" : type.getTypeName()) + " [Channel] "
		    + (channel == "Unknown" ? "" : channel) + " - " + message);
	}
    }

    public void LeaveChannel(BotType type, String channelName) {
	if (Config.getSetting("SilentJoin").equalsIgnoreCase("false")) {
	    if (type == BotType.Twitch) {
		TwitchBot twitchBot = MJRBot.getTwitchBotByChannelName(channelName);
		if (twitchBot.isConnected())
		    twitchBot.MessageToChat(twitchBot.getNick() + " Disconnected!");
	    } else if (type == BotType.Mixer) {
		MixerBot mixerBot = MJRBot.getMixerBotByChannelName(channelName);
		if (mixerBot.isConnected() & mixerBot.isAuthenticated())
		    mixerBot.sendMessage(MJRBot.getMixerBotByChannelName(channelName).getBotName() + " Disconnected!");
	    }
	}
	if (type == BotType.Twitch) {
	    TwitchBot twitchBot = MJRBot.getTwitchBotByChannelName(channelName);
	    twitchBot.partChannel(twitchBot.getChannel());
	    TextToConsole(type, channelName, "Left " + twitchBot.getChannel() + " channel", "Bot", null);
	    twitchBot.viewers = new String[0];
	    twitchBot.ConnectedToChannel = false;
	    twitchBot.setChannel("");
	} else {
	    MJRBot.getMixerBotByChannelName(channelName).disconnect();
	    TextToConsole(type, channelName, "Left " + channelName + " channel", "Bot", null);
	}
    }

    public void JoinChannel(BotType type, String channelName) {
	TwitchBot twitchBot = MJRBot.getTwitchBotByChannelName(channelName);
	twitchBot.setChannel(channelName);
	TextToConsole(type, channelName, "Joined " + twitchBot.getChannel().substring(twitchBot.getChannel().indexOf("#") + 1) + " channel",
		"Bot", null);
	twitchBot.joinChannel(twitchBot.getChannel());
    }
}
