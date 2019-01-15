package com.mjr.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import com.mjr.ChatBotManager;
import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.MJRBot.StorageType;
import com.mjr.Permissions;
import com.mjr.gameIntegrations.CallOfDuty;
import com.mjr.gameIntegrations.PUBG;
import com.mjr.sql.MySQLConnection;
import com.mjr.storage.Config;
import com.mjr.storage.ConfigMain;
import com.mjr.util.Utilities;

public class CustomCommands {

	public static void getCommand(Object bot, BotType type, String channelName, String command, String sender) throws IOException {
		String state = null;
		String permissionLevel = null;
		String response = null;
		if (MJRBot.storageType == StorageType.File) {
			String filelocation = MJRBot.filePath + channelName + File.separator;
			String filename = command.substring(command.indexOf("!") + 1);
			filename = filename + "Command" + ".properties";
			File filenew = new File(filelocation + filename);
			if (filenew.exists()) {
				FileReader reader = new FileReader(filenew);
				Properties properties = new Properties();
				properties.load(reader);
				state = properties.getProperty("state");
				permissionLevel = properties.getProperty("permissionlevel");
				response = properties.getProperty("response");
			} else if (Config.getSetting("MsgWhenCommandDoesntExist", channelName).equalsIgnoreCase("true"))
				Utilities.sendMessage(type, channelName, "@" + sender + " the command " + command + " doesnt exist!");
		} else {
			ResultSet result = MySQLConnection.executeQueryNoOutput("SELECT * FROM custom_commands WHERE channel = " + "\"" + channelName + "\"" + " AND command_name = " + "\"" + command.substring(command.indexOf("!") + 1) + "\"");
			try {
				if (result == null) {
				} else if (!result.next()) {
				} else {
					result.beforeFirst();
					result.next();
					state = result.getString("state");
					permissionLevel = result.getString("permission_level");
					response = result.getString("response");
				}
			} catch (SQLException e) {
				MJRBot.logErrorMessage(e);
			}
		}

		if (state != null && permissionLevel != null && response != null) {
			if (state.equalsIgnoreCase("true")) {
				boolean allowed = Permissions.hasPermission(bot, type, channelName, sender, permissionLevel);
				if (allowed) {
					response = replaceVariablesWithData(response, type, channelName, sender);
					Utilities.sendMessage(type, channelName, response);
				} else {
					if (Config.getSetting("MsgWhenCommandCantBeUsed", channelName).equalsIgnoreCase("true"))
						Utilities.sendMessage(type, channelName, "@" + sender + " the command " + command + " you dont have access to this command!");
				}
			}
		}
	}

	public static String replaceVariablesWithData(String response, BotType type, String channelName, String sender) {
		response = response.replaceAll("%sender%", sender);
		response = response.replaceAll("%channel%", channelName);
		response = response.replaceAll("%botname%", ConfigMain.getSetting("TwitchUsername"));
		if (type == BotType.Twitch)
			response = response.replaceAll("%subcount%", "" + ChatBotManager.getTwitchBotByChannelName(channelName).subscribers.size());
		else
			response = response.replaceAll("%subcount%", "" + ChatBotManager.getMixerBotByChannelName(channelName).subscribers.size());
		if (type == BotType.Twitch)
			response = response.replaceAll("%viewercount%", "" + ChatBotManager.getTwitchBotByChannelName(channelName).viewers.size());
		else
			response = response.replaceAll("%viewercount%", "" + ChatBotManager.getMixerBotByChannelName(channelName).getViewers().size());
		if (type == BotType.Twitch)
			response = response.replaceAll("%moderatorcount%", "" + ChatBotManager.getTwitchBotByChannelName(channelName).moderators.size());
		else
			response = response.replaceAll("%moderatorcount%", "" + ChatBotManager.getMixerBotByChannelName(channelName).getModerators().size());
		if (type == BotType.Twitch)
			response = response.replaceAll("%vipcount%", "" + ChatBotManager.getTwitchBotByChannelName(channelName).vips.size());
		if (response.contains("%time%")) {
			ZonedDateTime time = ZonedDateTime.now(ZoneId.of(Config.getSetting("SelectedTimeZone", channelName)));
			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-M-yyyy hh:mm:ss a");
			response = response.replaceAll("%time%", format.format(time) + " (Timezone: " + Config.getSetting("SelectedTimeZone", channelName) + ")");
		}
		try {
			String[] parts = response.split(" ");
			if (parts.length != 0) {
				for (int i = 0; i < parts.length; i++) {
					if (parts[i].startsWith("%cod_")) {
						parts[i] = CallOfDuty.replaceVariablesWithData(parts[i]);
					}
					if (parts[i].startsWith("%pubg_")) {
						parts[i] = PUBG.replaceVariablesWithData(parts[i]);
					}
				}
				response = String.join(" ", parts);
			} else {
				if (response.startsWith("%cod_")) {
					response = CallOfDuty.replaceVariablesWithData(response);
				}
				if (response.startsWith("%pubg_")) {
					response = PUBG.replaceVariablesWithData(response);
				}
			}
		} catch (Exception e) {
			response = "Invaild game stats variable format, Error: " + e.getMessage()
					+ (e.getMessage().contains("Missing args in game stats variable") ? "! Possible Formats are: %cod_STATNAMEHERE(game:pathform:user)% OR %cod_STATNAMEHERE(game:pathform:user:type)% or %pubg_STAT(pathform:user:type)%" : "");
		}
		return response;
	}

