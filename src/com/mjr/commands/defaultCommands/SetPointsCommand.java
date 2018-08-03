package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.storage.Config;
import com.mjr.storage.PointsSystem;

public class SetPointsCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (Config.getSetting("Points", channel).equalsIgnoreCase("true")) {
	    if (args.length == 3) {
		String points = args[1];
		String user = args[2];
		if (PointsSystem.isOnList(user, channel)) {
		    PointsSystem.setPoints(user.toLowerCase(), Integer.parseInt(points), channel);
		    Utilities.sendMessage(type, channel, "@" + sender + " Set " + points + " points" + " to " + user);
		} else {
		    Utilities.sendMessage(type, channel, "@" + sender + " Unable to set " + user + " points" + " to " + points);
		}
	    } else {
		Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! You need to enter !setpoints POINTS USER");
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
