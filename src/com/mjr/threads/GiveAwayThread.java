package com.mjr.threads;

import java.util.Random;

import com.mjr.MJRBot;
import com.mjr.commands.defaultCommands.GiveAwayCommand;
import com.mjr.files.Config;

public class GiveAwayThread extends Thread {
    private boolean Delay = true;
    private static long TimeDuration;
    public static String[] EnteredUsers;

    @Override
    @SuppressWarnings("deprecation")
    public void run() {
	TimeDuration = (Integer.parseInt(Config.getSetting("GiveawayDelay")) * 60) * 1000;

	String message = "Giveaway will end in " + TimeDuration + " minutes. To enter use !Enter";
	if (MJRBot.getTwitchBot() != null)
	    MJRBot.getTwitchBot().MessageToChat(message);
	else
	    MJRBot.getMixerBot().sendMessage(message);

	if (Delay) {
	    try {
		Thread.sleep(TimeDuration);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    Delay = false;
	} else {
	    if (EnteredUsers.length > 0) {
		GiveAwayCommand.Started = false;
		message = "Giveaway has ended! " + EnteredUsers.length + " entered in to the giveaway!";
		if (MJRBot.getTwitchBot() != null)
		    MJRBot.getTwitchBot().MessageToChat(message);
		else
		    MJRBot.getMixerBot().sendMessage(message);
		Random random = new Random();
		int ChosenUser = random.nextInt((EnteredUsers.length - 0) + 1) + 0;
		message = EnteredUsers[ChosenUser] + " has won the giveaway!";
		if (MJRBot.getTwitchBot() != null)
		    MJRBot.getTwitchBot().MessageToChat(message);
		else
		    MJRBot.getMixerBot().sendMessage(message);
		Delay = true;
		this.stop();
	    } else {
		message = "Giveaway canceled due to no users entered!";
		if (MJRBot.getTwitchBot() != null)
		    MJRBot.getTwitchBot().MessageToChat(message);
		else
		    MJRBot.getMixerBot().sendMessage(message);
	    }
	}
    }
}
