package com.mjr.commands.defaultCommands;

import com.mjr.AnalyticsData;
import com.mjr.ChatBotManager;
import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;
import com.mjr.util.ConsoleUtil;
import com.mjr.util.Utilities;

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
