package com.mjr;

import com.mjr.MJRBot.BotType;

public class Utilities {

    public static int getRandom(int min, int max) {
	if (min > max) {
	    throw new IllegalArgumentException("Min " + min + " greater than max " + max);
	}
	return (int) (min + Math.random() * ((long) max - min + 1));
    }

    public static boolean isNumeric(String str) {
	try {
	    double d = Double.parseDouble(str);
	} catch (NumberFormatException nfe) {
	    return false;
	}
	return true;
    }

    public static void sendMessage(BotType type, String channelName, String endMessage) {
	if (type == BotType.Twitch)
	    MJRBot.getTwitchBotByChannelName(channelName).sendMessage(endMessage);
	else
	    MJRBot.getMixerBotByChannelName(channelName).sendMessage(endMessage);
    }
}
