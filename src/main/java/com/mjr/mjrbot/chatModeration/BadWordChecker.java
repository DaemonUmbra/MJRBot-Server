package com.mjr.mjrbot.chatModeration;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.storage.sql.MySQLConnection;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.PermissionsManager;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class BadWordChecker {
	public static List<String> badWords = new ArrayList<String>();

	public static boolean isBadWord(Object bot, BotType type, String message, String sender) {
		try {
			if (MJRBot.storageType == StorageType.Database) {
				ResultSet set = null;
				if (type == BotType.Twitch)
					set = MySQLConnection.executeQuery("SELECT * FROM badwords WHERE twitch_channel_id = '" + MJRBotUtilities.getChannelIDFromBotType(type, bot) + "'");
				else if (type == BotType.Mixer)
					set = MySQLConnection.executeQuery("SELECT * FROM badwords WHERE mixer_channel = '" + MJRBotUtilities.getChannelNameFromBotType(type, bot) + "'");
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
					if (PermissionsManager.hasPermission(bot, type, sender, PermissionLevel.Moderator.getName())) {
						return false;
					} else {
						return true;
					}
				}
			}
		} catch (Exception e) {
			MJRBotUtilities.logErrorMessage(e);
		}
		return false;
	}
}
