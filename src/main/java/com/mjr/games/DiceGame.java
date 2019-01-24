package com.mjr.games;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.storage.EventLog;
import com.mjr.storage.EventLog.EventType;
import com.mjr.storage.PointsSystem;
import com.mjr.util.Utilities;

public class DiceGame {
	public static final int DEFAULT_MULTIIPLER = 1;

	public static int getWinPercent(double multi) {
		return ((int) ((100 - DEFAULT_MULTIIPLER) / multi)) / 2;
	}

	public static void procressTurn(BotType type, Object bot, String sender, int wager, double multi) {
		if (PointsSystem.hasPoints(sender, wager, type, bot)) {
			int randomNum = Utilities.getRandom(1, 100);
			PointsSystem.RemovePoints(sender, wager, type, bot);

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				MJRBot.getLogger().info(e.getMessage() + " " + e.getCause());
				e.printStackTrace();
			}
			if (randomNum < getWinPercent(multi)) {
				int profit = (int) (wager * multi);
				PointsSystem.AddPointsWithEventMsg(sender, profit, type, bot);
				Utilities.sendMessage(type, bot, "@" + sender + " Well Done, you have made a profit of " + (profit - wager) + " points! Your current points is: " + PointsSystem.getPoints(sender, type, bot));
				EventLog.addEvent(type, bot, sender, "Won the Dice Game", EventType.Games);
			} else {
				Utilities.sendMessage(type, bot, "@" + sender + " lost the wager! Your current points is: " + PointsSystem.getPoints(sender, type, bot));
				EventLog.addEvent(type, bot, sender, "Lost the Dice Game", EventType.Games);
			}
		} else
			Utilities.sendMessage(type, bot, "@" + sender + " you currently have insufficient points! You only have " + PointsSystem.getPoints(sender, type, bot));
	}
}
