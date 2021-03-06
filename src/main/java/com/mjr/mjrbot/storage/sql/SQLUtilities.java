package com.mjr.mjrbot.storage.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.util.MJRBotUtilities;

public class SQLUtilities {

	public static void createDatabaseStructure() {
		MySQLConnection.executeUpdate("CREATE DATABASE mjrbot");
		MySQLConnection.executeUpdate("CREATE TABLE mjrbot.channels (id int(50) not null auto_increment primary key,name varchar(35), bot_type varchar(12), twitch_channel_id int(20))");
		if (MJRBot.storageType == StorageType.Database) {
			MySQLConnection.executeUpdate("CREATE TABLE mjrbot.analytics (id int(50) not null auto_increment primary key, name varchar(35), value int(50))");

			MySQLConnection.executeUpdate("CREATE TABLE mjrbot.points (id int(50) not null auto_increment primary key, name varchar(35), mixer_channel varchar(35), amount int(50), twitch_channel_id int(20))");
			MySQLConnection.executeUpdate("CREATE TABLE mjrbot.ranks (id int(50) not null auto_increment primary key, name varchar(35), mixer_channel varchar(35), rank varchar(12), twitch_channel_id int(20))");
			MySQLConnection.executeUpdate("CREATE TABLE mjrbot.config (id int(50) not null auto_increment primary key, mixer_channel varchar(35), setting varchar(35), value varchar(128), twitch_channel_id int(20))");
			MySQLConnection.executeUpdate("CREATE TABLE mjrbot.quotes (id int(50) not null auto_increment primary key, mixer_channel varchar(35), quote varchar(128), twitch_channel_id int(20))");
			MySQLConnection.executeUpdate("CREATE TABLE mjrbot.badwords (id int(50) not null auto_increment primary key, mixer_channel varchar(35), word varchar(35), twitch_channel_id int(20))");
			MySQLConnection.executeUpdate("CREATE TABLE mjrbot.custom_commands (id int(50) not null auto_increment primary key, mixer_channel varchar(35), command_name varchar(35), state varchar(6), permission_level varchar(16), response varchar(500), twitch_channel_id int(20))");

			MySQLConnection.executeUpdate("CREATE TABLE mjrbot.tokens (id int(50) not null auto_increment primary key, channel varchar(35), access_token varchar(100), refresh_token varchar(100), platform varchar(10), channel_id int(20), expires_in int(12), last_login datetime");
			MySQLConnection.executeUpdate("CREATE TABLE mjrbot.moderation_actions (id int(50) not null auto_increment primary key, channel varchar(35), time varchar(35), user varchar(35), reason varchar(128), message varchar(500), platform varchar(10))");
			MySQLConnection.executeUpdate("CREATE TABLE mjrbot.events (id int(50) not null auto_increment primary key, channel varchar(35), time varchar(35), user varchar(35), type varchar(35), event_message varchar(256), platform varchar(10))");

			MySQLConnection.executeUpdate("CREATE TABLE mjrbot.discord_info (id int(50) not null auto_increment primary key, channel varchar(35), guild_id varchar(100), cross_link_channel_id varchar(100), event_log_channel_id varchar(100))");
			MySQLConnection.executeUpdate("CREATE TABLE mjrbot.channel_cross_link (id int(50) not null auto_increment primary key, mixer_channel varchar(35), twitch_channel_id int(20))");
		}
	}

	public static boolean doesDataBaseExist(String name) {
		ResultSet resultSet;
		try {
			resultSet = MySQLConnection.getConnection().getMetaData().getCatalogs();
			while (resultSet.next()) {
				String databaseName = resultSet.getString(1);
				if (databaseName.equals(name)) {
					return true;
				}
			}
			resultSet.close();
		} catch (SQLException e) {
			MJRBotUtilities.logErrorMessage(e);
		}

		return false;
	}

	public static HashMap<Integer, String> getChannelsTwitch() {
		HashMap<Integer, String> channels = new HashMap<Integer, String>();
		ResultSet result = MySQLConnection.executeQuery("SELECT twitch_channel_id, bot_type FROM channels WHERE bot_type = 'Twitch'");
		try {
			while (result.next()) {
				channels.put(result.getInt("twitch_channel_id"), result.getString("bot_type"));
			}
		} catch (SQLException e) {
			MJRBotUtilities.logErrorMessage(e);
		}
		try {
			result.close();
		} catch (SQLException e) {
			MJRBotUtilities.logErrorMessage(e);
		}
		return channels;
	}

	public static HashMap<String, String> getChannelsMixer() {
		HashMap<String, String> channels = new HashMap<String, String>();
		ResultSet result = MySQLConnection.executeQuery("SELECT name, bot_type FROM channels WHERE bot_type = 'Mixer'");
		try {
			while (result.next()) {
				channels.put(result.getString("name"), result.getString("bot_type"));
			}
		} catch (SQLException e) {
			MJRBotUtilities.logErrorMessage(e);
		}
		try {
			result.close();
		} catch (SQLException e) {
			MJRBotUtilities.logErrorMessage(e);
		}
		return channels;
	}
}
