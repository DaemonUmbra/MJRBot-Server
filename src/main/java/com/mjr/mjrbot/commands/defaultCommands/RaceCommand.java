package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.bases.MixerBot;
import com.mjr.mjrbot.bots.bases.TwitchBot;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.threads.RaceStartThread;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class RaceCommand implements ICommand {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (ChannelConfigManager.getSetting("Games", type, bot).equalsIgnoreCase("true")) {
			if (type == BotType.Twitch) {
				TwitchBot twitchBot = ((TwitchBot) bot);
				if (twitchBot.racingGame.isGameActive == false) {
					ChatBotManager.sendMessage(type, bot, "The race will start in 1 minute! Use !placebet CAR TYPE POINTS(Cars 1-8)(Types Top3, 1st) E.g !placebet 5 Top3 10");
					twitchBot.racingThread = new RaceStartThread(type, bot, twitchBot.getChannelName());
					twitchBot.racingThread.start();
					twitchBot.racingGame.isGameActive = true;
				}
			} else if (type == BotType.Mixer) {
				MixerBot mixerBot = ((MixerBot) bot);
				if (mixerBot.racingGame.isGameActive == false) {
					ChatBotManager.sendMessage(type, bot, "The race will start in 1 minute! Use !placebet CAR TYPE POINTS(Cars 1-8)(Types Top3, 1st) E.g !placebet 5 Top3 10");
					mixerBot.racingThread = new RaceStartThread(type, bot, mixerBot.getChannelName());
					mixerBot.racingThread.start();
					mixerBot.racingGame.isGameActive = true;
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
