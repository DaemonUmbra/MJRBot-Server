package com.mjr.threads;

import com.mjr.MJRBot;
import com.mjr.commands.defaultCommands.RaceCommand;
import com.mjr.games.RacingGame;

public class RaceStartThread extends Thread {
    private boolean Delay = true;
    private boolean threadActive = true;

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
		MJRBot.getTwitchBot().MessageToChat("Race is about to start! Make sure to get your bets in now!");
		try {
		    Thread.sleep(30000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		RacingGame.Start();
		Delay = true;
		RaceCommand.Started = false;
		threadActive = false;
	    }
	}
    }
}
