package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.console.IConsoleCommand;

public class GlobalOnlineDownBotCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		System.out.println("UpdateBotMessage: Sending all Messages!");
		String msg = "MJRBot is now back online!";
		for (TwitchBot bot : ChatBotManager.getTwitchBots().values()) {
			bot.sendMessage(msg);
		}
		for (MixerBot bot : ChatBotManager.getMixerBots().values()) {
			bot.sendMessage(msg);
		}
		System.out.println("UpdateBotMessage: All Messages have been sent!");
	}

	@Override
	public String getDescription() {
		return "Send predefined online message to all connected Twitch & Mixer channels";
	}

	@Override
	public String getParametersDescription() {
		return "";
	}

}
