package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.storage.Config;
import com.mjr.storage.PointsSystem;
import com.mjr.util.Utilities;

public class PointsCheckCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (Config.getSetting("Points", channel).equalsIgnoreCase("true")) {
	    if (args.length == 2) {
		String user = args[1];
		if (PointsSystem.isOnList(user, channel)) {
		    Utilities.sendMessage(type, channel, user + " currently has " + PointsSystem.getPoints(user, channel) + " points");
		} else {
		    Utilities.sendMessage(type, channel, "@" + sender + "Unable to find " + user + " current points value!");
		}
	    } else {
		Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! You need to enter !pointscheck USER");
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
