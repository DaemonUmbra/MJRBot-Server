package com.mjr.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import com.mjr.ConsoleUtil;
import com.mjr.ConsoleUtil.MessageType;
import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.TwitchBot;
import com.mjr.sql.MySQLConnection;
import com.mjr.storage.ConfigMain;

public class GetSubscribersThread extends Thread {
    private BotType type;
    private TwitchBot bot;

    public GetSubscribersThread(TwitchBot bot, BotType type) {
	super();
	this.type = type;
	this.bot = bot;
    }

    @Override
    public void run() {
	try {
	    String result = getList("https://api.twitch.tv/kraken/channels/" + bot.channelName.toLowerCase() + "/subscriptions?client_id=" + MJRBot.CLIENT_ID + "&access_token="
		    + MySQLConnection.executeQuery("SELECT access_token FROM tokens WHERE channel = '" + bot.channelName + "'").getString("access_token") + "&limit=25");
	    if (!result.contains("does not have a subscription program")) {
		String copyresult = result;
		String total = result.substring(result.indexOf("_total") + 8);
		int times = Integer.parseInt(total.substring(0, total.indexOf(",")));
		int current = 1;

		String newSubscriber = "";
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
			result = getList(newurl);
			copyresult = result;
			newresult = result;
		    }
		    for (int j = 0; j < 25; j++) {
			if (current <= times) {
			    newSubscriber = newresult.substring(newresult.indexOf("display_name") + 15);
			    newSubscriber = newSubscriber.substring(0, newSubscriber.indexOf("\""));
			    result = result.substring(result.indexOf(newSubscriber));

			    bot.subscribers.add(newSubscriber.toLowerCase());
			    if (current % 100 != 0) {
				if (result.indexOf("logo\":\"") != -1)
				    newresult = result.substring(result.indexOf("logo\":\""));
			    }
			    current++;
			}
		    }
		}
	    }
	    ConsoleUtil.TextToConsole(bot, type, bot.channelName, "Bot got " + bot.subscribers.size() + " subscribers", MessageType.Bot, null);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	System.exit(0);
    }

    public String getList(String urlLink) {
	try {
	    String result = "";
	    do {
		if (result.contains("Forbidden") || result.contains("Unauthorized"))
		    refreshToken();
		URL url = new URL(urlLink);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = "";
		while ((line = reader.readLine()) != null) {
		    result += line;
		}
		reader.close();
		connection.disconnect();
	    } while (result.contains("Forbidden") || result.contains("Unauthorized"));
	    return result;
	} catch (IOException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public void refreshToken() {
	URL url;
	try {
	    url = new URL(
		    "https://id.twitch.tv/oauth2/token?grant_type=refresh_token&refresh_token="
			    + MySQLConnection.executeQuery("SELECT access_token FROM tokens WHERE channel = '" + bot.channelName + "'").getString("refresh_token")
			    + "&client_id=" + MJRBot.CLIENT_ID + "&client_secret=" + ConfigMain.getSetting("TwitchClientSecret"));
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    connection.setRequestMethod("GET");
	    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    String line = "";
	    String result = "";
	    String access_token = result.substring(result.indexOf("access_token") + 16);
	    access_token = access_token.substring(0, access_token.indexOf(","));
	    String refresh_token = result.substring(result.indexOf("refresh_token") + 16);
	    refresh_token = refresh_token.substring(0, access_token.indexOf(","));
	    MySQLConnection.executeQuery("UPDATE tokens SET access_token='" + access_token + "', refresh_token='" + refresh_token
		    + "'WHERE channel='" + bot.channelName + "';");
	    while ((line = reader.readLine()) != null) {
		result += line;
	    }
	    reader.close();
	    connection.disconnect();
	} catch (SQLException | IOException e) {
	    e.printStackTrace();
	}
    }
}
