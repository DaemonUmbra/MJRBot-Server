package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.chatModeration.LinkChecker;
import com.mjr.commands.Command;

public class PermitCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (args.length == 2) {
	    String User = args[1];
	    Utilities.sendMessage(type, channel, User + " is now permited to post a link");
	    LinkChecker.PermitedUsers = LinkChecker.PermitedUsers + User.toLowerCase() + ", ";
	} else {
	    Utilities.sendMessage(type, channel, "Invalid arguments! You need to enter !permit USER");
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
