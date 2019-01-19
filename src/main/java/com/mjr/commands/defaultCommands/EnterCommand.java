package com.mjr.commands.defaultCommands;

import com.mjr.ChatBotManager;
import com.mjr.ChatBotManager.BotType;
import com.mjr.MixerBot;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.util.Utilities;

public class EnterCommand extends Command {

	@Override
	public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
		if (type == BotType.Twitch) {
			TwitchBot twitchBot = ChatBotManager.getTwitchBotByChannelName(channel);
			if (twitchBot.giveAwayActive)
				if (!twitchBot.giveawayEnteredUsers.contains(sender.toLowerCase()))
					twitchBot.giveawayEnteredUsers.add(sender.toLowerCase());
		} else if (type == BotType.Mixer){
			MixerBot mixerBot = ChatBotManager.getMixerBotByChannelName(channel);
			if (mixerBot.giveAwayActive)
				if (!mixerBot.giveawayEnteredUsers.contains(sender.toLowerCase()))
					mixerBot.giveawayEnteredUsers.add(sender.toLowerCase());
		}
		Utilities.sendMessage(type, channel, sender + " has now been entered in to the giveaway!");
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
