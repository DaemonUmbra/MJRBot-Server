package com.mjr.threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import com.mjr.ConsoleUtil;
import com.mjr.HTTPConnect;
import com.mjr.MJRBot.BotType;
import com.mjr.TwitchBot;
import com.mjr.files.Config;

public class Followers extends Thread {
    private BotType type;
    private TwitchBot bot;

    public Followers(TwitchBot bot, BotType type) {
	super();
	this.type = type;
	this.bot = bot;
    }

    @Override
    public void run() {
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
			newfollower = newresult.substring(newresult.indexOf("display_name") + 15);
			newfollower = newfollower.substring(0, newfollower.indexOf("\""));
			result = result.substring(result.indexOf(newfollower));

			bot.followers.add(newfollower.toLowerCase());

			if (current % 100 != 0) {
			    if (result.indexOf("type\":\"") != -1)
				newresult = result.substring(result.indexOf("type\":\""));
			}
			current++;
		    }
		}

	    }
	    ConsoleUtil.TextToConsole(type, bot.channelName, "Bot got " + bot.followers.size() + " followers", "Bot", null);
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    public static void checkFollower(TwitchBot bot, String user) {
	String newfollower = "";
	String result = "";
	boolean isfollower = false;
	String currentfollowers = Arrays.asList(bot.followers).toString();
	if (!currentfollowers.contains(user)) {
	    URL url;
	    try {
		url = new URL("https://api.twitch.tv/kraken/channels/" + bot.channelName.toLowerCase()
			+ "/follows?client_id=it37a0q1pxypsijpd94h6rdhiq3j08\u0026limit="
			+ (currentfollowers.length() - (currentfollowers.length() - 3)));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = "";
		while ((line = reader.readLine()) != null) {
		    result += line;
		}
		reader.close();

		for (int i = 0; i < (currentfollowers.length() - (currentfollowers.length() - 3)); i++) {
		    String newresult = "";
		    if (i >= 1)
			newresult = result.substring(result.indexOf("type\":\""));
		    else {
			newresult = result;
		    }
		    newfollower = newresult.substring(newresult.indexOf("display_name") + 15);
		    newfollower = newfollower.substring(0, newfollower.indexOf("\""));
		    result = result.substring(result.indexOf(newfollower));
		    if (user.equalsIgnoreCase(newfollower))
			isfollower = true;
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    if (!currentfollowers.contains(user) && isfollower) {
		bot.MessageToChat(user + " " + Config.getSetting("FollowerMessage"));
	    }
	    if (!bot.followers.contains(user))
		bot.followers.add(user);
	}
    }
}
