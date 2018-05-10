package com.mjr.commands.defaultCommands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mjr.HTTPConnect;
import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;

public class UptimeCommand extends Command {
    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (MJRBot.getTwitchBot() != null) {
	    String result = HTTPConnect.GetResponsefrom("https://api.twitch.tv/kraken/streams/"
		    + MJRBot.getTwitchBot().getChannel().substring(1) + "?client_id=it37a0q1pxypsijpd94h6rdhiq3j08");
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

		long diff = date.getTime() - parse.getTime();

		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000);
		int diffInDays = (int) ((date.getTime() - parse.getTime()) / (1000 * 60 * 60 * 24));

		((TwitchBot) bot).MessageToChat(MJRBot.getTwitchBot().getChannel().substring(1) + " has been live for " + diffInDays
			+ " day(s) " + diffHours + " hour(s) " + diffMinutes + " minute(s)");
	    } else {
		((TwitchBot) bot).MessageToChat(MJRBot.getTwitchBot().getChannel().substring(1) + " is currently not streaming!");
	    }
	} else
	    ((MixerBot) bot).sendMessage("This command isnt available for Mixer, right now sorry!");
    }

    @Override
    public String getPermissionLevel() {
	return "User";
    }
}
