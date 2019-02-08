package com.mjr.mjrbot.chatModeration;

import com.mjr.mjrbot.AnalyticsData;
import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.MixerBot;
import com.mjr.mjrbot.Permissions;
import com.mjr.mjrbot.Permissions.PermissionLevel;
import com.mjr.mjrbot.TwitchBot;
import com.mjr.mjrbot.storage.Config;
import com.mjr.mjrbot.storage.ModerationActionsLog;
import com.mjr.mjrbot.storage.RankSystem;
import com.mjr.mjrbot.util.Utilities;

public class ChatModeration {

	public static void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message) {
		// ChatModeration
		if (Config.getSetting("Ranks", type, bot).equalsIgnoreCase("true") && RankSystem.getRank(sender, type, bot) == "gold")
			return;
		else if (!Permissions.hasPermission(bot, type, sender, PermissionLevel.User.getName()))
			return;
		else {
			AnalyticsData.addNumOfMessagedModerated(1);
			if (Config.getSetting("LinkChecker", type, bot).equalsIgnoreCase("true")) {
				boolean allowed = LinkChecker.CheckLink(type, bot, message, sender);
				if (!allowed) {
					if (RankSystem.getRank(sender, type, bot) == "sliver")
						return;
					else if (RankSystem.getRank(sender, type, bot) == "bronze")
						return;
					ModerationActionsLog.addEvent(type, bot, sender, " flagged by Link Checker", message);
					Utilities.sendMessage(type, bot, "@" + sender + " " + Config.getSetting("LinkWarning", type, bot));
					if (type == BotType.Twitch) {
						Utilities.sendMessage(type, bot, "/timeout " + sender);
						Utilities.sendMessage(type, bot, "/unban " + sender);
					} else if (type == BotType.Mixer) {
						((MixerBot) bot).deleteLastMessageForUser(sender);
					}
					return;
				}
				if (type == BotType.Twitch)
					((TwitchBot) bot).linkPermitedUsers.remove(sender);
				else if (type == BotType.Mixer)
					((MixerBot) bot).linkPermitedUsers.remove(sender);
			}
			if (Config.getSetting("Badwords", type, bot).equalsIgnoreCase("true")) {
				boolean isBadWord = BadWordChecker.isBadWord(bot, type, message, sender);
				if (isBadWord) {
					if (Permissions.hasPermission(bot, type, sender, PermissionLevel.Moderator.getName()))
						return;
					else {
						ModerationActionsLog.addEvent(type, bot, sender, " flagged by Badwords Checker", message);
						Utilities.sendMessage(type, bot, "@" + sender + " " + Config.getSetting("LanguageWarning", type, bot));
						if (type == BotType.Twitch) {
							Utilities.sendMessage(type, bot, "/timeout " + sender);
							Utilities.sendMessage(type, bot, "/unban " + sender);
						} else if (type == BotType.Mixer) {
							((MixerBot) bot).deleteLastMessageForUser(sender);
						}
						return;
					}
				}
			}
			if (Config.getSetting("Emote", type, bot).equalsIgnoreCase("true")) {
				EmoteChecker.getEmotes(type, bot);
				boolean isSpam = EmoteChecker.isSpammingEmotes(message, sender, type, bot);
				if (isSpam) {
					if (Permissions.hasPermission(bot, type, sender, PermissionLevel.Moderator.getName()))
						return;
					else {
						if (RankSystem.getRank(sender, type, bot) == "sliver")
							return;
						ModerationActionsLog.addEvent(type, bot, sender, " flagged by Emote Spam Checker", message);
						Utilities.sendMessage(type, bot, "@" + sender + " " + Config.getSetting("EmoteWarning", type, bot));
						if (type == BotType.Twitch) {
							Utilities.sendMessage(type, bot, "/timeout " + sender);
							Utilities.sendMessage(type, bot, "/unban " + sender);
						} else if (type == BotType.Mixer) {
							((MixerBot) bot).deleteLastMessageForUser(sender);
						}
						return;
					}
				}
			}
			if (Config.getSetting("Symbol", type, bot).equalsIgnoreCase("true")) {
				boolean isSpam = SymbolChecker.isSpammingSymbol(message, sender, type, bot);
				if (isSpam) {
					if (Permissions.hasPermission(bot, type, sender, PermissionLevel.Moderator.getName()))
						return;
					if (type == BotType.Twitch && ((TwitchBot) bot).linkPermitedUsers.contains(sender))
						return;
					if (type == BotType.Mixer && ((MixerBot) bot).linkPermitedUsers.contains(sender))
						return;
					else {
						if (RankSystem.getRank(sender, type, bot) == "sliver")
							return;
						ModerationActionsLog.addEvent(type, bot, sender, " flagged by Symbol Spam Checker", message);
						Utilities.sendMessage(type, bot, "@" + sender + " " + Config.getSetting("SymbolWarning", type, bot));
						if (type == BotType.Twitch) {
							Utilities.sendMessage(type, bot, "/timeout " + sender);
							Utilities.sendMessage(type, bot, "/unban " + sender);
						} else if (type == BotType.Mixer) {
							((MixerBot) bot).deleteLastMessageForUser(sender);
						}
						return;
					}
				}
			}
		}
	}
}
