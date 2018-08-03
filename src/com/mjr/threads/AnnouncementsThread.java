package com.mjr.threads;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.storage.Config;
import com.mjr.Utilities;

public class AnnouncementsThread extends Thread {

    private BotType type;
    private String channelName;

    public AnnouncementsThread(BotType type, String channelName) {
	super();
	this.type = type;
	this.channelName = channelName;
    }

    @Override
    public void run() {
	while (type == BotType.Twitch ? MJRBot.getTwitchBotByChannelName(channelName).ConnectedToChannel
		: MJRBot.getMixerBotByChannelName(channelName).isConnected()) {
	    if (Config.getSetting("Announcements", channelName).equalsIgnoreCase("true")) {
		int delay = Integer.parseInt(Config.getSetting("AnnouncementsDelay", channelName));
		if (delay != 0) {
		    long TimeDuration = (delay * 60) * 1000;
		    try {
			Thread.sleep(TimeDuration);
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		    String message = "";
		    int count = 0;
		    do {
			message = Config.getSetting("AnnouncementMessage" + Utilities.getRandom(1, 5), channelName);
			count = count++;
		    } while (message == "" && count < 10);
		    if (message != "")
			Utilities.sendMessage(type, channelName, message);
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
