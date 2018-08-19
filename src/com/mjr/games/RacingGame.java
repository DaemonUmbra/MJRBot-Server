package com.mjr.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.mjr.MJRBot.BotType;
import com.mjr.storage.EventLog;
import com.mjr.storage.PointsSystem;
import com.mjr.storage.EventLog.EventType;
import com.mjr.Utilities;

public class RacingGame {

    public String userBets[][] = new String[4][1000];
    public int cars[] = new int[3];
    private List<String> top3Users = new ArrayList<String>();
    private List<String> firstUsers = new ArrayList<String>();

    public int numberOfBets = 0;
    public boolean isGameActive = false;

    public void placeBet(String User, String bet, String type, String points) {
	userBets[0][numberOfBets] = User.toLowerCase();
	userBets[1][numberOfBets] = bet.toLowerCase();
	userBets[2][numberOfBets] = type.toLowerCase();
	userBets[3][numberOfBets] = points.toLowerCase();
	numberOfBets++;
    }

    public void start(BotType type, String channelName) {
	boolean exists = false;

	// Only work out 3 Cars since we don't give winnings for anything lower than the third car
	for (int i = 0; i < 3; i++) {
	    exists = false;
	    do{
		int randomnum = Utilities.getRandom(1, 8);

		// Check if its already exists
		for (int k = 0; k < cars.length; k++) {
		    if (randomnum == cars[k]) {
			exists = true;
		    } else
			exists = false;
		}

		// If doesn't already exist add to array
		if (exists == false) {
		    cars[i] = randomnum;
		}
	    } while (exists == true);
	}

	// Check for Winning Users
	checkForWinners(type, channelName);
	isGameActive = false;
    }

    public void checkForWinners(BotType type, String channelName) {
	if (numberOfBets == 0) {
	    Utilities.sendMessage(type, channelName, "No one made any bets! So race got canceled!");
	    return;
	}
	Utilities.sendMessage(type, channelName,
		"First Place was Car " + cars[0] + ", Second Place was Car " + cars[1] + ", Third Place was Car " + cars[2]);

	// Check who got the bet correct
	for (int k = 0; k < numberOfBets; k++) {
	    if (userBets[2][k].equalsIgnoreCase("top3")) {
		if (userBets[1][k].equalsIgnoreCase(Integer.toString(cars[0])) || userBets[1][k].equalsIgnoreCase(Integer.toString(cars[1]))
			|| userBets[1][k].equalsIgnoreCase(Integer.toString(cars[2]))) {
		    top3Users.add(userBets[0][k]);
		}
	    }

	    if (userBets[2][k].equalsIgnoreCase("1st")) {
		if (userBets[1][k].equalsIgnoreCase(Integer.toString(cars[0]))) {
		    firstUsers.add(userBets[0][k]);
		}
	    }
	}

	// Calculate/Give Winnings
	if (top3Users.size() != 0 || firstUsers.size() != 0) {
	    String pointsMessage = "";
	    String message = "Top 3 winners are " + Arrays.asList(top3Users) + " and 1st place winners are " + Arrays.asList(firstUsers);
	    message.replace("[", "[ ");
	    message.replace("]", "] ");
	    Utilities.sendMessage(type, channelName, message);
	    float randomOds = nextFloat(1, 2);
	    for (int l = 0; l < top3Users.size(); l++) {
		int points = 0;
		for (int i = 0; i < numberOfBets; i++) {
		    if (top3Users.get(l).equalsIgnoreCase(userBets[0][i])) {
			if (randomOds < 1.2)
			    points = (int) Math.ceil((randomOds - 0.2 * Integer.parseInt(userBets[3][i])));
			else
			    points = (int) Math.ceil((randomOds * Integer.parseInt(userBets[3][i])));

		    }
		}
		pointsMessage = pointsMessage + "@" + top3Users.get(l) + " has won " + Integer.toString(points) + ", ";
		PointsSystem.AddPointsWithEventMsg(top3Users.get(l), points, channelName);
		EventLog.addEvent(channelName, top3Users.get(l) , "Won the Racing Game", EventType.Games);
	    }
	    randomOds = nextFloat(1, 2);
	    for (int m = 0; m < firstUsers.size(); m++) {
		int points = 0;
		for (int i = 0; i < numberOfBets; i++) {
		    if (firstUsers.get(m).equalsIgnoreCase(userBets[0][i])) {
			points = (int) Math.ceil((randomOds * Integer.parseInt(userBets[3][i])));
		    }
		}
		pointsMessage = pointsMessage + "@" + firstUsers.get(m) + " has won " + Integer.toString(points) + ", ";
		PointsSystem.AddPointsWithEventMsg(firstUsers.get(m), points, channelName);
		EventLog.addEvent(channelName, firstUsers.get(m) , "Won the Racing Game", EventType.Games);
	    }
	    Utilities.sendMessage(type, channelName, pointsMessage);
	}

	// Clean up
	userBets = new String[4][1000];
	cars = new int[8];
	top3Users.clear();
	firstUsers.clear();
    }

    public static float nextFloat(float min, float max) {
	return min + new Random().nextFloat() * (max - min);
    }
}
