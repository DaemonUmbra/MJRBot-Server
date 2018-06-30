package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.threads.GiveAwayThread;

public class GiveAwayCommand extends Command {
    public static boolean Started = false;

    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (Started == false) {
	    GiveAwayThread thread = new GiveAwayThread(type, channel);
	    thread.start();
	    Started = true;
	} else {
	    Utilities.sendMessage(type, channel, "Giveaway has already started!");
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
