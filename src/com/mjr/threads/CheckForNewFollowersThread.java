package com.mjr.threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.TwitchBot;
import com.mjr.files.Config;

public class CheckForNewFollowersThread extends Thread {
    private BotType type;
    private TwitchBot bot;

    public CheckForNewFollowersThread(TwitchBot bot, BotType type) {
	super();
	this.type = type;
	this.bot = bot;
    }

    @Override
    public void run() {
	while (bot.ConnectedToChannel) {
	    if (type == BotType.Twitch && bot.ConnectedToChannel) {
		if (Config.getSetting("FollowerCheck", bot.channelName).equalsIgnoreCase("true")) {
		    if (bot.viewers != null) {
			for (int i = 0; i < bot.viewers.size(); i++) {
			    checkFollower(bot, bot.viewers.get(i).toLowerCase());
			}
			try {
			    Thread.sleep(10000);
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
		    }
		}
	    }
	    try {
		Thread.sleep(60000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
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
		url = new URL("https://api.twitch.tv/kraken/channels/" + bot.channelName.toLowerCase() + "/follows?client_id="
			+ MJRBot.CLIENT_ID + "&limit=" + (currentfollowers.length() - (currentfollowers.length() - 3)));
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
		bot.MessageToChat(user + " " + Config.getSetting("FollowerMessage", bot.channelName));
	    }
	    if (!bot.followers.contains(user))
		bot.followers.add(user);
	}
    }
}
