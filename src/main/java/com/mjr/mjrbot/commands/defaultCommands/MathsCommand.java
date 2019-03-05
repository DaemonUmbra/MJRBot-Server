package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class MathsCommand implements ICommand {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (ChannelConfigManager.getSetting("Games", type, bot).equalsIgnoreCase("true")) {
			if (type == BotType.Twitch) {
				TwitchBot twitchBot = ((TwitchBot) bot);
				if (twitchBot.mathsGame.isGameActive == false) {
					MJRBotUtilities.sendMessage(type, bot, twitchBot.mathsGame.CreateQuestion(type, bot));
					MJRBotUtilities.sendMessage(type, bot, "Type !answer YOURANSWER (e.g !answer 10) to start guessing!");
					twitchBot.mathsGame.isGameActive = true;
				} else {
					MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Game Already started!");
				}
			} else if (type == BotType.Mixer) {
				MixerBot mixerBot = ((MixerBot) bot);
				if (mixerBot.mathsGame.isGameActive == false) {
					MJRBotUtilities.sendMessage(type, bot, mixerBot.mathsGame.CreateQuestion(type, bot));
					MJRBotUtilities.sendMessage(type, bot, "Type !answer YOURANSWER (e.g !answer 10) to start guessing!");
					mixerBot.mathsGame.isGameActive = true;
				} else {
					MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Game Already started!");
				}
			}
		}
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.Moderator;
	}

	@Override
	public boolean hasCooldown() {
		return true;
	}
}
