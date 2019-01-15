package com.mjr.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.TwitchBot;
import com.mjr.sql.MySQLConnection;
import com.mjr.storage.ConfigMain;
import com.mjr.util.ConsoleUtil;
import com.mjr.util.ConsoleUtil.MessageType;

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
			ResultSet set = MySQLConnection.executeQueryNoOutput("SELECT * FROM tokens WHERE channel = '" + bot.channelName + "' AND platform = 'Twitch'");
			if (set != null && set.next()) {
				String result = getList("https://api.twitch.tv/kraken/channels/" + bot.channelName.toLowerCase() + "/subscriptions?client_id=" + MJRBot.CLIENT_ID + "&oauth_token=" + set.getString("access_token") + "&limit=25");
				if (!result.contains("does not have a subscription program")) {
					String copyresult = result;
					String total = result.substring(result.indexOf("_total") + 8);
					int times = Integer.parseInt(total.substring(0, total.indexOf(",")));
					int current = 1;

					String newSubscriber = "";
					String newresult = result;

					if (times > 1600)
						times = 1600;
					int amount = (int) Math.ceil(((float) times / 25));
					for (int i = 0; i < amount; i++) {
						if (i != 0) {
							result = "";
							String newurl = copyresult.substring(copyresult.indexOf("next") + 7);
							newurl = newurl.substring(0, newurl.indexOf("}"));
							newurl = newurl + "&client_id=" + MJRBot.CLIENT_ID + "&oauth_token=" + set.getString("access_token");
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
				ConsoleUtil.textToConsole(bot, type, bot.channelName, "Bot got " + bot.subscribers.size() + " subscribers", MessageType.Bot, null);
				ConsoleUtil.textToConsole(bot, type, bot.channelName, "Subscriber list: " + String.join(", ", bot.subscribers), MessageType.Bot, null);
			}
		} catch (Exception e) {
			MJRBot.logErrorMessage(e, type, bot.channelName);
		}
	}

	public String getList(String urlLink) {
		String result = "";
		boolean refreshToken = false;
		boolean skip = false;
		do {
			try {
				if (refreshToken)
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
				return result;
			} catch (IOException e) {
				if (e.getMessage().contains("401") || e.getMessage().contains("403"))
					refreshToken = true;
				else if (e.getMessage().contains("400")) {
					skip = true;
					ConsoleUtil.textToConsole(bot, type, bot.channelName, "No subscribers due to does not have a subscription program", MessageType.Bot, null);
				}
				else
					MJRBot.logErrorMessage(e, type, bot.channelName);
			}
		} while (result.equals("") && skip == false);
		return null;
	}

	public void refreshToken() {
		ConsoleUtil.textToConsole(bot, type, bot.channelName, "Refreshing access_token!", MessageType.Bot, null);
		URL url;
		try {
			ResultSet tokenSet = MySQLConnection.executeQuery("SELECT refresh_token FROM tokens WHERE channel = '" + bot.channelName + "'");
			if (tokenSet.next()) {
				url = new URL("https://id.twitch.tv/oauth2/token?grant_type=refresh_token&refresh_token=" + tokenSet.getString("refresh_token") + "&client_id=" + MJRBot.CLIENT_ID + "&client_secret=" + ConfigMain.getSetting("TwitchClientSecret"));
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line = "";
				String result = "";
				while ((line = reader.readLine()) != null) {
					result += line;
				}
				String access_token = result.substring(result.indexOf("access_token") + 16);
				access_token = access_token.substring(0, access_token.indexOf(",") - 2);
				String refresh_token = result.substring(result.indexOf("refresh_token") + 16);
				refresh_token = refresh_token.substring(0, refresh_token.indexOf(",") - 2);
				MySQLConnection.executeUpdate("UPDATE tokens SET access_token='" + access_token + "', refresh_token='" + refresh_token + "'WHERE channel='" + bot.channelName + "';");

				reader.close();
				connection.disconnect();
			}
		} catch (SQLException | IOException e) {
			MJRBot.logErrorMessage(e, type, bot.channelName);
		}
	}
}
