package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.Permissions.PermissionLevel;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.storage.Config;
import com.mjr.mjrbot.storage.RankSystem;
import com.mjr.mjrbot.util.Utilities;

public class RankCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Ranks", type, bot).equalsIgnoreCase("true")) {
			Utilities.sendMessage(type, bot, sender + " you current rank is " + RankSystem.getRank(sender, type, bot));
		}
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
