package com.mjr.mjrbot.chatModeration;

import com.mjr.mjrbot.AnalyticsData;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.storage.ModerationActionsLogManager;
import com.mjr.mjrbot.storage.RankSystemManager;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.PermissionsManager;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class ChatModeration {

	public static void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message) {
		// ChatModeration
		if (ChannelConfigManager.getSetting("Ranks", type, bot).equalsIgnoreCase("true") && RankSystemManager.getRank(sender, type, bot) == "gold")
			return;
		else if (!PermissionsManager.hasPermission(bot, type, sender, PermissionLevel.User.getName()))
			return;
		else {
			AnalyticsData.addNumOfMessagedModerated(1);
			if (ChannelConfigManager.getSetting("LinkChecker", type, bot).equalsIgnoreCase("true")) {
				boolean allowed = LinkChecker.CheckLink(type, bot, message, sender);
				if (!allowed) {
					if (RankSystemManager.getRank(sender, type, bot) == "sliver")
						return;
					else if (RankSystemManager.getRank(sender, type, bot) == "bronze")
						return;
					ModerationActionsLogManager.addEvent(type, bot, sender, " flagged by Link Checker", message);
					MJRBotUtilities.sendMessage(type, bot, "@" + sender + " " + ChannelConfigManager.getSetting("LinkWarning", type, bot));
					if (type == BotType.Twitch) {
						MJRBotUtilities.sendMessage(type, bot, "/timeout " + sender);
						MJRBotUtilities.sendMessage(type, bot, "/unban " + sender);
					} else if (type == BotType.Mixer) {
						((MixerBot) bot).deleteLastMessageForUser(sender);
					}
					return;
				}
				if (type == BotType.Twitch)
					((TwitchBot) bot).getTwitchData().linkPermitedUsers.remove(sender);
				else if (type == BotType.Mixer)
					((MixerBot) bot).getMixerData().linkPermitedUsers.remove(sender);
			}
			if (ChannelConfigManager.getSetting("Badwords", type, bot).equalsIgnoreCase("true")) {
				boolean isBadWord = BadWordChecker.isBadWord(bot, type, message, sender);
				if (isBadWord) {
					if (PermissionsManager.hasPermission(bot, type, sender, PermissionLevel.Moderator.getName()))
						return;
					else {
						ModerationActionsLogManager.addEvent(type, bot, sender, " flagged by Badwords Checker", message);
						MJRBotUtilities.sendMessage(type, bot, "@" + sender + " " + ChannelConfigManager.getSetting("LanguageWarning", type, bot));
						if (type == BotType.Twitch) {
							MJRBotUtilities.sendMessage(type, bot, "/timeout " + sender);
							MJRBotUtilities.sendMessage(type, bot, "/unban " + sender);
						} else if (type == BotType.Mixer) {
							((MixerBot) bot).deleteLastMessageForUser(sender);
						}
						return;
					}
				}
			}
			if (ChannelConfigManager.getSetting("Emote", type, bot).equalsIgnoreCase("true")) {
				EmoteChecker.getEmotes(type, bot);
				boolean isSpam = EmoteChecker.isSpammingEmotes(message, sender, type, bot);
				if (isSpam) {
					if (PermissionsManager.hasPermission(bot, type, sender, PermissionLevel.Moderator.getName()))
						return;
					else {
						if (RankSystemManager.getRank(sender, type, bot) == "sliver")
							return;
						ModerationActionsLogManager.addEvent(type, bot, sender, " flagged by Emote Spam Checker", message);
						MJRBotUtilities.sendMessage(type, bot, "@" + sender + " " + ChannelConfigManager.getSetting("EmoteWarning", type, bot));
						if (type == BotType.Twitch) {
							MJRBotUtilities.sendMessage(type, bot, "/timeout " + sender);
							MJRBotUtilities.sendMessage(type, bot, "/unban " + sender);
						} else if (type == BotType.Mixer) {
							((MixerBot) bot).deleteLastMessageForUser(sender);
						}
						return;
					}
				}
			}
			if (ChannelConfigManager.getSetting("Symbol", type, bot).equalsIgnoreCase("true")) {
				boolean isSpam = SymbolChecker.isSpammingSymbol(message, sender, type, bot);
				if (isSpam) {
					if (PermissionsManager.hasPermission(bot, type, sender, PermissionLevel.Moderator.getName()))
						return;
					if (type == BotType.Twitch && ((TwitchBot) bot).getTwitchData().linkPermitedUsers.contains(sender))
						return;
					if (type == BotType.Mixer && ((MixerBot) bot).getMixerData().linkPermitedUsers.contains(sender))
						return;
					else {
						if (RankSystemManager.getRank(sender, type, bot) == "sliver")
							return;
						ModerationActionsLogManager.addEvent(type, bot, sender, " flagged by Symbol Spam Checker", message);
						MJRBotUtilities.sendMessage(type, bot, "@" + sender + " " + ChannelConfigManager.getSetting("SymbolWarning", type, bot));
						if (type == BotType.Twitch) {
							MJRBotUtilities.sendMessage(type, bot, "/timeout " + sender);
							MJRBotUtilities.sendMessage(type, bot, "/unban " + sender);
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
