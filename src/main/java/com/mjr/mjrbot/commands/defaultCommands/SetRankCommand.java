package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.storage.Config;
import com.mjr.mjrbot.storage.RankSystem;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.Permissions.PermissionLevel;

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
							MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Set " + user + " rank to" + rank);
						} else {
							MJRBotUtilities.sendMessage(type, bot, "@" + sender + " the rank + " + rank + " doesnt exist!");
						}
					} else {
						MJRBotUtilities.sendMessage(type, bot, "@" + sender + user + " is has already got that rank!");
					}
				} else {
					MJRBotUtilities.sendMessage(type, bot, "@" + sender + "Unable to give the rank of " + rank + " to " + user);
				}
			} else {
				MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !setrank RANK USER");
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
