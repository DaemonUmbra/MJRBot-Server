package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.files.Config;
import com.mjr.threads.BankHeistThread;

public class BankHeistCommand extends Command {
    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (Config.getSetting("Games").equalsIgnoreCase("true")) {
	    if (args.length == 2) {
		if (BankHeistThread.GameActive == false) {
		    if (Utilities.isNumeric(args[1])) {
			BankHeistThread.addEnteredUser(sender, Integer.parseInt(args[1]));
			BankHeistThread thread = new BankHeistThread();
			thread.start();
			BankHeistThread.GameActive = true;
			Utilities.sendMessage(sender + " has started planning a heist!" + " To join the crew enter !heist <points> you only have 1 minute!");
		    } else
			Utilities.sendMessage("Invalid arguments! You need to enter !heist <points>");
		} else {
		    if (Utilities.isNumeric(args[1]))
			BankHeistThread.addEnteredUser(sender, Integer.parseInt(args[1]));
		    else
			Utilities.sendMessage("Invalid arguments! You need to enter !heist <points>");
		}
	    } else
		Utilities.sendMessage("Invalid arguments! You need to enter !heist <points>");
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.User.getName();
    }
}
