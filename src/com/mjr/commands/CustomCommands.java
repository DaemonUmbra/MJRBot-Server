package com.mjr.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.Permissions;
import com.mjr.Utilities;
import com.mjr.files.Config;

public class CustomCommands {
    public static String filelocation;
    public static Properties properties = new Properties();
    protected static InputStream iStream;

    public static void getCommand(Object bot, BotType type, String channelName, String command, String sender) throws IOException {
	if (type == BotType.Twitch)
	    filelocation = MJRBot.filePath + MJRBot.getTwitchBotByChannelName(channelName) + File.separator;
	else {
	    filelocation = MJRBot.filePath + MJRBot.getMixerBotByChannelName(channelName) + File.separator;
	}
	String filename = command.substring(command.indexOf("!") + 1);
	filename = filename + "Command" + ".properties";
	File filenew = new File(filelocation + filename);
	if (filenew.exists()) {
	    FileReader reader = new FileReader(filenew);
	    properties.load(reader);
	    if (properties.getProperty("state").equalsIgnoreCase("true")) {
		String permission = properties.getProperty("permissionlevel");
		boolean allowed = Permissions.hasPermission(bot, type, channelName, sender, permission);
		if (allowed) {
		    Utilities.sendMessage(type, channelName, properties.getProperty("response"));
		} else {
		    if (Config.getSetting("MsgWhenCommandCantBeUsed").equalsIgnoreCase("true"))
			Utilities.sendMessage(type, channelName,
				"@" + sender + " the command " + command + " you dont have access to this command!");
		}
	    }

	} else if (type == BotType.Twitch)
	    if (Config.getSetting("MsgWhenCommandDoesntExist").equalsIgnoreCase("true"))
		Utilities.sendMessage(type, channelName, "@" + sender + " the command " + command + " doesnt exist!");
	    else if (Config.getSetting("MsgWhenCommandDoesntExist").equalsIgnoreCase("true"))
		Utilities.sendMessage(type, channelName, "@" + sender + " the command " + command + " doesnt exist!");
    }

    public static void AddCommand(BotType type, String channelName, String command, String response, String permission)
	    throws FileNotFoundException, IOException {
	if (type == BotType.Twitch)
	    filelocation = MJRBot.filePath + MJRBot.getTwitchBotByChannelName(channelName) + File.separator;
	else {
	    filelocation = MJRBot.filePath + MJRBot.getMixerBotByChannelName(channelName) + File.separator;
	}
	String filename = command.toLowerCase() + "Command" + ".properties";
	File filenew = new File(filelocation + filename);
	if (!filenew.exists()) {
	    if (permission.equalsIgnoreCase("Moderator") || permission.equalsIgnoreCase("Streamer")
		    || permission.equalsIgnoreCase("User")) {
		filenew.getParentFile().mkdirs();
		filenew.createNewFile();
		iStream = new FileInputStream(filelocation + filename);
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

    public static void RemoveCommand(BotType type, String channelName, String command) throws IOException {
	if (type == BotType.Twitch)
	    filelocation = MJRBot.filePath + MJRBot.getTwitchBotByChannelName(channelName) + File.separator;
	else {
	    filelocation = MJRBot.filePath + MJRBot.getMixerBotByChannelName(channelName) + File.separator;
	}
	String filename = command.toLowerCase() + "Command" + ".properties";
	File filenew = new File(filelocation + filename);
	if (filenew.exists()) {
	    filenew.delete();
	    Utilities.sendMessage(type, channelName, "Command " + command + " has been removed!");
	} else
	    Utilities.sendMessage(type, channelName, command + " doesnt exist!");
    }

    @SuppressWarnings("deprecation")
    public static void ChangeStateCommand(BotType type, String channelName, String command, String state) throws IOException {
	if (type == BotType.Twitch)
	    filelocation = MJRBot.filePath + MJRBot.getTwitchBotByChannelName(channelName) + File.separator;
	else {
	    filelocation = MJRBot.filePath + MJRBot.getMixerBotByChannelName(channelName) + File.separator;
	}
	String filename = command.toLowerCase() + "Command" + ".properties";
	File filenew = new File(filelocation + filename);
	if (filenew.exists()) {
	    FileReader reader = new FileReader(filenew);
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
    public static void ChangeResponseCommand(BotType type, String channelName, String command, String response) throws IOException {
	if (type == BotType.Twitch)
	    filelocation = MJRBot.filePath + MJRBot.getTwitchBotByChannelName(channelName) + File.separator;
	else {
	    filelocation = MJRBot.filePath + MJRBot.getMixerBotByChannelName(channelName) + File.separator;
	}
	String filename = command.toLowerCase() + "Command" + ".properties";
	File filenew = new File(filelocation + filename);
	if (filenew.exists()) {
	    FileReader reader = new FileReader(filenew);
	    properties.load(reader);
	    if (properties.getProperty("response").equalsIgnoreCase("response")) {
		Utilities.sendMessage(type, channelName, "The response for " + command + " is already " + response);
	    } else {
		properties.setProperty("response", response.toLowerCase());
		properties.save(new FileOutputStream(filenew), null);
		Utilities.sendMessage(type, channelName, command + " has been changed!");
	    }
	} else
	    Utilities.sendMessage(type, channelName, command + " doesnt exist!");
    }

    public static void ChangeComanndResponse(BotType type, String channelName, String command, String response)
	    throws FileNotFoundException, IOException {
	if (type == BotType.Twitch)
	    filelocation = MJRBot.filePath + MJRBot.getTwitchBotByChannelName(channelName) + File.separator;
	else {
	    filelocation = MJRBot.filePath + MJRBot.getMixerBotByChannelName(channelName) + File.separator;
	}
	String filename = command.toLowerCase() + "Command" + ".properties";
	File filenew = new File(filelocation + filename);
	if (!filenew.exists()) {
	    filenew.getParentFile().mkdirs();
	    filenew.createNewFile();
	    iStream = new FileInputStream(filelocation + filename);
	    properties.load(iStream);
	    properties.store(new FileOutputStream(filelocation + filename), null);

	    properties.setProperty("response", response);
	    properties.setProperty("state", "true");
	    properties.store(new FileOutputStream(filelocation + filename), null);
	    Utilities.sendMessage(type, channelName, "Command " + command + " has been added!");
	} else
	    Utilities.sendMessage(type, channelName, "Command " + command + " already exists!");
    }
}
