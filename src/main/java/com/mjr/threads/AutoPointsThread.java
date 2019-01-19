package com.mjr.threads;

import com.mjr.ChatBotManager;
import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.storage.Config;
import com.mjr.storage.EventLog;
import com.mjr.storage.EventLog.EventType;
import com.mjr.storage.PointsSystem;
import com.mjr.util.HTTPConnect;

public class AutoPointsThread extends Thread {
	private BotType type;
	private String channelName;

	public AutoPointsThread(BotType type, String channelName) {
		super();
		this.type = type;
		this.channelName = channelName;
	}

	@Override
	public void run() {
		try {
			while (type == BotType.Twitch ? ChatBotManager.getTwitchBotByChannelName(channelName).ConnectedToChannel : ChatBotManager.getMixerBotByChannelName(channelName).isConnected()) {
				if (Config.getSetting("Points", channelName).equalsIgnoreCase("true")) {
					int delay = Integer.parseInt(Config.getSetting("AutoPointsDelay", channelName));
					if (delay != 0) {
						long TimeDuration = (delay * 60) * 1000;
						long timenow = System.currentTimeMillis();
						boolean streaming = false;
						if (type == BotType.Twitch) {
							TwitchBot twitchBot = ChatBotManager.getTwitchBotByChannelName(channelName);
							if (twitchBot.ConnectedToChannel && !twitchBot.viewers.isEmpty() && !twitchBot.viewersJoinedTimes.isEmpty()) {
								String result = HTTPConnect.getRequest("https://api.twitch.tv/kraken/streams/" + channelName + "?client_id=" + MJRBot.CLIENT_ID);
								if (result.contains("created_at"))
									streaming = true;
								if (Config.getSetting("AutoPointsWhenOffline", channelName).equalsIgnoreCase("true") || streaming) {
									for (int i = 0; i < twitchBot.viewers.size(); i++) {
										if (twitchBot.viewersJoinedTimes.containsKey(twitchBot.viewers.get(i))) {
											long oldtime = twitchBot.viewersJoinedTimes.get(twitchBot.viewers.get(i));
											if ((timenow - oldtime) >= TimeDuration) {
												PointsSystem.AddPoints(twitchBot.viewers.get(i), 1, channelName);
												twitchBot.viewersJoinedTimes.put(twitchBot.viewers.get(i), System.currentTimeMillis());
											}
										}
									}
								}
							}
						} else if (type == BotType.Mixer) {
							MixerBot mixerBot = ChatBotManager.getMixerBotByChannelName(channelName);
							if (mixerBot.isConnected() && !mixerBot.getViewers().isEmpty() && !mixerBot.viewersJoinedTimes.isEmpty()) {
								streaming = mixerBot.isStreaming();
								if (Config.getSetting("AutoPointsWhenOffline", channelName).equalsIgnoreCase("true") || streaming) {
									for (int i = 0; i < mixerBot.getViewers().size(); i++) {
										if (mixerBot.viewersJoinedTimes.containsKey(mixerBot.getViewers().get(i))) {
											long oldtime = mixerBot.viewersJoinedTimes.get(mixerBot.getViewers().get(i));
											if ((timenow - oldtime) >= TimeDuration) {
												PointsSystem.AddPoints(mixerBot.getViewers().get(i), 1, channelName);
												mixerBot.viewersJoinedTimes.put(mixerBot.getViewers().get(i), System.currentTimeMillis());
											}
										}
									}
								}
							}
						}
						if (Config.getSetting("AutoPointsWhenOffline", channelName).equalsIgnoreCase("true") || streaming)
							EventLog.addEvent(channelName, "Current Viewers", "Added 1 Point to all current viewers (" + delay + " minutes Auto Points System)", EventType.Points);

						try {
							Thread.sleep(TimeDuration);
						} catch (InterruptedException e) {
							MJRBot.logErrorMessage(e, type, channelName);
						}
					}
				}
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					MJRBot.logErrorMessage(e, type, channelName);
				}
			}
		} catch (Exception e) {
			MJRBot.logErrorMessage(e, type, channelName);
		}
	}
}
