package com.mjr.mjrbot.commands.defaultCommands;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.Permissions.PermissionLevel;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.sql.MySQLConnection;
import com.mjr.mjrbot.util.Utilities;

public class PointsLeaderboardCommands extends Command {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (MJRBot.storageType == StorageType.File)
			Utilities.sendMessage(type, bot, "This feature is not currently available, when the bot is using a file based system");
		else {
			ResultSet result = null;
			if (type == BotType.Twitch)
				result = MySQLConnection.executeQuery("SELECT * FROM points WHERE twitch_channel_id ='" + Utilities.getChannelIDFromBotType(type, bot) + "' ORDER BY amount DESC LIMIT 10");
			else if (type == BotType.Mixer)
				result = MySQLConnection.executeQuery("SELECT * FROM points WHERE channel ='" + Utilities.getChannelNameFromBotType(type, bot) + "' ORDER BY amount DESC LIMIT 10");
			String top10Users = "";
			try {
				while (result.next()) {
					top10Users = top10Users + result.getString("name") + ":" + result.getInt("amount") + " point(s), ";
				}
				Utilities.sendMessage(type, bot, "The top 10 users in this channel for points are: " + top10Users.substring(0, top10Users.length() - 1));
			} catch (SQLException e) {
				MJRBot.logErrorMessage(e);
			}
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
