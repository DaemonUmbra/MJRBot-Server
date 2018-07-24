package com.mjr.chatModeration;

import com.mjr.files.Config;

public class SymbolChecker {

    public static boolean isSpammingSymbol(String message, String user, String channelName) {
	int number = 0;
	for (int i = 0; i < message.length(); i++) {
	    if (!(Character.isLetterOrDigit(message.charAt(i))) && !(message.charAt(i) == ' ')) {
		number++;
	    }
	}
	if (number >= Integer.parseInt(Config.getSetting("MaxSymbols", channelName)))
	    return true;
	else
	    return false;
    }
}
