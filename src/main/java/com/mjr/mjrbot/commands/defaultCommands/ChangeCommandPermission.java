package com.mjr.mjrbot.commands.defaultCommands;

import java.io.IOException;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.CustomCommands;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.storage.EventLogManager;
import com.mjr.mjrbot.storage.EventLogManager.EventType;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class ChangeCommandPermission implements ICommand {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (args.length == 3) {
			if (!args[1].contains("!")) {
				if (args[2].equalsIgnoreCase("User") || args[2].equalsIgnoreCase("Moderator") || args[2].equalsIgnoreCase("Subscriber") || args[2].equalsIgnoreCase("Streamer") || args[2].equalsIgnoreCase("VIP")
						|| args[2].equalsIgnoreCase("Follower")) {
					String command = args[1];
					String permission = args[2];
					try {
						CustomCommands.changeCommandPermission(type, bot, command, permission);
						EventLogManager.addEvent(type, bot, sender, "Edited the Custom Command permission for " + command, EventType.CustomCommands);
					} catch (IOException e) {
						MJRBotUtilities.logErrorMessage(e);
					}
				} else {
					ChatBotManager.sendMessage(type, bot, "@" + sender + " Invalid arguments! The following Permission values are: User or Follower or VIP or Subscriber or Moderator or Streamer");
				}
			} else {
				ChatBotManager.sendMessage(type, bot, "@" + sender + " Invalid arguments! Please dont include an ! in the Command Name");
			}
		} else {
			ChatBotManager.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !commandpermission COMMANDNAME PERMISSIONLEVEL");
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
