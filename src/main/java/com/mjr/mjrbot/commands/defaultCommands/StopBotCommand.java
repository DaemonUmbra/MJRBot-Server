package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.AnalyticsData;
import com.mjr.mjrbot.ChatBotManager;
import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MixerBot;
import com.mjr.mjrbot.Permissions.PermissionLevel;
import com.mjr.mjrbot.TwitchBot;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.util.ConsoleUtil;
import com.mjr.mjrbot.util.Utilities;

public class StopBotCommand extends Command {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		Utilities.sendMessage(type, bot, "Bot is shutting down!");
		ConsoleUtil.textToConsole("Stop Bot command has been triggered, Bot is shutting down!");
		MJRBot.updateThread.stop();
		for (Integer channelIDMain : ChatBotManager.getTwitchBots().keySet()) {
			TwitchBot tempBot = ChatBotManager.getTwitchBotByChannelID(channelIDMain);
			tempBot.disconnectTwitch();
		}
		for (String channelNameMain : ChatBotManager.getMixerBots().keySet()) {
			MixerBot tempBot = ChatBotManager.getMixerBotByChannelName(channelNameMain);
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
