package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.sql.MySQLConnection;

public class DisconnectCommand extends Command {

    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	MySQLConnection.executeUpdate("DELETE from channels where name = " + "\"" + channel.toLowerCase() + "\"" + " AND bot_type = " + "\""
		+ type.getTypeName() + "\"" + ")");
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.Streamer.getName();
    }

    @Override
    public boolean hasCooldown() {
	return false;
    }

}
