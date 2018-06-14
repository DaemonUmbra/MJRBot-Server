package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.PointsSystem;

public class RemovePointsCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Points").equalsIgnoreCase("true")) {
	    if (args.length == 3) {
		String Points = args[1];
		String User = args[2];
		if (PointsSystem.isOnList(User)) {
		    PointsSystem.RemovePoints(User.toLowerCase(), Integer.parseInt(Points));
		    Utilities.sendMessage("Removed " + Points + " points" + " to " + User);
		} else {
		    Utilities.sendMessage("Cant remove " + Points + " points" + " to " + User);
		}
	    } else {
		Utilities.sendMessage("Invalid arguments! You need to enter !removepoints POINTS USER");
	    }
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.Moderator.getName();
    }
}
