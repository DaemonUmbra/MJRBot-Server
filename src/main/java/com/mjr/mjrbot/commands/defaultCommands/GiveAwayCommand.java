package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.MixerBot;
import com.mjr.mjrbot.Permissions.PermissionLevel;
import com.mjr.mjrbot.TwitchBot;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.threads.GiveAwayThread;
import com.mjr.mjrbot.util.Utilities;

public class GiveAwayCommand extends Command {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (type == BotType.Twitch) {
			TwitchBot twitchBot = (TwitchBot) bot;
			if (twitchBot.giveAwayActive == false) {
				GiveAwayThread thread = new GiveAwayThread(type, bot, twitchBot.channelName);
				thread.start();
				twitchBot.giveAwayActive = true;
			} else {
				Utilities.sendMessage(type, bot, "Giveaway has already started!");
			}
		} else if (type == BotType.Mixer) {
			MixerBot mixerBot = (MixerBot) bot;
			if (mixerBot.giveAwayActive == false) {
				GiveAwayThread thread = new GiveAwayThread(type, bot, mixerBot.channelName);
				thread.start();
				mixerBot.giveAwayActive = true;
			} else {
				Utilities.sendMessage(type, bot, "Giveaway has already started!");
			}
		}
	}

	@Override
	public String getPermissionLevel() {
		return PermissionLevel.Moderator.getName();
	}

	@Override
	public boolean hasCooldown() {
		return false;
	}
}
