package com.mjr.mjrbot.threads;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.bases.MixerBot;
import com.mjr.mjrbot.bots.bases.TwitchBot;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.storage.EventLogManager;
import com.mjr.mjrbot.storage.EventLogManager.EventType;
import com.mjr.mjrbot.storage.PointsSystemManager;
import com.mjr.mjrbot.util.HTTPConnect;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.TwitchMixerAPICalls;

public class AutoPointsThread extends Thread {
	private BotType type;
	private Object bot;

	public AutoPointsThread(BotType type, Object bot, String channel) {
		super("AutoPointsThread for " + type.getTypeName() + "|" + channel);
		this.type = type;
		this.bot = bot;
	}

	@Override
	public void run() {
		while (type == BotType.Twitch ? ((TwitchBot) bot).isBotSetupCompleted() : ((MixerBot) bot).isConnected()) {
			try {
				if (ChannelConfigManager.getSetting("Points", type, bot).equalsIgnoreCase("true")) {
					int delay = Integer.parseInt(ChannelConfigManager.getSetting("AutoPointsDelay", type, bot));
					if (delay != 0) {
						long TimeDuration = (delay * 60) * 1000;
						long timenow = System.currentTimeMillis();
						boolean streaming = false;
						if (type == BotType.Twitch) {
							TwitchBot twitchBot = (TwitchBot) bot;
							if (twitchBot.isBotSetupCompleted() && !twitchBot.getTwitchData().getViewers().isEmpty() && !twitchBot.getTwitchData().viewersJoinedTimes.isEmpty()) {
								String result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetStreamsAPI(((TwitchBot) bot).getChannelID()));
								if (result.contains("created_at"))
									streaming = true;
								if (ChannelConfigManager.getSetting("AutoPointsWhenOffline", type, bot).equalsIgnoreCase("true") || streaming) {
									for (int i = 0; i < twitchBot.getTwitchData().getViewers().size(); i++) {
										if (twitchBot.getTwitchData().viewersJoinedTimes.containsKey(twitchBot.getTwitchData().getViewers().get(i))) {
											long oldtime = twitchBot.getTwitchData().viewersJoinedTimes.get(twitchBot.getTwitchData().getViewers().get(i));
											if ((timenow - oldtime) >= TimeDuration) {
												PointsSystemManager.AddPoints(twitchBot.getTwitchData().getViewers().get(i), 1, type, bot);
												twitchBot.getTwitchData().viewersJoinedTimes.put(twitchBot.getTwitchData().getViewers().get(i), System.currentTimeMillis());
											}
										}
									}
								}
							}
						} else if (type == BotType.Mixer) {
							MixerBot mixerBot = (MixerBot) bot;
							if (mixerBot.isConnected() && !mixerBot.getViewers().isEmpty() && !mixerBot.getMixerData().viewersJoinedTimes.isEmpty()) {
								streaming = mixerBot.isStreaming();
								if (ChannelConfigManager.getSetting("AutoPointsWhenOffline", type, bot).equalsIgnoreCase("true") || streaming) {
									for (int i = 0; i < mixerBot.getViewers().size(); i++) {
										if (mixerBot.getMixerData().viewersJoinedTimes.containsKey(mixerBot.getViewers().get(i))) {
											long oldtime = mixerBot.getMixerData().viewersJoinedTimes.get(mixerBot.getViewers().get(i));
											if ((timenow - oldtime) >= TimeDuration) {
												PointsSystemManager.AddPoints(mixerBot.getViewers().get(i), 1, type, bot);
												mixerBot.getMixerData().viewersJoinedTimes.put(mixerBot.getViewers().get(i), System.currentTimeMillis());
											}
										}
									}
								}
							}
						}
						if (ChannelConfigManager.getSetting("AutoPointsWhenOffline", type, bot).equalsIgnoreCase("true") || streaming)
							EventLogManager.addEvent(type, bot, "Current Viewers", "Added 1 Point to all current viewers (" + delay + " minutes Auto Points System)", EventType.Points);

						try {
							Thread.sleep(TimeDuration);
						} catch (InterruptedException e) {
							MJRBotUtilities.logErrorMessage(e, type, bot);
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
