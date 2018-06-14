package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.Utilities;
import com.mjr.commands.Command;

public class CommandsListCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	Utilities.sendMessage("You can check out the commands that " + ((TwitchBot) bot).getBotName()
		+ " offers over at http://goo.gl/iZhu2W");
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.User.getName();
    }
}
