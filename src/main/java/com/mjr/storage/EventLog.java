package com.mjr.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.MJRBot.StorageType;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.sql.MySQLConnection;
import com.mjr.util.Utilities;

import discord4j.core.object.util.Snowflake;

public class EventLog extends FileBase {
	public static String fileName = "Event_Log.txt";

	public enum EventType {
		Points("Points"), Games("Games"), Rank("Rank"), Commands("Commands"), User("User"), Quote("Quote"), CustomCommands("CustomCommands"), Sub("Sub"), Bits("Bits");

		private final String type;

		EventType(String type) {
			this.type = type;
		}

		public String getName() {
			return type;
		}
	}

	public static void addEvent(BotType type, Object bot, String user, String eventMessage, EventType eventType) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			if (MJRBot.storageType == StorageType.File) {
				File file = null;
				if(type == BotType.Twitch)
					file = loadFile(((TwitchBot)bot).channelID, fileName);
				else if(type == BotType.Mixer)
					file = loadFile(((MixerBot)bot).channelName, fileName);
				Path filePath = Paths.get(file.getPath());
				try {
					Files.write(filePath, ("\n" + dateFormat.format(date) + user + ": " + eventMessage + ";").getBytes(), StandardOpenOption.APPEND);
				} catch (IOException e) {
					MJRBot.logErrorMessage(e);
				}
			} else {
				MySQLConnection.executeUpdate("INSERT INTO events(channel, time, user, type, event_message, platform) VALUES (" + "\"" + Utilities.getChannelNameFromBotType(type, bot) + "\"" + "," + "\"" + dateFormat.format(date) + "\"" + "," + "\"" + user + "\"" + "," + "\"" + eventType.getName()
						+ "\"" + "," + "\"" + eventMessage + "\"" + "," + "\"" + type.getTypeName() + "\"" + ")");
				if (Config.getSetting("DiscordEnabled", type, bot).equalsIgnoreCase("true")) {
					ResultSet channel_id = MySQLConnection.executeQuery("SELECT event_log_channel_id FROM discord_info WHERE channel = '" + Utilities.getChannelNameFromBotType(type, bot) + "'");
					if (channel_id.next()) {
						if (!channel_id.getString("event_log_channel_id").equals("")) {
							Snowflake channel = Snowflake.of(Long.parseLong(channel_id.getString("event_log_channel_id")));
							MJRBot.bot.sendMessage(MJRBot.bot.getChannelByID(channel), "[" + eventType.getName() + "]" + " **" + user + "** : " + eventMessage);
						}
					}

				}
			}
		} catch (Exception e) {
			MJRBot.logErrorMessage(e);
		}
	}

}
