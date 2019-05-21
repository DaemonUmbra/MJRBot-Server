package com.mjr.mjrbot.storage;

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

import com.mjr.discordframework.DiscordBotUtilities;
import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.storage.sql.MySQLConnection;
import com.mjr.mjrbot.util.MJRBotUtilities;

import discord4j.core.object.util.Snowflake;

public class EventLogManager extends FileBase {
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
				if (type == BotType.Twitch)
					file = loadFile(((TwitchBot) bot).getChannelID(), fileName);
				else if (type == BotType.Mixer)
					file = loadFile(((MixerBot) bot).getChannelName(), fileName);
				Path filePath = Paths.get(file.getPath());
				try {
					Files.write(filePath, ("\n" + dateFormat.format(date) + user + ": " + eventMessage + ";").getBytes(), StandardOpenOption.APPEND);
				} catch (IOException e) {
					MJRBotUtilities.logErrorMessage(e);
				}
			} else {
				MySQLConnection.executeUpdate("INSERT INTO events(channel, time, user, type, event_message, platform) VALUES (" + "\"" + ChatBotManager.getChannelNameFromBotType(type, bot) + "\"" + "," + "\"" + dateFormat.format(date) + "\"" + ","
						+ "\"" + user + "\"" + "," + "\"" + eventType.getName() + "\"" + "," + "\"" + eventMessage + "\"" + "," + "\"" + type.getTypeName() + "\"" + ")");
				if (ChannelConfigManager.getSetting("DiscordEnabled", type, bot).equalsIgnoreCase("true")) {
					ResultSet channel_id = MySQLConnection.executeQuery("SELECT event_log_channel_id FROM discord_info WHERE channel = '" + ChatBotManager.getChannelNameFromBotType(type, bot) + "'");
					if (channel_id.next()) {
						if (!channel_id.getString("event_log_channel_id").equals("")) {
							Snowflake channel = Snowflake.of(Long.parseLong(channel_id.getString("event_log_channel_id")));
							MJRBot.getDiscordBot().sendMessage(DiscordBotUtilities.getChannelByID(MJRBot.getDiscordBot().getClient(), channel), "[" + type.getTypeName() + "]" + "[" + eventType.getName() + "]" + " **" + user + "** : " + eventMessage);
						}
					}

				}
			}
		} catch (Exception e) {
			MJRBotUtilities.logErrorMessage(e);
		}
	}

}
