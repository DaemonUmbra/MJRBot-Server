package com.mjr.commands;

public abstract class Command {
    public abstract void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args);
}
