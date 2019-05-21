package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.storage.RankSystemManager;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class SetRankCommand implements ICommand {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (ChannelConfigManager.getSetting("Ranks", type, bot).equalsIgnoreCase("true")) {
			if (args.length == 3) {
				String rank = args[1];
				String user = args[2];
				if (RankSystemManager.isOnList(user, type, bot)) {
					if (!RankSystemManager.hasRank(sender, rank, type, bot)) {
						if (RankSystemManager.isValidRank(rank)) {
							RankSystemManager.setRank(user, rank, type, bot);
							ChatBotManager.sendMessage(type, bot, "@" + sender + " Set " + user + " rank to" + rank);
						} else {
							ChatBotManager.sendMessage(type, bot, "@" + sender + " the rank + " + rank + " doesnt exist!");
						}
					} else {
						ChatBotManager.sendMessage(type, bot, "@" + sender + user + " is has already got that rank!");
					}
				} else {
					ChatBotManager.sendMessage(type, bot, "@" + sender + "Unable to give the rank of " + rank + " to " + user);
				}
			} else {
				ChatBotManager.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !setrank RANK USER");
			}
		}
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.Moderator;
	}

	@Override
	public boolean hasCooldown() {
		return false;
	}
}
