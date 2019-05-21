package com.mjr.mjrbot.commands.defaultCommands;

import java.io.File;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.storage.EventLogManager;
import com.mjr.mjrbot.storage.EventLogManager.EventType;
import com.mjr.mjrbot.storage.QuoteSystemManager;
import com.mjr.mjrbot.util.ConsoleUtil;
import com.mjr.mjrbot.util.ConsoleUtil.MessageType;
import com.mjr.mjrbot.util.PermissionsManager;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class QuoteCommand implements ICommand {
	public static String filename = "Quotes.txt";
	public File file;

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (ChannelConfigManager.getSetting("Quotes", type, bot).equalsIgnoreCase("true")) {
			if (type == BotType.Twitch)
				file = new File(MJRBot.filePath + ChatBotManager.getChannelIDFromBotType(type, bot) + File.separator + filename);
			else if (type == BotType.Mixer)
				file = new File(MJRBot.filePath + ChatBotManager.getChannelNameFromBotType(type, bot) + File.separator + filename);
			if (args.length == 1) {
				ChatBotManager.sendMessage(type, bot, QuoteSystemManager.getRandomQuote(type, bot, file));
			} else if (args.length == 3 && args[1].equalsIgnoreCase("get") && PermissionsManager.hasPermission(bot, type, sender, PermissionLevel.Moderator.getName())) {
				String quote = QuoteSystemManager.getQuote(type, bot, file, Integer.parseInt(args[2]));
				if (quote != null)
					ChatBotManager.sendMessage(type, bot, "Quote " + args[2] + " is: " + quote);
				else
					ChatBotManager.sendMessage(type, bot, "@" + sender + " Quote " + args[1] + " doesnt exist!");
			} else if (args.length > 1 && args[1].equalsIgnoreCase("add") && PermissionsManager.hasPermission(bot, type, sender, PermissionLevel.Moderator.getName())) {
				if (!message.contains("@")) {
					ChatBotManager.sendMessage(type, bot, sender + " your quote must be in the format as follows: !quote add <message> @<Name>");
				} else if (message.contains("\"") || message.contains("'")) {
					ChatBotManager.sendMessage(type, bot, "@" + sender + " your quote must not contain \" or '");
				} else {
					QuoteSystemManager.addQuote(type, bot, file, message);
					ChatBotManager.sendMessage(type, bot, "@" + sender + " your quote has been added!");
					ConsoleUtil.textToConsole(bot, type, "A new Quote has been added by " + sender + ". The quote message is " + message, MessageType.ChatBot, null);
					EventLogManager.addEvent(type, bot, sender, "Added a new quote", EventType.Quote);
				}
			} else if (args.length == 2 && args[1].equalsIgnoreCase("help")) {
				ChatBotManager.sendMessage(type, bot, "@" + sender + " Quote Help: You can use the following commands: !quote or !quote add <message> @<username> or !quote get <number>");
			}
		}
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionsManager.PermissionLevel.User;
	}

	@Override
	public boolean hasCooldown() {
		return false;
	}
}
