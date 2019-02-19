package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.storage.Config;
import com.mjr.mjrbot.storage.PointsSystem;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.Permissions.PermissionLevel;

public class AddPointsCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Points", type, bot).equalsIgnoreCase("true")) {
			if (args.length == 3) {
				String Points = args[1];
				String User = args[2];

				if (PointsSystem.isOnList(User, type, bot)) {
					PointsSystem.AddPointsWithEventMsg(User.toLowerCase(), Integer.parseInt(Points), type, bot);
					MJRBotUtilities.sendMessage(type, bot, "Added " + Points + " points" + " to " + User);
				} else {
					MJRBotUtilities.sendMessage(type, bot, "Cant add " + Points + " points" + " to " + User);
				}
			} else {
				MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !addpoints POINTS USER");
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
