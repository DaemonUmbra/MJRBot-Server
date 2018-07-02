package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.threads.RaceStartThread;

public class RaceCommand extends Command {
    public static boolean Started = false;

    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (Config.getSetting("Games", channel).equalsIgnoreCase("true")) {
	    if (Started == false) {
		Utilities.sendMessage(type, channel,
			"The race will start in 1 minute! Use !placebet CAR TYPE POINTS(Cars 1-8)(Types Top3, 1st) E.g !placebet 5 Top3 10");
		RaceStartThread userThread = new RaceStartThread(type, channel);
		userThread.start();
		Started = true;
	    }
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.User.getName();
    }

    @Override
    public boolean hasCooldown() {
	return true;
    }
}
