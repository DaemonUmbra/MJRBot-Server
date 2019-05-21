package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.storage.PointsSystemManager;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class AddPointsCommand implements ICommand {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (ChannelConfigManager.getSetting("Points", type, bot).equalsIgnoreCase("true")) {
			if (args.length == 3) {
				String Points = args[1];
				String User = args[2];

				if (PointsSystemManager.isOnList(User, type, bot)) {
					PointsSystemManager.AddPointsWithEventMsg(User.toLowerCase(), Integer.parseInt(Points), type, bot);
					ChatBotManager.sendMessage(type, bot, "Added " + Points + " points" + " to " + User);
				} else {
					ChatBotManager.sendMessage(type, bot, "Cant add " + Points + " points" + " to " + User);
				}
			} else {
				ChatBotManager.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !addpoints POINTS USER");
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
