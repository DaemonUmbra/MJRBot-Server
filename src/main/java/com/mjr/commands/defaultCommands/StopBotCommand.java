package com.mjr.commands.defaultCommands;

import com.mjr.AnalyticsData;
import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.MixerBot;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.util.ConsoleUtil;
import com.mjr.util.Utilities;

public class StopBotCommand extends Command {

	@Override
	public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
		Utilities.sendMessage(type, channel, "Bot is shutting down!");
		ConsoleUtil.textToConsole("Stop Bot command has been triggered, Bot is shutting down!");
		MJRBot.updateThread.stop();
		for (String channelNameMain : MJRBot.getTwitchBots().keySet()) {
			TwitchBot tempBot = MJRBot.getTwitchBotByChannelName(channelNameMain);
			tempBot.disconnectTwitch();
		}
		for (String channelNameMain : MJRBot.getMixerBots().keySet()) {
			MixerBot tempBot = MJRBot.getMixerBotByChannelName(channelNameMain);
			tempBot.disconnectMixer();
		}
		AnalyticsData.sendData();
		System.exit(0);
	}

	@Override
	public String getPermissionLevel() {
		return PermissionLevel.BotOwner.getName();
	}

	@Override
	public boolean hasCooldown() {
		return false;
	}

}
