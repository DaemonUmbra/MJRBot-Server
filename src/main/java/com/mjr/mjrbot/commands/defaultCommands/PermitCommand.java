package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.storage.EventLog;
import com.mjr.mjrbot.storage.EventLog.EventType;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.Permissions.PermissionLevel;

public class PermitCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (args.length == 2) {
			String user = args[1];
			MJRBotUtilities.sendMessage(type, bot, "@" + user + " is now permited to post a link in their next message!");
			if (type == BotType.Twitch)
				((TwitchBot) bot).getTwitchData().linkPermitedUsers.add(user.toLowerCase());
			else if (type == BotType.Mixer)
				((MixerBot) bot).getMixerData().linkPermitedUsers.add(user.toLowerCase());
			EventLog.addEvent(type, bot, sender, "Permitted the user " + user + " to post a link", EventType.Commands);
		} else {
			MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !permit USER");
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
