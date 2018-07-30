package com.mjr.commands.defaultCommands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import com.mjr.HTTPConnect;
import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.MixerBot;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;

public class UptimeCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (type == BotType.Twitch) {
	    String result = HTTPConnect
		    .GetResponsefrom("https://api.twitch.tv/kraken/streams/" + channel + "?client_id=" + MJRBot.CLIENT_ID);
	    if (result.contains("updated_at")) {
		String upTime = result.substring(result.indexOf("updated_at") + 13);
		upTime = upTime.substring(0, 20);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date parse = null;

		try {
		    parse = format.parse(upTime);
		} catch (ParseException e) {
		    e.printStackTrace();
		}

		runCommand(type, channel, sender, parse);
	    } else {
		Utilities.sendMessage(type, channel, "@" + sender + " " + channel + " is currently not streaming!");
	    }
	} else {
	    MixerBot mixerBot = ((MixerBot) bot);
	    if (mixerBot.isStreaming())
		runCommand(type, channel, sender, mixerBot.getUpdatedAt());
	    else
		Utilities.sendMessage(type, channel, "@" + sender + " " + channel + " is currently not streaming!");
	}
    }

    public void runCommand(BotType type, String channel, String sender, Date date2) {
	String currentTime = Instant.now().toString();
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	Date date = null;

	try {
	    date = format.parse(currentTime);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	long diff = date.getTime() - date2.getTime();
	long diffDay = diff / (24 * 60 * 60 * 1000);
	diff = diff - (diffDay * 24 * 60 * 60 * 1000);
	long diffHours = diff / (60 * 60 * 1000);
	diff = diff - (diffHours * 60 * 60 * 1000);
	long diffMinutes = diff / (60 * 1000);
	diff = diff - (diffMinutes * 60 * 1000);
	long diffSeconds = diff / 1000;
	diff = diff - (diffSeconds * 1000);

	Utilities.sendMessage(type, channel, "@" + sender + " " + channel + " has been live for " + diffDay + " day(s) " + diffHours
		+ " hour(s) " + diffMinutes + " minute(s)");
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
