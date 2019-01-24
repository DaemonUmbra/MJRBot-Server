package com.mjr.commands.defaultCommands;

import com.mjr.ChatBotManager.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.storage.Config;
import com.mjr.storage.PointsSystem;
import com.mjr.util.Utilities;

public class AddPointsCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Points", type, bot).equalsIgnoreCase("true")) {
			if (args.length == 3) {
				String Points = args[1];
				String User = args[2];

				if (PointsSystem.isOnList(User, type, bot)) {
					PointsSystem.AddPointsWithEventMsg(User.toLowerCase(), Integer.parseInt(Points), type, bot);
					Utilities.sendMessage(type, bot, "Added " + Points + " points" + " to " + User);
				} else {
					Utilities.sendMessage(type, bot, "Cant add " + Points + " points" + " to " + User);
				}
			} else {
				Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !addpoints POINTS USER");
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
