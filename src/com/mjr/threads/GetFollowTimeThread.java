package com.mjr.threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mjr.HTTPConnect;
import com.mjr.MJRBot.BotType;
import com.mjr.TwitchBot;
import com.mjr.Utilities;

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
	if (type == BotType.Twitch && bot.ConnectedToChannel) {
	    String time = checkFollowTime(bot, user.toLowerCase());
	    if(time == null) {
		Utilities.sendMessage(type, bot.channelName, user + " unable to obtain follow details!");
	    }
	    else {
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

		Utilities.sendMessage(type, bot.channelName, user + " you've been following this channel for " + diffDay + " day(s) " + diffHours + " hour(s) "
			+ diffMinutes + " minute(s)");
	    }
	}
    }

    public static String checkFollowTime(TwitchBot bot, String userTest) {
	URL url;
	try {
	    String result = "";
	    url = new URL("https://api.twitch.tv/kraken/channels/" + bot.channelName.toLowerCase()
		    + "/follows?client_id=it37a0q1pxypsijpd94h6rdhiq3j08\u0026limit=25");
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    connection.setRequestMethod("GET");
	    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    String line = "";
	    while ((line = reader.readLine()) != null) {
		result += line;
	    }
	    reader.close();

	    String copyresult = result;
	    String total = result.substring(result.indexOf("_total") + 8);
	    int times = Integer.parseInt(total.substring(0, total.indexOf(",")));
	    int current = 1;

	    String newfollower = "";
	    String newresult = result;

	    if (times > 1700)
		times = 1700;

	    for (int i = 0; i < (((times + 24) / 25) * 25) / 25; i++) {
		if (i != 0) {
		    String newurl = copyresult.substring(copyresult.indexOf("next\":\"") + 7);
		    newurl = newurl.substring(0, newurl.indexOf("\""));
		    newurl = newurl + "\u0026client_id=it37a0q1pxypsijpd94h6rdhiq3j08";
		    result = HTTPConnect.GetResponsefrom(newurl);
		    copyresult = result;
		    newresult = result;
		}

		for (int j = 0; j < 25; j++) {
		    if (current <= times) {
			String time = newresult.substring(newresult.indexOf("created_at") + 13);
			time = time.substring(0, 20);
			newfollower = newresult.substring(newresult.indexOf("display_name") + 15);
			newfollower = newfollower.substring(0, newfollower.indexOf("\""));
			if (userTest.equalsIgnoreCase(newfollower)) {
			    return time;
			}
			result = result.substring(result.indexOf(newfollower));

			if (current % 100 != 0) {
			    if (result.indexOf("type\":\"") != -1)
				newresult = result.substring(result.indexOf("updated_at"));
			}
			current++;
		    }
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }
}
