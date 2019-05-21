package com.mjr.mjrbot.threads;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.util.MJRBotUtilities;

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
			MJRBotUtilities.logErrorMessage(e, type, bot);
		}
		try {
			ChatBotManager.sendMessage(type, bot, "Race is about to start! Make sure to get your bets in now!");
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				MJRBotUtilities.logErrorMessage(e, type, bot);
			}
			if (type == BotType.Twitch)
				((TwitchBot) bot).racingGame.start(type, bot);
			else if (type == BotType.Mixer)
				((MixerBot) bot).racingGame.start(type, bot);
		} catch (Exception e) {
			MJRBotUtilities.logErrorMessage(e, type, bot);
		}
	}
}
