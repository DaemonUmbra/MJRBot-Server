package com.mjr.threads;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
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
			MJRBot.logErrorMessage(e);
		}
		Utilities.sendMessage(type, channelName, "Race is about to start! Make sure to get your bets in now!");
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			MJRBot.logErrorMessage(e);
		}
		if (type == BotType.Twitch)
			MJRBot.getTwitchBotByChannelName(channelName).racingGame.start(type, channelName);
		else
			MJRBot.getMixerBotByChannelName(channelName).racingGame.start(type, channelName);
	}
}
