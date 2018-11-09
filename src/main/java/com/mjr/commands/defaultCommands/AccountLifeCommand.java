package com.mjr.commands.defaultCommands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.util.Utilities;

public class AccountLifeCommand extends Command {

	@Override
	public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
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
					format.setTimeZone(TimeZone.getTimeZone("UTC"));
					Date parse = null;

					try {
						parse = format.parse(time);
					} catch (ParseException e) {
						e.printStackTrace();
					}

					OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
					Date date = Date.from(utc.toInstant());

					long diffInMilliSec = date.getTime() - parse.getTime();
					long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMilliSec) % 60;
					long diffHours = TimeUnit.MILLISECONDS.toHours(diffInMilliSec) % 24;
					long diffDays = TimeUnit.MILLISECONDS.toDays(diffInMilliSec) % 365;
					long diffYears = TimeUnit.MILLISECONDS.toDays(diffInMilliSec) / 365l;

					Utilities.sendMessage(type, channel, "@" + sender + " your twitch account is " + diffYears + " year(s) " + diffDays + " day(s) " + diffHours + " hour(s) " + diffMinutes + " minute(s) old");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			Utilities.sendMessage(type, channel, "@" + sender + " This command isnt available for Mixer, right now sorry!");
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
