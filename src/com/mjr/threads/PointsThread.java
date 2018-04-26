package com.mjr.threads;

import java.util.HashMap;

import com.mjr.MJRBot;
import com.mjr.files.Config;
import com.mjr.files.PointsSystem;

public class PointsThread extends Thread {
    public static long TimeDuration;
    public static HashMap<String, Long> viewersJoinedTimes = new HashMap<String, Long>();

    @Override
    public void run() {
	while (true) {
	    if ((MJRBot.getTwitchBot() != null && MJRBot.getTwitchBot().ConnectedToChannel)
		    || (MJRBot.getMixerBot() != null && MJRBot.getMixerBot().isConnected())) {
		if (Config.getSetting("Points").equalsIgnoreCase("true")) {
		    if (viewersJoinedTimes.isEmpty() == false) {
			TimeDuration = (Integer.parseInt(Config.getSetting("AutoPointsDelay")) * 60) * 1000;
			long timenow = System.currentTimeMillis();
			if (MJRBot.getTwitchBot().ConnectedToChannel) {
			    for (int i = 0; i < MJRBot.getTwitchBot().viewers.length; i++) {
				if (viewersJoinedTimes.containsKey(MJRBot.getTwitchBot().viewers[i])) {
				    long oldtime = viewersJoinedTimes.get(MJRBot.getTwitchBot().viewers[i]);
				    if ((timenow - oldtime) >= TimeDuration) {
					PointsSystem.AddPoints(MJRBot.getTwitchBot().viewers[i], 1);
					viewersJoinedTimes.put(MJRBot.getTwitchBot().viewers[i], System.currentTimeMillis());
				    }
				}
			    }
			} else if (MJRBot.getMixerBot().isConnected()) {
			    for (int i = 0; i < MJRBot.getMixerBot().getViewers().size(); i++) {
				if (viewersJoinedTimes.containsKey(MJRBot.getMixerBot().getViewers().get(i))) {
				    long oldtime = viewersJoinedTimes.get(MJRBot.getMixerBot().getViewers().get(i));
				    if ((timenow - oldtime) >= TimeDuration) {
					PointsSystem.AddPoints(MJRBot.getMixerBot().getViewers().get(i), 1);
					viewersJoinedTimes.put(MJRBot.getMixerBot().getViewers().get(i), System.currentTimeMillis());
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
