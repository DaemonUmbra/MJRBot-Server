package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.PointsSystem;

public class PointsCheckCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Points").equalsIgnoreCase("true")) {
	    if (args.length == 2) {
		String User = args[1];
		if (PointsSystem.isOnList(User)) {
		    Utilities.sendMessage(type, channel, User + " has " + PointsSystem.getPoints(User) + " points");
		} else {
		    Utilities.sendMessage(type, channel, "Cant find " + User);
		}
	    } else {
		Utilities.sendMessage(type, channel, "Invalid arguments! You need to enter !pointscheck USER");
	    }
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
