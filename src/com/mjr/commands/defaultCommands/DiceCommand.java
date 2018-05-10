package com.mjr.commands.defaultCommands;

import com.mjr.Permissions.PermissionLevel;
import com.mjr.Utilities;
import com.mjr.commands.Command;
import com.mjr.games.DiceGame;

public class DiceCommand extends Command {

    @Override
    public void onCommand(Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (args.length == 3) {
	    if (Utilities.isNumeric(args[1])) {
		if (Utilities.isNumeric(args[2])) {
		    int multi = Integer.parseInt(args[2]);
		    if(multi > 1 && multi < 100)
			DiceGame.procressTurn(sender, Integer.parseInt(args[1]), multi);
		} else
		    Utilities.sendMessage("Invalid arguments! You need to enter !dice wager multiplier");
	    } else
		Utilities.sendMessage("Invalid arguments! You need to enter !dice wager multiplier");
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.User.getName();
    }

}
