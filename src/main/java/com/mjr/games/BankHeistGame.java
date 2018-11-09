package com.mjr.games;

import java.util.HashMap;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.storage.EventLog;
import com.mjr.storage.EventLog.EventType;
import com.mjr.util.Utilities;
import com.mjr.storage.PointsSystem;

public class BankHeistGame {

    public static String[] messages = { "Bank Heist has been started!", "The crew have started robbing the City National Bank",
	    "The bank has been flooded with SWAT", "The crew have been shot down",
	    "The crew have made it out with all the money without being seen",
	    "The crew have made it out alive, dropping and leaving money on the way out",
	    "The SWAT are on high alert please wait 10 minutes before trying again" };

    public static void stage0(BotType type, String channelName) {
	Utilities.sendMessage(type, channelName, messages[0]); // Message saying Bank Heist has started
    }

    public static void stage1(BotType type, String channelName) {
	Utilities.sendMessage(type, channelName, messages[1]); // Message saying Bank Heist has started
    }

    public static void stage2(BotType type, String channelName, HashMap<String, Integer> enteredUsers) {
	int randNum = Utilities.getRandom(1, 2);

	if (randNum == 1) { // Flooded with SWAT
	    Utilities.sendMessage(type, channelName, BankHeistGame.messages[2]);
	    try {
		Thread.sleep(60000);
	    } catch (InterruptedException e) {
		MJRBot.getLogger().info(e.getMessage() + " " + e.getCause()); e.printStackTrace();
	    }
	    int randNum2 = Utilities.getRandom(1, 2);
	    if (randNum2 == 1)
		Utilities.sendMessage(type, channelName, BankHeistGame.messages[3]); // Crew were all killed
	    else if (randNum2 == 2) {
		Utilities.sendMessage(type, channelName, BankHeistGame.messages[5]); // Crew alive, dropping and leaving money on the way out
		giveOutWinnings(type, channelName, enteredUsers, false);
	    }
	} else if (randNum == 2) { // made it out with all the money
	    Utilities.sendMessage(type, channelName, BankHeistGame.messages[4]);
	    giveOutWinnings(type, channelName, enteredUsers, true);
	}
    }

    public static void giveOutWinnings(BotType type, String channelName, HashMap<String, Integer> enteredUsers, boolean highReward) {
	if (enteredUsers == null)
	    return;
	for (String user : enteredUsers.keySet()) {
	    int randPoints = 0;
	    int enteredPoints = enteredUsers.get(user);
	    if (highReward)
		randPoints = Utilities.getRandom(enteredPoints, enteredPoints * 4);
	    else
		randPoints = Utilities.getRandom(enteredPoints, enteredPoints * 2);
	    Utilities.sendMessage(type, channelName, user + " got " + randPoints + " points from the heist!");
	    EventLog.addEvent(channelName, user, "Won the Heist Game", EventType.Games);
	    PointsSystem.AddPointsWithEventMsg(user, randPoints, channelName);
	}
    }
}
