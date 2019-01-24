package com.mjr.commands.defaultCommands;

import com.mjr.ChatBotManager.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.games.DiceGame;
import com.mjr.storage.Config;
import com.mjr.util.Utilities;

public class DiceCommand extends Command {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Games", type, bot).equalsIgnoreCase("true")) {
			if (args.length == 3) {
				if (Utilities.isNumeric(args[1])) {
					if (Utilities.isNumeric(args[2])) {
						double multi = Double.parseDouble(args[2]);
						if (multi > 1 && multi < 100)
							DiceGame.procressTurn(type, bot, sender, Integer.parseInt(args[1]), multi);
						else
							Utilities.sendMessage(type, bot, "@" + sender + " Multiplier must be between 1.01 & 100!");
					} else
						Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !dice wager multiplier");
				} else
					Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !dice wager multiplier");
			} else
				Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !dice wager multiplier");
		}
	}

	@Override
	public String getPermissionLevel() {
		return PermissionLevel.User.getName();
	}

	@Override
	public boolean hasCooldown() {
		return true;
	}
}
