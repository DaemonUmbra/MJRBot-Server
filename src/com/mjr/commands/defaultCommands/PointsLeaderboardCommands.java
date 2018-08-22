package com.mjr.commands.defaultCommands;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mjr.MJRBot;
import com.mjr.Utilities;
import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.sql.MySQLConnection;

public class PointsLeaderboardCommands extends Command{

    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if(MJRBot.useFileSystem)
	    Utilities.sendMessage(type, channel, "This feature is not currently available, when the bot is using a file based system");
	else {
	    ResultSet result = MySQLConnection.executeQuery("SELECT * FROM points WHERE channel ='" + channel + "' ORDER BY amount DESC LIMIT 10");
	    String top10Users = "";
	    try {
		while(result.next()) {
		    top10Users = top10Users + result.getString("name") + ":" + result.getInt("amount") + " point(s), ";
		}
		Utilities.sendMessage(type, channel, "The top 10 users in this channel for points are: " + top10Users.substring(0, top10Users.length() - 1));
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.User.getName();
    }

    @Override
    public boolean hasCooldown() {
	return true;
    }

}
