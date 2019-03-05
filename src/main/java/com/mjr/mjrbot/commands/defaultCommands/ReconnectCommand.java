package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;

public class ReconnectCommand implements ICommand {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (type == BotType.Twitch) {
			TwitchBot twitchBot = (TwitchBot) bot;
			twitchBot.disconnectTwitch();
			ChatBotManager.removeTwitchBot(twitchBot);
		} else if (type == BotType.Mixer) {
			MixerBot mixerBot = (MixerBot) bot;
			mixerBot.disconnectMixer();
			ChatBotManager.removeMixerBot(mixerBot);
		}
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.Streamer;
	}

	@Override
	public boolean hasCooldown() {
		return false;
	}

}
