package com.mjr.threads;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.storage.Config;
import com.mjr.storage.EventLog;
import com.mjr.storage.EventLog.EventType;
import com.mjr.storage.PointsSystem;
import com.mjr.util.HTTPConnect;
import com.mjr.util.TwitchMixerAPICalls;

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
		try {
			while (type == BotType.Twitch ? ((TwitchBot) bot).ConnectedToChannel : ((MixerBot) bot).isConnected()) {
				if (Config.getSetting("Points", type, bot).equalsIgnoreCase("true")) {
					int delay = Integer.parseInt(Config.getSetting("AutoPointsDelay", type, bot));
					if (delay != 0) {
						long TimeDuration = (delay * 60) * 1000;
						long timenow = System.currentTimeMillis();
						boolean streaming = false;
						if (type == BotType.Twitch) {
							TwitchBot twitchBot = (TwitchBot) bot;
							if (twitchBot.ConnectedToChannel && !twitchBot.viewers.isEmpty() && !twitchBot.viewersJoinedTimes.isEmpty()) {
								String result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetStreamsAPI(((TwitchBot) bot).channelID));
								if (result.contains("created_at"))
									streaming = true;
								if (Config.getSetting("AutoPointsWhenOffline", type, bot).equalsIgnoreCase("true") || streaming) {
									for (int i = 0; i < twitchBot.viewers.size(); i++) {
										if (twitchBot.viewersJoinedTimes.containsKey(twitchBot.viewers.get(i))) {
											long oldtime = twitchBot.viewersJoinedTimes.get(twitchBot.viewers.get(i));
											if ((timenow - oldtime) >= TimeDuration) {
												PointsSystem.AddPoints(twitchBot.viewers.get(i), 1, type, bot);
												twitchBot.viewersJoinedTimes.put(twitchBot.viewers.get(i), System.currentTimeMillis());
											}
										}
									}
								}
							}
						} else if (type == BotType.Mixer) {
							MixerBot mixerBot = (MixerBot) bot;
							if (mixerBot.isConnected() && !mixerBot.getViewers().isEmpty() && !mixerBot.viewersJoinedTimes.isEmpty()) {
								streaming = mixerBot.isStreaming();
								if (Config.getSetting("AutoPointsWhenOffline", type, bot).equalsIgnoreCase("true") || streaming) {
									for (int i = 0; i < mixerBot.getViewers().size(); i++) {
										if (mixerBot.viewersJoinedTimes.containsKey(mixerBot.getViewers().get(i))) {
											long oldtime = mixerBot.viewersJoinedTimes.get(mixerBot.getViewers().get(i));
											if ((timenow - oldtime) >= TimeDuration) {
												PointsSystem.AddPoints(mixerBot.getViewers().get(i), 1, type, bot);
												mixerBot.viewersJoinedTimes.put(mixerBot.getViewers().get(i), System.currentTimeMillis());
											}
										}
									}
								}
							}
						}
						if (Config.getSetting("AutoPointsWhenOffline", type, bot).equalsIgnoreCase("true") || streaming)
							EventLog.addEvent(type, bot, "Current Viewers", "Added 1 Point to all current viewers (" + delay + " minutes Auto Points System)", EventType.Points);

						try {
							Thread.sleep(TimeDuration);
						} catch (InterruptedException e) {
							MJRBot.logErrorMessage(e, type, bot);
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
