package com.mjr.threads;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.storage.Config;
import com.mjr.storage.EventLog;
import com.mjr.storage.EventLog.EventType;
import com.mjr.storage.PointsSystem;

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
	while (type == BotType.Twitch ? MJRBot.getTwitchBotByChannelName(channelName).ConnectedToChannel
		: MJRBot.getMixerBotByChannelName(channelName).isConnected()) {
	    if (Config.getSetting("Points", channelName).equalsIgnoreCase("true")) {
		int delay = Integer.parseInt(Config.getSetting("AutoPointsDelay", channelName));
		if (delay != 0) {
		    long TimeDuration = (delay * 60) * 1000;
		    long timenow = System.currentTimeMillis();
		    if (type == BotType.Twitch) {
			TwitchBot twitchBot = MJRBot.getTwitchBotByChannelName(channelName);
			if (twitchBot.ConnectedToChannel && !twitchBot.viewers.isEmpty() && !twitchBot.viewersJoinedTimes.isEmpty()) {
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
		    } else if (type == BotType.Mixer) {
			MixerBot mixerBot = MJRBot.getMixerBotByChannelName(channelName);
			if (mixerBot.isConnected() && !mixerBot.getViewers().isEmpty() && !mixerBot.viewersJoinedTimes.isEmpty()) {
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

		    EventLog.addEvent(channelName, "Current Viewers",
			    "Added 1 Point to all current viewers (" + delay + " minutes Auto Points System)", EventType.Points);

		    try {
			Thread.sleep(TimeDuration);
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}
	    }
	    try {
		Thread.sleep(60000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }
}
