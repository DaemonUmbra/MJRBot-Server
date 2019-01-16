package com.mjr.commands.defaultCommands;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.MJRBot.StorageType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.sql.MySQLConnection;
import com.mjr.util.Utilities;

public class AddBadwordCommand extends Command {

	@Override
	public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
		if (args.length == 2) {
			if (MJRBot.storageType == StorageType.Database) {
				MySQLConnection.executeUpdate("INSERT INTO badwords(channel, word) VALUES (" + "\"" + channel + "\"" + "," + "\"" + args[1] + "\"" + ")");
				Utilities.sendMessage(type, channel, "@" + sender + " badword has been added!");
			} else {
				Utilities.sendMessage(type, channel, "This command isnt available for a file storage based bot!");
			}
		} else
			Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! You need to enter !addbadword WORD");
	}

	@Override
	public String getPermissionLevel() {
		return PermissionLevel.Moderator.getName();
	}

	@Override
	public boolean hasCooldown() {
		return false;
	}

}
