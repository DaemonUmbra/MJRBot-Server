package com.mjr.threads;

import com.mjr.MJRBot;
import com.mjr.files.Config;

public class CheckFollowers extends Thread {
    @Override
    public void run() {
	if (MJRBot.getTwitchBot() != null) {
	    while (MJRBot.getTwitchBot().ConnectedToChannel) {
		if (Config.getSetting("FollowerCheck").equalsIgnoreCase("true")) {
		    if (Followers.followers != null) {
			if (MJRBot.getTwitchBot().viewers != null) {
			    for (int i = 0; i < MJRBot.getTwitchBot().viewers.length; i++) {
				Followers.checkFollower(MJRBot.getTwitchBot().viewers[i]);
			    }
			    try {
				Thread.sleep(10000);
			    } catch (InterruptedException e) {
				e.printStackTrace();
			    }
			}
		    }
		}
	    }
	}
    }
}
