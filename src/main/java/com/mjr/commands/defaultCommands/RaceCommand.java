package com.mjr.commands.defaultCommands;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MixerBot;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.storage.Config;
import com.mjr.threads.RaceStartThread;
import com.mjr.util.Utilities;

public class RaceCommand extends Command {

	@Override
	public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Games", channel).equalsIgnoreCase("true")) {
			if (type == BotType.Twitch) {
				TwitchBot twitchBot = ((TwitchBot) bot);
				if (twitchBot.racingGame.isGameActive == false) {
					Utilities.sendMessage(type, channel, "The race will start in 1 minute! Use !placebet CAR TYPE POINTS(Cars 1-8)(Types Top3, 1st) E.g !placebet 5 Top3 10");
					twitchBot.racingThread = new RaceStartThread(type, channel);
					twitchBot.racingThread.start();
					twitchBot.racingGame.isGameActive = true;
				}
			} else {
				MixerBot mixerBot = ((MixerBot) bot);
				if (mixerBot.racingGame.isGameActive == false) {
					Utilities.sendMessage(type, channel, "The race will start in 1 minute! Use !placebet CAR TYPE POINTS(Cars 1-8)(Types Top3, 1st) E.g !placebet 5 Top3 10");
					mixerBot.racingThread = new RaceStartThread(type, channel);
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
