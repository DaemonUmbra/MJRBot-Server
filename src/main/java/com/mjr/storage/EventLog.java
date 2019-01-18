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

import com.mjr.MJRBot;
import com.mjr.MJRBot.StorageType;
import com.mjr.sql.MySQLConnection;

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

	public static void addEvent(String channelName, String user, String eventMessage, EventType type) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			if (MJRBot.storageType == StorageType.File) {
				File file = loadFile(channelName, fileName);
				Path filePath = Paths.get(file.getPath());
				try {
					Files.write(filePath, ("\n" + dateFormat.format(date) + user + ": " + eventMessage + ";").getBytes(), StandardOpenOption.APPEND);
				} catch (IOException e) {
					MJRBot.logErrorMessage(e);
				}
			} else {
				MySQLConnection.executeUpdate("INSERT INTO events(channel, time, user, type, event_message) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + dateFormat.format(date) + "\"" + "," + "\"" + user + "\"" + "," + "\"" + type.getName()
						+ "\"" + "," + "\"" + eventMessage + "\"" + ")");
				if (Config.getSetting("DiscordEnabled", channelName).equalsIgnoreCase("true")) {
					ResultSet channel_id = MySQLConnection.executeQuery("SELECT event_log_channel_id FROM discord_info WHERE channel = '" + channelName + "'");
					if (channel_id.next()) {
						if (!channel_id.getString("event_log_channel_id").equals("")) {
							Snowflake channel = Snowflake.of(Long.parseLong(channel_id.getString("event_log_channel_id")));
							MJRBot.bot.sendMessage(MJRBot.bot.getChannelByID(channel), "[" + type.getName() + "]" + " **" + user + "** : " + eventMessage);
						}
					}

				}
			}
		} catch (Exception e) {
			MJRBot.logErrorMessage(e);
		}
	}

}
