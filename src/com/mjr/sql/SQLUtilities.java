package com.mjr.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class SQLUtilities {

    public static void createDatabaseStructure() {
	if (!doesDataBaseExist("mjrbot")) {
	    MySQLConnection.executeQuery("CREATE DATABASE mjrbot");
	    MySQLConnection.executeQuery(
		    "CREATE TABLE mjrbot.channels (id int(50) not null auto_increment primary key,name varchar(35), bot_type varchar(12))");
	}
    }

    public static boolean doesDataBaseExist(String name) {
	ResultSet resultSet;
	try {
	    resultSet = MySQLConnection.getConnection().getMetaData().getCatalogs();
	    while (resultSet.next()) {
		String databaseName = resultSet.getString(1);
		if (databaseName.equals(name)) {
		    return true;
		}
	    }
	    resultSet.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return false;
    }

    public static HashMap<String, String> getChannelsTwitch() {
	HashMap<String, String> channels = new HashMap<String, String>();
	ResultSet result = MySQLConnection.executeQuery("SELECT name, bot_type FROM channels WHERE bot_type = 'Twitch'");
	try {
	    while (result.next()) {
		channels.put(result.getString("name"), result.getString("bot_type"));
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	try {
	    result.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return channels;
    }
    
    public static HashMap<String, String> getChannelsMixer() {
	HashMap<String, String> channels = new HashMap<String, String>();
	ResultSet result = MySQLConnection.executeQuery("SELECT name, bot_type FROM channels WHERE bot_type = 'Mixer'");
	try {
	    while (result.next()) {
		channels.put(result.getString("name"), result.getString("bot_type"));
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	try {
	    result.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return channels;
    }
}
