package com.mjr.mjrbot.console.commands;

import java.util.ArrayList;
import java.util.List;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.console.IConsoleCommand;
import com.mjr.mjrbot.util.MJRBotUtilities;

public class ChannelsReconnectAllCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		try {
		List<TwitchBot> twitchBotRemove = new ArrayList<TwitchBot>();
		List<MixerBot> mixerBotRemove = new ArrayList<MixerBot>();
		twitchBotRemove.addAll(ChatBotManager.getTwitchBots().values());
		mixerBotRemove.addAll(ChatBotManager.getMixerBots().values());
		for (TwitchBot bot : twitchBotRemove) {
			bot.disconnectTwitch();
		}
		for (MixerBot bot : mixerBotRemove) {
			bot.disconnectMixer();
			ChatBotManager.removeMixerBot(bot);
		}
		}catch(Exception e) {
			MJRBotUtilities.logErrorMessage("Error when trying to reconnect all channels", e);
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
