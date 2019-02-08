package com.mjr.mjrbot.commands.defaultCommands;

import java.io.IOException;

import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.Permissions.PermissionLevel;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.commands.CustomCommands;
import com.mjr.mjrbot.storage.EventLog;
import com.mjr.mjrbot.storage.EventLog.EventType;
import com.mjr.mjrbot.util.Utilities;

public class ChangeCommandResponse extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (args.length >= 3) {
			if (!args[1].contains("!")) {
				String command = args[1];
				String response = message.substring(message.indexOf(command));
				response = response.substring(response.indexOf(' ') + 1);
				try {
					CustomCommands.changeCommandResponse(type, bot, command, response);
					EventLog.addEvent(type, bot, sender, "Edited the Custom Command response for " + command, EventType.CustomCommands);
				} catch (IOException e) {
					MJRBot.logErrorMessage(e);
				}
			} else {
				Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! Please dont include an ! in the Command Name");
			}
		} else {
			Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !commandresponse COMMANDNAME RESPONSE");
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
