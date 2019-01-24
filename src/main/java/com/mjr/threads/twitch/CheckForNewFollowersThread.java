package com.mjr.threads.twitch;

import java.util.Arrays;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.TwitchBot;
import com.mjr.storage.Config;
import com.mjr.util.HTTPConnect;
import com.mjr.util.TwitchMixerAPICalls;

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
							MJRBot.logErrorMessage(e, type, bot.channelName);
						}
					}
				}
			}
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				MJRBot.logErrorMessage(e, type, bot.channelName);
			}
		}
	}

	public static void checkFollower(TwitchBot bot, String user) {
		String newfollower = "";
		String result = "";
		boolean isfollower = false;
		String currentfollowers = Arrays.asList(bot.followers).toString();
		if (!currentfollowers.contains(user)) {
			try {
				result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetChannelsFollowsAPI(bot.channelName.toLowerCase(), (currentfollowers.length() - (currentfollowers.length() - 3))));
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
				MJRBot.logErrorMessage(e, bot.channelName);
			}
			if (!currentfollowers.contains(user) && isfollower) {
				bot.sendMessage(user + " " + Config.getSetting("FollowerMessage", bot.channelName));
			}
			if (!bot.followers.contains(user))
				bot.followers.add(user);
		}
	}
}
