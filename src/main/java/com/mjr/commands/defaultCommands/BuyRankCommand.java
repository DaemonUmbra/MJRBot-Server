package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.storage.Config;
import com.mjr.storage.PointsSystem;
import com.mjr.storage.RankSystem;

public class BuyRankCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (Config.getSetting("Ranks", channel).equalsIgnoreCase("true")) {
	    if (args.length == 2) {
		String Rank = args[1];
		if (RankSystem.isOnList(sender, channel)) {
		    if (!RankSystem.hasRank(sender, Rank, channel)) {
			if (RankSystem.isValidRank(Rank)) {
			    if (PointsSystem.hasPoints(sender, RankSystem.getRankPrice(Rank), channel)) {
				PointsSystem.RemovePoints(sender, RankSystem.getRankPrice(Rank), channel);
				RankSystem.setRank(sender, Rank, channel);
				Utilities.sendMessage(type, channel, "Added " + Rank + " to " + sender);
			    } else {
				Utilities.sendMessage(type, channel,
					" you dont have the right amount of points! Do !points to check how many you got");
			    }
			} else {
			    Utilities.sendMessage(type, channel, "Rank doesnt exist!");
			}
		    } else {
			Utilities.sendMessage(type, channel, "@" + sender + " you already have this rank!");
		    }
		} else {
		    Utilities.sendMessage(type, channel, "Cant add " + Rank + " to " + sender);
		}
	    } else {
		Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! You need to enter !buyrank RANK");
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
