package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.MixerBot;
import com.mjr.mjrbot.Permissions.PermissionLevel;
import com.mjr.mjrbot.TwitchBot;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.storage.EventLog;
import com.mjr.mjrbot.storage.EventLog.EventType;
import com.mjr.mjrbot.util.Utilities;

public class PermitCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (args.length == 2) {
			String user = args[1];
			Utilities.sendMessage(type, bot, "@" + user + " is now permited to post a link in their next message!");
			if (type == BotType.Twitch)
				((TwitchBot) bot).linkPermitedUsers.add(user.toLowerCase());
			else if (type == BotType.Mixer)
				((MixerBot) bot).linkPermitedUsers.add(user.toLowerCase());
			EventLog.addEvent(type, bot, sender, "Permitted the user " + user + " to post a link", EventType.Commands);
		} else {
			Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !permit USER");
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
