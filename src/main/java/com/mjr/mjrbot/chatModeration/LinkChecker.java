package com.mjr.mjrbot.chatModeration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.util.PermissionsManager;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class LinkChecker {

	private static List<String> endings = new ArrayList<String>(Arrays.asList(".com", ".co.uk", ".co", ".tv", ".net", ".pro", ".org", ".gov", ".ly"));

	private static final Pattern urlPattern = Pattern.compile("(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)" + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*" + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

	public static boolean CheckLink(BotType type, Object bot, String message, String sender) {
		boolean isLink = false;
		message = message.replace(" ", "");
		message = message.replace(",", "");
		message = message.replace("www.", "");
		message = message.replace("-", "");
		message = message.replace("*", "");
		message = message.replace("/", "");
		message = message.replace("+", "");
		message = message.replace("@", "");
		message = message.replace("#", "");
		for (String end : endings)
			if (message.contains(end))
				isLink = true;
		if (!isLink) {

			Matcher matcher = urlPattern.matcher(message);
			while (matcher.find()) {
				isLink = true;
			}
		}
		if (isLink) {
			if (PermissionsManager.hasPermission(bot, type, sender, PermissionLevel.Moderator.getName()))
				return true;
			else if (type == BotType.Twitch && ((TwitchBot) bot).getTwitchData().linkPermitedUsers.contains(sender))
				return true;
			else if (type == BotType.Mixer && ((MixerBot) bot).getMixerData().linkPermitedUsers.contains(sender))
				return true;
			else
				return false;
		}
		return true;
	}
}
