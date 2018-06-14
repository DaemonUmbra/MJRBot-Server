package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.PointsSystem;

public class AddPointsCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Points").equalsIgnoreCase("true")) {
	    if (args.length == 3) {
		String Points = args[1];
		String User = args[2];

		if (PointsSystem.isOnList(User)) {
		    PointsSystem.AddPoints(User.toLowerCase(), Integer.parseInt(Points));
		    Utilities.sendMessage("Added " + Points + " points" + " to " + User);
		} else {
		    Utilities.sendMessage("Cant add " + Points + " points" + " to " + User);
		}
	    } else {
		Utilities.sendMessage("Invalid arguments! You need to enter !addpoints POINTS USER");
	    }
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.Moderator.getName();
    }
}
