package com.mjr.commands.defaultCommands;

import com.mjr.ChatBotManager.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.storage.Config;
import com.mjr.storage.PointsSystem;
import com.mjr.util.Utilities;

public class RemovePointsCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Points", type, bot).equalsIgnoreCase("true")) {
			if (args.length == 3) {
				String points = args[1];
				String user = args[2];
				if (PointsSystem.isOnList(user, type, bot)) {
					PointsSystem.RemovePoints(user.toLowerCase(), Integer.parseInt(points), type, bot);
					Utilities.sendMessage(type, bot, "@" + sender + " Removed " + points + " points" + " from " + user);
				} else {
					Utilities.sendMessage(type, bot, "@" + sender + " Unable to remove " + points + " points" + " from " + user);
				}
			} else {
				Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !removepoints POINTS USER");
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
