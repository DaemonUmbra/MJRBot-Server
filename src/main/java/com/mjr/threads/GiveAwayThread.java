package com.mjr.threads;

import java.util.Random;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.storage.Config;
import com.mjr.util.Utilities;

public class GiveAwayThread extends Thread {
    private BotType type;
    private String channelName;

    public GiveAwayThread(BotType type, String channelName) {
	super();
	this.type = type;
	this.channelName = channelName;
    }

    @Override
    public void run() {
	Utilities.sendMessage(type, channelName,
		"Giveaway will end in " + Config.getSetting("GiveawayDelay", channelName) + " minutes. To enter use !enter");
	try {
	    Thread.sleep((Integer.parseInt(Config.getSetting("GiveawayDelay", channelName)) * 60) * 1000);
	} catch (InterruptedException e) {
	    MJRBot.getLogger().info(e.getMessage() + " " + e.getCause()); e.printStackTrace();
	}
	if (type == BotType.Twitch) {
	    TwitchBot twitchBot = MJRBot.getTwitchBotByChannelName(channelName);
	    if (twitchBot.giveawayEnteredUsers.size() > 0) {
		Utilities.sendMessage(type, channelName,
			"Giveaway has ended! " + twitchBot.giveawayEnteredUsers.size() + " entered in to the giveaway!");
		Random random = new Random();
		int userNumber = random.nextInt((twitchBot.giveawayEnteredUsers.size() - 0) + 1) + 0;
		Utilities.sendMessage(type, channelName, "@" + twitchBot.giveawayEnteredUsers.get(userNumber) + " has won the giveaway!");
	    } else {
		Utilities.sendMessage(type, channelName, "Giveaway canceled due to no users entered!");
	    }
	    twitchBot.giveAwayActive = false;
	    twitchBot.giveawayEnteredUsers.clear();
	} else {
	    MixerBot mixerBot = MJRBot.getMixerBotByChannelName(channelName);
	    if (mixerBot.giveawayEnteredUsers.size() > 0) {
		Utilities.sendMessage(type, channelName,
			"Giveaway has ended! " + mixerBot.giveawayEnteredUsers.size() + " entered in to the giveaway!");
		Random random = new Random();
		int userNumber = random.nextInt((mixerBot.giveawayEnteredUsers.size() - 0) + 1) + 0;
		Utilities.sendMessage(type, channelName, "@" + mixerBot.giveawayEnteredUsers.get(userNumber) + " has won the giveaway!");
	    } else {
		Utilities.sendMessage(type, channelName, "Giveaway canceled due to no users entered!");
	    }
	    mixerBot.giveAwayActive = false;
	    mixerBot.giveawayEnteredUsers.clear();
	}
    }
}
