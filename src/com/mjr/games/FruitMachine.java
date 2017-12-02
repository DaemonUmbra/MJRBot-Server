package com.mjr.games;

import java.util.Random;

import com.mjr.MJRBot;

public class FruitMachine {

    public static int Slot1;

    public static int Slot2;

    public static int Slot3;

    public static String[] emotesTwtich = { "MrDestructoid", "PJSugar", "SSSsss", "PJSalt", "KappaPride", "StinkyCheese", "FunRun",
	    "twitchRaid" };
    public static String[] emotesMixer = { ":hamster", ":controller", ":127", ":coke", ":popcorn", ":hug-bot", ":spaceship",
	    ":wrench" };
    
    public static int timesLost = 0;

    public static String Spin() {
	Slot1 = 0;
	Slot2 = 0;
	Slot3 = 0;
	Random random = new Random();
	Slot1 = random.nextInt((7)) + 1;
	Slot2 = random.nextInt((7)) + 1;
	Slot3 = random.nextInt((7)) + 1;
	if (MJRBot.getTwitchBot() != null)
	    return emotesTwtich[Slot1] + " " + emotesTwtich[Slot2] + " " + emotesTwtich[Slot3];
	else
	    return emotesMixer[Slot1] + " " + emotesMixer[Slot2] + " " + emotesMixer[Slot3];

    }

    public static boolean hasWon() {
	if (Slot1 == Slot2 && Slot1 == Slot3) {
	    timesLost = 0;
	    return true;
	} else {
	    timesLost++;
	    return false;
	}
    }
}
