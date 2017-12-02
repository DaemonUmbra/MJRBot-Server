package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.games.MathsGame;

public class MathsCommand extends Command {
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Games").equalsIgnoreCase("true")) {
	    if (MathsGame.isMathsGameActive == false) {
		String endMessage = "Type !answer YOURANSWER (e.g !answer 10) to start guessing!";
		if (MJRBot.getTwitchBot() != null) {
		    ((TwitchBot) bot).MessageToChat(MathsGame.CreateQuestion());
		    ((TwitchBot) bot).MessageToChat(endMessage);
		} else {
		    ((MixerBot) bot).sendMessage(MathsGame.CreateQuestion());
		    ((MixerBot) bot).sendMessage(endMessage);
		}
		MathsGame.isMathsGameActive = true;
	    } else {
		String endMessage = "Game Already started!";
		if (MJRBot.getTwitchBot() != null)
		    ((TwitchBot) bot).MessageToChat(endMessage);
		else
		    ((MixerBot) bot).sendMessage(endMessage);
	    }
	}
    }
}