	public static void addCommand(BotType type, String channelName, String command, String response, String permission) throws FileNotFoundException, IOException {
		if (permission.equalsIgnoreCase("Moderator") || permission.equalsIgnoreCase("Streamer") || permission.equalsIgnoreCase("Streamer") || permission.equalsIgnoreCase("Followers") || permission.equalsIgnoreCase("User")) {
			if (MJRBot.storageType == StorageType.File) {
				String filelocation = MJRBot.filePath + channelName + File.separator;
				String filename = command.toLowerCase() + "Command" + ".properties";
				File filenew = new File(filelocation + filename);
				if (!filenew.exists()) {

					filenew.getParentFile().mkdirs();
					filenew.createNewFile();
					InputStream iStream = new FileInputStream(filelocation + filename);
					Properties properties = new Properties();
					properties.load(iStream);
					properties.store(new FileOutputStream(filelocation + filename), null);

					properties.setProperty("response", response);
					properties.setProperty("state", "true");
					properties.setProperty("permissionlevel", permission);

					properties.store(new FileOutputStream(filelocation + filename), null);
					Utilities.sendMessage(type, channelName, "Custom Command " + command + " has been added!");

				} else
					Utilities.sendMessage(type, channelName, "Custom Command " + command + " already exists!");
			} else {
				ResultSet result = MySQLConnection.executeQueryNoOutput("SELECT * FROM custom_commands WHERE channel = " + "\"" + channelName + "\"" + " AND command_name = " + "\"" + command + "\"");
				try {
					if (!result.next()) {
						MySQLConnection.executeUpdate("INSERT INTO custom_commands(channel, command_name, state, permission_level, response) VALUES (" + "\"" + channelName + "\"" + "," + "\"" + command + "\"" + "," + "\"" + "true" + "\"" + "," + "\""
								+ permission + "\"" + "," + "\"" + response + "\"" + ")");
						Utilities.sendMessage(type, channelName, "Custom Command " + command + " has been added!");
					} else
						Utilities.sendMessage(type, channelName, "Custom Command " + command + " already exists!");
				} catch (SQLException e) {
					MJRBot.logErrorMessage(e);
				}
			}
		} else
			Utilities.sendMessage(type, channelName, "The permission level " + permission + " doesnt  exists!");
	}

	public static void deleteCommand(BotType type, String channelName, String command) throws IOException {
		if (MJRBot.storageType == StorageType.File) {
			String filelocation = MJRBot.filePath + channelName + File.separator;
			String filename = command.toLowerCase() + "Command" + ".properties";
			File filenew = new File(filelocation + filename);
			if (filenew.exists()) {
				filenew.delete();
				Utilities.sendMessage(type, channelName, "Custom Command " + command + " has been removed!");
			} else
				Utilities.sendMessage(type, channelName, command + " doesnt exist!");
		}
		ResultSet result = MySQLConnection.executeQueryNoOutput("SELECT * FROM custom_commands WHERE channel = " + "\"" + channelName + "\"" + " AND command_name = " + "\"" + command + "\"");
		try {
			if (!result.next()) {
				MySQLConnection.executeUpdate("DELETE FROM custom_commands WHERE channel =" + "\"" + channelName + "\"" + " AND command_name = " + "\"" + command + "\"");
				Utilities.sendMessage(type, channelName, "Custom Command " + command + " has been removed!");
			} else {
				Utilities.sendMessage(type, channelName, command + " doesnt exist!");
			}
		} catch (SQLException e) {
			MJRBot.logErrorMessage(e);
		}
	}

