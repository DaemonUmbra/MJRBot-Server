package com.mjr.mjrbot.commands;

import com.mjr.mjrbot.ChatBotManager.BotType;

public abstract class Command {
	public abstract void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args);

	public abstract String getPermissionLevel();

	public abstract boolean hasCooldown();
}
