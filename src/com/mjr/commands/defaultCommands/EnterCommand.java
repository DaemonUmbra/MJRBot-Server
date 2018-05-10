package com.mjr.commands.defaultCommands;

import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.threads.GiveAwayThread;

public class EnterCommand extends Command {

    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (GiveAwayCommand.Started) {
	    GiveAwayThread.EnteredUsers.add(sender.toLowerCase());
	    Utilities.sendMessage(sender + " has now been entered in to the giveaway!");
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.User.getName();
    }
}
