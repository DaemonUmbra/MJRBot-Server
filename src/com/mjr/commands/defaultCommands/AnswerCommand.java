package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.PointsSystem;
import com.mjr.games.MathsGame;

public class AnswerCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (Config.getSetting("Games").equalsIgnoreCase("true")) {
	    if (MathsGame.isMathsGameActive == true) {
		if (args.length == 2) {
		    int index = Integer.parseInt(args[1]);
		    if (MathsGame.Answer == index) {
			Utilities.sendMessage(type, channel, sender + " Well done, You have got the right answer! You have gained 10 points!");
			PointsSystem.AddPoints(sender, 10);
			MathsGame.isMathsGameActive = false;
		    } else {
			Utilities.sendMessage(type, channel, sender + " you have got the wrong answer try again!");
		    }
		} else {
		    Utilities.sendMessage(type, channel, "Invalid arguments! You need to enter !answer YOURANSWER (Example !answer 10)");
		}
	    } else {
		Utilities.sendMessage(type, channel, "The maths game is currently not active!");
	    }
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.User.getName();
    }
}
