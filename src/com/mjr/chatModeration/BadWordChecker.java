package com.mjr.chatModeration;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions;
import com.mjr.Permissions.PermissionLevel;

public class BadWordChecker {
    public static boolean BadwordsBan = false;

    public static String[] BadWords = { "Fuck", "Shit", "Cunt", "Wanker", "Tosser", "Slag", "Slut", "Penis", "Cock", "Vagina", "Pussy",
	    "Boobs", "Tits", "Ass", "Bastard", "Twat", "Nigger", "Bitch", "***", "Nigga" };

    public static void CheckBadWords(Object bot, BotType type, String channelName, String message, String sender) {
	for (int i = 0; i < BadWords.length; i++) {
	    String check = BadWords[i];
	    if (message.toLowerCase().contains(check.toLowerCase())) {
		if (!Permissions.hasPermission(bot, type, channelName, sender, PermissionLevel.Moderator.getName())) {
		    BadwordsBan = true;
		}
	    }
	}
    }

    public static boolean hasUsedBadword() {
	if (BadwordsBan)
	    return true;
	return false;
    }
}
