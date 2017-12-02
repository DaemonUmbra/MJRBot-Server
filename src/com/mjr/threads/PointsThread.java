package com.mjr.threads;

import com.mjr.MJRBot;
import com.mjr.files.Config;
import com.mjr.files.PointsSystem;

public class PointsThread extends Thread {
    public static long TimeDuration;

    @Override
    public void run() {
	if (MJRBot.getTwitchBot() != null) {
	    while (MJRBot.getTwitchBot().ConnectedToChannel) {
		if (Config.getSetting("Points").equalsIgnoreCase("true")) {
		    if (MJRBot.getTwitchBot().viewersJoinedTimes.isEmpty() == false) {
			TimeDuration = (Integer.parseInt(Config.getSetting("AutoPointsDelay")) * 60) * 1000;
			long timenow = System.currentTimeMillis();
			for (int i = 0; i < MJRBot.getTwitchBot().viewers.length; i++) {
			    if (MJRBot.getTwitchBot().viewersJoinedTimes.containsKey(MJRBot.getTwitchBot().viewers[i])) {
				long oldtime = MJRBot.getTwitchBot().viewersJoinedTimes.get(MJRBot.getTwitchBot().viewers[i]);
				if ((timenow - oldtime) >= TimeDuration) {
				    PointsSystem.AddPoints(MJRBot.getTwitchBot().viewers[i], 1);
				    MJRBot.getTwitchBot().viewersJoinedTimes.put(MJRBot.getTwitchBot().viewers[i],
					    System.currentTimeMillis());
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
	}
    }
}
