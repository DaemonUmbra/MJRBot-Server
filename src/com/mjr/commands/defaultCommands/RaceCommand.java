package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.threads.RaceStartThread;

public class RaceCommand extends Command {
    public static boolean Started = false;

    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Started == false) {
	    String endMessage = "The race will start in 1 minute! Use !placebet CAR TYPE POINTS(Cars 1-8)(Types Top3, 1st) E.g !placebet 5 Top3 10";
	    if (MJRBot.getTwitchBot() != null)
		((TwitchBot) bot).MessageToChat(endMessage);
	    else
		((MixerBot) bot).sendMessage(endMessage);
	    RaceStartThread userThread = new RaceStartThread();
	    userThread.start();
	    Started = true;
	}
    }

    @Override
    public String getPermissionLevel() {
	return "User";
    }
}
