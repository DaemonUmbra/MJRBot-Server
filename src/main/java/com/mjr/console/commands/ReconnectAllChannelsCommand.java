package com.mjr.console.commands;

import com.mjr.ChatBotManager;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.console.ConsoleCommand;

public class ReconnectAllChannelsCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		for (TwitchBot bot : ChatBotManager.getTwitchBots().values()) {
			bot.disconnectTwitch();
			ChatBotManager.removeTwitchBot(bot);
		}
		for (MixerBot bot : ChatBotManager.getMixerBots().values()) {
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
