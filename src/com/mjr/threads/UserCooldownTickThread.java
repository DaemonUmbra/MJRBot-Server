package com.mjr.threads;

import java.util.HashMap;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;

public class UserCooldownTickThread extends Thread {
    @Override
    public void run() {
	while (true) {
	    HashMap<String, TwitchBot> channelListTwitch = MJRBot.getTwitchBots();
	    HashMap<String, MixerBot> channelListMixer = MJRBot.getMixerBots();

	    for (String channelNameMain : channelListTwitch.keySet()) {
		TwitchBot twitchBot = ((TwitchBot) channelListTwitch.get(channelNameMain));
		HashMap<String, Integer> users = twitchBot.usersCooldowns;
		for (String user : users.keySet()) {
		    int oldTime = users.get(user);
		    if (oldTime != 0) {
			users.remove(user);
			users.put(user, oldTime--);
		    }
		}
		twitchBot.usersCooldowns = users;
	    } // TODO Do for Mixer
	    try {
		Thread.sleep(1000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }
}
