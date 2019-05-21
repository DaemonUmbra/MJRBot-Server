package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.storage.sql.MySQLConnection;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class AddBadwordCommand implements ICommand {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (args.length == 2) {
			if (MJRBot.storageType == StorageType.Database) {
				if (type == BotType.Twitch)
					MySQLConnection.executeUpdate("INSERT INTO badwords(twitch_channel_id, word) VALUES (" + "\"" + ChatBotManager.getChannelIDFromBotType(type, bot) + "\"" + "," + "\"" + args[1] + "\"" + ")");
				if (type == BotType.Mixer)
					MySQLConnection.executeUpdate("INSERT INTO badwords(channel, word) VALUES (" + "\"" + ChatBotManager.getChannelNameFromBotType(type, bot) + "\"" + "," + "\"" + args[1] + "\"" + ")");
				ChatBotManager.sendMessage(type, bot, "@" + sender + " badword has been added!");
			} else {
				ChatBotManager.sendMessage(type, bot, "This command isnt available for a file storage based bot!");
			}
		} else
			ChatBotManager.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !addbadword WORD");
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.Moderator;
	}

	@Override
	public boolean hasCooldown() {
		return false;
	}

}
