package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.PointsSystem;
import com.mjr.games.RacingGame;

public class PlacebetCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Games").equalsIgnoreCase("true")) {
	    if (RaceCommand.Started) {
		if (args.length == 4) {
		    if (checkForValue(sender) == false) {
			String bet = args[1];
			String betType = args[2];
			String points = args[3];
			if (betType.equalsIgnoreCase("1st") || betType.equalsIgnoreCase("Top3")) {
			    RacingGame.PlaceBet(sender, bet, betType, points);
			    PointsSystem.RemovePoints(sender, Integer.parseInt(points), channel);
			} else {
			    Utilities.sendMessage(type, channel, "Invalid arguments! You need to enter !placebet CAR TYPE POINTS(Example !placebet 5 Top3 10) Cars range from 1 to 8, Types = Top3, 1st");
			}
		    } else {
			Utilities.sendMessage(type, channel, sender + " you have already made a bet!");
		    }

		} else {
		    Utilities.sendMessage(type, channel, "Invalid arguments! You need to enter !placebet CAR TYPE POINTS(Example !placebet 5 Top3 10) Cars range from 1 to 8, Types = Top3, 1st");
		}
	    } else {
		Utilities.sendMessage(type, channel, "Racing game hasnt been started yet!");
	    }
	}
    }

    private static boolean checkForValue(String val) {
	for (int i = 0; i < RacingGame.BetNumber; i++) {
	    if (RacingGame.bets[0][i].contains(val))
		return true;
	}

	return false;
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
