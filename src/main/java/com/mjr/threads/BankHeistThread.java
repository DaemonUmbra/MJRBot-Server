package com.mjr.threads;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.games.BankHeistGame;

public class BankHeistThread extends Thread {

	private BotType type;
	private Object bot;
	public boolean gameActive;

	public BankHeistThread(BotType type, Object bot, String channel) {
		super("BankHeistThread for " + type.getTypeName() + "|" + channel);
		this.type = type;
		this.bot = bot;
	}

	@Override
	public void run() {
		gameActive = true;
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			MJRBot.logErrorMessage(e, type, bot);
		}

		BankHeistGame.stage0(type, bot);

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			MJRBot.logErrorMessage(e, type, bot);
		}

		BankHeistGame.stage1(type, bot);

		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			MJRBot.logErrorMessage(e, type, bot);
		}

		if (type == BotType.Twitch) {
			BankHeistGame.stage2(type, bot, ((TwitchBot) bot).bankHeistEnteredUsers);
		} else {
			BankHeistGame.stage2(type, bot, ((MixerBot) bot).bankHeistEnteredUsers);
		}
		gameActive = false;
	}
}
