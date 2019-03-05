package com.mjr.mjrbot.games;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.storage.EventLogManager;
import com.mjr.mjrbot.storage.EventLogManager.EventType;
import com.mjr.mjrbot.storage.PointsSystemManager;
import com.mjr.mjrbot.util.MJRBotUtilities;

public class DiceGame {
	public static final int DEFAULT_MULTIIPLER = 1;

	public static int getWinPercent(double multi) {
		return ((int) ((100 - DEFAULT_MULTIIPLER) / multi)) / 2;
	}

	public static void procressTurn(BotType type, Object bot, String sender, int wager, double multi) {
		if (PointsSystemManager.hasPoints(sender, wager, type, bot)) {
			int randomNum = MJRBotUtilities.getRandom(1, 100);
			PointsSystemManager.RemovePoints(sender, wager, type, bot);

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				MJRBot.getLogger().info(e.getMessage() + " " + e.getCause());
				e.printStackTrace();
			}
			if (randomNum < getWinPercent(multi)) {
				int profit = (int) (wager * multi);
				PointsSystemManager.AddPointsWithEventMsg(sender, profit, type, bot);
				MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Well Done, you have made a profit of " + (profit - wager) + " points! Your current points is: " + PointsSystemManager.getPoints(sender, type, bot));
				EventLogManager.addEvent(type, bot, sender, "Won the Dice Game", EventType.Games);
			} else {
				MJRBotUtilities.sendMessage(type, bot, "@" + sender + " lost the wager! Your current points is: " + PointsSystemManager.getPoints(sender, type, bot));
				EventLogManager.addEvent(type, bot, sender, "Lost the Dice Game", EventType.Games);
			}
		} else
			MJRBotUtilities.sendMessage(type, bot, "@" + sender + " you currently have insufficient points! You only have " + PointsSystemManager.getPoints(sender, type, bot));
	}
}
