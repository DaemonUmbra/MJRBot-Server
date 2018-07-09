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
		TwitchBot twitchBot = channelListTwitch.get(channelNameMain);
		for (String user : twitchBot.usersCooldowns.keySet()) {
		    int oldTime = twitchBot.usersCooldowns.get(user);
		    if (oldTime > 0) {
			oldTime = oldTime - 1;
			if (twitchBot.usersCooldowns.containsKey(user))
			    twitchBot.usersCooldowns.remove(user);
			twitchBot.usersCooldowns.put(user, oldTime);
		    }
		}
	    }

	    for (String channelNameMain : channelListMixer.keySet()) {
		MixerBot mixerBot = channelListMixer.get(channelNameMain);
		for (String user : mixerBot.usersCooldowns.keySet()) {
		    int oldTime = mixerBot.usersCooldowns.get(user);
		    if (oldTime > 0) {
			oldTime = oldTime - 1;
			if (mixerBot.usersCooldowns.containsKey(user))
			    mixerBot.usersCooldowns.remove(user);
			mixerBot.usersCooldowns.put(user, oldTime);
		    }
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
