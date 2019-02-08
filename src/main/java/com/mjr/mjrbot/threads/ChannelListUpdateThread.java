package com.mjr.mjrbot.threads;

import java.util.HashMap;

import com.mjr.mjrbot.ChatBotManager;
import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MixerBot;
import com.mjr.mjrbot.TwitchBot;
import com.mjr.mjrbot.sql.SQLUtilities;
import com.mjr.mjrbot.storage.ConfigMain;
import com.mjr.mjrbot.util.ConsoleUtil;

public class ChannelListUpdateThread extends Thread {
	
	public ChannelListUpdateThread() {
		super("ChannelListUpdate");
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				HashMap<Integer, String> channelListTwitch = SQLUtilities.getChannelsTwitch();
				HashMap<String, String> channelListMixer = SQLUtilities.getChannelsMixer();
				HashMap<Integer, TwitchBot> currentChannelListTwitch = ChatBotManager.getTwitchBots();
				HashMap<String, MixerBot> currentChannelListMixer = ChatBotManager.getMixerBots();

				// Check for new channels
				ConsoleUtil.textToConsole("Update Channels List");
				boolean found = false;
				for (Integer channelID : channelListTwitch.keySet()) {
					found = false;
					for (Integer channelIDMain : currentChannelListTwitch.keySet()) {
						if (channelIDMain.equals(channelID)) {
							found = true;
						}
					}
					if (found == false) {
						ChatBotManager.createBot(null, channelID, channelListTwitch.get(channelID));
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
						ChatBotManager.createBot(channelName, 0, channelListMixer.get(channelName));
					}
				}

				// Check for removed channels
				HashMap<Integer, String> channelsToDisconnectTwitch = new HashMap<Integer, String>();
				HashMap<String, String> channelsToDisconnectMixer = new HashMap<String, String>();
				for (Integer channelNameMain : currentChannelListTwitch.keySet()) {
					if (!channelListTwitch.containsKey(channelNameMain)) {
						channelsToDisconnectTwitch.put(channelNameMain, BotType.Twitch.getTypeName());
					}
				}
				for (String channelNameMain : currentChannelListMixer.keySet()) {
					if (!channelListMixer.containsKey(channelNameMain)) {
						channelsToDisconnectMixer.put(channelNameMain, BotType.Mixer.getTypeName());
					}
				}

				for (Integer removeChannel : channelsToDisconnectTwitch.keySet()) {
					TwitchBot bot = ChatBotManager.getTwitchBotByChannelID(removeChannel);
					bot.disconnectTwitch();
					ChatBotManager.removeTwitchBot(removeChannel);
				}
				for (String removeChannel : channelsToDisconnectMixer.keySet()) {
					MixerBot bot = ChatBotManager.getMixerBotByChannelName(removeChannel);
					bot.disconnectMixer();
					ChatBotManager.removeMixerBot(removeChannel);
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