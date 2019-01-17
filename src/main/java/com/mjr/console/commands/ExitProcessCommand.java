package com.mjr.console.commands;

import com.mjr.AnalyticsData;
import com.mjr.ChatBotManager;
import com.mjr.MJRBot;
import com.mjr.MJRBot.StorageType;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.console.ConsoleCommand;

public class ExitProcessCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		for (TwitchBot bot : ChatBotManager.getTwitchBots().values()) {
			bot.disconnectTwitch();
		}
		for (MixerBot bot : ChatBotManager.getMixerBots().values()) {
			bot.disconnectMixer();
		}
		if (MJRBot.storageType == StorageType.Database)
			AnalyticsData.sendData();
		System.exit(0);
	}

	@Override
	public String getDescription() {
		return "Stops the bot process";
	}

	@Override
	public String getParametersDescription() {
		return "";
	}

}
