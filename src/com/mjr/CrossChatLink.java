package com.mjr;

import com.mjr.MJRBot.BotType;
import com.mjr.storage.Config;
import com.mjr.storage.ConfigMain;

public class CrossChatLink {

    public static void sendMessageAcrossPlatforms(BotType type, String channelName, String sender, String message) {
	//Exclude commands & bots messages
	if(message.startsWith("!") || sender.equalsIgnoreCase(ConfigMain.getSetting("TwitchUsername")) || sender.equalsIgnoreCase(ConfigMain.getSetting("MixerUsername/BotName")))
	    return;
	boolean twitch = type == BotType.Twitch ? false : true;
	boolean mixer = type == BotType.Mixer ? false : true;
	String platformPrefex = type == BotType.Twitch ? "[Twitch]" : "Mixer";
	String senderPrefex = " " + sender + ": ";

	if(twitch && Config.getSetting("TwitchChatLink", channelName).equalsIgnoreCase("true"))
	    Utilities.sendMessage(BotType.Twitch, channelName, platformPrefex + senderPrefex + message);
	if(mixer && Config.getSetting("MixerChatLink", channelName).equalsIgnoreCase("true"))
	    Utilities.sendMessage(BotType.Twitch, channelName, platformPrefex + senderPrefex + message);
    }
}
