package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.storage.PointsSystemManager;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class PointsCheckCommand implements ICommand {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (ChannelConfigManager.getSetting("Points", type, bot).equalsIgnoreCase("true")) {
			if (args.length == 2) {
				String user = args[1];
				if (PointsSystemManager.isOnList(user, type, bot)) {
					ChatBotManager.sendMessage(type, bot, user + " currently has " + PointsSystemManager.getPoints(user, type, bot) + " points");
				} else {
					ChatBotManager.sendMessage(type, bot, "@" + sender + "Unable to find " + user + " current points value!");
				}
			} else {
				ChatBotManager.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !pointscheck USER");
			}
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
