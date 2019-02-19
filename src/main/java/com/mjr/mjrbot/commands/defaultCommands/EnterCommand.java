package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.Permissions.PermissionLevel;

public class EnterCommand extends Command {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (type == BotType.Twitch) {
			TwitchBot twitchBot = (TwitchBot) bot;
			if (twitchBot.getTwitchData().giveAwayActive)
				if (!twitchBot.getTwitchData().giveawayEnteredUsers.contains(sender.toLowerCase()))
					twitchBot.getTwitchData().giveawayEnteredUsers.add(sender.toLowerCase());
		} else if (type == BotType.Mixer) {
			MixerBot mixerBot = (MixerBot) bot;
			if (mixerBot.getMixerData().giveAwayActive)
				if (!mixerBot.getMixerData().giveawayEnteredUsers.contains(sender.toLowerCase()))
					mixerBot.getMixerData().giveawayEnteredUsers.add(sender.toLowerCase());
		}
		MJRBotUtilities.sendMessage(type, bot, sender + " has now been entered in to the giveaway!");
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
