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

public class EventLog extends FileBase {
    public static String fileName = "Event_Log.txt";
    
    public enum EventType {
	Points("Points"), Games("Games"), Rank("Rank"), Commands("Commands"), User("User"), Quote("Quote");

	private final String type;

	EventType(String type) {
	    this.type = type;
	}

	public String getName() {
	    return type;
	}
    }

    public static void addEvent(String channelName, String user, String eventMessage, EventType type) {
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	if (MJRBot.useFileSystem) {
	    File file = loadFile(channelName, fileName);
	    Path filePath = Paths.get(file.getPath());
	    try {
		Files.write(filePath, ("\n" + dateFormat.format(date) + user + ": " + eventMessage + ";").getBytes(),
			StandardOpenOption.APPEND);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} else {
	    MySQLConnection.executeUpdate("INSERT INTO events(channel, time, user, type, event_message) VALUES (" + "\"" + channelName + "\""
		    + "," + "\"" + dateFormat.format(date) + "\"" + "," + "\"" + user + "\"" + "," + "\"" + type.getName() + "\""+ "," + "\"" + eventMessage + "\"" + ")");
	}
    }

}
