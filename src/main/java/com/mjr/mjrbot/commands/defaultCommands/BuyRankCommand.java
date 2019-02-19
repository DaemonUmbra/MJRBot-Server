package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.storage.Config;
import com.mjr.mjrbot.storage.PointsSystem;
import com.mjr.mjrbot.storage.RankSystem;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.Permissions.PermissionLevel;

public class BuyRankCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Ranks", type, bot).equalsIgnoreCase("true")) {
			if (args.length == 2) {
				String Rank = args[1];
				if (RankSystem.isOnList(sender, type, bot)) {
					if (!RankSystem.hasRank(sender, Rank, type, bot)) {
						if (RankSystem.isValidRank(Rank)) {
							if (PointsSystem.hasPoints(sender, RankSystem.getRankPrice(Rank), type, bot)) {
								PointsSystem.RemovePoints(sender, RankSystem.getRankPrice(Rank), type, bot);
								RankSystem.setRank(sender, Rank, type, bot);
								MJRBotUtilities.sendMessage(type, bot, "Added " + Rank + " to " + sender);
							} else {
								MJRBotUtilities.sendMessage(type, bot, " you dont have the right amount of points! Do !points to check how many you got");
							}
						} else {
							MJRBotUtilities.sendMessage(type, bot, "Rank doesnt exist!");
						}
					} else {
						MJRBotUtilities.sendMessage(type, bot, "@" + sender + " you already have this rank!");
					}
				} else {
					MJRBotUtilities.sendMessage(type, bot, "Cant add " + Rank + " to " + sender);
				}
			} else {
				MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !buyrank RANK");
			}
		}
	}

	@Override
	public String getPermissionLevel() {
		return PermissionLevel.User.getName();
	}

	@Override
	public boolean hasCooldown() {
		return false;
	}
}
