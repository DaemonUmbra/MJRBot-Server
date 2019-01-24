package com.mjr.commands.defaultCommands;

import java.io.File;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.Permissions;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.storage.Config;
import com.mjr.storage.EventLog;
import com.mjr.storage.EventLog.EventType;
import com.mjr.storage.QuoteSystem;
import com.mjr.util.ConsoleUtil;
import com.mjr.util.ConsoleUtil.MessageType;
import com.mjr.util.Utilities;

public class QuoteCommand extends Command {
	public static String filename = "Quotes.txt";
	public File file;

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Quotes", type, bot).equalsIgnoreCase("true")) {
			if(type == BotType.Twitch)
				file = new File(MJRBot.filePath + Utilities.getChannelIDFromBotType(type, bot) + File.separator + filename);
			else if(type == BotType.Mixer)
				file = new File(MJRBot.filePath + Utilities.getChannelNameFromBotType(type, bot)+ File.separator + filename);
			if (args.length == 1) {
				Utilities.sendMessage(type, bot, QuoteSystem.getRandomQuote(type, bot, file));
			} else if (args.length == 3 && args[1].equalsIgnoreCase("get") && Permissions.hasPermission(bot, type, sender, PermissionLevel.Moderator.getName())) {
				String quote = QuoteSystem.getQuote(type, bot, file, Integer.parseInt(args[2]));
				if (quote != null)
					Utilities.sendMessage(type, bot, "Quote " + args[2] + " is: " + quote);
				else
					Utilities.sendMessage(type, bot, "@" + sender + " Quote " + args[1] + " doesnt exist!");
			} else if (args.length > 1 && args[1].equalsIgnoreCase("add") && Permissions.hasPermission(bot, type, sender, PermissionLevel.Moderator.getName())) {
				if (!message.contains("@")) {
					Utilities.sendMessage(type, bot, sender + " your quote must be in the format as follows: !quote add <message> @<Name>");
				} else if (message.contains("\"") || message.contains("'")) {
					Utilities.sendMessage(type, bot, "@" + sender + " your quote must not contain \" or '");
				} else {
					QuoteSystem.addQuote(type, bot, file, message);
					Utilities.sendMessage(type, bot, "@" + sender + " your quote has been added!");
					ConsoleUtil.textToConsole(bot, type, "A new Quote has been added by " + sender + ". The quote message is " + message, MessageType.ChatBot, null);
					EventLog.addEvent(type, bot, sender, "Added a new quote", EventType.Quote);
				}
			} else if (args.length == 2 && args[1].equalsIgnoreCase("help")) {
				Utilities.sendMessage(type, bot, "@" + sender + " Quote Help: You can use the following commands: !quote or !quote add <message> @<username> or !quote get <number>");
			}
		}
	}

	@Override
	public String getPermissionLevel() {
		return Permissions.PermissionLevel.User.getName();
	}

	@Override
	public boolean hasCooldown() {
		return false;
	}
}
