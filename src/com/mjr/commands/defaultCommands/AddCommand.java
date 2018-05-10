package com.mjr.commands.defaultCommands;

import java.io.IOException;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.commands.CustomCommands;

public class AddCommand extends Command {
    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (args.length >= 4) {
	    if (!args[1].contains("!")) {
		if (args[2].equalsIgnoreCase("User") || args[2].equalsIgnoreCase("Moderator")) {
		    String command = args[1];
		    String permissionlevel = args[2];
		    String response = message.substring(message.indexOf(permissionlevel));
		    response = response.substring(response.indexOf(' ') + 1);
		    try {
			CustomCommands.AddCommand(command, response, permissionlevel);
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		} else {
		    String endMessage = "Invalid arguments! The following Permission values are: User or Moderator";
		    if (MJRBot.getTwitchBot() != null)
			((TwitchBot) bot).MessageToChat(endMessage);
		    else
			((MixerBot) bot).sendMessage(endMessage);
		}
	    } else {
		String endMessage = "Invalid arguments! Please dont include an ! in the Command Name";
		if (MJRBot.getTwitchBot() != null)
		    ((TwitchBot) bot).MessageToChat(endMessage);
		else
		    ((MixerBot) bot).sendMessage(endMessage);
	    }
	} else {
	    String endMessage = "Invalid arguments! You need to enter !addcommand COMMANDNAME PERMISSION RESPONSE";
	    if (MJRBot.getTwitchBot() != null)
		((TwitchBot) bot).MessageToChat(endMessage);
	    else
		((MixerBot) bot).sendMessage(endMessage);
	}
    }

    @Override
    public String getPermissionLevel() {
	return "Moderator";
    }
}
