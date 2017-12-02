package com.mjr;

import com.mjr.files.Config;

public class ConsoleUtli {

    public static void TextToConsole(String message, String MessageType, String sender) {
	if (MessageType == "Chat") {
	    if (sender != null) {
		if (Permissions.getPermissionLevel(sender) == "Bot") {
		    System.out.println("\n" + "[Bot] " + sender + ": " + message);
		} else if (MJRBot.getTwitchBot() != null && sender.endsWith(MJRBot.getTwitchBot().getChannel().replace("#", ""))) {
		    System.out.println("\n" + "[Streamer] " + sender + ": " + message);
		} else if (MJRBot.getMixerBot() != null && sender.equalsIgnoreCase(MJRBot.getChannel())) {
		    System.out.println("\n" + "[Streamer] " + sender + ": " + message);
		} else if (Permissions.getPermissionLevel(sender) == "Moderator" && sender.equalsIgnoreCase("mjrlegends")) {
		    System.out.println("\n" + "[Bot Owner] " + sender + ": " + message);
		} else if (Permissions.getPermissionLevel(sender) == "Moderator") {
		    System.out.println("\n" + "[Moderator] " + sender + ": " + message);
		} else if (Permissions.getPermissionLevel(sender) == "User") {
		    System.out.println("\n" + "[User] " + sender + ": " + message);
		}
	    }
	} else {
	    System.out.println("[MJRBot Info] " + message);
	}
    }

    public void LeaveChannel() {
	if (Config.getSetting("SilentJoin").equalsIgnoreCase("false")) {
	    if (MJRBot.getTwitchBot() != null)
		if (MJRBot.getTwitchBot().isConnected())
		    MJRBot.getTwitchBot().MessageToChat(MJRBot.getTwitchBot().getNick() + " Disconnected!");
		else if (MJRBot.getMixerBot().isConnected() & MJRBot.getMixerBot().isAuthenticated())
		    MJRBot.getMixerBot().sendMessage(MJRBot.getMixerBot().getBotName() + " Disconnected!");
	}
	if (MJRBot.getTwitchBot() != null) {
	    MJRBot.getTwitchBot().partChannel(MJRBot.getTwitchBot().getChannel());
	    TextToConsole("Left " + MJRBot.getTwitchBot().getChannel() + " channel", "Bot", null);
	    MJRBot.getTwitchBot().viewers = new String[0];
	    MJRBot.getTwitchBot().ConnectedToChannel = false;
	    MJRBot.getTwitchBot().setChannel("");
	} else {
	    MJRBot.getMixerBot().disconnect();
	    TextToConsole("Left " + MJRBot.getChannel() + " channel", "Bot", null);
	}
    }

    public void JoinChannel(String Stream) {
	MJRBot.getTwitchBot().setChannel(Stream);
	TextToConsole("Joined " + MJRBot.getTwitchBot().getChannel().substring(MJRBot.getTwitchBot().getChannel().indexOf("#") + 1)
		+ " channel", "Bot", null);
	MJRBot.getTwitchBot().joinChannel(MJRBot.getTwitchBot().getChannel());
    }
}
