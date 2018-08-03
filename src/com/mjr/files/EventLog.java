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

public class EventLog extends FileBase {
    public static String filename = "Event_Log.txt";

    public static void addEvent(String channelName, String user, String eventMessage) {
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	if (MJRBot.useFileSystem) {
	    File file = new File(MJRBot.filePath + channelName + File.separator + filename);
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
		Files.write(filePath, ("\n" + dateFormat.format(date) + user + ": " + eventMessage + ";").getBytes(),
			StandardOpenOption.APPEND);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} else {
	    MySQLConnection.executeUpdate("INSERT INTO events(channel, time, user, event_message) VALUES (" + "\"" + channelName + "\""
		    + "," + "\"" + dateFormat.format(date) + "\"" + "," + "\"" + user + "\"" + "," + "\"" + eventMessage + "\"" + ")");
	}
    }

}
