package com.mjr.mjrbot.commands.defaultCommands;

import java.io.IOException;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.commands.CustomCommands;
import com.mjr.mjrbot.storage.EventLog;
import com.mjr.mjrbot.storage.EventLog.EventType;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.Permissions.PermissionLevel;

public class ChangeCommandState extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (args.length == 3) {
			if (!args[1].contains("!")) {
				String command = args[1];
				String state = args[2];
				try {
					CustomCommands.changeCommandState(type, bot, command, state);
					EventLog.addEvent(type, bot, sender, "Edited the Custom Command state for " + command, EventType.CustomCommands);
				} catch (IOException e) {
					MJRBotUtilities.logErrorMessage(e);
				}
			} else {
				MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! Please dont include an ! in the Command Name");
			}
		} else {
			MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !commandstate COMMANDNAME TRUE/FALSE");
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
