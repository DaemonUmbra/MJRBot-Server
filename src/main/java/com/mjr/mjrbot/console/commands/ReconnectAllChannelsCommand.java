package com.mjr.mjrbot.console.commands;

import java.util.Collection;

import com.mjr.mjrbot.ChatBotManager;
import com.mjr.mjrbot.MixerBot;
import com.mjr.mjrbot.TwitchBot;
import com.mjr.mjrbot.console.ConsoleCommand;

public class ReconnectAllChannelsCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		Collection<TwitchBot> botsTwitch = ChatBotManager.getTwitchBots().values();
		Collection<MixerBot> botsMixer = ChatBotManager.getMixerBots().values();
		for (TwitchBot bot : botsTwitch) {
			bot.disconnectTwitch();
			ChatBotManager.removeTwitchBot(bot);
		}
		for (MixerBot bot : botsMixer) {
			bot.disconnectMixer();
			ChatBotManager.removeMixerBot(bot);
		}
	}

	@Override
	public String getDescription() {
		return "Reconnect all channels";
	}

	@Override
	public String getParametersDescription() {
		return "";
	}

}
