package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.storage.RankSystemManager;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class RankCheckCommand implements ICommand {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (ChannelConfigManager.getSetting("Ranks", type, bot).equalsIgnoreCase("true")) {
			if (args.length == 2) {
				String user = args[1];
				if (RankSystemManager.isOnList(user, type, bot)) {
					MJRBotUtilities.sendMessage(type, bot, user + " current rank is " + RankSystemManager.getRank(user, type, bot));
				} else {
					MJRBotUtilities.sendMessage(type, bot, "@" + sender + "Unable to find " + user + " current rank!");
				}
			} else {
				MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !getrank USER");
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
