package com.mjr.games;

import java.util.HashMap;

import com.mjr.MJRBot.BotType;
import com.mjr.Utilities;
import com.mjr.files.PointsSystem;

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
	    int randNum = Utilities.getRandom(2, 5);
	    int randNum2 = 0;
	    Utilities.sendMessage(type, channelName, BankHeistGame.messages[randNum]);
	    
	    if (randNum == 2) { // Flooded with SWAT
		try {
		    Thread.sleep(60000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		    randNum2 = Utilities.getRandom(1, 3);
		    if (randNum2 == 1)
			Utilities.sendMessage(type, channelName, BankHeistGame.messages[3]); // Crew were all killed
		    else {
			Utilities.sendMessage(type, channelName, BankHeistGame.messages[5]); // Crew alive, dropping and leaving money on the way out
			giveOutWinnings(type, channelName, enteredUsers, false);
		    }
	    } else if (randNum == 4) { // made it out with all the money 
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
	    PointsSystem.AddPoints(user, randPoints, channelName);
	}
    }
}
