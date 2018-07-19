package com.mjr.threads;

import java.util.HashMap;
import java.util.Iterator;

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
		TwitchBot twitchBot = channelListTwitch.get(channelNameMain);
		Iterator<String> iter = twitchBot.usersCooldowns.keySet().iterator();
		while (iter.hasNext()) {
		    String user = iter.next();
		    int oldTime = twitchBot.usersCooldowns.get(user);
		    if (oldTime > 0) {
			oldTime = oldTime - 1;
			if (twitchBot.usersCooldowns.containsKey(user)) {
			    iter.remove();
			    twitchBot.usersCooldowns.put(user, oldTime);
			}
		    }
		    iter.remove();
		}
	    }

	    for (String channelNameMain : channelListMixer.keySet()) {
		MixerBot mixerBot = channelListMixer.get(channelNameMain);
		Iterator<String> iter = mixerBot.usersCooldowns.keySet().iterator();
		while (iter.hasNext()) {
		    String user = iter.next();
		    int oldTime = mixerBot.usersCooldowns.get(user);
		    if (oldTime > 0) {
			oldTime = oldTime - 1;
			if (mixerBot.usersCooldowns.containsKey(user)) {
			    iter.remove();
			    mixerBot.usersCooldowns.put(user, oldTime);
			}
		    }
		    iter.remove();
		}
	    }
	    try {
		Thread.sleep(1000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }
}
