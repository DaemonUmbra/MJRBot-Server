package com.mjr.mjrbot.commands;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public interface ICommand {
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args);

	public PermissionLevel getPermissionLevel();

	public boolean hasCooldown();
}
