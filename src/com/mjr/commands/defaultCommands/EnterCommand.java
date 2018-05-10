package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.threads.GiveAwayThread;

public class EnterCommand extends Command {
    private static int NumberEntered = 0;

    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (GiveAwayCommand.Started) {
	    GiveAwayThread.EnteredUsers[NumberEntered] = sender.toLowerCase();
	    NumberEntered++;
	    String endMessage = sender + " has now been entered in to the giveaway!";
	    if (MJRBot.getTwitchBot() != null)
		((TwitchBot) bot).MessageToChat(endMessage);
	    else
		((MixerBot) bot).sendMessage(endMessage);
	}
    }

    @Override
    public String getPermissionLevel() {
	return "User";
    }
}
