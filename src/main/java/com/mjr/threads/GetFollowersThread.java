package com.mjr.threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.TwitchBot;
import com.mjr.util.ConsoleUtil;
import com.mjr.util.ConsoleUtil.MessageType;

public class GetFollowersThread extends Thread {
	private BotType type;
	private TwitchBot bot;

	public GetFollowersThread(TwitchBot bot, BotType type) {
		super();
		this.type = type;
		this.bot = bot;
	}

	@Override
	public void run() {
		URL url;
		try {
			String result = "";
			url = new URL("https://api.twitch.tv/kraken/channels/" + bot.channelName.toLowerCase() + "/follows?client_id=" + MJRBot.CLIENT_ID + "&limit=25");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				result += line;
			}
			reader.close();
			connection.disconnect();

			String copyresult = result;
			String total = result.substring(result.indexOf("_total") + 8);
			int times = Integer.parseInt(total.substring(0, total.indexOf(",")));
			int current = 1;

			String newfollower = "";
			String newresult = result;

			if (times > 1600)
				times = 1600;
			int amount = (int) Math.ceil(((float) times / 25));
			for (int i = 0; i < amount; i++) {
				if (i != 0) {
					result = "";
					String newurl = copyresult.substring(copyresult.indexOf("next") + 7);
					newurl = newurl.substring(0, newurl.indexOf("},") - 1);
					newurl = newurl + "&client_id=" + MJRBot.CLIENT_ID + "&offset=" + (current - 1);
					url = new URL(newurl);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					line = "";
					while ((line = reader.readLine()) != null) {
						result += line;
					}
					reader.close();
					connection.disconnect();
					copyresult = result;
					newresult = result;
				}
				for (int j = 0; j < 25; j++) {
					if (current <= times) {
						newfollower = newresult.substring(newresult.indexOf("display_name") + 15);
						newfollower = newfollower.substring(0, newfollower.indexOf("\""));
						result = result.substring(result.indexOf(newfollower));
						if (!newfollower.equalsIgnoreCase("bio"))
							bot.followers.add(newfollower.toLowerCase());
						if (current % 100 != 0) {
							if (result.indexOf("type\":\"") != -1)
								newresult = result.substring(result.indexOf("type\":\""));
						}
						current++;
					}
				}
			}
			ConsoleUtil.textToConsole(bot, type, bot.channelName, "Bot got " + bot.followers.size() + " followers", MessageType.Bot, null);
			ConsoleUtil.textToConsole(bot, type, bot.channelName, "Follower list: " + String.join(", ", bot.followers), MessageType.Bot, null);
		} catch (Exception e) {
			MJRBot.logErrorMessage(e, type, bot.channelName);
		}

	}
}
