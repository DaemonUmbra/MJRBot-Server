package com.mjr.threads;

import java.util.ArrayList;

import com.mjr.MJRBot;
import com.mjr.files.PointsSystem;

public class BankHeistThread extends Thread {

    public static ArrayList<String> enteredUsers = new ArrayList<String>();

    public static String[] messages = { "Bank Heist has been started!", "The crew have started robbing the City National Bank",
	    "The bank has been flooded with SWAT", "The crew have been shot down",
	    "The crew have made it out with all the money without being seen", "The crew have made it out but leaving money behide",
	    "The SWAT are on high alert please wait 10 minutes before trying again" };

    public static int path = 0;
    public static int stage = 0;
    public static int randNum2 = 0;

    public static boolean GameActive = false;

    @Override
    public void run() {
	while (GameActive) {
	    MJRBot.getTwitchBot().MessageToChat(
		    enteredUsers.get(0) + " has started planning a heist!" + " To join the crew enter !BankHeist you only have 1 minute!");
	    try {
		Thread.sleep(60000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    MJRBot.getTwitchBot().MessageToChat(messages[0]);
	    stage = 1;
	    try {
		Thread.sleep(10000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    if (stage == 1) {
		MJRBot.getTwitchBot().MessageToChat(messages[1]);
		stage = 2;
		try {
		    Thread.sleep(60000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		if (stage == 2) {
		    int randNum = random_int(2, 5);
		    MJRBot.getTwitchBot().MessageToChat(messages[randNum]);
		    if (randNum == 2) {
			stage = 3;
			try {
			    Thread.sleep(60000);
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
			if (stage == 3) {
			    randNum2 = random_int(1, 3);

			    if (randNum2 == 1)
				MJRBot.getTwitchBot().MessageToChat(messages[3]);
			    else
				MJRBot.getTwitchBot().MessageToChat(messages[5]);
			    path = 2;
			}
		    } else if (randNum == 4) {
			path = 1;
		    }
		    if (randNum == 4 || randNum == 2 && randNum2 == 2) {
			int max = 0;
			int min = 0;
			if (path == 1) {
			    max = 200;
			    min = 125;
			} else {
			    max = 100;
			    min = 50;
			}
			if (enteredUsers == null)
			    return;
			for (int i = 0; i < enteredUsers.size(); i++) {
			    int randNum1 = random_int(min, max + 1);
			    PointsSystem.AddPoints(enteredUsers.get(i), randNum1);
			    MJRBot.getTwitchBot().MessageToChat(enteredUsers.get(i) + " got " + randNum1 + " points from the heist!");
			}
		    }
		    stage = 0;
		    randNum = 0;
		    randNum2 = 0;
		    enteredUsers.clear();
		    GameActive = false;
		}
	    }
	}
    }

    public static int random_int(int Min, int Max) {
	return (int) (Math.random() * (Max - Min)) + Min;
    }

}
