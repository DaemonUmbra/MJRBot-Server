package com.mjr.threads;

import java.util.Random;

import com.mjr.TwitchBot;
import com.mjr.commands.defaultCommands.GiveAwayCommand;
import com.mjr.files.Config;

public class GiveAwayThread extends Thread {
    boolean Delay = true;
    private static long TimeDuration;
    public static String[] EnteredUsers;

    @Override @SuppressWarnings("deprecation")
    public void run() {
	TwitchBot bot = new TwitchBot();
	TimeDuration = (Integer.parseInt(Config.getSetting("GiveawayDelay")) * 60) * 1000;
	bot.MessageToChat("Giveaway will end in " + TimeDuration + " minutes. To enter use !Enter");
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
		bot.MessageToChat("Giveaway has ended! " + EnteredUsers.length + " entered in to the giveaway!");
		Random random = new Random();
		int ChosenUser = random.nextInt((EnteredUsers.length - 0) + 1) + 0;
		bot.MessageToChat(EnteredUsers[ChosenUser] + " has won the giveaway!");
		Delay = true;
		this.stop();
	    } else {
		bot.MessageToChat("Giveaway canceled due to no users entered!");
	    }
	}
    }
}
