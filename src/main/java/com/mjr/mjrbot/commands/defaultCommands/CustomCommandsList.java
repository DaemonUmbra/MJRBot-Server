package com.mjr.mjrbot.commands.defaultCommands;

import java.io.IOException;
import java.util.List;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.CustomCommands;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class CustomCommandsList implements ICommand {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		try {
			List<String> names = CustomCommands.getAllCommandNames(type, bot);
			if (names.size() == 0) {
				ChatBotManager.sendMessage(type, bot, "No custom commnands found!");
			} else {
				ChatBotManager.sendMessage(type, bot, "Custom Commands: " + String.join(", ", names));
			}
		} catch (IOException e) {
			MJRBotUtilities.logErrorMessage(e);
		}
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.Moderator;
	}

	@Override
	public boolean hasCooldown() {
		return true;
	}

}
