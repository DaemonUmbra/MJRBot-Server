package com.mjr.chatModeration;

import com.mjr.MJRBot.BotType;
import com.mjr.MixerBot;
import com.mjr.Permissions;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.Utilities;
import com.mjr.files.Config;
import com.mjr.files.Ranks;

public class ChatModeration {
    public static void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message) {
	// ChatModeration
	if (Config.getSetting("Ranks", channel).equalsIgnoreCase("true") && Ranks.getRank(sender, channel) == "gold")
	    return;
	else if (!Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.User.getName()))
	    return;
	else {
	    if (Config.getSetting("LinkChecker", channel).equalsIgnoreCase("true")) {
		boolean allowed = LinkChecker.CheckLink(bot, type, channel, message, sender);
		if (!allowed) {
		    if (Ranks.getRank(sender, channel) == "sliver")
			return;
		    else if (Ranks.getRank(sender, channel) == "bronze")
			return;
		    Utilities.sendMessage(type, channel, sender + " " + Config.getSetting("LinkWarning", channel));
		    Utilities.sendMessage(type, channel, "/timeout " + sender);
		    Utilities.sendMessage(type, channel, "/unban " + sender);
		    return;
		}
		if (type == BotType.Twitch)
		    ((TwitchBot) bot).linkPermitedUsers.remove(sender);
		else
		    ((MixerBot) bot).linkPermitedUsers.remove(sender);
	    }
	    if (Config.getSetting("Badwords", channel).equalsIgnoreCase("true")) {
		boolean isBadWord = BadWordChecker.isBadWord(bot, type, channel, message, sender);
		if (isBadWord) {
		    if (Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.Moderator.getName()))
			return;
		    else {
			Utilities.sendMessage(type, channel, sender + " " + Config.getSetting("LanguageWarning", channel));
			Utilities.sendMessage(type, channel, "/timeout " + sender);
			Utilities.sendMessage(type, channel, "/unban " + sender);
			return;
		    }
		}
	    }
	    if (Config.getSetting("Emote", channel).equalsIgnoreCase("true")) {
		EmoteChecker.getEmotes(type, channel);
		boolean isSpam = EmoteChecker.isSpammingEmotes(message, sender, channel);
		if (isSpam) {
		    if (Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.Moderator.getName()))
			return;
		    else {
			if (Ranks.getRank(sender, channel) == "sliver")
			    return;
			Utilities.sendMessage(type, channel, sender + " " + Config.getSetting("EmoteWarning", channel));
			Utilities.sendMessage(type, channel, "/timeout " + sender);
			Utilities.sendMessage(type, channel, "/unban " + sender);
			return;
		    }
		}
	    }
	    if (Config.getSetting("Symbol", channel).equalsIgnoreCase("true")) {
		boolean isSpam = SymbolChecker.isSpammingSymbol(message, sender, channel);
		if (isSpam) {
		    if (Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.Moderator.getName()))
			return;
		    if (type == BotType.Twitch && ((TwitchBot) bot).linkPermitedUsers.contains(sender))
			return;
		    if (type == BotType.Mixer && ((MixerBot) bot).linkPermitedUsers.contains(sender))
			return;
		    else {
			if (Ranks.getRank(sender, channel) == "sliver")
			    return;
			Utilities.sendMessage(type, channel, sender + " " + Config.getSetting("SymbolWarning", channel));
			Utilities.sendMessage(type, channel, "/timeout " + sender);
			Utilities.sendMessage(type, channel, "/unban " + sender);
			return;
		    }
		}
	    }
	}
    }
}
