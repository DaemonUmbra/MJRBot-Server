package com.mjr.games;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.Utilities;
import com.mjr.storage.EventLog;
import com.mjr.storage.EventLog.EventType;
import com.mjr.storage.PointsSystem;

public class DiceGame {
    public static final int DEFAULT_MULTIIPLER = 1;

    public static int getWinPercent(double multi) {
	return ((int) ((100 - DEFAULT_MULTIIPLER) / multi)) / 2;
    }

    public static void procressTurn(BotType type, String channelName, String sender, int wager, double multi) {
	if (PointsSystem.hasPoints(sender, wager, channelName)) {
	    int randomNum = Utilities.getRandom(1, 100);
	    PointsSystem.RemovePoints(sender, wager, channelName);

	    try {
		Thread.sleep(2000);
	    } catch (InterruptedException e) {
		MJRBot.getLogger().info(e.getMessage() + " " + e.getCause()); e.printStackTrace();
	    }
	    if (randomNum < getWinPercent(multi)) {
		int profit = (int) (wager * multi);
		PointsSystem.AddPointsWithEventMsg(sender, profit, channelName);
		Utilities.sendMessage(type, channelName, "@" + sender + " Well Done, you have made a profit of " + (profit - wager)
			+ " points! Your current points is: " + PointsSystem.getPoints(sender, channelName));
		EventLog.addEvent(channelName, sender , "Won the Dice Game", EventType.Games);
	    } else {
		Utilities.sendMessage(type, channelName,
			"@" + sender + " lost the wager! Your current points is: " + PointsSystem.getPoints(sender, channelName));
		EventLog.addEvent(channelName, sender , "Lost the Dice Game", EventType.Games);
	    }
	} else
	    Utilities.sendMessage(type, channelName,
		    "@" + sender + " you currently have insufficient points! You only have " + PointsSystem.getPoints(sender, channelName));
    }
}
