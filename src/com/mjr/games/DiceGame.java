package com.mjr.games;

import com.mjr.Utilities;
import com.mjr.files.PointsSystem;

public class DiceGame {
    public static final int DEFAULT_MULTIIPLER = 1;

    public static double getWinPercent(double multi) {
	return (100 - DEFAULT_MULTIIPLER) / multi;
    }

    public static void procressTurn(String sender, int wager, double multi) {
	int randomNum = Utilities.getRandom(1, 100);
	if (PointsSystem.hasPoints(sender, wager)) {
	    PointsSystem.RemovePoints(sender, wager);

	    if (randomNum > getWinPercent(multi)) {
		int profit = (int) (wager * multi);
		PointsSystem.AddPoints(sender, profit);
		try {
		    Thread.sleep(5000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		Utilities.sendMessage("@" + sender + " Well Done, you have made a profit of " + profit + " points!");
	    } else {
		Utilities.sendMessage("@" + sender + " lost the wager!");
	    }
	}
    }
}
