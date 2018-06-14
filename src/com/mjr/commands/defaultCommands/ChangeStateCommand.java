package com.mjr.commands.defaultCommands;

import java.io.IOException;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.commands.CustomCommands;

public class ChangeStateCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (args.length == 3) {
	    if (!args[1].contains("!")) {
		String command = args[1];
		String state = args[2];
		try {
		    CustomCommands.ChangeStateCommand(type, channel, command, state);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    } else {
		Utilities.sendMessage("Invalid arguments! Please dont include an ! in the Command Name");
	    }
	} else {
	    Utilities.sendMessage("Invalid arguments! You need to enter !commandstate COMMANDNAME TRUE/FALSE");
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.Moderator.getName();
    }
}
