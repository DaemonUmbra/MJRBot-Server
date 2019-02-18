package com.mjr.mjrbot.threads;

import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MixerBot;
import com.mjr.mjrbot.TwitchBot;
import com.mjr.mjrbot.storage.Config;
import com.mjr.mjrbot.util.HTTPConnect;
import com.mjr.mjrbot.util.TwitchMixerAPICalls;
import com.mjr.mjrbot.util.Utilities;

public class AnnouncementsThread extends Thread {

	private BotType type;
	private Object bot;

	public AnnouncementsThread(BotType type, Object bot, String channel) {
		super("AnnouncementsThread for " + type.getTypeName() + "|" + channel);
		this.type = type;
		this.bot = bot;
	}

	@Override
	public void run() {
		try {
			while (type == BotType.Twitch ? ((TwitchBot) bot).isBotConnected() : ((MixerBot) bot).isConnected()) {
				if (Config.getSetting("Announcements", type, bot).equalsIgnoreCase("true")) {
					boolean streaming = false;
					if (type == BotType.Twitch) {
						String result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetStreamsAPI(((TwitchBot) bot).channelID));
						if (result.contains("created_at"))
							streaming = true;
					} else {
						streaming = ((MixerBot) bot).isStreaming();
					}
					if (Config.getSetting("AnnouncementsWhenOffline", type, bot).equalsIgnoreCase("true") || streaming) {
						int delay = Integer.parseInt(Config.getSetting("AnnouncementsDelay", type, bot));
						if (delay != 0) {
							long TimeDuration = (delay * 60) * 1000;
							try {
								Thread.sleep(TimeDuration);
							} catch (InterruptedException e) {
								MJRBot.logErrorMessage(e);
							}
							String message = "";
							int count = 0;
							do {
								message = Config.getSetting("AnnouncementMessage" + Utilities.getRandom(1, 5), type, bot);
								count = count++;
							} while (message == "" && count < 10);
							if (message != "")
								Utilities.sendMessage(type, bot, message);
						}
					}
				}
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					MJRBot.logErrorMessage(e, type, bot);
				}
			}
		} catch (Exception e) {
			MJRBot.logErrorMessage(e, type, bot);
		}
	}
}
