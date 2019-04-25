package com.mjr.mjrbot.games;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.util.MJRBotUtilities;

public class SlotMachine {
	public static String[] emotesTwtich = { "MrDestructoid", "PJSugar", "SSSsss", "PJSalt", "KappaPride", "StinkyCheese", "BloodTrail", "twitchRaid" };
	public static String[] emotesMixer = { ":hamster", ":controller", ":127", ":coke", ":popcorn", ":hug-bot", ":spaceship", ":wrench" };

	public static ResultPair Spin(BotType type) {
		int slot1 = MJRBotUtilities.getRandom(0, 7);
		int slot2 = MJRBotUtilities.getRandom(0, 7);
		int slot3 = MJRBotUtilities.getRandom(0, 7);
		if (type == BotType.Twitch)
			return new ResultPair(emotesTwtich[slot1] + " " + emotesTwtich[slot2] + " " + emotesTwtich[slot3], slot1 == slot2 && slot1 == slot3 ? true : false);
		else if (type == BotType.Mixer)
			return new ResultPair(emotesMixer[slot1] + " " + emotesMixer[slot2] + " " + emotesMixer[slot3], slot1 == slot2 && slot1 == slot3 ? true : false);
		return null;

	}
}
