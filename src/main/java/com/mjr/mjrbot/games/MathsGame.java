package com.mjr.mjrbot.games;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.util.ConsoleUtil;
import com.mjr.mjrbot.util.ConsoleUtil.MessageType;
import com.mjr.mjrbot.util.MJRBotUtilities;

public class MathsGame {
	public int rannum1;
	public int rannum2;
	public int ransign;
	public String sign = "+";
	public boolean isGameActive = false;

	public int Answer;

	public String CreateQuestion(BotType type, Object bot) {
		rannum1 = MJRBotUtilities.getRandom(0, 100);
		rannum2 = MJRBotUtilities.getRandom(0, 100);
		ransign = MJRBotUtilities.getRandom(1, 3);

		switch (ransign) {
		case 1:
			sign = "+";
			Answer = rannum1 + rannum2;
			break;
		case 2:
			sign = "-";
			Answer = rannum1 - rannum2;
			break;
		case 3:
			sign = "*";
			Answer = rannum1 * rannum2;
			break;
		}
		ConsoleUtil.textToConsole(bot, type, "Maths Game Answer is: " + Answer, MessageType.ChatBot, null);
		return "The question is " + new Integer(rannum1).toString() + sign + new Integer(rannum2).toString();
	}
}
