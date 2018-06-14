package com.mjr.games;

import com.mjr.MJRBot.BotType;
import com.mjr.Utilities;

public class FruitMachine {

    public static int Slot1;

    public static int Slot2;

    public static int Slot3;

    public static String[] emotesTwtich = { "MrDestructoid", "PJSugar", "SSSsss", "PJSalt", "KappaPride", "StinkyCheese", "FunRun",
	    "twitchRaid" };
    public static String[] emotesMixer = { ":hamster", ":controller", ":127", ":coke", ":popcorn", ":hug-bot", ":spaceship", ":wrench" };

    public static int timesLost = 0;

    public static String Spin(BotType type) {
	Slot1 = Utilities.getRandom(0, 7);
	Slot2 = Utilities.getRandom(0, 7);
	Slot3 = Utilities.getRandom(0, 7);
	if (type == BotType.Twitch)
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
