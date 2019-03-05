package com.mjr.mjrbot.commands.defaultCommands;

import java.io.IOException;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.CustomCommands;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.storage.EventLogManager;
import com.mjr.mjrbot.storage.EventLogManager.EventType;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class ChangeCommandResponse implements ICommand {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (args.length >= 3) {
			if (!args[1].contains("!")) {
				String command = args[1];
				String response = message.substring(message.indexOf(command));
				response = response.substring(response.indexOf(' ') + 1);
				try {
					CustomCommands.changeCommandResponse(type, bot, command, response);
					EventLogManager.addEvent(type, bot, sender, "Edited the Custom Command response for " + command, EventType.CustomCommands);
				} catch (IOException e) {
					MJRBotUtilities.logErrorMessage(e);
				}
			} else {
				MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! Please dont include an ! in the Command Name");
			}
		} else {
			MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !commandresponse COMMANDNAME RESPONSE");
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
