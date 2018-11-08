package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.MixerBot;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.storage.Config;

public class MathsCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (Config.getSetting("Games", channel).equalsIgnoreCase("true")) {
	    if (type == BotType.Twitch) {
		TwitchBot twitchBot = ((TwitchBot) bot);
		if (twitchBot.mathsGame.isGameActive == false) {
		    Utilities.sendMessage(type, channel, twitchBot.mathsGame.CreateQuestion(type, channel));
		    Utilities.sendMessage(type, channel, "Type !answer YOURANSWER (e.g !answer 10) to start guessing!");
		    twitchBot.mathsGame.isGameActive = true;
		} else {
		    Utilities.sendMessage(type, channel, "@" + sender + " Game Already started!");
		}
	    } else {
		MixerBot mixerBot = ((MixerBot) bot);
		if (mixerBot.mathsGame.isGameActive == false) {
		    Utilities.sendMessage(type, channel, mixerBot.mathsGame.CreateQuestion(type, channel));
		    Utilities.sendMessage(type, channel, "Type !answer YOURANSWER (e.g !answer 10) to start guessing!");
		    mixerBot.mathsGame.isGameActive = true;
		} else {
		    Utilities.sendMessage(type, channel, "@" + sender + " Game Already started!");
		}
	    }
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.Moderator.getName();
    }

    @Override
    public boolean hasCooldown() {
	return true;
    }
}
