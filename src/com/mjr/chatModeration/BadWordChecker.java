package com.mjr.chatModeration;

import com.mjr.MJRBot.BotType;
import com.mjr.MixerBot;
import com.mjr.Permissions;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;

public class BadWordChecker {
    public static String[] badWords = { "Fuck", "Shit", "Cunt", "Wanker", "Tosser", "Slag", "Slut", "Penis", "Cock", "Vagina", "Pussy",
	    "Boobs", "Tits", "Ass", "Bastard", "Twat", "Nigger", "Bitch", "***", "Nigga" };

    public static boolean isBadWord(Object bot, BotType type, String channelName, String message, String sender) {
	for (int i = 0; i < badWords.length; i++) {
	    if (message.toLowerCase().contains(badWords[i].toLowerCase())) {
		if (Permissions.hasPermission(bot, type, channelName, sender, PermissionLevel.Moderator.getName())) {
		    return false;
		} else if (type == BotType.Twitch && ((TwitchBot) bot).linkPermitedUsers.contains(sender)) {
		    return false;
		} else if (type == BotType.Mixer && ((MixerBot) bot).linkPermitedUsers.contains(sender)) {
		    return false;
		} else {
		    return true;
		}
	    }
	}
	return false;
    }
}
