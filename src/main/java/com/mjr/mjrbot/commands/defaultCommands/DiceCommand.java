package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.games.DiceGame;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class DiceCommand implements ICommand {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (ChannelConfigManager.getSetting("Games", type, bot).equalsIgnoreCase("true")) {
			if (args.length == 3) {
				if (MJRBotUtilities.isNumeric(args[1])) {
					if (MJRBotUtilities.isNumeric(args[2])) {
						double multi = Double.parseDouble(args[2]);
						if (multi > 1 && multi < 100)
							DiceGame.procressTurn(type, bot, sender, Integer.parseInt(args[1]), multi);
						else
							MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Multiplier must be between 1.01 & 100!");
					} else
						MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !dice wager multiplier");
				} else
					MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !dice wager multiplier");
			} else
				MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !dice wager multiplier");
		}
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.User;
	}

	@Override
	public boolean hasCooldown() {
		return true;
	}
}
