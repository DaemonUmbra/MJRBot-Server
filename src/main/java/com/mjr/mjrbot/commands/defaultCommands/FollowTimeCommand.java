package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.Permissions.PermissionLevel;
import com.mjr.mjrbot.TwitchBot;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.threads.twitch.GetFollowTimeThread;
import com.mjr.mjrbot.util.Utilities;

public class FollowTimeCommand extends Command {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (type == BotType.Twitch) {
			GetFollowTimeThread thread = new GetFollowTimeThread((TwitchBot) bot, type, sender);
			thread.start();
		} else if (type == BotType.Mixer)
			Utilities.sendMessage(type, bot, "This command isnt available for Mixer, right now sorry!");
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
