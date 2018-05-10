package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.Ranks;

public class SetRankCommand extends Command {
    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Ranks").equalsIgnoreCase("true")) {
	    if (args.length == 3) {
		String Rank = args[1];
		String User = args[2];
		if (Ranks.isOnList(User)) {
		    if (!Ranks.hasRank(sender, Rank)) {
			if (Ranks.isValidRank(Rank)) {
			    Ranks.setRank(User, Rank);
			    String endMessage = "Added " + Rank + " to " + User;
			    if (MJRBot.getTwitchBot() != null)
				((TwitchBot) bot).MessageToChat(endMessage);
			    else
				((MixerBot) bot).sendMessage(endMessage);
			} else {
			    String endMessage = "Rank doesnt exist!";
			    if (MJRBot.getTwitchBot() != null)
				((TwitchBot) bot).MessageToChat(endMessage);
			    else
				((MixerBot) bot).sendMessage(endMessage);
			}
		    } else {
			String endMessage = User + " is has already got that rank!";
			if (MJRBot.getTwitchBot() != null)
			    ((TwitchBot) bot).MessageToChat(endMessage);
			else
			    ((MixerBot) bot).sendMessage(endMessage);
		    }
		} else {
		    String endMessage = "Cant add " + Rank + " to " + User;
		    if (MJRBot.getTwitchBot() != null)
			((TwitchBot) bot).MessageToChat(endMessage);
		    else
			((MixerBot) bot).sendMessage(endMessage);
		}
	    } else {
		String endMessage = "Invalid arguments! You need to enter !setrank RANK USER";
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
