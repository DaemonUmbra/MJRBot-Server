package com.mjr.threads;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.Utilities;
import com.mjr.files.Config;

public class AnnouncementsThread extends Thread {

    private static long TimeDuration;
    private boolean Delay = true;

    private BotType type;
    private String channelName;

    public AnnouncementsThread(BotType type, String channelName) {
	super();
	this.type = type;
	this.channelName = channelName;
    }

    @Override
    public void run() {
	while (true) {
	    if ((type == BotType.Twitch && MJRBot.getTwitchBotByChannelName(channelName).ConnectedToChannel)
		    || (MJRBot.getMixerBotByChannelName(channelName) != null
			    && MJRBot.getMixerBotByChannelName(channelName).isConnected())) {
		if (Config.getSetting("Announcements").equalsIgnoreCase("true")) {
		    TimeDuration = (Integer.parseInt(Config.getSetting("AnnouncementsDelay")) * 60) * 1000;
		    if (Delay) {
			try {
			    Thread.sleep(TimeDuration);
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
		    }
		    Utilities.sendMessage(type, channelName, Config.getSetting("AnnouncementMessage" + Utilities.getRandom(1, 5)));
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
