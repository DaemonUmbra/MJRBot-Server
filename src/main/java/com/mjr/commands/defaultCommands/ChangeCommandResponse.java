package com.mjr.commands.defaultCommands;

import java.io.IOException;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.commands.CustomCommands;
import com.mjr.storage.EventLog;
import com.mjr.storage.EventLog.EventType;
import com.mjr.util.ConsoleUtil;
import com.mjr.util.ConsoleUtil.MessageType;
import com.mjr.util.Utilities;

public class ChangeCommandResponse extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
		if (args.length >= 3) {
			if (!args[1].contains("!")) {
				String command = args[1];
				String response = message.substring(message.indexOf(command));
				response = response.substring(response.indexOf(' ') + 1);
				try {
					CustomCommands.changeCommandResponse(type, channel, command, response);
					EventLog.addEvent(channel, sender, "Edited the Custom Command response for " + command, EventType.CustomCommands);
				} catch (IOException e) {
					MJRBot.logErrorMessage(e);
				}
			} else {
				Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! Please dont include an ! in the Command Name");
			}
		} else {
			Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! You need to enter !commandresponse COMMANDNAME RESPONSE");
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
