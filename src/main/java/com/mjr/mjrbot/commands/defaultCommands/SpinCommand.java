package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.games.ResultPair;
import com.mjr.mjrbot.games.SlotMachine;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.storage.EventLogManager;
import com.mjr.mjrbot.storage.EventLogManager.EventType;
import com.mjr.mjrbot.storage.PointsSystemManager;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class SpinCommand implements ICommand {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (ChannelConfigManager.getSetting("Games", type, bot).equalsIgnoreCase("true")) {
			if (PointsSystemManager.hasPoints(sender, 1, type, bot)) {
				ResultPair result = SlotMachine.Spin(type);
				ChatBotManager.sendMessage(type, bot, "@" + sender + " the Slot Machine is spinning...");
				int waittime = 0;
				while (waittime < 250) { // TODO: Change to a Thread
					waittime++;
				}
				if (result.hasWon()) {
					ChatBotManager.sendMessage(type, bot, "@" + sender + " " + result.getResult() + " you have Won! You have been given " + PointsSystemManager.AddRandomPoints(sender, type, bot) + " Points");
					EventLogManager.addEvent(type, bot, sender, "Won the Spin Game", EventType.Games);
				} else {
					ChatBotManager.sendMessage(type, bot, "@" + sender + " " + result.getResult() + " you have lost! 1 Point taken!");
					PointsSystemManager.RemovePoints(sender, 1, type, bot);
					EventLogManager.addEvent(type, bot, sender, "Lost the Spin Game", EventType.Games);
				}
			} else {
				ChatBotManager.sendMessage(type, bot, "@" + sender + " you currently have insufficient points! You only have " + PointsSystemManager.getPoints(sender, type, bot));
			}
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
