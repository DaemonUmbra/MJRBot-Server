package com.mjr.commands.defaultCommands;

import com.mjr.ChatBotManager.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.storage.Config;
import com.mjr.storage.RankSystem;
import com.mjr.util.Utilities;

public class RankCheckCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Ranks", channel).equalsIgnoreCase("true")) {
			if (args.length == 2) {
				String user = args[1];
				if (RankSystem.isOnList(user, channel)) {
					Utilities.sendMessage(type, channel, user + " current rank is " + RankSystem.getRank(user, channel));
				} else {
					Utilities.sendMessage(type, channel, "@" + sender + "Unable to find " + user + " current rank!");
				}
			} else {
				Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! You need to enter !getrank USER");
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
