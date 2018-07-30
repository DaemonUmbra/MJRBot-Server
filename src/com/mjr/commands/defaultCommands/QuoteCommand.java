package com.mjr.commands.defaultCommands;

import java.io.File;

import com.mjr.ConsoleUtil;
import com.mjr.ConsoleUtil.MessageType;
import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.Permissions;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.QuoteFile;

public class QuoteCommand extends Command {
    public static String filename = "Quotes.txt";
    public File file;

    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (Config.getSetting("Quotes", channel).equalsIgnoreCase("true")) {
	    file = new File(MJRBot.filePath + channel + File.separator + filename);
	    if (args.length == 1) {
		Utilities.sendMessage(type, channel, QuoteFile.getRandomQuote(channel, file));
	    } else if (args.length == 3 && args[1].equalsIgnoreCase("get")
		    && Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.Moderator.getName())) {
		String quote = QuoteFile.getQuote(channel, file, Integer.parseInt(args[2]));
		if (quote != null)
		    Utilities.sendMessage(type, channel, "Quote " + args[2] + " is: " + quote);
		else
		    Utilities.sendMessage(type, channel, "@" + sender + " Quote " + args[1] + " doesnt exist!");
	    } else if (args.length > 1 && args[1].equalsIgnoreCase("add")
		    && Permissions.hasPermission(bot, type, channel, sender, PermissionLevel.Moderator.getName())) {
		if (!message.contains("@")) {
		    Utilities.sendMessage(type, channel,
			    sender + " your quote must be in the format as follows: !quote add <message> @<Name>");
		} else if (message.contains("\"") || message.contains("'")) {
		    Utilities.sendMessage(type, channel, "@" + sender + " your quote must not contain \" or '");
		} else {
		    QuoteFile.addQuote(channel, file, message);
		    Utilities.sendMessage(type, channel, "@" + sender + " your quote has been added!");
		    ConsoleUtil.TextToConsole(bot, type, channel,
			    "A new Quote has been added by " + sender + ". The quote message is " + message, MessageType.Bot, null);
		}
	    } else if (args.length == 2 && args[1].equalsIgnoreCase("help")) {
		Utilities.sendMessage(type, channel,
			"@" + sender + " Quote Help: You can use the following commands: !quote or !quote add <message> @<username> or !quote get <number>");
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
