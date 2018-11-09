package com.mjr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mjr.MJRBot.BotType;
import com.mjr.storage.Config;

public class Permissions {

	public static List<String> knownBots = new ArrayList<String>(Arrays.asList("nightbot", "pretzelrocks", "streamelements", "moobot", "xanbot"));

	public enum PermissionLevel {
		User("User", 0), Subscriber("Subscriber", 1), Moderator("Moderator", 2), Streamer("Streamer", 4), KnownBot("KnownBot", 5), Bot("Bot", 6), BotOwner("BotOwner", 6);

		private final String permission;
		private final int tierValue;

		PermissionLevel(String permission, int tierValue) {
			this.permission = permission;
			this.tierValue = tierValue;
		}

		public String getName() {
			return permission;
		}

		public int getTierValue() {
			return tierValue;
		}

		public static int getTierValueByName(String permission) {
			for (PermissionLevel level : PermissionLevel.values())
				if (level.getName().equalsIgnoreCase(permission))
					return level.getTierValue();
			return 0;
		}
	}

	public static String getPermissionLevel(Object bot, BotType type, String channelName, String user) {
		user = user.toLowerCase();
		if (type == BotType.Twitch) {
			if (user.equalsIgnoreCase(channelName))
				return PermissionLevel.Streamer.getName();
			else if (user.equalsIgnoreCase(MJRBot.getTwitchBotByChannelName(channelName).getBotName()))
				return PermissionLevel.Bot.getName();
			else if (knownBots.contains(user.toLowerCase()))
				return PermissionLevel.KnownBot.getName();
			else if (user.equalsIgnoreCase("mjrlegends"))
				return PermissionLevel.BotOwner.getName();
			if (((TwitchBot) bot).moderators != null && ((TwitchBot) bot).moderators.contains(user) || user.equalsIgnoreCase(Config.getSetting("UserName", channelName)) || user.equalsIgnoreCase(channelName))
				return PermissionLevel.Moderator.getName();
			if (((TwitchBot) bot).subscribers != null && ((TwitchBot) bot).subscribers.contains(user))
				return PermissionLevel.Subscriber.getName();
			else
				return PermissionLevel.User.getName();
		} else {
			if (user.equalsIgnoreCase(channelName))
				return PermissionLevel.Streamer.getName();
			else if (user.equalsIgnoreCase(MJRBot.getMixerBotByChannelName(channelName).getBotName()))
				return PermissionLevel.Bot.getName();
			else if (knownBots.contains(user.toLowerCase()))
				return PermissionLevel.KnownBot.getName();
			else if (user.equalsIgnoreCase("mjrlegends"))
				return PermissionLevel.BotOwner.getName();
			else if (!MJRBot.getMixerBotByChannelName(channelName).getModerators().isEmpty() && MJRBot.getMixerBotByChannelName(channelName).getModerators().contains(user) || user.equalsIgnoreCase(Config.getSetting("UserName", channelName))
					|| user.equalsIgnoreCase(channelName))
				return PermissionLevel.Moderator.getName();
			else
				return PermissionLevel.User.getName();
		}
	}

	public static boolean hasPermission(Object bot, BotType type, String channelName, String user, String permission) {
		String userPermissionLevel = getPermissionLevel(bot, type, channelName, user);
		if (getPermissionLevel(bot, type, channelName, user).equalsIgnoreCase(permission))
			return true;
		else if (PermissionLevel.getTierValueByName(userPermissionLevel) >= PermissionLevel.getTierValueByName(permission))
			return true;
		return false;
	}
}