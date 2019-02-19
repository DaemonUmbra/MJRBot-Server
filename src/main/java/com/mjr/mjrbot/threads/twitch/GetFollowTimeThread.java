package com.mjr.mjrbot.threads.twitch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.util.HTTPConnect;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.TwitchMixerAPICalls;

public class GetFollowTimeThread extends Thread {
	private TwitchBot bot;
	private String user;

	public GetFollowTimeThread(TwitchBot bot, String user) {
		super("GetFollowTimeThread for" + bot.getChannelName());
		this.bot = bot;
		this.user = user;
	}

	@Override
	public void run() {
		try {
			if (bot.isBotConnected()) {
				String time = checkFollowTime(bot, user.toLowerCase());
				if (time == null) {
					MJRBotUtilities.sendMessage(BotType.Twitch, bot, "@" + user + " unable to obtain follow details for you!");
				} else {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
					format.setTimeZone(TimeZone.getTimeZone("UTC"));
					Date parse = null;

					try {
						parse = format.parse(time);
					} catch (ParseException e) {
						MJRBotUtilities.logErrorMessage(e, BotType.Twitch, bot);
					}

					OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
					Date date = Date.from(utc.toInstant());

					long diffInMilliSec = date.getTime() - parse.getTime();
					long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMilliSec) % 60;
					long diffHours = TimeUnit.MILLISECONDS.toHours(diffInMilliSec) % 24;
					long diffDays = TimeUnit.MILLISECONDS.toDays(diffInMilliSec) % 365;
					long diffYears = TimeUnit.MILLISECONDS.toDays(diffInMilliSec) / 365l;

					MJRBotUtilities.sendMessage(BotType.Twitch, bot, user + " you've been following this channel for " + diffYears + " year(s) " + diffDays + " day(s) " + diffHours + " hour(s) " + diffMinutes + " minute(s)");
				}
			}
		} catch (Exception e) {
			MJRBotUtilities.logErrorMessage(e, BotType.Twitch, bot);
		}
	}

	public static String checkFollowTime(TwitchBot bot, String userTest) {
		try {
			String result = "";
			result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetChannelsFollowsAPI(bot.getChannelID(), 25));
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
					result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetChannelsFollowsAPI(bot.getChannelID(), 25, current + 1));
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
			MJRBotUtilities.logErrorMessage(e, BotType.Twitch, bot);
		}
		return null;
	}
}
