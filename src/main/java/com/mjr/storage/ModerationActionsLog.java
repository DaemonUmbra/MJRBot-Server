package com.mjr.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mjr.MJRBot;
import com.mjr.sql.MySQLConnection;

public class ModerationActionsLog extends FileBase {
    public static String fileName = "Moderation_Actions_Log.txt";

    public static void addEvent(String channelName, String user, String reason, String message) {
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	if (MJRBot.useFileSystem) {
	    File file = loadFile(channelName, fileName);
	    Path filePath = Paths.get(file.getPath());
	    try {
		Files.write(filePath, ("\n" + dateFormat.format(date) + user + ": " + reason + " Message: " + message + ";").getBytes(),
			StandardOpenOption.APPEND);
	    } catch (IOException e) {
		MJRBot.getLogger().info(e.getMessage() + " " + e.getCause()); e.printStackTrace();
	    }
	} else {
	    MySQLConnection.executeUpdate("INSERT INTO moderation_actions(channel, time, user, reason, message) VALUES (" + "\""
		    + channelName + "\"" + "," + "\"" + dateFormat.format(date) + "\"" + "," + "\"" + user + "\"" + "," + "\"" + reason
		    + "\"" + "," + "\"" + message + "\"" + ")");
	}
    }

}
