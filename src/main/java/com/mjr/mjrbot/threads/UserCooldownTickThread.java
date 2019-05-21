package com.mjr.mjrbot.threads;

import java.util.HashMap;
import java.util.Iterator;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.bases.MixerBot;
import com.mjr.mjrbot.bots.bases.TwitchBot;
import com.mjr.mjrbot.util.MJRBotUtilities;

public class UserCooldownTickThread extends Thread {

	public UserCooldownTickThread() {
		super("UserCooldownTickThread");
	}

	@Override
	public void run() {
		while (true) {
			try {
				HashMap<Integer, TwitchBot> channelListTwitch = ChatBotManager.getTwitchBots();
				HashMap<String, MixerBot> channelListMixer = ChatBotManager.getMixerBots();

				for (Integer channelNameMain : channelListTwitch.keySet()) {
					TwitchBot twitchBot = channelListTwitch.get(channelNameMain);
					Iterator<String> iter = twitchBot.getTwitchData().usersCooldowns.keySet().iterator();
					while (iter.hasNext()) {
						String user = iter.next();
						int oldTime = twitchBot.getTwitchData().usersCooldowns.get(user);
						if (oldTime > 0) {
							oldTime = oldTime - 1;
							if (twitchBot.getTwitchData().usersCooldowns.containsKey(user)) {
								twitchBot.getTwitchData().usersCooldowns.put(user, oldTime);
							}
						}
						iter.remove();
					}
				}

				for (String channelNameMain : channelListMixer.keySet()) {
					MixerBot mixerBot = channelListMixer.get(channelNameMain);
					Iterator<String> iter = mixerBot.getMixerData().usersCooldowns.keySet().iterator();
					while (iter.hasNext()) {
						String user = iter.next();
						int oldTime = mixerBot.getMixerData().usersCooldowns.get(user);
						if (oldTime > 0) {
							oldTime = oldTime - 1;
							if (mixerBot.getMixerData().usersCooldowns.containsKey(user)) {
								mixerBot.getMixerData().usersCooldowns.put(user, oldTime);
							}
						}
						iter.remove();
					}
				}
			} catch (Exception e) {
				MJRBotUtilities.logErrorMessage(e);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				MJRBotUtilities.logErrorMessage(e);
			}
		}
	}
}
