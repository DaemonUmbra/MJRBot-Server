package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.storage.Config;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.Permissions.PermissionLevel;

public class MathsCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Games", type, bot).equalsIgnoreCase("true")) {
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
	public String getPermissionLevel() {
		return PermissionLevel.Moderator.getName();
	}

	@Override
	public boolean hasCooldown() {
		return true;
	}
}
