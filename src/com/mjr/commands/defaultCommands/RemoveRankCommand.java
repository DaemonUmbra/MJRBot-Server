package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.Ranks;

public class RemoveRankCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Ranks").equalsIgnoreCase("true")) {
	    if (args.length == 2) {
		String User = args[1];
		if (Ranks.isOnList(User)) {
		    Ranks.removeRank(User);
		    Utilities.sendMessage(type, channel, "Removed " + User + " rank");
		} else {
		    Utilities.sendMessage(type, channel, "Cant find " + User);
		}
	    } else {
		Utilities.sendMessage(type, channel, "Invalid arguments! You need to enter !removerank USER");
	    }
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.Moderator.getName();
    }
}
