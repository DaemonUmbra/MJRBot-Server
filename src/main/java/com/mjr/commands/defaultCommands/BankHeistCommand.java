package com.mjr.commands.defaultCommands;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MixerBot;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.storage.Config;
import com.mjr.threads.BankHeistThread;
import com.mjr.util.Utilities;

public class BankHeistCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Games", channel).equalsIgnoreCase("true")) {
			if (args.length == 2) {
				if (type == BotType.Twitch) {
					TwitchBot twitchBot = ((TwitchBot) bot);
					if (twitchBot.bankHeistThread == null || twitchBot.bankHeistThread.gameActive == false || twitchBot.bankHeistThread.isAlive() == false) {
						if (Utilities.isNumeric(args[1])) {
							twitchBot.bankHeistEnteredUsers.clear();
							twitchBot.bankHeistEnteredUsers.put(sender, Integer.parseInt(args[1]));
							twitchBot.bankHeistThread = new BankHeistThread(type, channel);
							twitchBot.bankHeistThread.start();
							Utilities.sendMessage(type, channel, "@" + sender + " has started planning a heist!" + " To join the crew enter !heist <points> you only have 1 minute!");
						} else
							Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! You need to enter !heist <points>");
					} else {
						if (Utilities.isNumeric(args[1]))
							twitchBot.bankHeistEnteredUsers.put(sender, Integer.parseInt(args[1]));
						else
							Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! You need to enter !heist <points>");
					}
				} else if (type == BotType.Mixer) {
					MixerBot mixerBot = ((MixerBot) bot);
					if (mixerBot.bankHeistThread == null || mixerBot.bankHeistThread.gameActive == false || mixerBot.bankHeistThread.isAlive() == false) {
						if (Utilities.isNumeric(args[1])) {
							mixerBot.bankHeistEnteredUsers.clear();
							mixerBot.bankHeistEnteredUsers.put(sender, Integer.parseInt(args[1]));
							mixerBot.bankHeistThread = new BankHeistThread(type, channel);
							mixerBot.bankHeistThread.start();
							Utilities.sendMessage(type, channel, "@" + sender + " has started planning a heist!" + " To join the crew enter !heist <points> you only have 1 minute!");
						} else
							Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! You need to enter !heist <points>");
					} else {
						if (Utilities.isNumeric(args[1]))
							mixerBot.bankHeistEnteredUsers.put(sender, Integer.parseInt(args[1]));
						else
							Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! You need to enter !heist <points>");
					}
				}
			} else
				Utilities.sendMessage(type, channel, "@" + sender + " Invalid arguments! You need to enter !heist <points>");
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
