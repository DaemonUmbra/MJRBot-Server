package com.mjr.threads;

import com.mjr.MJRBot;
import com.mjr.files.Config;

public class Announcements extends Thread {
    private static long TimeDuration;
    private boolean Delay = true;

    @Override
    public void run() {
	while (MJRBot.getTwitchBot().ConnectedToChannel) {
	    if (Config.getSetting("Announcements").equalsIgnoreCase("true")) {
		TimeDuration = (Integer.parseInt(Config.getSetting("AnnouncementsDelay")) * 60) * 1000;
		if (Delay) {
		    try {
			Thread.sleep(TimeDuration);
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}
		MJRBot.getTwitchBot().MessageToChat("Test Announcement!");
	    }
	    try {
		Thread.sleep(TimeDuration);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }

	}
    }
}
