package com.mjr.mjrbot.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.storage.ChannelConfigManager;

public class PermissionsManager {

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
		String channelName = MJRBotUtilities.getChannelNameFromBotType(type, bot);
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
			else if (((TwitchBot) bot).getTwitchData().getModerators() != null && ((TwitchBot) bot).getTwitchData().getModerators().contains(user) || user.equalsIgnoreCase(ChannelConfigManager.getSetting("UserName", channelName))
					|| user.equalsIgnoreCase(channelName))
				return PermissionLevel.Moderator.getName();
			else if (((TwitchBot) bot).getTwitchData().getSubscribers() != null && ((TwitchBot) bot).getTwitchData().getSubscribers().contains(user))
				return PermissionLevel.Subscriber.getName();
			else if (((TwitchBot) bot).getTwitchData().getVips() != null && ((TwitchBot) bot).getTwitchData().getVips().contains(user))
				return PermissionLevel.VIP.getName();
			else if (((TwitchBot) bot).getTwitchData().getFollowers() != null && ((TwitchBot) bot).getTwitchData().getFollowers().contains(user))
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
					|| user.equalsIgnoreCase(ChannelConfigManager.getSetting("UserName", channelName)) || user.equalsIgnoreCase(channelName))
				return PermissionLevel.Moderator.getName();
			else if (((MixerBot) bot).getMixerData().getSubscribers() != null && ((MixerBot) bot).getMixerData().getSubscribers().contains(user))
				return PermissionLevel.Subscriber.getName();
			else if (((MixerBot) bot).getMixerData().getFollowers() != null && ((MixerBot) bot).getMixerData().getFollowers().contains(user))
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
	
	public static boolean hasPermission(Object bot, BotType type, String user, PermissionLevel permission) {
		return hasPermission(bot, type, user, permission.getName());
	}
}