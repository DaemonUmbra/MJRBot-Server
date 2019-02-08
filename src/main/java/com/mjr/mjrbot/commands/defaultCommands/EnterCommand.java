package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.MixerBot;
import com.mjr.mjrbot.Permissions.PermissionLevel;
import com.mjr.mjrbot.TwitchBot;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.util.Utilities;

public class EnterCommand extends Command {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (type == BotType.Twitch) {
			TwitchBot twitchBot = (TwitchBot) bot;
			if (twitchBot.giveAwayActive)
				if (!twitchBot.giveawayEnteredUsers.contains(sender.toLowerCase()))
					twitchBot.giveawayEnteredUsers.add(sender.toLowerCase());
		} else if (type == BotType.Mixer) {
			MixerBot mixerBot = (MixerBot) bot;
			if (mixerBot.giveAwayActive)
				if (!mixerBot.giveawayEnteredUsers.contains(sender.toLowerCase()))
					mixerBot.giveawayEnteredUsers.add(sender.toLowerCase());
		}
		Utilities.sendMessage(type, bot, sender + " has now been entered in to the giveaway!");
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
