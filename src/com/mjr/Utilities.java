package com.mjr;

public class Utilities {

    public static int getRandom(int min, int max) {
	if (min > max) {
	    throw new IllegalArgumentException("Min " + min + " greater than max " + max);
	}
	return (int) (min + Math.random() * ((long) max - min + 1));
    }
}
