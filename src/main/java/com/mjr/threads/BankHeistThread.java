package com.mjr.threads;

import com.mjr.ChatBotManager;
import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.games.BankHeistGame;

public class BankHeistThread extends Thread {

	private BotType type;
	private String channelName;
	public boolean gameActive;

	public BankHeistThread(BotType type, String channelName) {
		super();
		this.type = type;
		this.channelName = channelName;
	}

	@Override
	public void run() {
		gameActive = true;
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			MJRBot.logErrorMessage(e, type, channelName);
		}

		BankHeistGame.stage0(type, channelName);

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			MJRBot.logErrorMessage(e, type, channelName);
		}

		BankHeistGame.stage1(type, channelName);

		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			MJRBot.logErrorMessage(e, type, channelName);
		}

		if (type == BotType.Twitch) {
			BankHeistGame.stage2(type, channelName, ChatBotManager.getTwitchBotByChannelName(channelName).bankHeistEnteredUsers);
		} else {
			BankHeistGame.stage2(type, channelName, ChatBotManager.getMixerBotByChannelName(channelName).bankHeistEnteredUsers);
		}
		gameActive = false;
	}
}
