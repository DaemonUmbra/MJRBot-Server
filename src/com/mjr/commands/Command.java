package com.mjr.commands;

import com.mjr.MJRBot.BotType;

public abstract class Command {
    public abstract void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args);

    public abstract String getPermissionLevel();
    public abstract boolean hasCooldown();
}
