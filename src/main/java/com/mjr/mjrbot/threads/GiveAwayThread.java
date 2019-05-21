package com.mjr.mjrbot.threads;

import java.util.Random;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.bases.MixerBot;
import com.mjr.mjrbot.bots.bases.TwitchBot;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.util.MJRBotUtilities;

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
			ChatBotManager.sendMessage(type, bot, "Giveaway will end in " + ChannelConfigManager.getSetting("GiveawayDelay", type, bot) + " minutes. To enter use !enter");
			try {
				Thread.sleep((Integer.parseInt(ChannelConfigManager.getSetting("GiveawayDelay", type, bot)) * 60) * 1000);
			} catch (InterruptedException e) {
				MJRBotUtilities.logErrorMessage(e, type, bot);
			}
			if (type == BotType.Twitch) {
				TwitchBot twitchBot = (TwitchBot) bot;
				if (twitchBot.getTwitchData().giveawayEnteredUsers.size() > 0) {
					ChatBotManager.sendMessage(type, bot, "Giveaway has ended! " + twitchBot.getTwitchData().giveawayEnteredUsers.size() + " entered in to the giveaway!");
					Random random = new Random();
					int userNumber = random.nextInt((twitchBot.getTwitchData().giveawayEnteredUsers.size() - 0) + 1) + 0;
					ChatBotManager.sendMessage(type, bot, "@" + twitchBot.getTwitchData().giveawayEnteredUsers.get(userNumber) + " has won the giveaway!");
				} else {
					ChatBotManager.sendMessage(type, bot, "Giveaway canceled due to no users entered!");
				}
				twitchBot.getTwitchData().giveAwayActive = false;
				twitchBot.getTwitchData().giveawayEnteredUsers.clear();
			} else {
				MixerBot mixerBot = (MixerBot) bot;
				if (mixerBot.getMixerData().giveawayEnteredUsers.size() > 0) {
					ChatBotManager.sendMessage(type, bot, "Giveaway has ended! " + mixerBot.getMixerData().giveawayEnteredUsers.size() + " entered in to the giveaway!");
					Random random = new Random();
					int userNumber = random.nextInt((mixerBot.getMixerData().giveawayEnteredUsers.size() - 0) + 1) + 0;
					ChatBotManager.sendMessage(type, bot, "@" + mixerBot.getMixerData().giveawayEnteredUsers.get(userNumber) + " has won the giveaway!");
				} else {
					ChatBotManager.sendMessage(type, bot, "Giveaway canceled due to no users entered!");
				}
				mixerBot.getMixerData().giveAwayActive = false;
				mixerBot.getMixerData().giveawayEnteredUsers.clear();
			}
		} catch (Exception e) {
			MJRBotUtilities.logErrorMessage(e, type, bot);
		}
	}
}
