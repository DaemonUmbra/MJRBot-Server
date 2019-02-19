package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.storage.Config;
import com.mjr.mjrbot.storage.PointsSystem;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.Permissions.PermissionLevel;

public class SetPointsCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Points", type, bot).equalsIgnoreCase("true")) {
			if (args.length == 3) {
				String points = args[1];
				String user = args[2];
				if (PointsSystem.isOnList(user, type, bot)) {
					PointsSystem.setPoints(user.toLowerCase(), Integer.parseInt(points), type, bot, true, true);
					MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Set " + points + " points" + " to " + user);
				} else {
					MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Unable to set " + user + " points" + " to " + points);
				}
			} else {
				MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !setpoints POINTS USER");
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
