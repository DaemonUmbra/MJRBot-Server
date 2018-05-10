package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
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
		String endMessage = sender + " the Fruit Machine is spinning...";
		if (MJRBot.getTwitchBot() != null)
		    ((TwitchBot) bot).MessageToChat(endMessage);
		else
		    ((MixerBot) bot).sendMessage(endMessage);
		int waittime = 0;
		while (waittime < 250) {
		    waittime++;
		}
		if (FruitMachine.hasWon()) {
		    endMessage = sender + " " + Answer + " you have Won! " + PointsSystem.AddRandomPoints(sender) + " Points";
		    if (MJRBot.getTwitchBot() != null)
			((TwitchBot) bot).MessageToChat(endMessage);
		    else
			((MixerBot) bot).sendMessage(endMessage);
		} else {
		    endMessage = sender + " " + Answer + " you have lost! 1 Point taken! The Fruit Machine hasnt been won in "
			    + FruitMachine.timesLost + " turns";
		    if (MJRBot.getTwitchBot() != null)
			((TwitchBot) bot).MessageToChat(endMessage);
		    else
			((MixerBot) bot).sendMessage(endMessage);
		    PointsSystem.RemovePoints(sender, 1);
		}
	    } else {
		String endMessage = sender + " you currently have insufficient points! You only have " + PointsSystem.getPoints(sender);
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
