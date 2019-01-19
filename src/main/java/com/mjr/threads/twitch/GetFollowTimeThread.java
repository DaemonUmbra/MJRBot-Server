package com.mjr.threads.twitch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.TwitchBot;
import com.mjr.util.HTTPConnect;
import com.mjr.util.Utilities;

public class GetFollowTimeThread extends Thread {
	private BotType type;
	private TwitchBot bot;
	private String user;

	public GetFollowTimeThread(TwitchBot bot, BotType type, String user) {
		super();
		this.type = type;
		this.bot = bot;
		this.user = user;
	}

	@Override
	public void run() {
		try {
			if (type == BotType.Twitch && bot.ConnectedToChannel) {
				String time = checkFollowTime(bot, user.toLowerCase());
				if (time == null) {
					Utilities.sendMessage(type, bot.channelName, "@" + user + " unable to obtain follow details for you!");
				} else {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
					format.setTimeZone(TimeZone.getTimeZone("UTC"));
					Date parse = null;

					try {
						parse = format.parse(time);
					} catch (ParseException e) {
						MJRBot.logErrorMessage(e, type, bot.channelName);
					}

					OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
					Date date = Date.from(utc.toInstant());

					long diffInMilliSec = date.getTime() - parse.getTime();
					long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMilliSec) % 60;
					long diffHours = TimeUnit.MILLISECONDS.toHours(diffInMilliSec) % 24;
					long diffDays = TimeUnit.MILLISECONDS.toDays(diffInMilliSec) % 365;
					long diffYears = TimeUnit.MILLISECONDS.toDays(diffInMilliSec) / 365l;

					Utilities.sendMessage(type, bot.channelName, user + " you've been following this channel for " + diffYears + " year(s) " + diffDays + " day(s) " + diffHours + " hour(s) " + diffMinutes + " minute(s)");
				}
			}
		} catch (Exception e) {
			MJRBot.logErrorMessage(e, type, bot.channelName);
		}
	}

	public static String checkFollowTime(TwitchBot bot, String userTest) {
		try {
			String result = "";
			result = HTTPConnect.getRequest("https://api.twitch.tv/kraken/channels/" + bot.channelName.toLowerCase() + "/follows?client_id=" + MJRBot.CLIENT_ID + "&limit=25");
			String copyresult = result;
			String total = result.substring(result.indexOf("_total") + 8);
			int times = Integer.parseInt(total.substring(0, total.indexOf(",")));
			int current = 1;

			String newfollower = "";
			String newresult = result;

			if (times > 1700)
				times = 1700;
			int amount = (int) Math.ceil(((float) times / 25));
			for (int i = 0; i < amount; i++) {
				if (i != 0) {
					result = "";
					String newurl = copyresult.substring(copyresult.indexOf("next") + 7);
					newurl = newurl.substring(0, newurl.indexOf("},") - 1);
					newurl = newurl + "&client_id=" + MJRBot.CLIENT_ID + "&offset=" + (current + 1);
					result = HTTPConnect.getRequest(newurl);
					copyresult = result;
					newresult = result;
				}
				for (int j = 0; j < 25; j++) {
					if (current <= times) {
						String time = newresult.substring(newresult.indexOf("created_at") + 13);
						time = time.substring(0, 20);
						newfollower = newresult.substring(newresult.indexOf("display_name") + 15);
						newfollower = newfollower.substring(0, newfollower.indexOf("\""));
						result = result.substring(result.indexOf(newfollower));
						if (userTest.equalsIgnoreCase(newfollower)) {
							return time;
						}
						if (current % 100 != 0) {
							if (result.indexOf("logo\":\"") != -1)
								newresult = result.substring(result.indexOf("logo\":\""));
						}
						current++;
					}
				}
			}
		} catch (Exception e) {
			MJRBot.logErrorMessage(e, bot.channelName);
		}
		return null;
	}
}
