package com.mjr.threads;

import com.mjr.MJRBot;
import com.mjr.commands.defaultCommands.RaceCommand;
import com.mjr.games.RacingGame;

public class RaceStartThread extends Thread {
    boolean Delay = true;

    @Override
    @SuppressWarnings("deprecation")
    public void run() {
	while (true) {
	    if (Delay) {
		try {
		    Thread.sleep(60000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		Delay = false;
	    } else {
		MJRBot.getTwitchBot().MessageToChat("Race to about to start!");
		try {
		    Thread.sleep(30000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		RacingGame.Start();
		Delay = true;
		RaceCommand.Started = false;
		this.stop();
	    }
	}
    }
}
