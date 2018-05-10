package com.mjr.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mjr.MJRBot;
import com.mjr.commands.defaultCommands.GiveAwayCommand;
import com.mjr.files.Config;

public class GiveAwayThread extends Thread {
    private boolean Delay = true;
    private static long TimeDuration;
    public static List<String> EnteredUsers = new ArrayList<String>();

    @Override
    @SuppressWarnings("deprecation")
    public void run() {
	TimeDuration = (Integer.parseInt(Config.getSetting("GiveawayDelay")) * 60) * 1000;

	String message = "Giveaway will end in " + Config.getSetting("GiveawayDelay") + " minutes. To enter use !enter";
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
	}
	if (EnteredUsers.size() > 0) {
	    GiveAwayCommand.Started = false;
	    message = "Giveaway has ended! " + EnteredUsers.size() + " entered in to the giveaway!";
	    if (MJRBot.getTwitchBot() != null)
		MJRBot.getTwitchBot().MessageToChat(message);
	    else
		MJRBot.getMixerBot().sendMessage(message);
	    Random random = new Random();
	    int ChosenUser = random.nextInt((EnteredUsers.size() - 0) + 1) + 0;
	    message = EnteredUsers.get(ChosenUser) + " has won the giveaway!";
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
