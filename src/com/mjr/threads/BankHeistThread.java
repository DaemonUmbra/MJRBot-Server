package com.mjr.threads;

import java.util.HashMap;

import com.mjr.MJRBot;
import com.mjr.Utilities;
import com.mjr.files.PointsSystem;

public class BankHeistThread extends Thread {

    private static HashMap<String, Integer> enteredUsers = new HashMap<String, Integer>();

    public static String[] messages = { "Bank Heist has been started!", "The crew have started robbing the City National Bank",
	    "The bank has been flooded with SWAT", "The crew have been shot down",
	    "The crew have made it out with all the money without being seen",
	    "The crew have made it out alive, dropping and leaving money on the way out",
	    "The SWAT are on high alert please wait 10 minutes before trying again" };

    public static int path = 0;
    public static int stage = 0;
    public static int randNum2 = 0;

    public static boolean GameActive = false;

    @Override
    public void run() {
	while (GameActive) {
	    try {
		Thread.sleep(60000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    Utilities.sendMessage(messages[0]);
	    stage = 1;
	    try {
		Thread.sleep(10000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    if (stage == 1) {
		Utilities.sendMessage(messages[1]);
		stage = 2;
		try {
		    Thread.sleep(60000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		if (stage == 2) {
		    int randNum = Utilities.getRandom(2, 5);
		    Utilities.sendMessage(messages[randNum]);
		    if (randNum == 2) {
			stage = 3;
			try {
			    Thread.sleep(60000);
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
			if (stage == 3) {
			    randNum2 = Utilities.getRandom(1, 3);

			    if (randNum2 == 1)
				Utilities.sendMessage(messages[3]);
			    else
				Utilities.sendMessage(messages[5]);
			    path = 2;
			}
		    } else if (randNum == 4) {
			path = 1;
		    }
		    if (randNum == 4 || randNum == 2 && randNum2 == 2) {
			if (enteredUsers == null)
			    return;
			for (String user : enteredUsers.keySet()) {
			    int randPoints = 0;
			    int enteredPoints = enteredUsers.get(user);
			    if (path == 1)
				Utilities.getRandom(enteredPoints, enteredPoints * 4);
			    else
				Utilities.getRandom(enteredPoints, enteredPoints * 2);
			    Utilities.sendMessage(user + " got " + randPoints + " points from the heist!");
			    PointsSystem.AddPoints(user, randPoints);
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

    public static HashMap<String, Integer> getEnteredUsers() {
	return enteredUsers;
    }

    public static void setEnteredUsers(HashMap<String, Integer> enteredUsers) {
	BankHeistThread.enteredUsers = enteredUsers;
    }

    public static void addEnteredUser(String name, int points) {
	BankHeistThread.enteredUsers.put(name, points);
    }
}
