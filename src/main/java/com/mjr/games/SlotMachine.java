package com.mjr.games;

import com.mjr.MJRBot.BotType;
import com.mjr.util.Utilities;

public class SlotMachine {
    public static String[] emotesTwtich = { "MrDestructoid", "PJSugar", "SSSsss", "PJSalt", "KappaPride", "StinkyCheese", "FunRun",
	    "twitchRaid" };
    public static String[] emotesMixer = { ":hamster", ":controller", ":127", ":coke", ":popcorn", ":hug-bot", ":spaceship", ":wrench" };

    public static ResultPair Spin(BotType type) {
	int slot1 = Utilities.getRandom(0, 7);
	int slot2 = Utilities.getRandom(0, 7);
	int slot3 = Utilities.getRandom(0, 7);
	if (type == BotType.Twitch)
	    return new ResultPair(emotesTwtich[slot1] + " " + emotesTwtich[slot2] + " " + emotesTwtich[slot3],
		    slot1 == slot2 && slot1 == slot3 ? true : false);
	else
	    return new ResultPair(emotesMixer[slot1] + " " + emotesMixer[slot2] + " " + emotesMixer[slot3],
		    slot1 == slot2 && slot1 == slot3 ? true : false);

    }
}
