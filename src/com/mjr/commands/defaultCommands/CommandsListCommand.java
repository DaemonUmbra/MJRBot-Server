package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;

public class CommandsListCommand extends Command {
    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (MJRBot.getTwitchBot() != null)
	    ((TwitchBot) bot).MessageToChat("You can check out the commands that " + ((TwitchBot) bot).getBotName()
		    + " offers over at http://goo.gl/iZhu2W");
	else
	    ((MixerBot) bot).sendMessage("You can check out the commands that " + ((MixerBot) bot).getBotName()
		    + " offers over at http://goo.gl/iZhu2W");

    }

    @Override
    public String getPermissionLevel() {
	return "User";
    }
}
