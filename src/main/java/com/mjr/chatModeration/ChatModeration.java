package com.mjr.chatModeration;

import com.mjr.AnalyticsData;
import com.mjr.ChatBotManager.BotType;
import com.mjr.MixerBot;
import com.mjr.Permissions;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.storage.Config;
import com.mjr.storage.ModerationActionsLog;
import com.mjr.storage.RankSystem;
import com.mjr.util.Utilities;

public class ChatModeration {

	public static void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message) {
		// ChatModeration
		if (Config.getSetting("Ranks", channel).equalsIgnoreCase("true") && RankSystem.getRank(sender, channel) == "gold")
			return;
		else if (!Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.User.getName()))
			return;
		else {
			AnalyticsData.addNumOfMessagedModerated(1);
			if (Config.getSetting("LinkChecker", channel).equalsIgnoreCase("true")) {
				boolean allowed = LinkChecker.CheckLink(bot, type, channel, message, sender);
				if (!allowed) {
					if (RankSystem.getRank(sender, channel) == "sliver")
						return;
					else if (RankSystem.getRank(sender, channel) == "bronze")
						return;
					ModerationActionsLog.addEvent(channel, sender, " flagged by Link Checker", message);
					Utilities.sendMessage(type, channel, "@" + sender + " " + Config.getSetting("LinkWarning", channel));
					if (type == BotType.Twitch) {
						Utilities.sendMessage(type, channel, "/timeout " + sender);
						Utilities.sendMessage(type, channel, "/unban " + sender);
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
			if (Config.getSetting("Badwords", channel).equalsIgnoreCase("true")) {
				boolean isBadWord = BadWordChecker.isBadWord(bot, type, channel, message, sender);
				if (isBadWord) {
					if (Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.Moderator.getName()))
						return;
					else {
						ModerationActionsLog.addEvent(channel, sender, " flagged by Badwords Checker", message);
						Utilities.sendMessage(type, channel, "@" + sender + " " + Config.getSetting("LanguageWarning", channel));
						if (type == BotType.Twitch) {
							Utilities.sendMessage(type, channel, "/timeout " + sender);
							Utilities.sendMessage(type, channel, "/unban " + sender);
						} else if (type == BotType.Mixer) {
							((MixerBot) bot).deleteLastMessageForUser(sender);
						}
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
						if (RankSystem.getRank(sender, channel) == "sliver")
							return;
						ModerationActionsLog.addEvent(channel, sender, " flagged by Emote Spam Checker", message);
						Utilities.sendMessage(type, channel, "@" + sender + " " + Config.getSetting("EmoteWarning", channel));
						if (type == BotType.Twitch) {
							Utilities.sendMessage(type, channel, "/timeout " + sender);
							Utilities.sendMessage(type, channel, "/unban " + sender);
						} else if (type == BotType.Mixer) {
							((MixerBot) bot).deleteLastMessageForUser(sender);
						}
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
						if (RankSystem.getRank(sender, channel) == "sliver")
							return;
						ModerationActionsLog.addEvent(channel, sender, " flagged by Symbol Spam Checker", message);
						Utilities.sendMessage(type, channel, "@" + sender + " " + Config.getSetting("SymbolWarning", channel));
						if (type == BotType.Twitch) {
							Utilities.sendMessage(type, channel, "/timeout " + sender);
							Utilities.sendMessage(type, channel, "/unban " + sender);
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
