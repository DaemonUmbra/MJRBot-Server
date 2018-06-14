package com.mjr.games;

import com.mjr.Utilities;
import com.mjr.MJRBot.BotType;
import com.mjr.files.PointsSystem;

public class DiceGame {
    public static final int DEFAULT_MULTIIPLER = 1;

    public static int getWinPercent(double multi) {
	return (int) ((100 - DEFAULT_MULTIIPLER) / multi);
    }

    public static void procressTurn(BotType type, String channelName, String sender, int wager, double multi) {
	if (PointsSystem.hasPoints(sender, wager)) {
	    int randomNum = Utilities.getRandom(1, 100);
	    PointsSystem.RemovePoints(sender, wager);

	    try {
		Thread.sleep(2000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    if (randomNum < getWinPercent(multi)) {
		int profit = (int) (wager * multi);
		PointsSystem.AddPoints(sender, profit);
		Utilities.sendMessage(type, channelName, "@" + sender + " Well Done, you have made a profit of " + (profit - wager)
			+ " points! Your current points is: " + PointsSystem.getPoints(sender));
	    } else {
		Utilities.sendMessage(type, channelName, "@" + sender + " lost the wager! Your current points is: " + PointsSystem.getPoints(sender));
	    }
	}
	else
	    Utilities.sendMessage(type, channelName, "@" + sender + " you currently have insufficient points! You only have " + PointsSystem.getPoints(sender));
    }
}
