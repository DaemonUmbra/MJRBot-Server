package com.mjr.games;

import java.util.Random;

public class MathsGame {
    public static Random rand = new Random();
    public static int rannum1;
    public static int rannum2;
    public static int ransign;
    public static String sign = "+";
    public static boolean isMathsGameActive = false;

    public static int Answer;

    public static String CreateQuestion() {
	rannum1 = rand.nextInt((100 - 0) + 1) + 0;
	rannum2 = rand.nextInt((100 - 0) + 1) + 0;
	ransign = rand.nextInt((3) + 1) + 1;

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
	return "The question is " + new Integer(rannum1).toString() + sign + new Integer(rannum2).toString();
    }
}
