package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.games.ResultPair;
import com.mjr.mjrbot.games.SlotMachine;
import com.mjr.mjrbot.storage.Config;
import com.mjr.mjrbot.storage.EventLog;
import com.mjr.mjrbot.storage.EventLog.EventType;
import com.mjr.mjrbot.storage.PointsSystem;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.Permissions.PermissionLevel;

public class SpinCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Games", type, bot).equalsIgnoreCase("true")) {
			if (PointsSystem.hasPoints(sender, 1, type, bot)) {
				ResultPair result = SlotMachine.Spin(type);
				MJRBotUtilities.sendMessage(type, bot, "@" + sender + " the Slot Machine is spinning...");
				int waittime = 0;
				while (waittime < 250) { // TODO: Change to a Thread
					waittime++;
				}
				if (result.hasWon()) {
					MJRBotUtilities.sendMessage(type, bot, "@" + sender + " " + result.getResult() + " you have Won! You have been given " + PointsSystem.AddRandomPoints(sender, type, bot) + " Points");
					EventLog.addEvent(type, bot, sender, "Won the Spin Game", EventType.Games);
				} else {
					MJRBotUtilities.sendMessage(type, bot, "@" + sender + " " + result.getResult() + " you have lost! 1 Point taken!");
					PointsSystem.RemovePoints(sender, 1, type, bot);
					EventLog.addEvent(type, bot, sender, "Lost the Spin Game", EventType.Games);
				}
			} else {
				MJRBotUtilities.sendMessage(type, bot, "@" + sender + " you currently have insufficient points! You only have " + PointsSystem.getPoints(sender, type, bot));
			}
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
