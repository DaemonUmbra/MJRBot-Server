package com.mjr.commands.defaultCommands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.mjr.ConsoleUtil;
import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.Permissions;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;

public class QuoteCommand extends Command {
    public static String filename = "Quotes.txt";
    public static File file;

    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (Config.getSetting("Quotes").equalsIgnoreCase("true")) {
	    if (args.length == 1) {
		if (type == BotType.Twitch)
		    file = new File(MJRBot.filePath + MJRBot.getTwitchBotByChannelName(channel).getChannel().substring(1) + File.separator
			    + filename);
		else {
		    file = new File(MJRBot.filePath + MJRBot.getMixerBotByChannelName(channel) + File.separator + filename);
		}
		String token1 = "";
		Scanner inFile1 = null;
		try {
		    inFile1 = new Scanner(file).useDelimiter(";\\s*");
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		}
		List<String> temps = new ArrayList<String>();
		while (inFile1.hasNext()) {
		    token1 = inFile1.next();
		    temps.add(token1);
		}
		inFile1.close();
		String[] tempsArray = temps.toArray(new String[0]);
		Random rand = new Random();
		((TwitchBot) bot).sendMessage(MJRBot.getTwitchBotByChannelName(channel).getChannel(),
			tempsArray[rand.nextInt(tempsArray.length)]);
	    } else if (args.length == 3 && args[1].equalsIgnoreCase("get")
		    && Permissions.hasPermission(type, channel, sender, PermissionLevel.Moderator.getName())) {
		String token1 = "";
		Scanner inFile1 = null;
		try {
		    inFile1 = new Scanner(file).useDelimiter(";\\s*");
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		}
		List<String> temps = new ArrayList<String>();
		while (inFile1.hasNext()) {
		    token1 = inFile1.next();
		    temps.add(token1);
		}
		inFile1.close();
		String[] tempsArray = temps.toArray(new String[0]);
		if (Integer.parseInt(args[2]) < tempsArray.length)
		    ((TwitchBot) bot).sendMessage(MJRBot.getTwitchBotByChannelName(channel).getChannel(),
			    "Quote " + args[2] + " is: " + tempsArray[Integer.parseInt(args[2])]);
		else
		    ((TwitchBot) bot).sendMessage(MJRBot.getTwitchBotByChannelName(channel).getChannel(),
			    "Quote " + args[1] + " doesnt exist!");
	    } else if (args.length > 1 && args[1].equalsIgnoreCase("add")
		    && Permissions.hasPermission(type, channel, sender, PermissionLevel.Moderator.getName())) {
		if (!message.contains("@")) {
		    Utilities.sendMessage(type, channel,
			    sender + " your quote must be in the format as follows: !quote add <message> @<Name>");
		} else if (message.contains("\"") || message.contains("'")) {
		    Utilities.sendMessage(type, channel, sender + " your quote must not contain \" or '");
		} else {
		    String fileTemp = file.getPath();
		    Path filePath = Paths.get(fileTemp);
		    if (!Files.exists(filePath)) {
			try {
			    Files.createFile(filePath);
			} catch (IOException e) {
			    e.printStackTrace();
			}
		    }
		    message = message.substring(11);
		    message = "'" + message;
		    message = message.replace(" @", "' ");
		    try {
			Files.write(filePath, ("\n" + message + " " + Calendar.getInstance().get(Calendar.YEAR) + ";").getBytes(),
				StandardOpenOption.APPEND);
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		    Utilities.sendMessage(type, channel, sender + " your quote has been added!");
		    ConsoleUtil.TextToConsole(type, channel, "A new Quote has been added by " + sender + ". The quote message is " + message, "Bot", null);
		}
	    } else if (args.length == 2 && args[1].equalsIgnoreCase("help")) {
		((TwitchBot) bot).sendMessage(MJRBot.getTwitchBotByChannelName(channel).getChannel(),
			"Quote Help: You can use the following commands: !quote or !quote add <message> @<username> or !quote get <number>");
	    }
	}
    }

    @Override
    public String getPermissionLevel() {
	return Permissions.PermissionLevel.User.getName();
    }
}
