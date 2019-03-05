package com.mjr.mjrbot.commands.defaultCommands;

import java.io.IOException;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.CustomCommands;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.storage.EventLogManager;
import com.mjr.mjrbot.storage.EventLogManager.EventType;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class AddCommand implements ICommand {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (args.length >= 4) {
			if (!args[1].contains("!")) {
				if (args[2].equalsIgnoreCase("User") || args[2].equalsIgnoreCase("Moderator") || args[2].equalsIgnoreCase("Subscriber") || args[2].equalsIgnoreCase("Streamer") || args[2].equalsIgnoreCase("VIP")
						|| args[2].equalsIgnoreCase("Follower")) {
					String command = args[1];
					String permissionlevel = args[2];
					String response = message.substring(message.indexOf(permissionlevel));
					response = response.substring(response.indexOf(' ') + 1);
					try {
						CustomCommands.addCommand(type, bot, command, response, permissionlevel);
						EventLogManager.addEvent(type, bot, sender, "Added a new Custom Command of " + command, EventType.CustomCommands);
					} catch (IOException e) {
						MJRBotUtilities.logErrorMessage(e);
					}
				} else {
					MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! The following Permission values are: User or Follower or VIP or Subscriber or Moderator or Streamer");
				}
			} else {
				MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! Please dont include an ! in the Command Name");
			}
		} else {
			MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !addcommand COMMANDNAME PERMISSION RESPONSE");
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
