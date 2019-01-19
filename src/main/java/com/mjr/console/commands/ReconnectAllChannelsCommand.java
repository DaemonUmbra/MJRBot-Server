package com.mjr.console.commands;

import java.util.Collection;

import com.mjr.ChatBotManager;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.console.ConsoleCommand;

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
