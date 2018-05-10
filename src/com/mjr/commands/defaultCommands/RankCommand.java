package com.mjr.commands.defaultCommands;

import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.Ranks;

public class RankCommand extends Command {
    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Ranks").equalsIgnoreCase("true")) {
	    Utilities.sendMessage(sender + " you current rank is " + Ranks.getRank(sender));
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.User.getName();
    }
}
