package com.mjr.threads;

import java.util.HashMap;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
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
	    if ((type == BotType.Twitch && MJRBot.getTwitchBotByChannelName(channelName).ConnectedToChannel)
		    || (MJRBot.getMixerBotByChannelName(channelName) != null && MJRBot.getMixerBotByChannelName(channelName).isConnected())) {
		if (Config.getSetting("Points").equalsIgnoreCase("true")) {
		    if (viewersJoinedTimes.isEmpty() == false) {
			TimeDuration = (Integer.parseInt(Config.getSetting("AutoPointsDelay")) * 60) * 1000;
			long timenow = System.currentTimeMillis();
			if (MJRBot.getTwitchBotByChannelName(channelName).ConnectedToChannel) {
			    for (int i = 0; i < MJRBot.getTwitchBotByChannelName(channelName).viewers.length; i++) {
				if (viewersJoinedTimes.containsKey(MJRBot.getTwitchBotByChannelName(channelName).viewers[i])) {
				    long oldtime = viewersJoinedTimes.get(MJRBot.getTwitchBotByChannelName(channelName).viewers[i]);
				    if ((timenow - oldtime) >= TimeDuration) {
					PointsSystem.AddPoints(MJRBot.getTwitchBotByChannelName(channelName).viewers[i], 1, channelName);
					viewersJoinedTimes.put(MJRBot.getTwitchBotByChannelName(channelName).viewers[i], System.currentTimeMillis());
				    }
				}
			    }
			} else if (MJRBot.getMixerBotByChannelName(channelName).isConnected()) {
			    for (int i = 0; i < MJRBot.getMixerBotByChannelName(channelName).getViewers().size(); i++) {
				if (viewersJoinedTimes.containsKey(MJRBot.getMixerBotByChannelName(channelName).getViewers().get(i))) {
				    long oldtime = viewersJoinedTimes.get(MJRBot.getMixerBotByChannelName(channelName).getViewers().get(i));
				    if ((timenow - oldtime) >= TimeDuration) {
					PointsSystem.AddPoints(MJRBot.getMixerBotByChannelName(channelName).getViewers().get(i), 1, channelName);
					viewersJoinedTimes.put(MJRBot.getMixerBotByChannelName(channelName).getViewers().get(i), System.currentTimeMillis());
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
	    }
	    try {
		Thread.sleep(60000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }
}
