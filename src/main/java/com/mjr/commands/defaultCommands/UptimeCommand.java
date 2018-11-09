package com.mjr.commands.defaultCommands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.MixerBot;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.util.HTTPConnect;
import com.mjr.util.Utilities;

public class UptimeCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
		if (type == BotType.Twitch) {
			String result = HTTPConnect.GetResponsefrom("https://api.twitch.tv/kraken/streams/" + channel + "?client_id=" + MJRBot.CLIENT_ID);
			if (result.contains("created_at")) {
				String upTime = result.substring(result.indexOf("created_at") + 13);
				upTime = upTime.substring(0, 20);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				format.setTimeZone(TimeZone.getTimeZone("UTC"));
				Date parse = null;

				try {
					parse = format.parse(upTime);
				} catch (ParseException e) {
					MJRBot.getLogger().info(e.getMessage() + " " + e.getCause());
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
		OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
		Date date = Date.from(utc.toInstant());
		long diffInMilliSec = date.getTime() - date2.getTime();
		long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMilliSec) % 60;
		long diffHours = TimeUnit.MILLISECONDS.toHours(diffInMilliSec) % 24;
		long diffDays = TimeUnit.MILLISECONDS.toDays(diffInMilliSec) % 365;

		Utilities.sendMessage(type, channel, "@" + sender + " " + channel + " has been live for " + diffDays + " day(s) " + diffHours + " hour(s) " + diffMinutes + " minute(s)");
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
