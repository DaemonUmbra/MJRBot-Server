package com.mjr.threads;

import java.util.HashMap;

import com.mjr.ConsoleUtil;
import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.sql.SQLUtilities;

public class ChannelListUpdateThread extends Thread {

    @Override
    public void run() {
	while (true) {
	    HashMap<String, String> channelList = SQLUtilities.getChannels();
	    HashMap<String, TwitchBot> channelListTwitch = MJRBot.getTwitchBots();
	    HashMap<String, MixerBot> channelListMixer = MJRBot.getMixerBots();

	    // Check for new channels
	    ConsoleUtil.TextToConsole("Update Channels List", "Bot", null);
	    boolean found = false;
	    for (String channelName : channelList.keySet()) {
		found = false;
		for (String channelNameMain : channelListTwitch.keySet()) {
		    if (channelNameMain.equalsIgnoreCase(channelName)) {
			found = true;
		    }
		}
		for (String channelNameMain : channelListMixer.keySet()) {
		    if (channelNameMain.equalsIgnoreCase(channelName)) {
			found = true;
		    }
		}
		if (found == false) {
		    MJRBot.createBot(channelName, channelList.get(channelName));
		}
	    }

	    // Check for removed channels
	    HashMap<String, String> channelsToDisconnect = new HashMap<String, String>();
	    for (String channelNameMain : channelListTwitch.keySet()) {
		if (!channelList.containsKey(channelNameMain)) {
		    channelsToDisconnect.put(channelNameMain, BotType.Twitch.getTypeName());
		}
	    }
	    for (String channelNameMain : channelListMixer.keySet()) {
		if (!channelList.containsKey(channelNameMain)) {
		    channelsToDisconnect.put(channelNameMain, BotType.Mixer.getTypeName());
		}
	    }

	    for (String removeChannel : channelsToDisconnect.keySet()) {
		if (channelsToDisconnect.get(removeChannel).equalsIgnoreCase(BotType.Twitch.getTypeName())) {
		    TwitchBot bot = MJRBot.getTwitchBotByChannelName(removeChannel);
		    bot.disconnectTwitch();
		    MJRBot.removeTwitchBot(removeChannel);
		} else if (channelsToDisconnect.get(removeChannel).equalsIgnoreCase(BotType.Mixer.getTypeName())) {
		    MixerBot bot = MJRBot.getMixerBotByChannelName(removeChannel);
		    bot.disconnect();
		    MJRBot.removeMixerBot(removeChannel);
		}
	    }
	    try {
		Thread.sleep(10000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }
}