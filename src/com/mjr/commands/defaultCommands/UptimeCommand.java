package com.mjr.commands.defaultCommands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mjr.HTTPConnect;
import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;

public class UptimeCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (type == BotType.Twitch) {
	    String result = HTTPConnect
		    .GetResponsefrom("https://api.twitch.tv/kraken/streams/" + channel + "?client_id=it37a0q1pxypsijpd94h6rdhiq3j08");
	    if (result.contains("updated_at")) {
		String uptime = result.substring(result.indexOf("updated_at") + 13);
		uptime = uptime.substring(0, 20);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date parse = null;

		try {
		    parse = format.parse(uptime);
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

		Utilities.sendMessage(type, channel,
			channel + " has been live for " + diffDay + " day(s) " + diffHours + " hour(s) " + diffMinutes + " minute(s)");
	    } else {
		Utilities.sendMessage(type, channel, channel + " is currently not streaming!");
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
