package com.mjr.mjrbot.games;

import java.util.HashMap;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.storage.EventLogManager;
import com.mjr.mjrbot.storage.EventLogManager.EventType;
import com.mjr.mjrbot.storage.PointsSystemManager;
import com.mjr.mjrbot.util.MJRBotUtilities;

public class BankHeistGame {

	public static String[] messages = { "Bank Heist has been started!", "The crew have started robbing the City National Bank", "The bank has been flooded with SWAT", "The crew have been shot down",
			"The crew have made it out with all the money without being seen", "The crew have made it out alive, dropping and leaving money on the way out", "The SWAT are on high alert please wait 10 minutes before trying again" };

	public static void stage0(BotType type, Object bot) {
		ChatBotManager.sendMessage(type, bot, messages[0]); // Message saying Bank Heist has started
	}

	public static void stage1(BotType type, Object bot) {
		ChatBotManager.sendMessage(type, bot, messages[1]); // Message saying Bank Heist has started
	}

	public static void stage2(BotType type, Object bot, HashMap<String, Integer> enteredUsers) {
		int randNum = MJRBotUtilities.getRandom(1, 2);

		if (randNum == 1) { // Flooded with SWAT
			ChatBotManager.sendMessage(type, bot, BankHeistGame.messages[2]);
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				MJRBotUtilities.logErrorMessage(e);
			}
			int randNum2 = MJRBotUtilities.getRandom(1, 2);
			if (randNum2 == 1)
				ChatBotManager.sendMessage(type, bot, BankHeistGame.messages[3]); // Crew were all killed
			else if (randNum2 == 2) {
				ChatBotManager.sendMessage(type, bot, BankHeistGame.messages[5]); // Crew alive, dropping and leaving money on the way out
				giveOutWinnings(type, bot, enteredUsers, false);
			}
		} else if (randNum == 2) { // made it out with all the money
			ChatBotManager.sendMessage(type, bot, BankHeistGame.messages[4]);
			giveOutWinnings(type, bot, enteredUsers, true);
		}
	}

	public static void giveOutWinnings(BotType type, Object bot, HashMap<String, Integer> enteredUsers, boolean highReward) {
		if (enteredUsers == null)
			return;
		for (String user : enteredUsers.keySet()) {
			int randPoints = 0;
			int enteredPoints = enteredUsers.get(user);
			if (highReward)
				randPoints = MJRBotUtilities.getRandom(enteredPoints, enteredPoints * 4);
			else
				randPoints = MJRBotUtilities.getRandom(enteredPoints, enteredPoints * 2);
			ChatBotManager.sendMessage(type, bot, user + " got " + randPoints + " points from the heist!");
			EventLogManager.addEvent(type, bot, user, "Won the Heist Game", EventType.Games);
			PointsSystemManager.AddPointsWithEventMsg(user, randPoints, type, bot);
		}
	}
}
