package com.mjr.commands.defaultCommands;

import com.mjr.ChatBotManager;
import com.mjr.ChatBotManager.BotType;
import com.mjr.MixerBot;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.threads.GiveAwayThread;
import com.mjr.util.Utilities;

public class GiveAwayCommand extends Command {

	@Override
	public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
		if (type == BotType.Twitch) {
			TwitchBot twitchBot = ChatBotManager.getTwitchBotByChannelName(channel);
			if (twitchBot.giveAwayActive == false) {
				GiveAwayThread thread = new GiveAwayThread(type, channel);
				thread.start();
				twitchBot.giveAwayActive = true;
			} else {
				Utilities.sendMessage(type, channel, "Giveaway has already started!");
			}
		} else if (type == BotType.Mixer) {
			MixerBot mixerBot = ChatBotManager.getMixerBotByChannelName(channel);
			if (mixerBot.giveAwayActive == false) {
				GiveAwayThread thread = new GiveAwayThread(type, channel);
				thread.start();
				mixerBot.giveAwayActive = true;
			} else {
				Utilities.sendMessage(type, channel, "Giveaway has already started!");
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
