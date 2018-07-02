package com.mjr.chatModeration;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.files.Config;
import com.mjr.files.Ranks;

public class ChatModeration {
    public static void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message) {
	// ChatModeration
	if (Ranks.getRank(sender) == "gold")
	    return;
	else if (type == BotType.Twitch && !Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.User.getName()))
	    return;
	else {
	    if (Config.getSetting("LinkChecker").equalsIgnoreCase("true")) {
		LinkChecker.CheckLink(bot, type, channel, message, sender);
		if (LinkChecker.Link) {
		    if (!LinkChecker.isAllowed()) {
			if (Ranks.getRank(sender) == "sliver")
			    return;
			else if (Ranks.getRank(sender) == "bronze")
			    return;
			((TwitchBot) bot).sendMessage(channel, sender + " " + Config.getSetting("LinkWarning"));
			((TwitchBot) bot).sendMessage(channel, "/timeout " + sender);
			((TwitchBot) bot).sendMessage(channel, "/unban " + sender);
			return;
		    }
		}
	    }
	    if (Config.getSetting("Badwords").equalsIgnoreCase("true")) {
		BadWordChecker.CheckBadWords(bot, type, channel, message, sender);
		if (BadWordChecker.hasUsedBadword()) {
		    ((TwitchBot) bot).sendMessage(channel, sender + " " + Config.getSetting("LanguageWarning"));
		    ((TwitchBot) bot).sendMessage(channel, "/timeout " + sender);
		    ((TwitchBot) bot).sendMessage(channel, "/unban " + sender);
		    BadWordChecker.BadwordsBan = false;
		    return;
		}
	    }
	    if (Config.getSetting("Emote").equalsIgnoreCase("true")) {
		EmoteChecker.getEmotes(channel);
		EmoteChecker.checkEmoteSpam(message, sender);
		if (EmoteChecker.hasUsedToMany()) {
		    if (Ranks.getRank(sender) == "sliver")
			return;
		    ((TwitchBot) bot).sendMessage(channel, sender + " " + Config.getSetting("EmoteWarning"));
		    ((TwitchBot) bot).sendMessage(channel, "/timeout " + sender);
		    ((TwitchBot) bot).sendMessage(channel, "/unban " + sender);
		    EmoteChecker.Ban = false;
		    return;
		}
	    }
	    if (Config.getSetting("Symbol").equalsIgnoreCase("true") && !EmoteChecker.hasUsedToMany()) {
		SymbolChecker.checkSymbolSpam(message, sender);
		if (SymbolChecker.hasUsedToMany()) {
		    if (Ranks.getRank(sender) == "sliver")
			return;
		    ((TwitchBot) bot).sendMessage(channel, sender + " " + Config.getSetting("SymbolWarning"));
		    ((TwitchBot) bot).sendMessage(channel, "/timeout " + sender);
		    ((TwitchBot) bot).sendMessage(channel, "/unban " + sender);
		    SymbolChecker.Ban = false;
		    return;
		}
	    }
	}
    }
}
