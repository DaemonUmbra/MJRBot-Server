package com.mjr.commands.defaultCommands;

import com.mjr.ChatBotManager.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.storage.Config;
import com.mjr.storage.PointsSystem;
import com.mjr.util.Utilities;

public class PointsCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Points", type, bot).equalsIgnoreCase("true")) {
			Utilities.sendMessage(type, bot, sender + " you currently have " + PointsSystem.getPoints(sender, type, bot) + " points.");
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
