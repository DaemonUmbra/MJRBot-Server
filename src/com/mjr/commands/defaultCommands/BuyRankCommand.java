package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.PointsSystem;
import com.mjr.files.Ranks;

public class BuyRankCommand extends Command {
    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Ranks").equalsIgnoreCase("true")) {
	    if (args.length == 2) {
		String Rank = args[1];
		if (Ranks.isOnList(sender)) {
		    if (!Ranks.hasRank(sender, Rank)) {
			if (Ranks.isValidRank(Rank)) {
			    if (PointsSystem.hasPoints(sender, Ranks.getRankPrice(Rank))) {
				PointsSystem.RemovePoints(sender, Ranks.getRankPrice(Rank));
				Ranks.setRank(sender, Rank);
				Utilities.sendMessage("Added " + Rank + " to " + sender);
			    } else {
				Utilities.sendMessage(" you dont have the right amount of points! Do !points to check how many you got");
			    }
			} else {
			    Utilities.sendMessage("Rank doesnt exist!");
			}
		    } else {
			Utilities.sendMessage(sender + " is has already got that rank!");
		    }
		} else {
		    Utilities.sendMessage("Cant add " + Rank + " to " + sender);
		}
	    } else {
		Utilities.sendMessage("Invalid arguments! You need to enter !buyrank RANK");
	    }
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.User.getName();
    }
}
