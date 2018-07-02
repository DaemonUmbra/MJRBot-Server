package com.mjr.threads;

import java.util.HashMap;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.files.Config;
import com.mjr.files.PointsSystem;

public class PointsThread extends Thread {
    public static long TimeDuration;
    public static HashMap<String, Long> viewersJoinedTimes = new HashMap<String, Long>();
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
	    if (Config.getSetting("Points").equalsIgnoreCase("true")) {
		if (viewersJoinedTimes.isEmpty() == false) {
		    TimeDuration = (Integer.parseInt(Config.getSetting("AutoPointsDelay")) * 60) * 1000;
		    long timenow = System.currentTimeMillis();
		    if (type == BotType.Twitch) {
			TwitchBot twitchBot = MJRBot.getTwitchBotByChannelName(channelName);
			if (twitchBot.ConnectedToChannel && twitchBot.viewers != null && twitchBot.viewers.size() != 0) {
			    for (int i = 0; i < twitchBot.viewers.size(); i++) {
				if (viewersJoinedTimes.containsKey(twitchBot.viewers.get(i))) {
				    long oldtime = viewersJoinedTimes.get(twitchBot.viewers.get(i));
				    if ((timenow - oldtime) >= TimeDuration) {
					PointsSystem.AddPoints(twitchBot.viewers.get(i), 1, channelName);
					viewersJoinedTimes.put(twitchBot.viewers.get(i), System.currentTimeMillis());
				    }
				}
			    }
			}
		    } else if (type == BotType.Mixer) {
			MixerBot mixerBot = MJRBot.getMixerBotByChannelName(channelName);
			if (mixerBot.isConnected() && mixerBot.getViewers().size() != 0) {
			    for (int i = 0; i < mixerBot.getViewers().size(); i++) {
				if (viewersJoinedTimes.containsKey(mixerBot.getViewers().get(i))) {
				    long oldtime = viewersJoinedTimes.get(mixerBot.getViewers().get(i));
				    if ((timenow - oldtime) >= TimeDuration) {
					PointsSystem.AddPoints(mixerBot.getViewers().get(i), 1, channelName);
					viewersJoinedTimes.put(mixerBot.getViewers().get(i), System.currentTimeMillis());
				    }
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
