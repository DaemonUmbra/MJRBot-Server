package com.mjr.commands.defaultCommands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;

public class AccountLifeCommand extends Command {

    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (type == BotType.Twitch) {
	    URL url;
	    try {
		String result = "";
		url = new URL("https://api.twitch.tv/kraken/users/" + sender.toLowerCase() + "/?client_id=" + MJRBot.CLIENT_ID);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = "";
		while ((line = reader.readLine()) != null) {
		    result += line;
		}
		reader.close();
		if (result.contains("created_at")) {
		    String time = result.substring(result.indexOf("created_at") + 13);
		    time = time.substring(0, 20);

		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		    Date parse = null;

		    try {
			parse = format.parse(time);
		    } catch (ParseException e) {
			e.printStackTrace();
		    }

		    long currentTime = System.currentTimeMillis();

		    Date date = new Date(currentTime);

		    long diff = (long) (date.getTime() - parse.getTime());
		    long diffDay = diff / (24 * 60 * 60 * 1000);
		    diff = diff - (diffDay * 24 * 60 * 60 * 1000);
		    long diffHours = diff / (60 * 60 * 1000);
		    diff = diff - (diffHours * 60 * 60 * 1000);
		    long diffMinutes = diff / (60 * 1000);
		    diff = diff - (diffMinutes * 60 * 1000);
		    long diffSeconds = diff / 1000;
		    diff = diff - (diffSeconds * 1000);

		    int diffMonths = (int) (diffDay / 31);
		    diffDay = diffDay - (diffMonths * 31);

		    int diffYears = (int) (diffMonths / 12);
		    diffMonths = diffMonths - (diffYears * 12);

		    Utilities.sendMessage(type, channel, sender + " your twitch account is " + diffYears + " year(s) " + diffMonths
			    + " month(s) " + diffDay + " day(s) " + diffHours + " hour(s) " + diffMinutes + " minute(s) old");
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	} else
	    Utilities.sendMessage(type, channel, "This command isnt available for Mixer, right now sorry!");
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
