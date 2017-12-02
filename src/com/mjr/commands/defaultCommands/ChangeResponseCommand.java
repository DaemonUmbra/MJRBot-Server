package com.mjr.commands.defaultCommands;

import java.io.IOException;

import com.mjr.ConsoleUtli;
import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.commands.CustomCommands;

public class ChangeResponseCommand extends Command {
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (args.length >= 3) {
	    if (!args[1].contains("!")) {
		String command = args[1];
		String response = message.substring(message.indexOf(command));
		response = response.substring(response.indexOf(' ') + 1);
		try {
		    CustomCommands.ChangeResponseCommand(command, response);
		} catch (IOException e) {
		    ConsoleUtli.TextToConsole(e.getMessage(), "Bot", null);
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
	    String endMessage = "Invalid arguments! You need to enter !commandresponse COMMANDNAME RESPONSE";
	    if (MJRBot.getTwitchBot() != null)
		((TwitchBot) bot).MessageToChat(endMessage);
	    else
		((MixerBot) bot).sendMessage(endMessage);
	}
    }
}
