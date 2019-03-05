package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.storage.PointsSystemManager;
import com.mjr.mjrbot.storage.RankSystemManager;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class BuyRankCommand implements ICommand {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (ChannelConfigManager.getSetting("Ranks", type, bot).equalsIgnoreCase("true")) {
			if (args.length == 2) {
				String Rank = args[1];
				if (RankSystemManager.isOnList(sender, type, bot)) {
					if (!RankSystemManager.hasRank(sender, Rank, type, bot)) {
						if (RankSystemManager.isValidRank(Rank)) {
							if (PointsSystemManager.hasPoints(sender, RankSystemManager.getRankPrice(Rank), type, bot)) {
								PointsSystemManager.RemovePoints(sender, RankSystemManager.getRankPrice(Rank), type, bot);
								RankSystemManager.setRank(sender, Rank, type, bot);
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
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.User;
	}

	@Override
	public boolean hasCooldown() {
		return false;
	}
}
