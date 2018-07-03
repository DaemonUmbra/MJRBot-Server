package com.mjr.threads;

import java.util.HashMap;

import com.mjr.MJRBot.BotType;
import com.mjr.games.BankHeistGame;

public class BankHeistThread extends Thread {

    private static HashMap<String, Integer> enteredUsers = new HashMap<String, Integer>(); // TODO: Made this not static

    public static boolean GameActive = false;

    private BotType type;
    private String channelName;

    public BankHeistThread(BotType type, String channelName) {
	super();
	this.type = type;
	this.channelName = channelName;
    }

    @Override
    public void run() {
	while (GameActive) {

	    try {
		Thread.sleep(60000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }

	    BankHeistGame.stage0(type, channelName);

	    try {
		Thread.sleep(10000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }

	    BankHeistGame.stage1(type, channelName);

	    try {
		Thread.sleep(60000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }

	    BankHeistGame.stage2(type, channelName, enteredUsers);

	    enteredUsers.clear();
	    GameActive = false;

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
