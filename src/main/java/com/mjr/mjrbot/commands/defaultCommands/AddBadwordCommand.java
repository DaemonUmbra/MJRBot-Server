package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.Permissions.PermissionLevel;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.sql.MySQLConnection;
import com.mjr.mjrbot.util.Utilities;

public class AddBadwordCommand extends Command {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (args.length == 2) {
			if (MJRBot.storageType == StorageType.Database) {
				if(type == BotType.Twitch)
					MySQLConnection.executeUpdate("INSERT INTO badwords(twitch_channel_id, word) VALUES (" + "\"" + Utilities.getChannelIDFromBotType(type, bot) + "\"" + "," + "\"" + args[1] + "\"" + ")");
				if(type == BotType.Mixer)
					MySQLConnection.executeUpdate("INSERT INTO badwords(channel, word) VALUES (" + "\"" + Utilities.getChannelNameFromBotType(type, bot) + "\"" + "," + "\"" + args[1] + "\"" + ")");
				Utilities.sendMessage(type, bot, "@" + sender + " badword has been added!");
			} else {
				Utilities.sendMessage(type, bot, "This command isnt available for a file storage based bot!");
			}
		} else
			Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !addbadword WORD");
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
