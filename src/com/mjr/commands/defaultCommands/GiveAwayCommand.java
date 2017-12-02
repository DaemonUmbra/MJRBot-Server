package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.threads.GiveAwayThread;

public class GiveAwayCommand extends Command {
    public static boolean Started = false;

    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Started == false) {
	    GiveAwayThread thread = new GiveAwayThread();
	    thread.start();
	    Started = true;
	}else{
	    String endMessage = "Giveaway has already started!";
	    if (MJRBot.getTwitchBot() != null)
		((TwitchBot) bot).MessageToChat(endMessage);
	    else
		((MixerBot) bot).sendMessage(endMessage);
	}
    }
}
