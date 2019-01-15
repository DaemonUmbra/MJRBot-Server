package com.mjr.threads;

import com.mjr.ChatBotManager;
import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.util.Utilities;

public class RaceStartThread extends Thread {
	private BotType type;
	private String channelName;

	public RaceStartThread(BotType type, String channelName) {
		super();
		this.type = type;
		this.channelName = channelName;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			MJRBot.logErrorMessage(e, type, channelName);
		}
		try {
			Utilities.sendMessage(type, channelName, "Race is about to start! Make sure to get your bets in now!");
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				MJRBot.logErrorMessage(e, type, channelName);
			}
			if (type == BotType.Twitch)
				ChatBotManager.getTwitchBotByChannelName(channelName).racingGame.start(type, channelName);
			else
				ChatBotManager.getMixerBotByChannelName(channelName).racingGame.start(type, channelName);
		} catch (Exception e) {
			MJRBot.logErrorMessage(e, type, channelName);
		}
	}
}
