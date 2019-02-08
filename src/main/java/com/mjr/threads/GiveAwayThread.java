package com.mjr.threads;

import java.util.Random;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.storage.Config;
import com.mjr.util.Utilities;

public class GiveAwayThread extends Thread {
	private BotType type;
	private Object bot;

	public GiveAwayThread(BotType type, Object bot, String channel) {
		super("GiveAwayThread for " + type.getTypeName() + "|" + channel);
		this.type = type;
		this.bot = bot;
	}

	@Override
	public void run() {
		try {
			Utilities.sendMessage(type, bot, "Giveaway will end in " + Config.getSetting("GiveawayDelay", type, bot) + " minutes. To enter use !enter");
			try {
				Thread.sleep((Integer.parseInt(Config.getSetting("GiveawayDelay", type, bot)) * 60) * 1000);
			} catch (InterruptedException e) {
				MJRBot.logErrorMessage(e, type, bot);
			}
			if (type == BotType.Twitch) {
				TwitchBot twitchBot = (TwitchBot) bot;
				if (twitchBot.giveawayEnteredUsers.size() > 0) {
					Utilities.sendMessage(type, bot, "Giveaway has ended! " + twitchBot.giveawayEnteredUsers.size() + " entered in to the giveaway!");
					Random random = new Random();
					int userNumber = random.nextInt((twitchBot.giveawayEnteredUsers.size() - 0) + 1) + 0;
					Utilities.sendMessage(type, bot, "@" + twitchBot.giveawayEnteredUsers.get(userNumber) + " has won the giveaway!");
				} else {
					Utilities.sendMessage(type, bot, "Giveaway canceled due to no users entered!");
				}
				twitchBot.giveAwayActive = false;
				twitchBot.giveawayEnteredUsers.clear();
			} else {
				MixerBot mixerBot = (MixerBot) bot;
				if (mixerBot.giveawayEnteredUsers.size() > 0) {
					Utilities.sendMessage(type, bot, "Giveaway has ended! " + mixerBot.giveawayEnteredUsers.size() + " entered in to the giveaway!");
					Random random = new Random();
					int userNumber = random.nextInt((mixerBot.giveawayEnteredUsers.size() - 0) + 1) + 0;
					Utilities.sendMessage(type, bot, "@" + mixerBot.giveawayEnteredUsers.get(userNumber) + " has won the giveaway!");
				} else {
					Utilities.sendMessage(type, bot, "Giveaway canceled due to no users entered!");
				}
				mixerBot.giveAwayActive = false;
				mixerBot.giveawayEnteredUsers.clear();
			}
		} catch (Exception e) {
			MJRBot.logErrorMessage(e, type, bot);
		}
	}
}
