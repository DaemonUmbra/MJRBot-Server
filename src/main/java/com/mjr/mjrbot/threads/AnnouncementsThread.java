package com.mjr.mjrbot.threads;

import java.util.ArrayList;
import java.util.List;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.util.HTTPConnect;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.TwitchMixerAPICalls;

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
		while (type == BotType.Twitch ? ((TwitchBot) bot).isBotSetupCompleted() : ((MixerBot) bot).isConnected()) {
			try {
				if (ChannelConfigManager.getSetting("Announcements", type, bot).equalsIgnoreCase("true")) {
					boolean streaming = false;
					if (type == BotType.Twitch) {
						String result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetStreamsAPI(((TwitchBot) bot).getChannelID()));
						if (result.contains("created_at"))
							streaming = true;
					} else {
						streaming = ((MixerBot) bot).isStreaming();
					}
					if (ChannelConfigManager.getSetting("AnnouncementsWhenOffline", type, bot).equalsIgnoreCase("true") || streaming) {
						int delay = Integer.parseInt(ChannelConfigManager.getSetting("AnnouncementsDelay", type, bot));
						if (delay != 0) {
							long TimeDuration = (delay * 60) * 1000;
							try {
								Thread.sleep(TimeDuration);
							} catch (InterruptedException e) {
								MJRBotUtilities.logErrorMessage(e);
							}
							String message = "";

							List<String> validMessages = new ArrayList<String>();
							for (int i = 1; i < 6; i++) {
								message = ChannelConfigManager.getSetting("AnnouncementMessage" + i, type, bot);
								if (message != null && message != "")
									validMessages.add(message);
							}
							if (validMessages.size() != 0)
								MJRBotUtilities.sendMessage(type, bot, validMessages.get(MJRBotUtilities.getRandom(0, validMessages.size() - 1)));
						}
					}
				}
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					MJRBotUtilities.logErrorMessage(e, type, bot);
				}
			} catch (Exception e) {
				MJRBotUtilities.logErrorMessage(e, type, bot);
			}
		}
	}
}
