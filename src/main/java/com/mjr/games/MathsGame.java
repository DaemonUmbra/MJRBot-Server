package com.mjr.games;

import com.mjr.ConsoleUtil;
import com.mjr.ConsoleUtil.MessageType;
import com.mjr.MJRBot.BotType;
import com.mjr.Utilities;

public class MathsGame {
    public int rannum1;
    public int rannum2;
    public int ransign;
    public String sign = "+";
    public boolean isGameActive = false;

    public int Answer;

    public String CreateQuestion(BotType type, String channelName) {
	rannum1 = Utilities.getRandom(0, 100);
	rannum2 = Utilities.getRandom(0, 100);
	ransign = Utilities.getRandom(1, 3);

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
	ConsoleUtil.TextToConsole(null, type, channelName, "Maths Game Answer is: " + Answer, MessageType.Bot, null);
	return "The question is " + new Integer(rannum1).toString() + sign + new Integer(rannum2).toString();
    }
}
