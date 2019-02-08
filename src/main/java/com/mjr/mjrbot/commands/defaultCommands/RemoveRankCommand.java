package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.Permissions.PermissionLevel;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.storage.Config;
import com.mjr.mjrbot.storage.RankSystem;
import com.mjr.mjrbot.util.Utilities;

public class RemoveRankCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Ranks", type, bot).equalsIgnoreCase("true")) {
			if (args.length == 2) {
				String user = args[1];
				if (RankSystem.isOnList(user, type, bot)) {
					RankSystem.removeRank(user, type, bot);
					Utilities.sendMessage(type, bot, "@" + sender + " Removed " + user + " rank");
				} else {
					Utilities.sendMessage(type, bot, "@" + sender + " Unable to remove " + user + " rank");
				}
			} else {
				Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !removerank USER");
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
