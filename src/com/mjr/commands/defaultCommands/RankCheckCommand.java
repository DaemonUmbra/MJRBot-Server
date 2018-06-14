package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.Ranks;

public class RankCheckCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Ranks").equalsIgnoreCase("true")) {
	    if (args.length == 2) {
		String User = args[1];
		if (Ranks.isOnList(User)) {
		    Utilities.sendMessage(User + " has " + Ranks.getRank(User) + " Rank");
		} else {
		    Utilities.sendMessage("Cant find " + User);
		}
	    } else {
		Utilities.sendMessage("Invalid arguments! You need to enter !getrank USER");
	    }
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.Moderator.getName();
    }
}
