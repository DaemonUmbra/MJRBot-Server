package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.games.MathsGame;

public class MathsCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Games").equalsIgnoreCase("true")) {
	    if (MathsGame.isMathsGameActive == false) {
		Utilities.sendMessage(type, channel, MathsGame.CreateQuestion(type, channel));
		Utilities.sendMessage(type, channel, "Type !answer YOURANSWER (e.g !answer 10) to start guessing!");
		MathsGame.isMathsGameActive = true;
	    } else {
		Utilities.sendMessage(type, channel, "Game Already started!");
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
