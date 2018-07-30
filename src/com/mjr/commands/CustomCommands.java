package com.mjr.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.Permissions;
import com.mjr.Utilities;
import com.mjr.files.Config;

public class CustomCommands { // TODO: Add Database Storage Function

    public static void getCommand(Object bot, BotType type, String channelName, String command, String sender) throws IOException {
	String filelocation = MJRBot.filePath + channelName + File.separator;
	String filename = command.substring(command.indexOf("!") + 1);
	filename = filename + "Command" + ".properties";
	File filenew = new File(filelocation + filename);
	if (filenew.exists()) {
	    FileReader reader = new FileReader(filenew);
	    Properties properties = new Properties();
	    properties.load(reader);
	    if (properties.getProperty("state").equalsIgnoreCase("true")) {
		String permission = properties.getProperty("permissionlevel");
		boolean allowed = Permissions.hasPermission(bot, type, channelName, sender, permission);
		if (allowed) {
		    String response = properties.getProperty("response");
		    response = response.replaceAll("%sender%", sender);
		    response = response.replaceAll("%channel%", channelName);
		    response = response.replaceAll("%botname%", channelName);
		    if(response.contains("%time%")) {
			String currentTime = Instant.now().toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			Date date = null;

			try {
			    date = format.parse(currentTime);
			} catch (ParseException e) {
			    e.printStackTrace();
			}
			response = response.replaceAll("%time%", date.toString());
		    }
		    Utilities.sendMessage(type, channelName, response);
		} else {
		    if (Config.getSetting("MsgWhenCommandCantBeUsed", channelName).equalsIgnoreCase("true"))
			Utilities.sendMessage(type, channelName,
				"@" + sender + " the command " + command + " you dont have access to this command!");
		}
	    }

	} else if (type == BotType.Twitch)
	    if (Config.getSetting("MsgWhenCommandDoesntExist", channelName).equalsIgnoreCase("true"))
		Utilities.sendMessage(type, channelName, "@" + sender + " the command " + command + " doesnt exist!");
	    else if (Config.getSetting("MsgWhenCommandDoesntExist", channelName).equalsIgnoreCase("true"))
		Utilities.sendMessage(type, channelName, "@" + sender + " the command " + command + " doesnt exist!");
    }

    public static void addCommand(BotType type, String channelName, String command, String response, String permission)
	    throws FileNotFoundException, IOException {
	String filelocation = MJRBot.filePath + channelName + File.separator;
	String filename = command.toLowerCase() + "Command" + ".properties";
	File filenew = new File(filelocation + filename);
	if (!filenew.exists()) {
	    if (permission.equalsIgnoreCase("Moderator") || permission.equalsIgnoreCase("Streamer")
		    || permission.equalsIgnoreCase("User")) {
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
		Utilities.sendMessage(type, channelName, "Command " + command + " has been added!");
	    } else
		Utilities.sendMessage(type, channelName, "The permission level " + permission + " doesnt  exists!");
	} else
	    Utilities.sendMessage(type, channelName, "Command " + command + " already exists!");
    }

    public static void deleteCommand(BotType type, String channelName, String command) throws IOException {
	String filelocation = MJRBot.filePath + channelName + File.separator;
	String filename = command.toLowerCase() + "Command" + ".properties";
	File filenew = new File(filelocation + filename);
	if (filenew.exists()) {
	    filenew.delete();
	    Utilities.sendMessage(type, channelName, "Command " + command + " has been removed!");
	} else
	    Utilities.sendMessage(type, channelName, command + " doesnt exist!");
    }

    @SuppressWarnings("deprecation")
    public static void changeCommandState(BotType type, String channelName, String command, String state) throws IOException {
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
    }

    @SuppressWarnings("deprecation")
    public static void changeCommandResponse(BotType type, String channelName, String command, String response) throws IOException {
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
		Utilities.sendMessage(type, channelName, command + " has been adjusted!");
	    }
	} else
	    Utilities.sendMessage(type, channelName, command + " doesnt exist!");
    }
    
    @SuppressWarnings("deprecation")
    public static void changeCommandPermission(BotType type, String channelName, String command, String response) throws IOException {
	String filelocation = MJRBot.filePath + channelName + File.separator;
	String filename = command.toLowerCase() + "Command" + ".properties";
	File filenew = new File(filelocation + filename);
	if (filenew.exists()) {
	    FileReader reader = new FileReader(filenew);
	    Properties properties = new Properties();
	    properties.load(reader);
	    if (properties.getProperty("permissionlevel").equalsIgnoreCase("permissionlevel")) {
		Utilities.sendMessage(type, channelName, "The permissionlevel for " + command + " is already " + response);
	    } else {
		properties.setProperty("permissionlevel", response.toLowerCase());
		properties.save(new FileOutputStream(filenew), null);
		Utilities.sendMessage(type, channelName, command + " has been adjusted!");
	    }
	} else
	    Utilities.sendMessage(type, channelName, command + " doesnt exist!");
    }
}
