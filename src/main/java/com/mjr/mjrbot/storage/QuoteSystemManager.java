package com.mjr.mjrbot.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.defaultCommands.QuoteCommand;
import com.mjr.mjrbot.storage.sql.MySQLConnection;
import com.mjr.mjrbot.util.MJRBotUtilities;

@SuppressWarnings("resource")
public class QuoteSystemManager {

	public static String getQuote(BotType type, Object bot, File file, int number) {
		List<String> quotes = getAllQuotes(type, bot, file);
		if (number < quotes.size())
			return quotes.get(number);
		else
			return null;
	}

	public static String getRandomQuote(BotType type, Object bot, File file) {
		Random rand = new Random();
		List<String> quotes = getAllQuotes(type, bot, file);
		return quotes.get(rand.nextInt(quotes.size()));
	}

	public static List<String> getAllQuotes(BotType type, Object bot, File file) {
		if (MJRBot.storageType == StorageType.File) {
			String token1 = "";
			Scanner inFile1 = null;
			try {
				inFile1 = new Scanner(file).useDelimiter(";\\s*");
			} catch (FileNotFoundException e) {
				MJRBotUtilities.logErrorMessage(e);
			}
			List<String> temps = new ArrayList<String>();
			while (inFile1.hasNext()) {
				token1 = inFile1.next();
				temps.add(token1);
			}
			inFile1.close();
			return Arrays.asList(temps.toArray(new String[0]));
		} else {
			List<String> quotes = new ArrayList<String>();
			ResultSet result = null;
			if (type == BotType.Twitch)
				result = MySQLConnection.executeQuery("SELECT * FROM quotes WHERE twitch_channel_id = " + "\"" + MJRBotUtilities.getChannelIDFromBotType(type, bot) + "\"");
			else if (type == BotType.Mixer)
				result = MySQLConnection.executeQuery("SELECT * FROM quotes WHERE mixer_channel = " + "\"" + MJRBotUtilities.getChannelNameFromBotType(type, bot) + "\"");
			try {
				if (result == null)
					return new ArrayList<String>();
				else if (!result.next())
					return new ArrayList<String>();
				else {
					while (result.next()) {
						String quote = result.getString("quote");
						quotes.add(quote);
					}
					return quotes;
				}
			} catch (SQLException e) {
				MJRBotUtilities.logErrorMessage(e);
				return new ArrayList<String>();
			}
		}
	}

	public static void addQuote(BotType type, Object bot, File file, String quote) {
		quote = quote.substring(11);
		quote = "'" + quote;
		quote = quote.replace(" @", "' ");
		quote = quote.trim() + " " + Calendar.getInstance().get(Calendar.YEAR);
		if (MJRBot.storageType == StorageType.File) {
			String fileTemp = file.getPath();
			Path filePath = Paths.get(fileTemp);
			if (!Files.exists(filePath)) {
				try {
					Files.createFile(filePath);
				} catch (IOException e) {
					MJRBotUtilities.logErrorMessage(e);
				}
			}

			try {
				Files.write(filePath, ("\n" + quote + ";").getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e) {
				MJRBotUtilities.logErrorMessage(e);
			}
		} else {
			if (type == BotType.Twitch)
				MySQLConnection.executeUpdate("INSERT INTO quotes(twitch_channel_id, quote) VALUES (" + "\"" + MJRBotUtilities.getChannelIDFromBotType(type, bot) + "\"" + "," + "\"" + quote + "\"" + ")");
			else if (type == BotType.Mixer)
				MySQLConnection.executeUpdate("INSERT INTO quotes(mixer_channel, quote) VALUES (" + "\"" + MJRBotUtilities.getChannelNameFromBotType(type, bot) + "\"" + "," + "\"" + quote + "\"" + ")");
		}
	}

	public static void migrateFile(BotType type, Object bot) {
		File file = null;
		if (type == BotType.Twitch)
			file = new File(MJRBot.filePath + MJRBotUtilities.getChannelIDFromBotType(type, bot) + File.separator + QuoteCommand.filename);
		else if (type == BotType.Mixer)
			file = new File(MJRBot.filePath + MJRBotUtilities.getChannelNameFromBotType(type, bot) + File.separator + QuoteCommand.filename);
		List<String> quotes = getAllQuotes(type, bot, file);
		for (String quote : quotes) {
			if (type == BotType.Twitch)
				MySQLConnection.executeUpdate("INSERT INTO quotes(twitch_channel_id, quote) VALUES (" + "\"" + MJRBotUtilities.getChannelIDFromBotType(type, bot) + "\"" + "," + "\"" + quote + "\"" + ")");
			else if (type == BotType.Mixer)
				MySQLConnection.executeUpdate("INSERT INTO quotes(mixer_channel, quote) VALUES (" + "\"" + MJRBotUtilities.getChannelNameFromBotType(type, bot) + "\"" + "," + "\"" + quote + "\"" + ")");
		}
	}
}
