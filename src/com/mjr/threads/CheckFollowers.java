package com.mjr.threads;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.files.Config;

public class CheckFollowers extends Thread {
    private BotType type;
    private String channelName;
    
    public CheckFollowers(BotType type, String channelName) {
	super();
	this.type = type;
	this.channelName = channelName;
    }

    @Override
    public void run() {
	while (true) {
	    if (type == BotType.Twitch && MJRBot.getTwitchBotByChannelName(channelName).ConnectedToChannel) {
		if (Config.getSetting("FollowerCheck").equalsIgnoreCase("true")) {
		    if (Followers.followers != null) {
			if (MJRBot.getTwitchBotByChannelName(channelName).viewers != null) {
			    for (int i = 0; i < MJRBot.getTwitchBotByChannelName(channelName).viewers.length; i++) {
				Followers.checkFollower(channelName, MJRBot.getTwitchBotByChannelName(channelName).viewers[i]);
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
	    try {
		Thread.sleep(60000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }
}
