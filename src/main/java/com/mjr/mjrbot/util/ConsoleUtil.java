package com.mjr.mjrbot.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.Permissions;
import com.mjr.mjrbot.Permissions.PermissionLevel;

public class ConsoleUtil {
	private static TreeMap<Date, String> lastChatMessages = new TreeMap<Date, String>();
	private static TreeMap<Date, String> lastChatBotMessages = new TreeMap<Date, String>();
	private static TreeMap<Date, String> lastBotMessages = new TreeMap<Date, String>();
	private static TreeMap<Date, String> lastErrorMessages = new TreeMap<Date, String>();

	private static boolean showChatMessages = true;
	private static boolean showChatBotMessages = true;
	private static boolean showBotMessages = true;
	private static boolean showErrorMessages = true;

	public enum MessageType {
		Chat("Chat"), ChatBot("ChatBot"), Bot("Bot"), Error("Error");

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

	public static void outputMessage(MessageType type, String message) {
		if (type == MessageType.Chat) {
			if (lastChatMessages.size() != 0)
				lastChatMessages.remove(lastChatMessages.firstKey());
			lastChatMessages.put(Date.from(Instant.now()), message);
			if (showChatMessages)
				System.out.println(message);
		} else if (type == MessageType.ChatBot) {
			if (lastChatBotMessages.size() != 0)
				lastChatBotMessages.remove(lastChatBotMessages.firstKey());
			lastChatBotMessages.put(Date.from(Instant.now()), message);
			if (showChatBotMessages)
				System.out.println(message);
		} else if (type == MessageType.Bot) {
			if (lastBotMessages.size() != 0)
				lastBotMessages.remove(lastBotMessages.firstKey());
			lastBotMessages.put(Date.from(Instant.now()), message);
			if (showBotMessages)
				System.out.println(message);
		} else if (type == MessageType.Error) {
			if (lastErrorMessages.size() != 0)
				lastErrorMessages.remove(lastErrorMessages.firstKey());
			lastErrorMessages.put(Date.from(Instant.now()), message);
			if (showErrorMessages)
				System.out.println(message);
		}
		MJRBot.getLogger().info(message);
	}

	public static TreeSet<Entry<Date, String>> getSortedMap(TreeMap<Date, String> originalMap) {
		TreeSet<Map.Entry<Date, String>> sortedset = new TreeSet<Map.Entry<Date, String>>(new Comparator<Map.Entry<Date, String>>() {
			@Override
			public int compare(Map.Entry<Date, String> e1, Map.Entry<Date, String> e2) {
				return e1.getValue().compareTo(e2.getValue());
			}
		});

		sortedset.addAll(originalMap.entrySet());
		return sortedset;
	}

	public static void refreshConsoleMessages() {
		clearConsole();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			MJRBot.logErrorMessage(e);
		}
		TreeMap<Date, String> temp = new TreeMap<Date, String>();
		if (showChatMessages) {
			temp.putAll(lastChatMessages);
		}
		if (showChatBotMessages) {
			temp.putAll(lastChatBotMessages);
		}
		if (showBotMessages) {
			temp.putAll(lastBotMessages);
		}
		SortedSet<Entry<Date, String>> map = getSortedMap(temp);
		for (int i = 0; i < map.size(); i++)
			System.out.println(map.toArray()[i].toString().substring(map.toArray()[i].toString().indexOf("=") + 1));
	}

	public static void clearConsole() {
		try {
			if (OSUtilities.isWindows()) {
				Runtime.getRuntime().exec("cls");
			} else {
				Runtime.getRuntime().exec("clear");
			}
		} catch (IOException e) {
			MJRBot.logErrorMessage(e);
		}
	}

	public static void textToConsole(Object bot, BotType type, String message, MessageType messageType, String sender) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		if (messageType == MessageType.Chat) {
			if (sender != null) {
				String channel = Utilities.getChannelNameFromBotType(type, bot);
				if (Permissions.hasPermission(bot, type, sender, PermissionLevel.BotOwner.getName())) {
					outputMessage(MessageType.Chat, dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Bot Owner]" + sender + ": " + message);
				} else if (Permissions.hasPermission(bot, type, sender, PermissionLevel.Bot.getName())) {
					outputMessage(MessageType.Chat, dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Bot]" + sender + ": " + message);
				} else if (Permissions.hasPermission(bot, type, sender, PermissionLevel.KnownBot.getName())) {
					outputMessage(MessageType.Chat, dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Known Bot]" + sender + ": " + message);
				} else if (type == BotType.Twitch && sender.endsWith(channel)) {
					outputMessage(MessageType.Chat, dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Streamer]" + sender + ": " + message);
				} else if (type == BotType.Mixer && sender.equalsIgnoreCase(channel)) {
					outputMessage(MessageType.Chat, dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel + " [Streamer]" + sender + ": " + message);
				} else if (Permissions.hasPermission(bot, type, sender, PermissionLevel.Moderator.getName())) {
					outputMessage(MessageType.Chat, dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel + " - [Moderator]" + sender + ": " + message);
				} else if (Permissions.hasPermission(bot, type, sender, PermissionLevel.User.getName())) {
					outputMessage(MessageType.Chat, dateFormat.format(date) + " [Bot Type]" + type.getTypeName() + " [Channel]" + channel + " - [User]" + sender + ": " + message);
				}
			}
		} else if (messageType == MessageType.ChatBot) {
			String channel = Utilities.getChannelNameFromBotType(type, bot);
			outputMessage(MessageType.ChatBot, dateFormat.format(date) + " [MJRBot Info] " + "[Bot Type] " + (type == null ? "Unknown" : type.getTypeName()) + " [Channel] " + (channel == "" ? "Unknown" : channel) + " - " + message);
		} else if (messageType == MessageType.Bot) {
			outputMessage(MessageType.Bot, dateFormat.format(date) + " [MJRBot Info]" + " - " + message);
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

	public static boolean isShowErrorMessages() {
		return showErrorMessages;
	}

	public static void setShowErrorMessages(boolean showErrorMessages) {
		ConsoleUtil.showErrorMessages = showErrorMessages;
	}

	public static TreeMap<Date, String> getLastChatMessages() {
		return lastChatMessages;
	}

	public static void setLastChatMessages(TreeMap<Date, String> lastChatMessages) {
		ConsoleUtil.lastChatMessages = lastChatMessages;
	}

	public static void addLastChatMessages(Date date, String message) {
		lastChatMessages.put(date, message);
	}

	public static TreeMap<Date, String> getLastChatBotMessages() {
		return lastChatBotMessages;
	}

	public static void setLastChatBotMessages(TreeMap<Date, String> lastChatBotMessages) {
		ConsoleUtil.lastChatBotMessages = lastChatBotMessages;
	}

	public static void addLastChatBotMessages(Date date, String message) {
		lastChatBotMessages.put(date, message);
	}

	public static TreeMap<Date, String> getLastBotMessages() {
		return lastBotMessages;
	}

	public static void setLastBotMessages(TreeMap<Date, String> lastBotMessages) {
		ConsoleUtil.lastBotMessages = lastBotMessages;
	}

	public static void addLastBotMessages(Date date, String message) {
		lastBotMessages.put(date, message);
	}

	public static TreeMap<Date, String> getLastErrorMessages() {
		return lastErrorMessages;
	}

	public static void setLastErrorMessages(TreeMap<Date, String> lastErrorMessages) {
		ConsoleUtil.lastErrorMessages = lastErrorMessages;
	}

	public static void addLastErrorMessages(Date date, String message) {
		lastErrorMessages.put(date, message);
	}
}
