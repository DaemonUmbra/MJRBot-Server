package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.bases.TwitchBot;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.threads.twitch.GetFollowTimeThread;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class FollowTimeCommand implements ICommand {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (type == BotType.Twitch) {
			GetFollowTimeThread thread = new GetFollowTimeThread((TwitchBot) bot, sender);
			thread.start();
		} else if (type == BotType.Mixer)
			ChatBotManager.sendMessage(type, bot, "This command isnt available for Mixer, right now sorry!");
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.User;
	}

	@Override
	public boolean hasCooldown() {
		return true;
	}

}
