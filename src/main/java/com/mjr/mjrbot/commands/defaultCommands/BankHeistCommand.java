package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.threads.BankHeistThread;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class BankHeistCommand implements ICommand {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (ChannelConfigManager.getSetting("Games", type, bot).equalsIgnoreCase("true")) {
			if (args.length == 2) {
				if (type == BotType.Twitch) {
					TwitchBot twitchBot = ((TwitchBot) bot);
					if (twitchBot.bankHeistThread == null || twitchBot.bankHeistThread.gameActive == false || twitchBot.bankHeistThread.isAlive() == false) {
						if (MJRBotUtilities.isNumeric(args[1])) {
							twitchBot.getTwitchData().bankHeistEnteredUsers.clear();
							twitchBot.getTwitchData().bankHeistEnteredUsers.put(sender, Integer.parseInt(args[1]));
							twitchBot.bankHeistThread = new BankHeistThread(type, bot, twitchBot.getChannelName());
							twitchBot.bankHeistThread.start();
							MJRBotUtilities.sendMessage(type, bot, "@" + sender + " has started planning a heist!" + " To join the crew enter !heist <points> you only have 1 minute!");
						} else
							MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !heist <points>");
					} else {
						if (MJRBotUtilities.isNumeric(args[1]))
							twitchBot.getTwitchData().bankHeistEnteredUsers.put(sender, Integer.parseInt(args[1]));
						else
							MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !heist <points>");
					}
				} else if (type == BotType.Mixer) {
					MixerBot mixerBot = ((MixerBot) bot);
					if (mixerBot.bankHeistThread == null || mixerBot.bankHeistThread.gameActive == false || mixerBot.bankHeistThread.isAlive() == false) {
						if (MJRBotUtilities.isNumeric(args[1])) {
							mixerBot.getMixerData().bankHeistEnteredUsers.clear();
							mixerBot.getMixerData().bankHeistEnteredUsers.put(sender, Integer.parseInt(args[1]));
							mixerBot.bankHeistThread = new BankHeistThread(type, bot, mixerBot.getChannelName());
							mixerBot.bankHeistThread.start();
							MJRBotUtilities.sendMessage(type, bot, "@" + sender + " has started planning a heist!" + " To join the crew enter !heist <points> you only have 1 minute!");
						} else
							MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !heist <points>");
					} else {
						if (MJRBotUtilities.isNumeric(args[1]))
							mixerBot.getMixerData().bankHeistEnteredUsers.put(sender, Integer.parseInt(args[1]));
						else
							MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !heist <points>");
					}
				}
			} else
				MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !heist <points>");
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
