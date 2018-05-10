package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.PointsSystem;
import com.mjr.games.MathsGame;

public class AnswerCommand extends Command {
    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Games").equalsIgnoreCase("true")) {
	    if (MathsGame.isMathsGameActive == true) {
		if (args.length == 2) {
		    int index = Integer.parseInt(args[1]);
		    if (MathsGame.Answer == index) {
			String endMessage = sender + " Well done, You have got the right answer! You have gained 10 points!";
			if (MJRBot.getTwitchBot() != null)
			    ((TwitchBot) bot).MessageToChat(endMessage);
			else
			    ((MixerBot) bot).sendMessage(endMessage);
			PointsSystem.AddPoints(sender, 10);
			MathsGame.isMathsGameActive = false;
		    } else {
			String endMessage = sender + " you have got the wrong answer try again!";
			if (MJRBot.getTwitchBot() != null)
			    ((TwitchBot) bot).MessageToChat(endMessage);
			else
			    ((MixerBot) bot).sendMessage(endMessage);
		    }
		} else {
		    String endMessage = "Invalid arguments! You need to enter !answer YOURANSWER (Example !answer 10)";
		    if (MJRBot.getTwitchBot() != null)
			((TwitchBot) bot).MessageToChat(endMessage);
		    else
			((MixerBot) bot).sendMessage(endMessage);
		}
	    } else {
		String endMessage = "The maths game is currently not active!";
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
