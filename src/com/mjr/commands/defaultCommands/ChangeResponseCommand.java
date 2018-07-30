package com.mjr.commands.defaultCommands;

import java.io.IOException;

import com.mjr.ConsoleUtil;
import com.mjr.ConsoleUtil.MessageType;
import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.commands.CustomCommands;

public class ChangeResponseCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (args.length >= 3) {
	    if (!args[1].contains("!")) {
		String command = args[1];
		String response = message.substring(message.indexOf(command));
		response = response.substring(response.indexOf(' ') + 1);
		try {
		    CustomCommands.changeCommandResponse(type, channel, command, response);
		} catch (IOException e) {
		    ConsoleUtil.TextToConsole(bot, type, channel, e.getMessage(), MessageType.Bot, null);
		    e.printStackTrace();
		}
	    } else {
		Utilities.sendMessage(type, channel, "Invalid arguments! Please dont include an ! in the Command Name");
	    }
	} else {
	    Utilities.sendMessage(type, channel, "Invalid arguments! You need to enter !commandresponse COMMANDNAME RESPONSE");
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.Moderator.getName();
    }

    @Override
    public boolean hasCooldown() {
	return false;
    }
}
