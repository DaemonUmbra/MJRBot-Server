package com.mjr.files;

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

    public static void addEvent(String channelName, File file, String user, String reason, String message) {
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	if (MJRBot.useFileSystem) {
	    String fileTemp = file.getPath();
	    Path filePath = Paths.get(fileTemp);
	    if (!Files.exists(filePath)) {
		try {
		    Files.createFile(filePath);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }

	    try {
		Files.write(filePath, ("\n" + dateFormat.format(date) + user + ": " + reason + " Message: " + message + ";").getBytes(), StandardOpenOption.APPEND);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} else {
	    MySQLConnection.executeUpdate("INSERT INTO moderation_actions(channel, time, user, reason, message) VALUES (" + "\"" + channelName
		    + "\"" + "," + "\"" + dateFormat.format(date) + "\"" + "," + "\"" + user + "\"" + "," + "\"" + reason + "\""+ "," + "\"" + message + "\"" + ")");
	}
    }

}
