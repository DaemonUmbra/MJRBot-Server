package com.mjr.commands.defaultCommands;

import com.mjr.ChatBotManager.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.threads.twitch.GetFollowTimeThread;
import com.mjr.util.Utilities;

public class FollowTimeCommand extends Command {

	@Override
	public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
		if (type == BotType.Twitch) {
			GetFollowTimeThread thread = new GetFollowTimeThread((TwitchBot) bot, type, sender);
			thread.start();
		} else
			Utilities.sendMessage(type, channel, "This command isnt available for Mixer, right now sorry!");
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
