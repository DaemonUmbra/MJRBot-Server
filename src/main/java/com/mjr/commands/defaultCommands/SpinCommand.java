package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;
import com.mjr.games.ResultPair;
import com.mjr.games.SlotMachine;
import com.mjr.storage.Config;
import com.mjr.storage.EventLog;
import com.mjr.storage.EventLog.EventType;
import com.mjr.storage.PointsSystem;
import com.mjr.util.Utilities;

public class SpinCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Games", channel).equalsIgnoreCase("true")) {
			if (PointsSystem.hasPoints(sender, 1, channel)) {
				ResultPair result = SlotMachine.Spin(type);
				Utilities.sendMessage(type, channel, "@" + sender + " the Slot Machine is spinning...");
				int waittime = 0;
				while (waittime < 250) { // TODO: Change to a Thread
					waittime++;
				}
				if (result.hasWon()) {
					Utilities.sendMessage(type, channel, "@" + sender + " " + result.getResult() + " you have Won! You have been given " + PointsSystem.AddRandomPoints(sender, channel) + " Points");
					EventLog.addEvent(channel, sender, "Won the Spin Game", EventType.Games);
				} else {
					Utilities.sendMessage(type, channel, "@" + sender + " " + result.getResult() + " you have lost! 1 Point taken!");
					PointsSystem.RemovePoints(sender, 1, channel);
					EventLog.addEvent(channel, sender, "Lost the Spin Game", EventType.Games);
				}
			} else {
				Utilities.sendMessage(type, channel, "@" + sender + " you currently have insufficient points! You only have " + PointsSystem.getPoints(sender, channel));
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
