package com.mjr.commands.defaultCommands;

import com.mjr.Permissions.PermissionLevel;
import com.mjr.MJRBot;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.threads.BankHeistThread;

public class BankHeistCommand extends Command {
    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Games").equalsIgnoreCase("true")) {
	    if (args.length == 2) {
		if (BankHeistThread.GameActive == false) {
		    if (Utilities.isNumeric(args[1])) {
			BankHeistThread.addEnteredUser(sender, Integer.parseInt(args[1]));
			BankHeistThread thread = new BankHeistThread();
			thread.start();
			BankHeistThread.GameActive = true;
			MJRBot.getTwitchBot().MessageToChat(sender + " has started planning a heist!" + " To join the crew enter !heist <points> you only have 1 minute!");
		    } else
			Utilities.sendMessage("Invalid arguments! You need to enter !heist <points>", bot);
		} else {
		    if (Utilities.isNumeric(args[1]))
			BankHeistThread.addEnteredUser(sender, Integer.parseInt(args[1]));
		    else
			Utilities.sendMessage("Invalid arguments! You need to enter !heist <points>", bot);
		}
	    } else
		Utilities.sendMessage("Invalid arguments! You need to enter !heist <points>", bot);
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.User.getName();
    }
}
