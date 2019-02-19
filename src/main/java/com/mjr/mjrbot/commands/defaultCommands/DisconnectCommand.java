package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.storage.sql.MySQLConnection;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.Permissions.PermissionLevel;

public class DisconnectCommand extends Command {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (type == BotType.Twitch)
			MySQLConnection.executeUpdate("DELETE from channels where twitch_channel_id = " + "\"" + MJRBotUtilities.getChannelIDFromBotType(type, bot) + "\"" + " AND bot_type = " + "\"" + type.getTypeName() + "\"");
		else if (type == BotType.Mixer)
			MySQLConnection.executeUpdate("DELETE from channels where name = " + "\"" + MJRBotUtilities.getChannelNameFromBotType(type, bot).toLowerCase() + "\"" + " AND bot_type = " + "\"" + type.getTypeName() + "\"");
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
