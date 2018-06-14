package com.mjr;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.files.Config;

public class ConsoleUtil {

    public static void TextToConsole(String message, String MessageType, String sender) {
	TextToConsole(null, null, message, MessageType, sender);
   }

    public static void TextToConsole(BotType type, String channel, String message, String MessageType, String sender) {
	if (MessageType == "Chat") {
	    if (sender != null) {
		if (Permissions.hasPermission(type, channel, sender, PermissionLevel.Bot.getName())) {
		    System.out.println("\n" + "[Bot] " + sender + ": " + message);
		} else if (type == BotType.Twitch && sender.endsWith(channel)) {
		    System.out.println("\n" + "[Streamer] " + sender + ": " + message);
		} else if (type == BotType.Mixer && sender.equalsIgnoreCase(channel)) {
		    System.out.println("\n" + "[Streamer] " + sender + ": " + message);
		} else if (Permissions.hasPermission(type, channel, sender, PermissionLevel.BotOwner.getName())) {
		    System.out.println("\n" + "[Bot Owner] " + sender + ": " + message);
		} else if (Permissions.hasPermission(type, channel, sender, PermissionLevel.Moderator.getName())) {
		    System.out.println("\n" + "[Moderator] " + sender + ": " + message);
		} else if (Permissions.hasPermission(type, channel, sender, PermissionLevel.User.getName())) {
		    System.out.println("\n" + "[User] " + sender + ": " + message);
		}
	    }
	} else {
	    System.out.println("[MJRBot Info] " + message);
	}
    }

    public void LeaveChannel(BotType type, String channelName) {
	if (Config.getSetting("SilentJoin").equalsIgnoreCase("false")) {
	    if (type == BotType.Twitch)
		if (MJRBot.getTwitchBotByChannelName(channelName).isConnected())
		    MJRBot.getTwitchBotByChannelName(channelName)
			    .MessageToChat(MJRBot.getTwitchBotByChannelName(channelName).getNick() + " Disconnected!");
		else if (MJRBot.getMixerBotByChannelName(channelName).isConnected()
			& MJRBot.getMixerBotByChannelName(channelName).isAuthenticated())
		    MJRBot.getMixerBotByChannelName(channelName)
			    .sendMessage(MJRBot.getMixerBotByChannelName(channelName).getBotName() + " Disconnected!");
	}
	if (type == BotType.Twitch) {
	    MJRBot.getTwitchBotByChannelName(channelName).partChannel(MJRBot.getTwitchBotByChannelName(channelName).getChannel());
	    TextToConsole(type, channelName, "Left " + MJRBot.getTwitchBotByChannelName(channelName).getChannel() + " channel", "Bot",
		    null);
	    MJRBot.getTwitchBotByChannelName(channelName).viewers = new String[0];
	    MJRBot.getTwitchBotByChannelName(channelName).ConnectedToChannel = false;
	    MJRBot.getTwitchBotByChannelName(channelName).setChannel("");
	} else {
	    MJRBot.getMixerBotByChannelName(channelName).disconnect();
	    TextToConsole(type, channelName, "Left " + channelName + " channel", "Bot", null);
	}
    }

    public void JoinChannel(BotType type, String channelName) {
	MJRBot.getTwitchBotByChannelName(channelName).setChannel(channelName);
	TextToConsole(type, channelName, "Joined " + MJRBot.getTwitchBotByChannelName(channelName).getChannel()
		.substring(MJRBot.getTwitchBotByChannelName(channelName).getChannel().indexOf("#") + 1) + " channel", "Bot", null);
	MJRBot.getTwitchBotByChannelName(channelName).joinChannel(MJRBot.getTwitchBotByChannelName(channelName).getChannel());
    }
}
