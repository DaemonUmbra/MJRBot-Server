package com.mjr.threads;

import com.mjr.Utilities;
import com.mjr.MJRBot.BotType;
import com.mjr.commands.defaultCommands.RaceCommand;
import com.mjr.games.RacingGame;

public class RaceStartThread extends Thread {
    private boolean Delay = true;
    private boolean threadActive = true;
    private BotType type;
    private String channelName;
    
    public RaceStartThread(BotType type, String channelName) {
	super();
	this.type = type;
	this.channelName = channelName;
    }

    @Override
    public void run() {
	while (threadActive) {
	    if (Delay) {
		try {
		    Thread.sleep(60000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		Delay = false;
	    } else {
		Utilities.sendMessage(type, channelName, "Race is about to start! Make sure to get your bets in now!");
		try {
		    Thread.sleep(30000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		RacingGame.Start(type, channelName);
		Delay = true;
		RaceCommand.Started = false;
		threadActive = false;
	    }
	}
    }
}
