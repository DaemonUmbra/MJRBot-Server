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
