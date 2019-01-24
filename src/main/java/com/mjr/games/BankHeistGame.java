package com.mjr.games;

import java.util.HashMap;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.storage.EventLog;
import com.mjr.storage.EventLog.EventType;
import com.mjr.storage.PointsSystem;
import com.mjr.util.Utilities;

public class BankHeistGame {

	public static String[] messages = { "Bank Heist has been started!", "The crew have started robbing the City National Bank", "The bank has been flooded with SWAT", "The crew have been shot down",
			"The crew have made it out with all the money without being seen", "The crew have made it out alive, dropping and leaving money on the way out", "The SWAT are on high alert please wait 10 minutes before trying again" };

	public static void stage0(BotType type, Object bot) {
		Utilities.sendMessage(type, bot, messages[0]); // Message saying Bank Heist has started
	}

	public static void stage1(BotType type, Object bot) {
		Utilities.sendMessage(type, bot, messages[1]); // Message saying Bank Heist has started
	}

	public static void stage2(BotType type, Object bot, HashMap<String, Integer> enteredUsers) {
		int randNum = Utilities.getRandom(1, 2);

		if (randNum == 1) { // Flooded with SWAT
			Utilities.sendMessage(type, bot, BankHeistGame.messages[2]);
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				MJRBot.logErrorMessage(e);
			}
			int randNum2 = Utilities.getRandom(1, 2);
			if (randNum2 == 1)
				Utilities.sendMessage(type, bot, BankHeistGame.messages[3]); // Crew were all killed
			else if (randNum2 == 2) {
				Utilities.sendMessage(type, bot, BankHeistGame.messages[5]); // Crew alive, dropping and leaving money on the way out
				giveOutWinnings(type, bot, enteredUsers, false);
			}
		} else if (randNum == 2) { // made it out with all the money
			Utilities.sendMessage(type, bot, BankHeistGame.messages[4]);
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
				randPoints = Utilities.getRandom(enteredPoints, enteredPoints * 4);
			else
				randPoints = Utilities.getRandom(enteredPoints, enteredPoints * 2);
			Utilities.sendMessage(type, bot, user + " got " + randPoints + " points from the heist!");
			EventLog.addEvent(type, bot, user, "Won the Heist Game", EventType.Games);
			PointsSystem.AddPointsWithEventMsg(user, randPoints, type, bot);
		}
	}
}
