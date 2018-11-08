package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.storage.Config;
import com.mjr.storage.RankSystem;

public class RankCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (Config.getSetting("Ranks", channel).equalsIgnoreCase("true")) {
	    Utilities.sendMessage(type, channel, sender + " you current rank is " + RankSystem.getRank(sender, channel));
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.User.getName();
    }

    @Override
    public boolean hasCooldown() {
	return true;
    }
}
