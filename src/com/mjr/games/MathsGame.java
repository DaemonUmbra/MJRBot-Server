package com.mjr.games;

import java.util.Random;

import com.mjr.ConsoleUtil;
import com.mjr.Utilities;

public class MathsGame {
    public static int rannum1;
    public static int rannum2;
    public static int ransign;
    public static String sign = "+";
    public static boolean isMathsGameActive = false;

    public static int Answer;

    public static String CreateQuestion() {
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
	ConsoleUtil.TextToConsole("Maths Game Answer is: " + Answer, "Bot", null);
	return "The question is " + new Integer(rannum1).toString() + sign + new Integer(rannum2).toString();
    }
}
