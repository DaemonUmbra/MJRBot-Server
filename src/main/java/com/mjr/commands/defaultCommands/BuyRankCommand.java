package com.mjr.commands.defaultCommands;

import com.mjr.ChatBotManager.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.storage.Config;
import com.mjr.storage.PointsSystem;
import com.mjr.storage.RankSystem;
import com.mjr.util.Utilities;

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
								Utilities.sendMessage(type, bot, "Added " + Rank + " to " + sender);
							} else {
								Utilities.sendMessage(type, bot, " you dont have the right amount of points! Do !points to check how many you got");
							}
						} else {
							Utilities.sendMessage(type, bot, "Rank doesnt exist!");
						}
					} else {
						Utilities.sendMessage(type, bot, "@" + sender + " you already have this rank!");
					}
				} else {
					Utilities.sendMessage(type, bot, "Cant add " + Rank + " to " + sender);
				}
			} else {
				Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !buyrank RANK");
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
