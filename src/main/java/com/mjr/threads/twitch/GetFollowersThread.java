package com.mjr.threads.twitch;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.TwitchBot;
import com.mjr.util.ConsoleUtil;
import com.mjr.util.ConsoleUtil.MessageType;
import com.mjr.util.HTTPConnect;
import com.mjr.util.TwitchMixerAPICalls;

public class GetFollowersThread extends Thread {
	private BotType type;
	private TwitchBot bot;

	public GetFollowersThread(TwitchBot bot, BotType type) {
		super("GetFollowersThread for" + bot.channelName);
		this.type = type;
		this.bot = bot;
	}

	@Override
	public void run() {
		try {
			String result = "";
			result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetChannelsFollowsAPI(bot.channelID, 25));
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
					result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetChannelsFollowsAPI(bot.channelID, 25, current - 1));
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
			ConsoleUtil.textToConsole(bot, type, "Bot got " + bot.followers.size() + " followers", MessageType.ChatBot, null);
			ConsoleUtil.textToConsole(bot, type, "Follower list: " + String.join(", ", bot.followers), MessageType.ChatBot, null);
		} catch (Exception e) {
			MJRBot.logErrorMessage(e, type, bot);
		}

	}
}
