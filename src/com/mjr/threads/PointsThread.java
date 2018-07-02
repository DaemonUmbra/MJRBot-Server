package com.mjr.threads;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.files.Config;
import com.mjr.files.PointsSystem;

public class PointsThread extends Thread {
    public static long TimeDuration;
    private BotType type;
    private String channelName;

    public PointsThread(BotType type, String channelName) {
	super();
	this.type = type;
	this.channelName = channelName;
    }

    @Override
    public void run() {
	while (true) {
	    if (Config.getSetting("Points", channelName).equalsIgnoreCase("true")) {
		TimeDuration = (Integer.parseInt(Config.getSetting("AutoPointsDelay", channelName)) * 60) * 1000;
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
		}
		else if (type == BotType.Mixer) {
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

		try {
		    Thread.sleep(TimeDuration);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    } else {
		try {
		    Thread.sleep(60000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}
    }
}
