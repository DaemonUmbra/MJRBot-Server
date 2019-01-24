package com.mjr.commands.defaultCommands;

import java.io.IOException;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.commands.CustomCommands;
import com.mjr.storage.EventLog;
import com.mjr.storage.EventLog.EventType;
import com.mjr.util.Utilities;

public class ChangeCommandPermission extends Command {
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
						EventLog.addEvent(type, bot, sender, "Edited the Custom Command permission for " + command, EventType.CustomCommands);
					} catch (IOException e) {
						MJRBot.logErrorMessage(e);
					}
				} else {
					Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! The following Permission values are: User or Follower or VIP or Subscriber or Moderator or Streamer");
				}
			} else {
				Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! Please dont include an ! in the Command Name");
			}
		} else {
			Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !commandpermission COMMANDNAME PERMISSIONLEVEL");
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
