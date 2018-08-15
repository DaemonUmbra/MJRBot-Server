package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.MixerBot;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.storage.EventLog;
import com.mjr.storage.EventLog.EventType;

public class PermitCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (args.length == 2) {
	    String user = args[1];
	    Utilities.sendMessage(type, channel, "@" + user + " is now permited to post a link in their next message!");
	    if (type == BotType.Twitch)
		((TwitchBot) bot).linkPermitedUsers.add(user.toLowerCase());
	    else
		((MixerBot) bot).linkPermitedUsers.add(user.toLowerCase());
	    EventLog.addEvent(channel, sender, "Permitted the user " + user + " to post a link", EventType.Commands);
	} else {
	    Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! You need to enter !permit USER");
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.Moderator.getName();
    }

    @Override
    public boolean hasCooldown() {
	return false;
    }
}
