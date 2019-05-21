package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.storage.PointsSystemManager;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class SetPointsCommand implements ICommand {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (ChannelConfigManager.getSetting("Points", type, bot).equalsIgnoreCase("true")) {
			if (args.length == 3) {
				String points = args[1];
				String user = args[2];
				if (PointsSystemManager.isOnList(user, type, bot)) {
					PointsSystemManager.setPoints(user.toLowerCase(), Integer.parseInt(points), type, bot, true, true);
					ChatBotManager.sendMessage(type, bot, "@" + sender + " Set " + points + " points" + " to " + user);
				} else {
					ChatBotManager.sendMessage(type, bot, "@" + sender + " Unable to set " + user + " points" + " to " + points);
				}
			} else {
				ChatBotManager.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !setpoints POINTS USER");
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
