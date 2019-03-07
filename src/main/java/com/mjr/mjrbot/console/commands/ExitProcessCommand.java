package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.AnalyticsData;
import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.console.IConsoleCommand;
import com.mjr.mjrbot.storage.sql.MySQLConnection;

public class ExitProcessCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (MJRBot.getDiscordBot() != null) {
			MJRBot.getDiscordBot().getClient().logout();
			MJRBot.setDiscordBot(null);
			System.out.println("Discord Bot disconnected!");
		}
		for (TwitchBot bot : ChatBotManager.getTwitchBots().values()) {
			bot.disconnectTwitch();
		}
		for (MixerBot bot : ChatBotManager.getMixerBots().values()) {
			bot.disconnectMixer();
		}
		if (MJRBot.storageType == StorageType.Database && MySQLConnection.isConnected())
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
