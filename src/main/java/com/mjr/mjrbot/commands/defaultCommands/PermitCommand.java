package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.storage.EventLogManager;
import com.mjr.mjrbot.storage.EventLogManager.EventType;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class PermitCommand implements ICommand {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (args.length == 2) {
			String user = args[1];
			ChatBotManager.sendMessage(type, bot, "@" + user + " is now permited to post a link in their next message!");
			if (type == BotType.Twitch)
				((TwitchBot) bot).getTwitchData().linkPermitedUsers.add(user.toLowerCase());
			else if (type == BotType.Mixer)
				((MixerBot) bot).getMixerData().linkPermitedUsers.add(user.toLowerCase());
			EventLogManager.addEvent(type, bot, sender, "Permitted the user " + user + " to post a link", EventType.Commands);
		} else {
			ChatBotManager.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !permit USER");
		}
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.Moderator;
	}

	@Override
	public boolean hasCooldown() {
		return false;
	}
}
