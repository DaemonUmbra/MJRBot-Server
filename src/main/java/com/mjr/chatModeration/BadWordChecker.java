package com.mjr.chatModeration;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.MJRBot.StorageType;
import com.mjr.Permissions;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.sql.MySQLConnection;

public class BadWordChecker {
	public static List<String> badWords = new ArrayList<String>();

	public static boolean isBadWord(Object bot, BotType type, String channelName, String message, String sender) {
		try {
			if (MJRBot.storageType == StorageType.Database) {
				ResultSet set = MySQLConnection.executeQuery("SELECT * FROM badwords WHERE channel = '" + channelName + "'");
				if (set != null) {
					while (set.next()) {
						badWords.add(set.getString("word"));
					}
				}
			} else {
				badWords = Arrays.asList("Fuck", "Shit", "Cunt", "Wanker", "Tosser", "Slag", "Slut", "Penis", "Cock", "Vagina", "Pussy", "Boobs", "Tits", "Ass", "Bastard", "Twat", "Nigger", "Bitch", "***", "Nigga");
			}
			if (badWords.isEmpty())
				badWords = Arrays.asList("Fuck", "Shit", "Cunt", "Wanker", "Tosser", "Slag", "Slut", "Penis", "Cock", "Vagina", "Pussy", "Boobs", "Tits", "Ass", "Bastard", "Twat", "Nigger", "Bitch", "***", "Nigga");

			for (int i = 0; i < badWords.size(); i++) {
				if (message.toLowerCase().contains(badWords.get(i).toLowerCase())) {
					if (Permissions.hasPermission(bot, type, channelName, sender, PermissionLevel.Moderator.getName())) {
						return false;
					} else {
						return true;
					}
				}
			}
		} catch (Exception e) {
			MJRBot.logErrorMessage(e);
		}
		return false;
	}
}
