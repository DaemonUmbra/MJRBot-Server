package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.console.IConsoleCommand;

public class GlobalTempDownBotCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		System.out.println("UpdateBotMessage: Sending all Messages!");
		String msg = "MJRBot will be down for a short while, it will be back with you shortly!";
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
		return "Send predefined temp down message to all connected Twitch & Mixer channels";
	}

	@Override
	public String getParametersDescription() {
		return "";
	}

}
