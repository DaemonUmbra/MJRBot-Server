package com.mjr.threads;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.util.Utilities;

public class RaceStartThread extends Thread {
	private BotType type;
	private Object bot;

	public RaceStartThread(BotType type, Object bot, String channel) {
		super("RaceStartThread for " + type.getTypeName() + "|" + channel);
		this.type = type;
		this.bot = bot;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			MJRBot.logErrorMessage(e, type, bot);
		}
		try {
			Utilities.sendMessage(type, bot, "Race is about to start! Make sure to get your bets in now!");
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				MJRBot.logErrorMessage(e, type, bot);
			}
			if (type == BotType.Twitch)
				((TwitchBot) bot).racingGame.start(type, bot);
			else if (type == BotType.Mixer)
				((MixerBot) bot).racingGame.start(type, bot);
		} catch (Exception e) {
			MJRBot.logErrorMessage(e, type, bot);
		}
	}
}
