package com.mjr.threads;

import java.util.HashMap;

import com.mjr.ChatBotManager;
import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.sql.SQLUtilities;
import com.mjr.storage.ConfigMain;
import com.mjr.util.ConsoleUtil;

public class ChannelListUpdateThread extends Thread {

	@Override
	public void run() {
		while (true) {
			try {
				HashMap<String, String> channelListTwitch = SQLUtilities.getChannelsTwitch();
				HashMap<String, String> channelListMixer = SQLUtilities.getChannelsMixer();
				HashMap<String, TwitchBot> currentChannelListTwitch = ChatBotManager.getTwitchBots();
				HashMap<String, MixerBot> currentChannelListMixer = ChatBotManager.getMixerBots();

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
						ChatBotManager.createBot(channelName, channelListTwitch.get(channelName));
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
						ChatBotManager.createBot(channelName, channelListMixer.get(channelName));
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
						TwitchBot bot = ChatBotManager.getTwitchBotByChannelName(removeChannel);
						bot.disconnectTwitch();
						ChatBotManager.removeTwitchBot(removeChannel);
					} else if (channelsToDisconnect.get(removeChannel).equalsIgnoreCase(BotType.Mixer.getTypeName())) {
						MixerBot bot = ChatBotManager.getMixerBotByChannelName(removeChannel);
						bot.disconnectMixer();
						ChatBotManager.removeMixerBot(removeChannel);
					}
				}
				try {
					Thread.sleep(Integer.parseInt(ConfigMain.getSetting("UpdateChannelFromDatabaseTime(Seconds)")) * 1000);
				} catch (InterruptedException e) {
					MJRBot.logErrorMessage(e);
				}
			} catch (Exception e) {
				MJRBot.logErrorMessage(e);
			}
		}
	}
}