package com.mjr.commands.defaultCommands;

import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.Ranks;

public class RemoveRankCheckCommand extends Command {
    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Ranks").equalsIgnoreCase("true")) {
	    if (args.length == 2) {
		String User = args[1];
		if (Ranks.isOnList(User)) {
		    Ranks.removeRank(User);
		    Utilities.sendMessage("Removed " + User + " rank");
		} else {
		    Utilities.sendMessage("Cant find " + User);
		}
	    } else {
		Utilities.sendMessage("Invalid arguments! You need to enter !removerank USER");
	    }
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.Moderator.getName();
    }
}
