package com.mjr.chatModeration;

import com.mjr.Permissions;

public class BadWordChecker {
    public static boolean BadwordsBan = false;

    public static String[] BadWords = { "Fuck", "Shit", "Cunt", "Wanker", "Tosser", "Slag", "Slut", "Penis", "Cock", "Vagina", "Pussy",
	    "Boobs", "Tits", "Ass", "Bastard", "Twat", "Nigger", "Bitch", "***", "fuck", "shit", "cunt", "wanker", "tosser", "slag",
	    "slut", "penis", "cock", "vagina", "pussy", "boobs", "tits", "ass", "bastard", "twat", "nigger", "bitch" };

    public static void CheckBadWords(String message, String sender) {
	for (int i = 0; i < BadWords.length; i++) {
	    String Check = BadWords[i];
	    if (message.contains(Check)) {
		if (Permissions.getPermissionLevel(sender) != "Moderator") {
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
