package com.mjr.mjrbot.chatModeration;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.storage.Config;

public class SymbolChecker {

	public static boolean isSpammingSymbol(String message, String user, BotType type, Object bot) {
		int number = 0;
		for (int i = 0; i < message.length(); i++) {
			if (!(Character.isLetterOrDigit(message.charAt(i))) && !(message.charAt(i) == ' ')) {
				number++;
			}
		}
		if (number >= Integer.parseInt(Config.getSetting("MaxSymbols", type, bot)))
			return true;
		else
			return false;
	}
}
