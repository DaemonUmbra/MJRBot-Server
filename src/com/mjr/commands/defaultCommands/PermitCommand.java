package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.chatModeration.LinkChecker;
import com.mjr.commands.Command;

public class PermitCommand extends Command {
    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (args.length == 2) {
	    String User = args[1];
	    String endMessage = User + " is now permited to post a link";
	    if (MJRBot.getTwitchBot() != null)
		((TwitchBot) bot).MessageToChat(endMessage);
	    else
		((MixerBot) bot).sendMessage(endMessage);
	    LinkChecker.PermitedUsers = LinkChecker.PermitedUsers + User.toLowerCase() + ", ";
	} else {
	    String endMessage = "Invalid arguments! You need to enter !permit USER";
	    if (MJRBot.getTwitchBot() != null)
		((TwitchBot) bot).MessageToChat(endMessage);
	    else
		((MixerBot) bot).sendMessage(endMessage);
	}
    }
}
