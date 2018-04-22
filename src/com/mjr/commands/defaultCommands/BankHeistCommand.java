package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.threads.BankHeistThread;

public class BankHeistCommand extends Command {
    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Games").equalsIgnoreCase("true")) {
	    if (BankHeistThread.GameActive == false) {
		BankHeistThread.enteredUsers.add(sender);
		BankHeistThread thread = new BankHeistThread();
		thread.start();
		BankHeistThread.GameActive = true;
	    } else {
		String endMessage = "Heist has already started!";
		if (MJRBot.getTwitchBot() != null)
		    ((TwitchBot) bot).MessageToChat(endMessage);
		else
		    ((MixerBot) bot).sendMessage(endMessage);
	    }
	}
    }
}
