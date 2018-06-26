package com.mjr.commands.defaultCommands;

import java.io.IOException;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.commands.CustomCommands;

public class AddCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (args.length >= 4) {
	    if (!args[1].contains("!")) {
		if (args[2].equalsIgnoreCase("User") || args[2].equalsIgnoreCase("Moderator")) {
		    String command = args[1];
		    String permissionlevel = args[2];
		    String response = message.substring(message.indexOf(permissionlevel));
		    response = response.substring(response.indexOf(' ') + 1);
		    try {
			CustomCommands.AddCommand(type, channel, command, response, permissionlevel);
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		} else {
		    Utilities.sendMessage(type, channel, "Invalid arguments! The following Permission values are: User or Moderator");
		}
	    } else {
		Utilities.sendMessage(type, channel, "Invalid arguments! Please dont include an ! in the Command Name");
	    }
	} else {
	    Utilities.sendMessage(type, channel, "Invalid arguments! You need to enter !addcommand COMMANDNAME PERMISSION RESPONSE");
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
