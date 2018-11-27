package com.mjr.commands.defaultCommands;

import java.io.IOException;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.commands.CustomCommands;
import com.mjr.storage.EventLog;
import com.mjr.storage.EventLog.EventType;
import com.mjr.util.Utilities;

public class AddCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
		if (args.length >= 4) {
			if (!args[1].contains("!")) {
				if (args[2].equalsIgnoreCase("User") || args[2].equalsIgnoreCase("Moderator") || args[2].equalsIgnoreCase("Subscriber") || args[2].equalsIgnoreCase("Streamer") || args[2].equalsIgnoreCase("VIP")) {
					String command = args[1];
					String permissionlevel = args[2];
					String response = message.substring(message.indexOf(permissionlevel));
					response = response.substring(response.indexOf(' ') + 1);
					try {
						CustomCommands.addCommand(type, channel, command, response, permissionlevel);
						EventLog.addEvent(channel, sender, "Added a new Custom Command of " + command, EventType.CustomCommands);
					} catch (IOException e) {
						MJRBot.logErrorMessage(e);
					}
				} else {
					Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! The following Permission values are: User or VIP or Subscriber or Moderator or Streamer");
				}
			} else {
				Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! Please dont include an ! in the Command Name");
			}
		} else {
			Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! You need to enter !addcommand COMMANDNAME PERMISSION RESPONSE");
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
