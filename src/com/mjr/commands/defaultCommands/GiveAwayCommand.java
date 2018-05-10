package com.mjr.commands.defaultCommands;

import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.threads.GiveAwayThread;

public class GiveAwayCommand extends Command {
    public static boolean Started = false;

    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Started == false) {
	    GiveAwayThread thread = new GiveAwayThread();
	    thread.start();
	    Started = true;
	} else {
	    Utilities.sendMessage("Giveaway has already started!");
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.Moderator.getName();
    }
}
