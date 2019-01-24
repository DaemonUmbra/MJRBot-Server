package com.mjr.commands.defaultCommands;

import com.mjr.ChatBotManager.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.storage.Config;
import com.mjr.storage.RankSystem;
import com.mjr.util.Utilities;

public class SetRankCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Ranks", type, bot).equalsIgnoreCase("true")) {
			if (args.length == 3) {
				String rank = args[1];
				String user = args[2];
				if (RankSystem.isOnList(user, type, bot)) {
					if (!RankSystem.hasRank(sender, rank, type, bot)) {
						if (RankSystem.isValidRank(rank)) {
							RankSystem.setRank(user, rank, type, bot);
							Utilities.sendMessage(type, bot, "@" + sender + " Set " + user + " rank to" + rank);
						} else {
							Utilities.sendMessage(type, bot, "@" + sender + " the rank + " + rank + " doesnt exist!");
						}
					} else {
						Utilities.sendMessage(type, bot, "@" + sender + user + " is has already got that rank!");
					}
				} else {
					Utilities.sendMessage(type, bot, "@" + sender + "Unable to give the rank of " + rank + " to " + user);
				}
			} else {
				Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !setrank RANK USER");
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
