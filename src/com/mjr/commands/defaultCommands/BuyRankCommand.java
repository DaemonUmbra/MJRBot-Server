package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.PointsSystem;
import com.mjr.files.Ranks;

public class BuyRankCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (Config.getSetting("Ranks", channel).equalsIgnoreCase("true")) {
	    if (args.length == 2) {
		String Rank = args[1];
		if (Ranks.isOnList(sender, channel)) {
		    if (!Ranks.hasRank(sender, Rank, channel)) {
			if (Ranks.isValidRank(Rank)) {
			    if (PointsSystem.hasPoints(sender, Ranks.getRankPrice(Rank), channel)) {
				PointsSystem.RemovePoints(sender, Ranks.getRankPrice(Rank), channel);
				Ranks.setRank(sender, Rank, channel);
				Utilities.sendMessage(type, channel, "Added " + Rank + " to " + sender);
			    } else {
				Utilities.sendMessage(type, channel,
					" you dont have the right amount of points! Do !points to check how many you got");
			    }
			} else {
			    Utilities.sendMessage(type, channel, "Rank doesnt exist!");
			}
		    } else {
			Utilities.sendMessage(type, channel, sender + " is has already got that rank!");
		    }
		} else {
		    Utilities.sendMessage(type, channel, "Cant add " + Rank + " to " + sender);
		}
	    } else {
		Utilities.sendMessage(type, channel, "Invalid arguments! You need to enter !buyrank RANK");
	    }
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.User.getName();
    }

    @Override
    public boolean hasCooldown() {
	return false;
    }
}
