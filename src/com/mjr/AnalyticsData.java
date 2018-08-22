package com.mjr;

public class AnalyticsData {
    private static int numOfCommandsUsed = 0;
    private static int numOfMessagedModerated = 0;
    private static int numOfPointsGained = 0;
    private static int numOfPointsRemoved = 0;

    public static int getNumOfCommandsUsed() {
	return numOfCommandsUsed;
    }

    public static void addNumOfCommandsUsed(int number) {
	numOfCommandsUsed += numOfCommandsUsed;
    }

    public static int getNumOfMessagedModerated() {
	return numOfMessagedModerated;
    }

    public static void addNumOfMessagedModerated(int number) {
	numOfMessagedModerated += numOfMessagedModerated;
    }

    public static int getNumOfPointsGained() {
	return numOfPointsGained;
    }

    public static void addNumOfPointsGained(int number) {
	numOfPointsGained += numOfPointsGained;
    }

    public static int getNumOfPointsRemoved() {
	return numOfPointsRemoved;
    }

    public static void addNumOfPointsRemoved(int number) {
	numOfPointsRemoved += numOfPointsRemoved;
    }

    public static void clearNumOfCommandsUsed() {
	numOfCommandsUsed = 0;
    }
    
    public static void clearNumOfMessagedModerated() {
	numOfMessagedModerated = 0;
    }
    
    public static void clearNumOfPointsGained() {
	numOfPointsGained = 0;
    }
    
    public static void clearNumOfPointsRemoved() {
	numOfPointsRemoved = 0;
    }
}
