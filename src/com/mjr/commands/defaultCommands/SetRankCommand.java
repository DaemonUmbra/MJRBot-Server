package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.Ranks;

public class SetRankCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Ranks").equalsIgnoreCase("true")) {
	    if (args.length == 3) {
		String Rank = args[1];
		String User = args[2];
		if (Ranks.isOnList(User)) {
		    if (!Ranks.hasRank(sender, Rank)) {
			if (Ranks.isValidRank(Rank)) {
			    Ranks.setRank(User, Rank);
			    Utilities.sendMessage(type, channel, "Added " + Rank + " to " + User);
			} else {
			    Utilities.sendMessage(type, channel, "Rank doesnt exist!");
			}
		    } else {
			Utilities.sendMessage(type, channel, User + " is has already got that rank!");
		    }
		} else {
		    Utilities.sendMessage(type, channel, "Cant add " + Rank + " to " + User);
		}
	    } else {
		Utilities.sendMessage(type, channel, "Invalid arguments! You need to enter !setrank RANK USER");
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
