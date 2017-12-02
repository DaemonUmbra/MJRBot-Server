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

import com.mjr.ConsoleUtli;
import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.Permissions;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.files.Config;

public class QuoteCommand extends Command {
    public static String filename = "Quotes.txt";
    public static String file;

    public QuoteCommand() {
	if (MJRBot.getTwitchBot() != null)
	    file = "C:" + File.separator + "MJRBot" + File.separator + MJRBot.getTwitchBot().getChannel().substring(1) + File.separator
		    + filename;
	else {
	    file = "C:" + File.separator + "MJRBot" + File.separator + MJRBot.getChannel() + File.separator + filename;
	}
    }

    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Quotes").equalsIgnoreCase("true")) {
	    if (args.length == 1) {
		String token1 = "";
		Scanner inFile1 = null;
		try {
		    inFile1 = new Scanner(new File(file)).useDelimiter(";\\s*");
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
		((TwitchBot) bot).sendMessage(MJRBot.getTwitchBot().getChannel(), tempsArray[rand.nextInt(tempsArray.length)]);
	    } else if (args.length == 3 && args[1].equalsIgnoreCase("get")
		    && Permissions.getPermissionLevel(sender).equalsIgnoreCase("Moderator")) {
		String token1 = "";
		Scanner inFile1 = null;
		try {
		    inFile1 = new Scanner(new File(file)).useDelimiter(";\\s*");
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
		    ((TwitchBot) bot).sendMessage(MJRBot.getTwitchBot().getChannel(),
			    "Quote " + args[1] + " is: " + tempsArray[Integer.parseInt(args[2])]);
		else
		    ((TwitchBot) bot).sendMessage(MJRBot.getTwitchBot().getChannel(), "Quote " + args[1] + " doesnt exist!");
	    } else if (args.length > 1 && args[1].equalsIgnoreCase("add")
		    && Permissions.getPermissionLevel(sender).equalsIgnoreCase("Moderator")) {
		if (!message.contains("@")) {
		    String endMessage = sender + " your quote must be in the format as follows: !quote <message> @<Name>";
		    if (MJRBot.getTwitchBot() != null)
			((TwitchBot) bot).MessageToChat(endMessage);
		    else
			((MixerBot) bot).sendMessage(endMessage);
		} else if (message.contains("\"") || message.contains("'")) {
		    String endMessage = sender + " your quote must not contain \" or '";
		    if (MJRBot.getTwitchBot() != null)
			((TwitchBot) bot).MessageToChat(endMessage);
		    else
			((MixerBot) bot).sendMessage(endMessage);
		} else {
		    Path filePath = Paths.get(file);
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
		    String endMessage = sender + " your quote has been added!";
		    if (MJRBot.getTwitchBot() != null)
			((TwitchBot) bot).MessageToChat(endMessage);
		    else
			((MixerBot) bot).sendMessage(endMessage);
		    ConsoleUtli.TextToConsole("A new Quote has been added by " + sender + ". The quote message is " + message, "Bot", null);
		}
	    } else if (args.length == 2 && args[1].equalsIgnoreCase("help")) {
		((TwitchBot) bot)
			.sendMessage(MJRBot.getTwitchBot().getChannel(),
				"Quote Help: You can use the following commands: !quote or !quote add <message> @<username> or !quote get <number>");
	    }
	}
    }
}
