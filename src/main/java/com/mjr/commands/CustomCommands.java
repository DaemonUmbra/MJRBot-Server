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

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.Permissions;
import com.mjr.gameIntegrations.CallOfDuty;
import com.mjr.sql.MySQLConnection;
import com.mjr.storage.Config;
import com.mjr.storage.ConfigMain;
import com.mjr.util.Utilities;

public class CustomCommands {

	public static void getCommand(Object bot, BotType type, String channelName, String command, String sender) throws IOException {
		String state = null;
		String permissionLevel = null;
		String response = null;
		if (MJRBot.useFileSystem) {
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
					response = response.replaceAll("%sender%", sender);
					response = response.replaceAll("%channel%", channelName);
					response = response.replaceAll("%botname%", ConfigMain.getSetting("TwitchUsername"));
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
									if(parts[i].startsWith("%cod_STATNAMEHERE") || parts[i].startsWith("%cod_("))
										throw new IndexOutOfBoundsException("Invalid stat name!");
									String stat = parts[i].substring(parts[i].indexOf("%cod_") + 5);
									stat = stat.substring(0, stat.indexOf("("));

									String temp = parts[i].substring(parts[i].indexOf(stat));
									temp = temp.substring(temp.indexOf("(") + 1);
									temp = temp.substring(0, temp.indexOf(")"));

									String[] codParts = temp.split(":");
									if (codParts.length != 3 && codParts.length != 4)
										throw new IndexOutOfBoundsException("Missing args in cod stats variable!");

									if (!codParts[0].equalsIgnoreCase("bo4"))
										throw new IndexOutOfBoundsException("Invalid game!");

									if (!codParts[1].equalsIgnoreCase("xbl") && !codParts[1].equalsIgnoreCase("psn") && !codParts[1].equalsIgnoreCase("battle"))
										throw new IndexOutOfBoundsException("Invalid pathform!");

									if (codParts.length == 3) {
										parts[i] = CallOfDuty.getProfileAllStat(stat, codParts[0], codParts[1], codParts[2].replaceAll("#", "%23"));
									}
									else if (codParts.length == 4) {
										if (!codParts[3].equalsIgnoreCase("mp") && !codParts[3].equalsIgnoreCase("zombies") && !codParts[3].equalsIgnoreCase("blackout"))
											throw new IndexOutOfBoundsException("Invalid game mode type!");
										parts[i] = CallOfDuty.getGameTypeAllStat(stat, codParts[0], codParts[1], codParts[2].replaceAll("#", "%23"), codParts[3]);
									}
								}
							}
							response = String.join(" ", parts);
						} else {
							if (response.startsWith("%cod_")) {
								if(response.startsWith("%cod_STATNAMEHERE") || response.startsWith("%cod_("))
									throw new IndexOutOfBoundsException("Invalid stat name!");
								String stat = response.substring(response.indexOf("%cod_") + 5);
								stat = stat.substring(0, stat.indexOf("("));

								String temp = response.substring(response.indexOf(stat));
								temp = temp.substring(temp.indexOf("(") + 1);
								temp = temp.substring(0, temp.indexOf(")"));

								String[] codParts = temp.split(":");
								if (codParts.length != 3 && codParts.length != 4)
									throw new IndexOutOfBoundsException("Missing args in cod stats variable!");

								if (!codParts[0].equalsIgnoreCase("bo4"))
									throw new IndexOutOfBoundsException("Invalid game!");

								if (!codParts[1].equalsIgnoreCase("xbl") && !codParts[1].equalsIgnoreCase("psn") && !codParts[1].equalsIgnoreCase("battle"))
									throw new IndexOutOfBoundsException("Invalid pathform!");

								if (codParts.length == 3) {
									response = CallOfDuty.getProfileAllStat(stat, codParts[0], codParts[1], codParts[2].replaceAll("#", "%23"));
								}
								else if (codParts.length == 4) {
									if (!codParts[3].equalsIgnoreCase("mp") && !codParts[3].equalsIgnoreCase("zombies") && !codParts[3].equalsIgnoreCase("blackout"))
										throw new IndexOutOfBoundsException("Invalid game mode type!");
									response = CallOfDuty.getGameTypeAllStat(stat, codParts[0], codParts[1], codParts[2].replaceAll("#", "%23"), codParts[3]);
								}
							}
						}
					} catch (Exception e) {
						response = "Invaild cod stats variable format, Error: " + e.getMessage() + (e.getMessage().contains("Missing args in cod stats variable") ? "! Format is: %cod_STATNAMEHERE(game:pathform:user)%" : "");
					}
					Utilities.sendMessage(type, channelName, response);
				} else {
					if (Config.getSetting("MsgWhenCommandCantBeUsed", channelName).equalsIgnoreCase("true"))
						Utilities.sendMessage(type, channelName, "@" + sender + " the command " + command + " you dont have access to this command!");
				}
			}
		}
	}

	public static void addCommand(BotType type, String channelName, String command, String response, String permission) throws FileNotFoundException, IOException {
		if (permission.equalsIgnoreCase("Moderator") || permission.equalsIgnoreCase("Streamer") || permission.equalsIgnoreCase("User")) {
			if (MJRBot.useFileSystem) {
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
		if (MJRBot.useFileSystem) {
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
		if (MJRBot.useFileSystem) {
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
		if (MJRBot.useFileSystem) {
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
		if (MJRBot.useFileSystem) {
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
