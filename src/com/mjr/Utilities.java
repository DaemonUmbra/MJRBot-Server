package com.mjr;

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
    
    public static void sendMessage(String endMessage, Object bot){
	if (MJRBot.getTwitchBot() != null)
	    ((TwitchBot) bot).MessageToChat(endMessage);
	else
	    ((MixerBot) bot).sendMessage(endMessage);
    }
}
