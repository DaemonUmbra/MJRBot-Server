package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.MixerBot;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.files.PointsSystem;

public class AnswerCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (Config.getSetting("Games", channel).equalsIgnoreCase("true")) {
	    if (type == BotType.Twitch) {
		TwitchBot twitchBot = ((TwitchBot) bot);
		if (twitchBot.mathsGame.isGameActive == true) {
		    if (args.length == 2) {
			int index = Integer.parseInt(args[1]);
			if (twitchBot.mathsGame.Answer == index) {
			    int profit = PointsSystem.AddRandomPoints(sender, channel);
			    Utilities.sendMessage(type, channel,
				    "@" + sender + " Well done, You have got the right answer! You have gained " + profit + " points!");
			    twitchBot.mathsGame.isGameActive = false;
			} else {
			    Utilities.sendMessage(type, channel, sender + " you have got the wrong answer try again!");
			}
		    } else {
			Utilities.sendMessage(type, channel,
				"@" + sender + " Invalid arguments! You need to enter !answer YOURANSWER (Example !answer 10)");
		    }
		} else {
		    Utilities.sendMessage(type, channel, "@" + sender + " The maths game is currently not active!");
		}
	    } else {
		MixerBot mixerBot = ((MixerBot) bot);
		if (mixerBot.mathsGame.isGameActive == true) {
		    if (args.length == 2) {
			int index = Integer.parseInt(args[1]);
			if (mixerBot.mathsGame.Answer == index) {
			    int profit = PointsSystem.AddRandomPoints(sender, channel);
			    Utilities.sendMessage(type, channel,
				    "@" + sender + " Well done, You have got the right answer! You have gained " + profit + " points!");
			    mixerBot.mathsGame.isGameActive = false;
			} else {
			    Utilities.sendMessage(type, channel, "@" + sender + " you have got the wrong answer try again!");
			}
		    } else {
			Utilities.sendMessage(type, channel,
				"@" + sender + " Invalid arguments! You need to enter !answer YOURANSWER (Example !answer 10)");
		    }
		} else {
		    Utilities.sendMessage(type, channel, "@" + sender + " The maths game is currently not active!");
		}
	    }
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.User.getName();
    }

    @Override
    public boolean hasCooldown() {
	return true;
    }
}
