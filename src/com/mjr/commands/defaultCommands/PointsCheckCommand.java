package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.PointsSystem;

public class PointsCheckCommand extends Command {
    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Points").equalsIgnoreCase("true")) {
	    if (args.length == 2) {
		String User = args[1];
		if (PointsSystem.isOnList(User)) {
		    String endMessage = User + " has " + PointsSystem.getPoints(User) + " points";
		    if (MJRBot.getTwitchBot() != null)
			((TwitchBot) bot).MessageToChat(endMessage);
		    else
			((MixerBot) bot).sendMessage(endMessage);
		} else {
		    String endMessage = "Cant find " + User;
		    if (MJRBot.getTwitchBot() != null)
			((TwitchBot) bot).MessageToChat(endMessage);
		    else
			((MixerBot) bot).sendMessage(endMessage);
		}
	    } else {
		String endMessage = "Invalid arguments! You need to enter !pointscheck USER";
		if (MJRBot.getTwitchBot() != null)
		    ((TwitchBot) bot).MessageToChat(endMessage);
		else
		    ((MixerBot) bot).sendMessage(endMessage);
	    }
	}
    }

    @Override
    public String getPermissionLevel() {
	return "Moderator";
    }
}
