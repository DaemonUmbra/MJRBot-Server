package com.mjr.mjrbot.threads.twitch;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.storage.ConfigMain;
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
		int tries = 0;
		do {
			try {
				if (refreshToken)
					refreshToken(tries);
				result = HTTPConnect.getRequest(urlLink);
				return result;
			} catch (IOException e) {
				if (e.getMessage().contains("401") || e.getMessage().contains("403")) {
					refreshToken = true;
					if (refreshToken)
						tries++;
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
		} while (result.equals("") && skip == false && tries < 6);
		if (tries == 6) {
			ConsoleUtil.textToConsole(bot, BotType.Twitch, "Unable to refresh token after 5 Attempts!", MessageType.ChatBot, null);
			MJRBot.getDiscordBot().sendAdminEventMessage("[" + BotType.Twitch.getTypeName() + "] **" + TwitchBot.getChannelNameFromChannelID(bot.getChannelID()) + " ** Unable to refresh token after 5 Attempts!");
		}
		return null;
	}

	public void refreshToken(int tries) {
		ConsoleUtil.textToConsole(bot, BotType.Twitch, "Refreshing access_token! Attempt " + tries + " out of 5", MessageType.ChatBot, null);
		MJRBot.getDiscordBot().sendAdminEventMessage("[" + BotType.Twitch.getTypeName() + "] **" + TwitchBot.getChannelNameFromChannelID(bot.getChannelID()) + " ** Refreshing access_token! Attempt " + tries + " out of 5");
		try {
			ResultSet tokenSet = MySQLConnection.executeQuery("SELECT refresh_token FROM tokens WHERE channel_id = '" + bot.getChannelID() + "'");
			if (tokenSet.next() && tokenSet.getString("refresh_token") != null && tokenSet.getString("refresh_token") != "") {
				String result = HTTPConnect.postRequest(TwitchMixerAPICalls.twitchGetoAuth2TokenAPI(tokenSet.getString("refresh_token"), ConfigMain.getSetting("TwitchClientSecret")));
				String access_token = result.substring(result.indexOf("access_token") + 16);
				access_token = access_token.substring(0, access_token.indexOf(",") - 2);
				String refresh_token = result.substring(result.indexOf("refresh_token") + 16);
				refresh_token = refresh_token.substring(0, refresh_token.indexOf(",") - 2);
				MySQLConnection.executeUpdate("UPDATE tokens SET access_token='" + access_token + "', refresh_token='" + refresh_token + "'WHERE channel_id='" + bot.getChannelID() + "';");
			}
		} catch (SQLException | IOException e) {
			if (!e.getMessage().contains("400"))
				MJRBotUtilities.logErrorMessage(e, BotType.Twitch, bot);
		}
	}
}
