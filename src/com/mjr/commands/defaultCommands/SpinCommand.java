package com.mjr.commands.defaultCommands;

import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.PointsSystem;
import com.mjr.games.FruitMachine;

public class SpinCommand extends Command {
    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Games").equalsIgnoreCase("true")) {
	    if (PointsSystem.hasPoints(sender, 1)) {
		String Answer = FruitMachine.Spin();
		Utilities.sendMessage(sender + " the Fruit Machine is spinning...");
		int waittime = 0;
		while (waittime < 250) {
		    waittime++;
		}
		if (FruitMachine.hasWon()) {
		    Utilities.sendMessage(sender + " " + Answer + " you have Won! " + PointsSystem.AddRandomPoints(sender) + " Points");
		} else {
		    Utilities.sendMessage(sender + " " + Answer + " you have lost! 1 Point taken! The Fruit Machine hasnt been won in "
			    + FruitMachine.timesLost + " turns");
		    PointsSystem.RemovePoints(sender, 1);
		}
	    } else {
		Utilities.sendMessage(sender + " you currently have insufficient points! You only have " + PointsSystem.getPoints(sender));
	    }
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.User.getName();
    }
}
