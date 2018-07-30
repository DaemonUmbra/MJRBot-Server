package com.mjr.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mjr.MJRBot.BotType;
import com.mjr.Utilities;
import com.mjr.commands.defaultCommands.GiveAwayCommand;
import com.mjr.files.Config;

public class GiveAwayThread extends Thread {
    private boolean Delay = true;
    private static long TimeDuration;
    public static List<String> EnteredUsers = new ArrayList<String>();
    private BotType type;
    private String channelName;

    public GiveAwayThread(BotType type, String channelName) {
	super();
	this.type = type;
	this.channelName = channelName;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void run() {
	TimeDuration = (Integer.parseInt(Config.getSetting("GiveawayDelay", channelName)) * 60) * 1000;
	Utilities.sendMessage(type, channelName,
		"Giveaway will end in " + Config.getSetting("GiveawayDelay", channelName) + " minutes. To enter use !enter");

	if (Delay) {
	    try {
		Thread.sleep(TimeDuration);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
	if (EnteredUsers.size() > 0) {
	    GiveAwayCommand.Started = false;
	    Utilities.sendMessage(type, channelName, "Giveaway has ended! " + EnteredUsers.size() + " entered in to the giveaway!");
	    Random random = new Random();
	    int ChosenUser = random.nextInt((EnteredUsers.size() - 0) + 1) + 0;
	    Utilities.sendMessage(type, channelName, "@" + EnteredUsers.get(ChosenUser) + " has won the giveaway!");
	    Delay = true;
	    this.stop();
	} else {
	    Utilities.sendMessage(type, channelName, "Giveaway canceled due to no users entered!");
	}
    }
}
