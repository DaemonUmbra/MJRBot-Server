package com.mjr.mjrbot.threads.twitch;

import java.io.IOException;
import java.sql.ResultSet;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.OAuthManager;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.storage.sql.MySQLConnection;
import com.mjr.mjrbot.util.ConsoleUtil;
import com.mjr.mjrbot.util.ConsoleUtil.MessageType;
import com.mjr.mjrbot.util.HTTPConnect;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.TwitchMixerAPICalls;

public class GetSubscribersThread extends Thread {
	private TwitchBot bot;

	public GetSubscribersThread(TwitchBot bot) {
		super("GetSubscribersThread for" + bot.getChannelName());
		this.bot = bot;
	}

	@Override
	public void run() {
		try {
			ResultSet set = MySQLConnection.executeQuery("SELECT * FROM tokens WHERE channel_id = '" + bot.getChannelID() + "' AND platform = 'Twitch'", false);
			if (set != null && set.next()) {
				String result = getList(TwitchMixerAPICalls.twitchGetChannelsSubscriptionsAPI(bot.getChannelID(), set.getString("access_token"), 25));
				if (result != null && !result.contains("does not have a subscription program")) {
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
							newurl = newurl + "&client_id=" + MJRBot.CLIENT_ID_Twitch + "&oauth_token=" + set.getString("access_token") + "&api_version=5";
							result = getList(newurl);
							copyresult = result;
							newresult = result;
						}
						for (int j = 0; j < 25; j++) {
							if (current <= times) {
								newSubscriber = newresult.substring(newresult.indexOf("display_name") + 15);
								newSubscriber = newSubscriber.substring(0, newSubscriber.indexOf("\""));
								result = result.substring(result.indexOf(newSubscriber));

								bot.getTwitchData().addSubscriber(newSubscriber);
								if (current % 100 != 0) {
									if (result.indexOf("logo\":\"") != -1)
										newresult = result.substring(result.indexOf("logo\":\""));
								}
								current++;
							}
						}
					}
				}
				ConsoleUtil.textToConsole(bot, BotType.Twitch, "Bot got " + bot.getTwitchData().getSubscribers().size() + " subscribers", MessageType.ChatBot, null);
				ConsoleUtil.textToConsole(bot, BotType.Twitch, "Subscriber list: " + String.join(", ", bot.getTwitchData().getSubscribers()), MessageType.ChatBot, null);
			} else
				MJRBot.getDiscordBot().sendErrorMessage("Unable to find access token for channel " + bot.getChannelName() + " Platform: " + "Twitch");
		} catch (Exception e) {
			MJRBotUtilities.logErrorMessage(e, BotType.Twitch, bot);
		}
	}

	public String getList(String urlLink) {
		String result = "";
		boolean refreshToken = false;
		boolean skip = false;
		do {
			try {
				if (refreshToken)
					OAuthManager.refreshTokenTwitch(5, bot);
				result = HTTPConnect.getRequest(urlLink);
				return result;
			} catch (IOException e) {
				if (e.getMessage().contains("401") || e.getMessage().contains("403")) {
					refreshToken = true;
				} else if (e.getMessage().contains("400")) {
					skip = true;
					ConsoleUtil.textToConsole(bot, BotType.Twitch, "No subscribers due to does not have a subscription program", MessageType.ChatBot, null);
				} else
					MJRBotUtilities.logErrorMessage(e, BotType.Twitch, bot);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					MJRBotUtilities.logErrorMessage(e, BotType.Twitch, bot);
				}
			}
		} while (result.equals("") && skip == false);
		return null;
	}

}
