package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
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
				((TwitchBot) bot).MessageToChat("Added " + Rank + " to " + sender);
			    } else {
				String endMessage = sender
					+ " you dont have the right amount of points! Do !points to check how many you got";
				if (MJRBot.getTwitchBot() != null)
				    ((TwitchBot) bot).MessageToChat(endMessage);
				else
				    ((MixerBot) bot).sendMessage(endMessage);
			    }
			} else {
			    String endMessage = "Rank doesnt exist!";
			    if (MJRBot.getTwitchBot() != null)
				((TwitchBot) bot).MessageToChat(endMessage);
			    else
				((MixerBot) bot).sendMessage(endMessage);
			}
		    } else {
			String endMessage = sender + " is has already got that rank!";
			if (MJRBot.getTwitchBot() != null)
			    ((TwitchBot) bot).MessageToChat(endMessage);
			else
			    ((MixerBot) bot).sendMessage(endMessage);
		    }
		} else {
		    String endMessage = "Cant add " + Rank + " to " + sender;
		    if (MJRBot.getTwitchBot() != null)
			((TwitchBot) bot).MessageToChat(endMessage);
		    else
			((MixerBot) bot).sendMessage(endMessage);
		}
	    } else {
		String endMessage = "Invalid arguments! You need to enter !buyrank RANK";
		if (MJRBot.getTwitchBot() != null)
		    ((TwitchBot) bot).MessageToChat(endMessage);
		else
		    ((MixerBot) bot).sendMessage(endMessage);
	    }
	}
    }

    @Override
    public String getPermissionLevel() {
	return "User";
    }
}
