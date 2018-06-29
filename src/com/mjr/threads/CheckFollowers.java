package com.mjr.threads;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.TwitchBot;
import com.mjr.files.Config;

public class CheckFollowers extends Thread {
    private BotType type;
    private TwitchBot bot;

    public CheckFollowers(TwitchBot bot, BotType type) {
	super();
	this.type = type;
	this.bot = bot;
    }

    @Override
    public void run() {
	while (true) {
	    if (type == BotType.Twitch && MJRBot.getTwitchBotByChannelName(bot.channelName).ConnectedToChannel) {
		if (Config.getSetting("FollowerCheck").equalsIgnoreCase("true")) {
		    if (MJRBot.getTwitchBotByChannelName(bot.channelName).viewers != null) {
			for (int i = 0; i < MJRBot.getTwitchBotByChannelName(bot.channelName).viewers.length; i++) {
			    Followers.checkFollower(bot, MJRBot.getTwitchBotByChannelName(bot.channelName).viewers[i].toLowerCase());
			}
			try {
			    Thread.sleep(10000);
			} catch (InterruptedException e) {
			    e.printStackTrace();
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
