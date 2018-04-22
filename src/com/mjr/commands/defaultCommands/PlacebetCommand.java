package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.PointsSystem;
import com.mjr.games.RacingGame;

public class PlacebetCommand extends Command {
    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Games").equalsIgnoreCase("true")) {
	    if (RaceCommand.Started) {
		if (args.length == 4) {
		    if (checkForValue(sender) == false) {
			String bet = args[1];
			String type = args[2];
			String points = args[3];
			if (type.equalsIgnoreCase("1st") || type.equalsIgnoreCase("Top3")) {
			    RacingGame.PlaceBet(sender, bet, type, points);
			    PointsSystem.RemovePoints(sender, Integer.parseInt(points));
			} else {
			    String endMessage = "Invalid arguments! You need to enter !placebet CAR TYPE POINTS(Example !placebet 5 Top3 10) Cars range from 1 to 8, Types = Top3, 1st";
			    if (MJRBot.getTwitchBot() != null)
				((TwitchBot) bot).MessageToChat(endMessage);
			    else
				((MixerBot) bot).sendMessage(endMessage);
			}
		    } else {
			String endMessage = sender + " you have already made a bet!";
			if (MJRBot.getTwitchBot() != null)
			    ((TwitchBot) bot).MessageToChat(endMessage);
			else
			    ((MixerBot) bot).sendMessage(endMessage);
		    }

		} else {
		    String endMessage = "Invalid arguments! You need to enter !placebet CAR TYPE POINTS(Example !placebet 5 Top3 10) Cars range from 1 to 8, Types = Top3, 1st";
		    if (MJRBot.getTwitchBot() != null)
			((TwitchBot) bot).MessageToChat(endMessage);
		    else
			((MixerBot) bot).sendMessage(endMessage);
		}
	    } else {
		String endMessage = "Racing game hasnt been started yet!";
		if (MJRBot.getTwitchBot() != null)
		    ((TwitchBot) bot).MessageToChat(endMessage);
		else
		    ((MixerBot) bot).sendMessage(endMessage);
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
}
