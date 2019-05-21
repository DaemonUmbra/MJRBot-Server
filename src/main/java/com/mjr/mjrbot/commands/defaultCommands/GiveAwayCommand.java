package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.threads.GiveAwayThread;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class GiveAwayCommand implements ICommand {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (type == BotType.Twitch) {
			TwitchBot twitchBot = (TwitchBot) bot;
			if (twitchBot.getTwitchData().giveAwayActive == false) {
				GiveAwayThread thread = new GiveAwayThread(type, bot, twitchBot.getChannelName());
				thread.start();
				twitchBot.getTwitchData().giveAwayActive = true;
			} else {
				ChatBotManager.sendMessage(type, bot, "Giveaway has already started!");
			}
		} else if (type == BotType.Mixer) {
			MixerBot mixerBot = (MixerBot) bot;
			if (mixerBot.getMixerData().giveAwayActive == false) {
				GiveAwayThread thread = new GiveAwayThread(type, bot, mixerBot.getChannelName());
				thread.start();
				mixerBot.getMixerData().giveAwayActive = true;
			} else {
				ChatBotManager.sendMessage(type, bot, "Giveaway has already started!");
			}
		}
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.Moderator;
	}

	@Override
	public boolean hasCooldown() {
		return false;
	}
}
