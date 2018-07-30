package com.mjr.commands.defaultCommands;

import java.io.IOException;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.commands.CustomCommands;

public class RemoveCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (args.length == 2) {
	    if (!args[1].contains("!")) {
		String command = args[1];
		try {
		    CustomCommands.deleteCommand(type, channel, command);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    } else {
		Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! Please dont include an ! in the Command Name");
	    }
	} else {
	    Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! You need to enter !removecommand COMMANDNAME");
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
