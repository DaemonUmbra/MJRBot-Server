package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.MixerBot;
import com.mjr.mjrbot.Permissions.PermissionLevel;
import com.mjr.mjrbot.TwitchBot;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.storage.Config;
import com.mjr.mjrbot.threads.RaceStartThread;
import com.mjr.mjrbot.util.Utilities;

public class RaceCommand extends Command {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Games", type, bot).equalsIgnoreCase("true")) {
			if (type == BotType.Twitch) {
				TwitchBot twitchBot = ((TwitchBot) bot);
				if (twitchBot.racingGame.isGameActive == false) {
					Utilities.sendMessage(type, bot, "The race will start in 1 minute! Use !placebet CAR TYPE POINTS(Cars 1-8)(Types Top3, 1st) E.g !placebet 5 Top3 10");
					twitchBot.racingThread = new RaceStartThread(type, bot, twitchBot.channelName);
					twitchBot.racingThread.start();
					twitchBot.racingGame.isGameActive = true;
				}
			} else if (type == BotType.Mixer) {
				MixerBot mixerBot = ((MixerBot) bot);
				if (mixerBot.racingGame.isGameActive == false) {
					Utilities.sendMessage(type, bot, "The race will start in 1 minute! Use !placebet CAR TYPE POINTS(Cars 1-8)(Types Top3, 1st) E.g !placebet 5 Top3 10");
					mixerBot.racingThread = new RaceStartThread(type, bot, mixerBot.channelName);
					mixerBot.racingThread.start();
					mixerBot.racingGame.isGameActive = true;
				}
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
