package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.PointsSystem;
import com.mjr.games.ResultPair;
import com.mjr.games.SlotMachine;

public class SpinCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (Config.getSetting("Games", channel).equalsIgnoreCase("true")) {
	    if (PointsSystem.hasPoints(sender, 1, channel)) {
		ResultPair result = SlotMachine.Spin(type);
		Utilities.sendMessage(type, channel, sender + " the Slot Machine is spinning...");
		int waittime = 0;
		while (waittime < 250) { // TODO: Change to a Thread
		    waittime++;
		}
		if (result.hasWon()) {
		    Utilities.sendMessage(type, channel, sender + " " + result.getResult() + " you have Won! You have been given "
			    + PointsSystem.AddRandomPoints(sender, channel) + " Points");
		} else {
		    Utilities.sendMessage(type, channel, sender + " " + result.getResult()
			    + " you have lost! 1 Point taken! The Slot Machine hasnt been won in " + SlotMachine.timesLost + " turns");
		    PointsSystem.RemovePoints(sender, 1, channel);
		}
	    } else {
		Utilities.sendMessage(type, channel,
			sender + " you currently have insufficient points! You only have " + PointsSystem.getPoints(sender, channel));
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
