package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.AnalyticsData;
import com.mjr.mjrbot.ChatBotManager;
import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.MixerBot;
import com.mjr.mjrbot.TwitchBot;
import com.mjr.mjrbot.console.ConsoleCommand;
import com.mjr.mjrbot.sql.MySQLConnection;

public class ExitProcessCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		for (TwitchBot bot : ChatBotManager.getTwitchBots().values()) {
			bot.disconnectTwitch();
		}
		for (MixerBot bot : ChatBotManager.getMixerBots().values()) {
			bot.disconnectMixer();
		}
		if (MJRBot.storageType == StorageType.Database && MySQLConnection.connected)
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
