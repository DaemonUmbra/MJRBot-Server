package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.storage.EventLogManager;
import com.mjr.mjrbot.storage.EventLogManager.EventType;
import com.mjr.mjrbot.storage.PointsSystemManager;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class AnswerCommand implements ICommand {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (ChannelConfigManager.getSetting("Games", type, bot).equalsIgnoreCase("true")) {
			if (type == BotType.Twitch) {
				TwitchBot twitchBot = ((TwitchBot) bot);
				if (twitchBot.mathsGame.isGameActive == true) {
					if (args.length == 2) {
						int index = Integer.parseInt(args[1]);
						if (twitchBot.mathsGame.Answer == index) {
							int profit = PointsSystemManager.AddRandomPoints(sender, type, bot);
							MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Well done, You have got the right answer! You have gained " + profit + " points!");
							EventLogManager.addEvent(type, bot, sender, "Won the Maths Game", EventType.Games);
							twitchBot.mathsGame.isGameActive = false;
						} else {
							MJRBotUtilities.sendMessage(type, bot, sender + " you have got the wrong answer try again!");
							EventLogManager.addEvent(type, bot, sender, "Got the wrong answer on the Maths Game", EventType.Games);
						}
					} else {
						MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !answer YOURANSWER (Example !answer 10)");
					}
				} else {
					MJRBotUtilities.sendMessage(type, bot, "@" + sender + " The maths game is currently not active!");
				}
			} else if (type == BotType.Mixer) {
				MixerBot mixerBot = ((MixerBot) bot);
				if (mixerBot.mathsGame.isGameActive == true) {
					if (args.length == 2) {
						int index = Integer.parseInt(args[1]);
						if (mixerBot.mathsGame.Answer == index) {
							int profit = PointsSystemManager.AddRandomPoints(sender, type, bot);
							MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Well done, You have got the right answer! You have gained " + profit + " points!");
							EventLogManager.addEvent(type, bot, sender, "Won the Maths Game", EventType.Games);
							mixerBot.mathsGame.isGameActive = false;
						} else {
							MJRBotUtilities.sendMessage(type, bot, "@" + sender + " you have got the wrong answer try again!");
							EventLogManager.addEvent(type, bot, sender, "Got the wrong answer on the Maths Game", EventType.Games);
						}
					} else {
						MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !answer YOURANSWER (Example !answer 10)");
					}
				} else {
					MJRBotUtilities.sendMessage(type, bot, "@" + sender + " The maths game is currently not active!");
				}
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
