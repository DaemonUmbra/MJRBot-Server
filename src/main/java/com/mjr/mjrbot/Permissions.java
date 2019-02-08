package com.mjr.mjrbot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.storage.Config;
import com.mjr.mjrbot.util.Utilities;

public class Permissions {

	public static List<String> knownBots = new ArrayList<String>(Arrays.asList("nightbot", "pretzelrocks", "streamelements", "moobot", "xanbot"));

	public enum PermissionLevel {
		User("User", 0), Follower("Follower", 1), VIP("VIP", 2), Subscriber("Subscriber", 3), Moderator("Moderator", 4), Streamer("Streamer", 5), KnownBot("KnownBot", 6), Bot("Bot", 7), BotOwner("BotOwner", 7);

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

	public static String getPermissionLevel(Object bot, BotType type, String user) {
		String channelName = Utilities.getChannelNameFromBotType(type, bot);
		user = user.toLowerCase();
		if (type == BotType.Twitch) {
			if (user.equalsIgnoreCase(channelName))
				return PermissionLevel.Streamer.getName();
			else if (user.equalsIgnoreCase(((TwitchBot) bot).getBotName()))
				return PermissionLevel.Bot.getName();
			else if (knownBots.contains(user.toLowerCase()))
				return PermissionLevel.KnownBot.getName();
			else if (user.equalsIgnoreCase("mjrlegends"))
				return PermissionLevel.BotOwner.getName();
			else if (((TwitchBot) bot).moderators != null && ((TwitchBot) bot).moderators.contains(user) || user.equalsIgnoreCase(Config.getSetting("UserName", channelName)) || user.equalsIgnoreCase(channelName))
				return PermissionLevel.Moderator.getName();
			else if (((TwitchBot) bot).subscribers != null && ((TwitchBot) bot).subscribers.contains(user))
				return PermissionLevel.Subscriber.getName();
			else if (((TwitchBot) bot).vips != null && ((TwitchBot) bot).vips.contains(user))
				return PermissionLevel.VIP.getName();
			else if (((TwitchBot) bot).followers != null && ((TwitchBot) bot).followers.contains(user))
				return PermissionLevel.Follower.getName();
			else
				return PermissionLevel.User.getName();
		} else if (type == BotType.Mixer) {
			if (user.equalsIgnoreCase(channelName))
				return PermissionLevel.Streamer.getName();
			else if (user.equalsIgnoreCase(ChatBotManager.getMixerBotByChannelName(channelName).getBotName()))
				return PermissionLevel.Bot.getName();
			else if (knownBots.contains(user.toLowerCase()))
				return PermissionLevel.KnownBot.getName();
			else if (user.equalsIgnoreCase("mjrlegends"))
				return PermissionLevel.BotOwner.getName();
			else if (!ChatBotManager.getMixerBotByChannelName(channelName).getModerators().isEmpty() && ChatBotManager.getMixerBotByChannelName(channelName).getModerators().contains(user)
					|| user.equalsIgnoreCase(Config.getSetting("UserName", channelName)) || user.equalsIgnoreCase(channelName))
				return PermissionLevel.Moderator.getName();
			else if (((MixerBot) bot).subscribers != null && ((MixerBot) bot).subscribers.contains(user))
				return PermissionLevel.Subscriber.getName();
			else if (((MixerBot) bot).followers != null && ((MixerBot) bot).followers.contains(user))
				return PermissionLevel.Follower.getName();
			else
				return PermissionLevel.User.getName();
		}
		return PermissionLevel.User.getName();
	}

	public static boolean hasPermission(Object bot, BotType type, String user, String permission) {
		String userPermissionLevel = getPermissionLevel(bot, type, user);
		if (getPermissionLevel(bot, type, user).equalsIgnoreCase(permission))
			return true;
		else if (PermissionLevel.getTierValueByName(userPermissionLevel) >= PermissionLevel.getTierValueByName(permission))
			return true;
		return false;
	}
}