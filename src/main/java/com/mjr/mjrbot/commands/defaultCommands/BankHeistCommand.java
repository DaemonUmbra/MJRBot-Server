package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.MixerBot;
import com.mjr.mjrbot.Permissions.PermissionLevel;
import com.mjr.mjrbot.TwitchBot;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.storage.Config;
import com.mjr.mjrbot.threads.BankHeistThread;
import com.mjr.mjrbot.util.Utilities;

public class BankHeistCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Games", type, bot).equalsIgnoreCase("true")) {
			if (args.length == 2) {
				if (type == BotType.Twitch) {
					TwitchBot twitchBot = ((TwitchBot) bot);
					if (twitchBot.bankHeistThread == null || twitchBot.bankHeistThread.gameActive == false || twitchBot.bankHeistThread.isAlive() == false) {
						if (Utilities.isNumeric(args[1])) {
							twitchBot.bankHeistEnteredUsers.clear();
							twitchBot.bankHeistEnteredUsers.put(sender, Integer.parseInt(args[1]));
							twitchBot.bankHeistThread = new BankHeistThread(type, bot, twitchBot.channelName);
							twitchBot.bankHeistThread.start();
							Utilities.sendMessage(type, bot, "@" + sender + " has started planning a heist!" + " To join the crew enter !heist <points> you only have 1 minute!");
						} else
							Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !heist <points>");
					} else {
						if (Utilities.isNumeric(args[1]))
							twitchBot.bankHeistEnteredUsers.put(sender, Integer.parseInt(args[1]));
						else
							Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !heist <points>");
					}
				} else if (type == BotType.Mixer) {
					MixerBot mixerBot = ((MixerBot) bot);
					if (mixerBot.bankHeistThread == null || mixerBot.bankHeistThread.gameActive == false || mixerBot.bankHeistThread.isAlive() == false) {
						if (Utilities.isNumeric(args[1])) {
							mixerBot.bankHeistEnteredUsers.clear();
							mixerBot.bankHeistEnteredUsers.put(sender, Integer.parseInt(args[1]));
							mixerBot.bankHeistThread = new BankHeistThread(type, bot, mixerBot.channelName);
							mixerBot.bankHeistThread.start();
							Utilities.sendMessage(type, bot, "@" + sender + " has started planning a heist!" + " To join the crew enter !heist <points> you only have 1 minute!");
						} else
							Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !heist <points>");
					} else {
						if (Utilities.isNumeric(args[1]))
							mixerBot.bankHeistEnteredUsers.put(sender, Integer.parseInt(args[1]));
						else
							Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !heist <points>");
					}
				}
			} else
				Utilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !heist <points>");
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
