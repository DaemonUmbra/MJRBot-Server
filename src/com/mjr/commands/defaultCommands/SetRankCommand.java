package com.mjr.commands.defaultCommands;

import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.Ranks;

public class SetRankCommand extends Command {
    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Ranks").equalsIgnoreCase("true")) {
	    if (args.length == 3) {
		String Rank = args[1];
		String User = args[2];
		if (Ranks.isOnList(User)) {
		    if (!Ranks.hasRank(sender, Rank)) {
			if (Ranks.isValidRank(Rank)) {
			    Ranks.setRank(User, Rank);
			    Utilities.sendMessage("Added " + Rank + " to " + User);
			} else {
			    Utilities.sendMessage("Rank doesnt exist!");
			}
		    } else {
			Utilities.sendMessage(User + " is has already got that rank!");
		    }
		} else {
		    Utilities.sendMessage("Cant add " + Rank + " to " + User);
		}
	    } else {
		Utilities.sendMessage("Invalid arguments! You need to enter !setrank RANK USER");
	    }
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.Moderator.getName();
    }
}
