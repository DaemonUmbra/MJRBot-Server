package com.mjr.mjrbot.threads;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.bases.MixerBot;
import com.mjr.mjrbot.bots.bases.TwitchBot;
import com.mjr.mjrbot.games.BankHeistGame;
import com.mjr.mjrbot.util.MJRBotUtilities;

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
			MJRBotUtilities.logErrorMessage(e, type, bot);
		}

		BankHeistGame.stage0(type, bot);

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			MJRBotUtilities.logErrorMessage(e, type, bot);
		}

		BankHeistGame.stage1(type, bot);

		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			MJRBotUtilities.logErrorMessage(e, type, bot);
		}

		if (type == BotType.Twitch) {
			BankHeistGame.stage2(type, bot, ((TwitchBot) bot).getTwitchData().bankHeistEnteredUsers);
		} else {
			BankHeistGame.stage2(type, bot, ((MixerBot) bot).getMixerData().bankHeistEnteredUsers);
		}
		gameActive = false;
	}
}
