package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.storage.Config;
import com.mjr.storage.RankSystem;
import com.mjr.util.Utilities;

public class RemoveRankCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Ranks", channel).equalsIgnoreCase("true")) {
			if (args.length == 2) {
				String user = args[1];
				if (RankSystem.isOnList(user, channel)) {
					RankSystem.removeRank(user, channel);
					Utilities.sendMessage(type, channel, "@" + sender + " Removed " + user + " rank");
				} else {
					Utilities.sendMessage(type, channel, "@" + sender + " Unable to remove " + user + " rank");
				}
			} else {
				Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! You need to enter !removerank USER");
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
