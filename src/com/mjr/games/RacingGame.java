package com.mjr.games;

import java.util.Arrays;
import java.util.Random;

import com.mjr.MJRBot.BotType;
import com.mjr.Utilities;
import com.mjr.files.PointsSystem;

public class RacingGame {

    public static String bets[][] = new String[4][1000];
    private static String[] Top3Users;
    private static String[] WinnerUsers;
    public static int BetNumber = 0;

    public static int cars[] = new int[8];
    private static String[] WinnersUsers;

    public static void PlaceBet(String User, String Bet, String type, String points) {
	bets[0][BetNumber] = User.toLowerCase();
	bets[1][BetNumber] = Bet.toLowerCase();
	bets[2][BetNumber] = type.toLowerCase();
	bets[3][BetNumber] = points.toLowerCase();
	BetNumber++;
    }

    public static void Start(BotType type, String channelName) {
	boolean Good = false;
	boolean InThere = false;
	for (int i = 0; i < 8; i++) {
	    Good = false;
	    InThere = false;
	    while (Good == false) {
		int randomnum = Utilities.getRandom(1, 8);
		for (int k = 0; k < cars.length; k++) {
		    if (randomnum == cars[k]) {
			InThere = true;
		    } else
			InThere = false;
		}
		if (InThere) {
		    Good = false;
		} else {
		    cars[i] = randomnum;
		    Good = true;
		}
	    }
	}
	CheckForWinners(type, channelName);
    }

    public static void CheckForWinners(BotType type, String channelName) {
	if (BetNumber == 0) {
	    Utilities.sendMessage(type, channelName, "No one made any bets! So race got canceled!");
	    return;
	}
	String WinnerBetWinners = "";
	String Top3BetWinners = "";

	boolean Results = false;

	Utilities.sendMessage(type, channelName, "First Place was Car " + cars[0] + ", Second Place was Car " + cars[1] + ", Third Place was Car " + cars[2]);
	for (int k = 0; k < BetNumber; k++) {
	    if (bets[2][k].equalsIgnoreCase("top3")) {
		if (bets[1][k].equalsIgnoreCase(Integer.toString(cars[0])) || bets[1][k].equalsIgnoreCase(Integer.toString(cars[1]))
			|| bets[1][k].equalsIgnoreCase(Integer.toString(cars[2]))) {
		    Top3BetWinners = Top3BetWinners + bets[0][k] + ", ";
		}
	    }

	    if (bets[2][k].equalsIgnoreCase("1st")) {
		if (bets[1][k].equalsIgnoreCase(Integer.toString(cars[0]))) {
		    WinnerBetWinners = WinnerBetWinners + bets[0][k] + ", ";
		}
	    }
	}
	if (Top3BetWinners.length() < 1 && WinnerBetWinners.length() < 1)
	    Results = false;
	else
	    Results = true;

	if (Results) {
	    Top3Users = Top3BetWinners.split(", ");
	    WinnerUsers = WinnerBetWinners.split(", ");

	    String pointsMessage = "User ";
	    String Message = "Top 3 winners are " + Arrays.asList(Top3Users) + " and 1st place winners are " + Arrays.asList(WinnerUsers);
	    Message.replace("[", "[ ");
	    Message.replace("]", "] ");
	    Utilities.sendMessage(type, channelName, Message);
	    if (Top3BetWinners.length() != 0) {
		float randomOds = nextFloat(1, 2);
		for (int l = 0; l < Top3Users.length; l++) {
		    int points = 0;
		    for (int i = 0; i < BetNumber; i++) {
			if (Top3Users[l].equalsIgnoreCase(bets[0][i])) {
			    if (randomOds < 1.2)
				points = (int) Math.ceil((randomOds - 0.2 * Integer.parseInt(bets[3][i])));
			    else
				points = (int) Math.ceil((randomOds * Integer.parseInt(bets[3][i])));

			}
		    }
		    pointsMessage = pointsMessage + Top3Users[l] + " has won " + Integer.toString(points) + ", ";
		    PointsSystem.AddPoints(Top3Users[l], points);
		}
	    }
	    if (WinnerBetWinners.length() != 0) {
		float randomOds = nextFloat(1, 2);
		for (int m = 0; m < WinnersUsers.length; m++) {
		    int points = 0;
		    for (int i = 0; i < BetNumber; i++) {
			if (WinnersUsers[m].equalsIgnoreCase(bets[0][i])) {
			    points = (int) Math.ceil((randomOds * Integer.parseInt(bets[3][i])));
			}
		    }
		    pointsMessage = pointsMessage + WinnersUsers[m] + " has won " + Integer.toString(points) + ", ";
		    PointsSystem.AddPoints(WinnerUsers[m], points);
		}
	    }
	    Utilities.sendMessage(type, channelName, pointsMessage);
	}
	for (int i = 0; i < bets.length; i++)
	    Arrays.fill(bets[i], "");

	Top3Users = new String[0];
	WinnerUsers = new String[0];
    }

    public static float nextFloat(float min, float max) {
	return min + new Random().nextFloat() * (max - min);
    }
}