	@SuppressWarnings("deprecation")
	public static void changeCommandState(BotType type, String channelName, String command, String state) throws IOException {
		if (MJRBot.storageType == StorageType.File) {
			String filelocation = MJRBot.filePath + channelName + File.separator;
			String filename = command.toLowerCase() + "Command" + ".properties";
			File filenew = new File(filelocation + filename);
			if (filenew.exists()) {
				FileReader reader = new FileReader(filenew);
				Properties properties = new Properties();
				properties.load(reader);
				if (properties.getProperty("state").equalsIgnoreCase("true")) {
					if (state.equalsIgnoreCase("true")) {
						Utilities.sendMessage(type, channelName, command + " is already enabled!");
					} else {
						properties.setProperty("state", state.toLowerCase());
						properties.save(new FileOutputStream(filenew), null);
						Utilities.sendMessage(type, channelName, command + " has been disabled!");
					}
				} else if (properties.getProperty("state").equalsIgnoreCase("false")) {
					if (state.equalsIgnoreCase("false")) {
						Utilities.sendMessage(type, channelName, command + " is already enabled!");
					} else {
						properties.setProperty("state", state.toLowerCase());
						properties.save(new FileOutputStream(filenew), null);
						Utilities.sendMessage(type, channelName, command + " has been enabled!");
					}
				}
			} else
				Utilities.sendMessage(type, channelName, command + " doesnt exist!");
		} else {
			ResultSet result = MySQLConnection.executeQueryNoOutput("SELECT * FROM custom_commands WHERE channel = " + "\"" + channelName + "\"" + " AND command_name = " + "\"" + command + "\"");
			try {
				if (!result.next()) {
					MySQLConnection.executeUpdate("UPDATE custom_commands SET state=" + "\"" + state + "\"" + " WHERE channel = " + "\"" + channelName + "\"" + " AND command_name = " + "\"" + command + "\"");
					if (state.equalsIgnoreCase("false")) {
						Utilities.sendMessage(type, channelName, command + " is already enabled!");
					} else {
						Utilities.sendMessage(type, channelName, command + " has been enabled!");
					}
				} else {
					Utilities.sendMessage(type, channelName, command + " doesnt exist!");
				}
			} catch (SQLException e) {
				MJRBot.logErrorMessage(e);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void changeCommandResponse(BotType type, String channelName, String command, String response) throws IOException {
		if (MJRBot.storageType == StorageType.File) {
			String filelocation = MJRBot.filePath + channelName + File.separator;
			String filename = command.toLowerCase() + "Command" + ".properties";
			File filenew = new File(filelocation + filename);
			if (filenew.exists()) {
				FileReader reader = new FileReader(filenew);
				Properties properties = new Properties();
				properties.load(reader);
				if (properties.getProperty("response").equalsIgnoreCase("response")) {
					Utilities.sendMessage(type, channelName, "The response for " + command + " is already " + response);
				} else {
					properties.setProperty("response", response.toLowerCase());
					properties.save(new FileOutputStream(filenew), null);
					Utilities.sendMessage(type, channelName, "Custom Command " + command + " has been adjusted!");
				}
			} else
				Utilities.sendMessage(type, channelName, command + " doesnt exist!");
		} else {
			ResultSet result = MySQLConnection.executeQueryNoOutput("SELECT * FROM custom_commands WHERE channel = " + "\"" + channelName + "\"" + " AND command_name = " + "\"" + command + "\"");
			try {
				if (!result.next()) {
					MySQLConnection.executeUpdate("UPDATE custom_commands SET response=" + "\"" + response + "\"" + " WHERE channel = " + "\"" + channelName + "\"" + " AND command_name = " + "\"" + command + "\"");
					Utilities.sendMessage(type, channelName, "Custom Command " + command + " has been adjusted!");
				} else {
					Utilities.sendMessage(type, channelName, command + " doesnt exist!");
				}
			} catch (SQLException e) {
				MJRBot.logErrorMessage(e);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void changeCommandPermission(BotType type, String channelName, String command, String permissionLevel) throws IOException {
		if (MJRBot.storageType == StorageType.File) {
			String filelocation = MJRBot.filePath + channelName + File.separator;
			String filename = command.toLowerCase() + "Command" + ".properties";
			File filenew = new File(filelocation + filename);
			if (filenew.exists()) {
				FileReader reader = new FileReader(filenew);
				Properties properties = new Properties();
				properties.load(reader);
				if (properties.getProperty("permissionlevel").equalsIgnoreCase("permissionlevel")) {
					Utilities.sendMessage(type, channelName, "The permissionlevel for " + command + " is already " + permissionLevel);
				} else {
					properties.setProperty("permissionlevel", permissionLevel.toLowerCase());
					properties.save(new FileOutputStream(filenew), null);
					Utilities.sendMessage(type, channelName, "Custom Command " + command + " has been adjusted!");
				}
			} else
				Utilities.sendMessage(type, channelName, command + " doesnt exist!");
		} else {
			ResultSet result = MySQLConnection.executeQueryNoOutput("SELECT * FROM custom_commands WHERE channel = " + "\"" + channelName + "\"" + " AND command_name = " + "\"" + command + "\"");
			try {
				if (!result.next()) {
					MySQLConnection.executeUpdate("UPDATE custom_commands SET permission_level=" + "\"" + permissionLevel + "\"" + " WHERE channel = " + "\"" + channelName + "\"" + " AND command_name = " + "\"" + command + "\"");
					Utilities.sendMessage(type, channelName, "Custom Command " + command + " has been adjusted!");
				} else {
					Utilities.sendMessage(type, channelName, command + " doesnt exist!");
				}
			} catch (SQLException e) {
				MJRBot.logErrorMessage(e);
			}
		}
	}
}
