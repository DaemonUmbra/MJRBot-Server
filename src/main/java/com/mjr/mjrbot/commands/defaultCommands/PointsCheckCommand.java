package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.Permissions.PermissionLevel;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.storage.Config;
import com.mjr.mjrbot.storage.PointsSystem;
import com.mjr.mjrbot.util.Utilities;

public class PointsCheckCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Points", type, bot).equalsIgnoreCase("true")) {
			if (args.length == 2) {
				String user = args[1];
				if (PointsSystem.isOnList(user, type, bot)) {
					Utilities.sendMessage(type, bot, user + " currently has " + PointsSystem.getPoints(user, type, bot) + " points");
				} else {
					Utilities.sendMessage(type, bot, "@" + sender + "Unable to find " + user + " current points value!");
				}
			} else {
				Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !pointscheck USER");
			}
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
