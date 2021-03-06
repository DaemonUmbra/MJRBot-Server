package com.mjr.mjrbot.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.bases.MixerBot;
import com.mjr.mjrbot.bots.bases.TwitchBot;
import com.mjr.mjrbot.storage.sql.MySQLConnection;
import com.mjr.mjrbot.util.MJRBotUtilities;

public class ModerationActionsLogManager extends FileBase {
	public static String fileName = "Moderation_Actions_Log.txt";

	public static void addEvent(BotType type, Object bot, String user, String reason, String message) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		if (MJRBot.storageType == StorageType.File) {
			File file = null;
			if (type == BotType.Twitch)
				file = loadFile(((TwitchBot) bot).getChannelID(), fileName);
			else if (type == BotType.Mixer)
				file = loadFile(((MixerBot) bot).getChannelName(), fileName);
			Path filePath = Paths.get(file.getPath());
			try {
				Files.write(filePath, ("\n" + dateFormat.format(date) + user + ": " + reason + " Message: " + message + ";").getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e) {
				MJRBotUtilities.logErrorMessage(e);
			}
		} else {
			MySQLConnection.executeUpdate("INSERT INTO moderation_actions(channel, time, user, reason, message, platform) VALUES (" + "\"" + ChatBotManager.getChannelNameFromBotType(type, bot) + "\"" + "," + "\"" + dateFormat.format(date) + "\""
					+ "," + "\"" + user + "\"" + "," + "\"" + reason + "\"" + "," + "\"" + message + "\"" + "," + "\"" + type.getTypeName() + "\"" + ")");
		}
	}

}
