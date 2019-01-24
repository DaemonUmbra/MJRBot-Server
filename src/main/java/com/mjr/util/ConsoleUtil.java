package com.mjr.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.Permissions;
import com.mjr.Permissions.PermissionLevel;

public class ConsoleUtil {

	private static boolean showChatMessages = true;
	private static boolean showChatBotMessages = true;
	private static boolean showBotMessages = true;

	public enum MessageType {
		Chat("Chat"), ChatBot("ChatBot"), Bot("Bot");

		private final String name;

		MessageType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public static void textToConsole(String message) {
		textToConsole(null, null, message, MessageType.Bot, null);
	}

	public static void textToConsole(Object bot, BotType type, String message, MessageType messageType, String sender) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		if (messageType == MessageType.Chat && showChatMessages) {
			if (sender != null) {
				String channel = Utilities.getChannelNameFromBotType(type, bot);
				if (Permissions.hasPermission(bot, type, sender, PermissionLevel.BotOwner.getName())) {
					MJRBot.getLogger().info(dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Bot Owner]" + sender + ": " + message);
				} else if (Permissions.hasPermission(bot, type, sender, PermissionLevel.Bot.getName())) {
					MJRBot.getLogger().info(dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Bot]" + sender + ": " + message);
				} else if (Permissions.hasPermission(bot, type, sender, PermissionLevel.KnownBot.getName())) {
					MJRBot.getLogger().info(dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Known Bot]" + sender + ": " + message);
				} else if (type == BotType.Twitch && sender.endsWith(channel)) {
					MJRBot.getLogger().info(dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Streamer]" + sender + ": " + message);
				} else if (type == BotType.Mixer && sender.equalsIgnoreCase(channel)) {
					MJRBot.getLogger().info(dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Streamer]" + sender + ": " + message);
				} else if (Permissions.hasPermission(bot, type, sender, PermissionLevel.Moderator.getName())) {
					MJRBot.getLogger().info(dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel + " - [Moderator]" + sender + ": " + message);
				} else if (Permissions.hasPermission(bot, type, sender, PermissionLevel.User.getName())) {
					MJRBot.getLogger().info(dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel + " - [User]" + sender + ": " + message);
				}
			}
		} else if (messageType == MessageType.ChatBot && showChatBotMessages) {
			String channel = Utilities.getChannelNameFromBotType(type, bot);
			MJRBot.getLogger().info(dateFormat.format(date) + " [MJRBot Info] " + "[Bot Type] " + (type == null ? "Unknown" : type.getTypeName()) + " [Channel] " + (channel == "" ? "Unknown" : channel) + " - " + message);
		} else if (messageType == MessageType.Bot && showBotMessages) {
			MJRBot.getLogger().info(dateFormat.format(date) + " [MJRBot Info]" + " - " + message);
		}
	}

	public static boolean isShowChatMessages() {
		return showChatMessages;
	}

	public static void setShowChatMessages(boolean showChatMessages) {
		ConsoleUtil.showChatMessages = showChatMessages;
	}

	public static boolean isShowChatBotMessages() {
		return showChatBotMessages;
	}

	public static void setShowChatBotMessages(boolean showChatBotMessages) {
		ConsoleUtil.showChatBotMessages = showChatBotMessages;
	}

	public static boolean isShowBotMessages() {
		return showBotMessages;
	}

	public static void setShowBotMessages(boolean showBotMessages) {
		ConsoleUtil.showBotMessages = showBotMessages;
	}
}
