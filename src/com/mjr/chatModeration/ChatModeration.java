package com.mjr.chatModeration;

import com.mjr.MJRBot;
import com.mjr.Permissions;
import com.mjr.TwitchBot;
import com.mjr.files.Config;
import com.mjr.files.Ranks;

public class ChatModeration {
    public static void onCommand(TwitchBot bot, String channel, String sender, String login, String hostname, String message) {
	// ChatModeration
	if (Ranks.getRank(sender) == "gold")
	    return;
	else if (MJRBot.getTwitchBot() != null && !Permissions.getPermissionLevel(sender).equalsIgnoreCase("User"))
	    return;
	else {
	    if (Config.getSetting("LinkChecker").equalsIgnoreCase("true")) {
		LinkChecker.CheckLink(message, sender);
		if (LinkChecker.Link) {
		    if (!LinkChecker.isAllowed()) {
			if (Ranks.getRank(sender) == "sliver")
			    return;
			else if (Ranks.getRank(sender) == "bronze")
			    return;
			bot.sendMessage(bot.getChannel(), sender + " " + Config.getSetting("LinkWarning"));
			bot.sendMessage(bot.getChannel(), "/timeout " + sender);
			bot.sendMessage(bot.getChannel(), "/unban " + sender);
			return;
		    }
		}
	    }
	    if (Config.getSetting("Badwords").equalsIgnoreCase("true")) {
		BadWordChecker.CheckBadWords(message, sender);
		if (BadWordChecker.hasUsedBadword()) {
		    bot.sendMessage(bot.getChannel(), sender + " " + Config.getSetting("LanguageWarning"));
		    bot.sendMessage(bot.getChannel(), "/timeout " + sender);
		    bot.sendMessage(bot.getChannel(), "/unban " + sender);
		    BadWordChecker.BadwordsBan = false;
		    return;
		}
	    }
	    if (Config.getSetting("Emote").equalsIgnoreCase("true")) {
		EmoteChecker.getEmotes();
		EmoteChecker.checkEmoteSpam(message, sender);
		if (EmoteChecker.hasUsedToMany()) {
		    if (Ranks.getRank(sender) == "sliver")
			return;
		    bot.sendMessage(bot.getChannel(), sender + " " + Config.getSetting("EmoteWarning"));
		    bot.sendMessage(bot.getChannel(), "/timeout " + sender);
		    bot.sendMessage(bot.getChannel(), "/unban " + sender);
		    EmoteChecker.Ban = false;
		    return;
		}
	    }
	    if (Config.getSetting("Symbol").equalsIgnoreCase("true") && !EmoteChecker.hasUsedToMany()) {
		SymbolChecker.checkSymbolSpam(message, sender);
		if (SymbolChecker.hasUsedToMany()) {
		    if (Ranks.getRank(sender) == "sliver")
			return;
		    bot.sendMessage(bot.getChannel(), sender + " " + Config.getSetting("SymbolWarning"));
		    bot.sendMessage(bot.getChannel(), "/timeout " + sender);
		    bot.sendMessage(bot.getChannel(), "/unban " + sender);
		    SymbolChecker.Ban = false;
		    return;
		}
	    }
	}
    }
}
