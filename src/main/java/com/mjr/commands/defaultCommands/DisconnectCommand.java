package com.mjr.commands.defaultCommands;

import com.mjr.ChatBotManager.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.sql.MySQLConnection;
import com.mjr.util.Utilities;

public class DisconnectCommand extends Command {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if(type == BotType.Twitch)
			MySQLConnection.executeUpdate("DELETE from channels where twitch_channel_id = " + "\"" + Utilities.getChannelIDFromBotType(type, bot) + "\"" + " AND bot_type = " + "\"" + type.getTypeName() + "\"");
		else if(type == BotType.Mixer)
			MySQLConnection.executeUpdate("DELETE from channels where name = " + "\"" + Utilities.getChannelNameFromBotType(type, bot).toLowerCase() + "\"" + " AND bot_type = " + "\"" + type.getTypeName() + "\"");
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
