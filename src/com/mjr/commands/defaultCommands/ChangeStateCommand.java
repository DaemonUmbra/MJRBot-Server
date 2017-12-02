package com.mjr.commands.defaultCommands;

import java.io.IOException;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.commands.CustomCommands;

public class ChangeStateCommand extends Command {
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (args.length == 3) {
	    if (!args[1].contains("!")) {
		String command = args[1];
		String state = args[2];
		try {
		    CustomCommands.ChangeStateCommand(command, state);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    } else {
		String endMessage = "Invalid arguments! Please dont include an ! in the Command Name";
		if (MJRBot.getTwitchBot() != null)
		    ((TwitchBot) bot).MessageToChat(endMessage);
		else
		    ((MixerBot) bot).sendMessage(endMessage);
	    }
	} else {
	    String endMessage = "Invalid arguments! You need to enter !commandstate COMMANDNAME TRUE/FALSE";
	    if (MJRBot.getTwitchBot() != null)
		((TwitchBot) bot).MessageToChat(endMessage);
	    else
		((MixerBot) bot).sendMessage(endMessage);
	}
    }
}
