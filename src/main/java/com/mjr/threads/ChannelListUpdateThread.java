package com.mjr.threads;

import java.util.HashMap;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.sql.SQLUtilities;
import com.mjr.storage.ConfigMain;
import com.mjr.util.ConsoleUtil;

public class ChannelListUpdateThread extends Thread {

    @Override
    public void run() {
	while (true) {
	    HashMap<String, String> channelListTwitch = SQLUtilities.getChannelsTwitch();
	    HashMap<String, String> channelListMixer = SQLUtilities.getChannelsMixer();
	    HashMap<String, TwitchBot> currentChannelListTwitch = MJRBot.getTwitchBots();
	    HashMap<String, MixerBot> currentChannelListMixer = MJRBot.getMixerBots();

	    // Check for new channels
	    ConsoleUtil.textToConsole("Update Channels List");
	    boolean found = false;
	    for (String channelName : channelListTwitch.keySet()) {
		found = false;
		for (String channelNameMain : currentChannelListTwitch.keySet()) {
		    if (channelNameMain.equalsIgnoreCase(channelName)) {
			found = true;
		    }
		}
		if (found == false) {
		    MJRBot.createBot(channelName, channelListTwitch.get(channelName));
		}
	    }

	    for (String channelName : channelListMixer.keySet()) {
		found = false;
		for (String channelNameMain : currentChannelListMixer.keySet()) {
		    if (channelNameMain.equalsIgnoreCase(channelName)) {
			found = true;
		    }
		}
		if (found == false) {
		    MJRBot.createBot(channelName, channelListMixer.get(channelName));
		}
	    }

	    // Check for removed channels
	    HashMap<String, String> channelsToDisconnect = new HashMap<String, String>();
	    for (String channelNameMain : currentChannelListTwitch.keySet()) {
		if (!channelListTwitch.containsKey(channelNameMain)) {
		    channelsToDisconnect.put(channelNameMain, BotType.Twitch.getTypeName());
		}
	    }
	    for (String channelNameMain : currentChannelListMixer.keySet()) {
		if (!channelListMixer.containsKey(channelNameMain)) {
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
		    bot.disconnectMixer();
		    MJRBot.removeMixerBot(removeChannel);
		}
	    }
	    try {
		Thread.sleep(Integer.parseInt(ConfigMain.getSetting("UpdateChannelFromDatabaseTime(Seconds)")) * 1000);
	    } catch (InterruptedException e) {
		MJRBot.getLogger().info(e.getMessage() + " " + e.getCause()); e.printStackTrace();
	    }
	}
    }
}