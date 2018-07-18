package com.mjr.chatModeration;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions;
import com.mjr.Permissions.PermissionLevel;
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
		LinkChecker.CheckLink(bot, type, channel, message, sender);
		if (LinkChecker.Link) {
		    if (!LinkChecker.isAllowed()) {
			if (Ranks.getRank(sender, channel) == "sliver")
			    return;
			else if (Ranks.getRank(sender, channel) == "bronze")
			    return;
			Utilities.sendMessage(type, channel, sender + " " + Config.getSetting("LinkWarning", channel));
			Utilities.sendMessage(type, channel, "/timeout " + sender);
			Utilities.sendMessage(type, channel, "/unban " + sender);
			return;
		    }
		}
	    }
	    if (Config.getSetting("Badwords", channel).equalsIgnoreCase("true")) {
		BadWordChecker.CheckBadWords(bot, type, channel, message, sender);
		if (BadWordChecker.hasUsedBadword()) {
		    Utilities.sendMessage(type, channel, sender + " " + Config.getSetting("LanguageWarning", channel));
		    Utilities.sendMessage(type, channel, "/timeout " + sender);
		    Utilities.sendMessage(type, channel, "/unban " + sender);
		    BadWordChecker.BadwordsBan = false;
		    return;
		}
	    }
	    if (Config.getSetting("Emote", channel).equalsIgnoreCase("true")) {
		EmoteChecker.getEmotes(type, channel);
		EmoteChecker.checkEmoteSpam(message, sender, channel);
		if (EmoteChecker.hasUsedToMany()) {
		    if (Ranks.getRank(sender, channel) == "sliver")
			return;
		    Utilities.sendMessage(type, channel, sender + " " + Config.getSetting("EmoteWarning", channel));
		    Utilities.sendMessage(type, channel, "/timeout " + sender);
		    Utilities.sendMessage(type, channel, "/unban " + sender);
		    EmoteChecker.Ban = false;
		    return;
		}
	    }
	    if (Config.getSetting("Symbol", channel).equalsIgnoreCase("true") && !EmoteChecker.hasUsedToMany()) {
		SymbolChecker.checkSymbolSpam(message, sender, channel);
		if (SymbolChecker.hasUsedToMany()) {
		    if (Ranks.getRank(sender, channel) == "sliver")
			return;
		    Utilities.sendMessage(type, channel, sender + " " + Config.getSetting("SymbolWarning", channel));
		    Utilities.sendMessage(type, channel, "/timeout " + sender);
		    Utilities.sendMessage(type, channel, "/unban " + sender);
		    SymbolChecker.Ban = false;
		    return;
		}
	    }
	}
    }
}
