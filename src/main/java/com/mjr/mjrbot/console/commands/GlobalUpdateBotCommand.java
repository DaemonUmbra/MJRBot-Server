package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.bases.MixerBot;
import com.mjr.mjrbot.bots.bases.TwitchBot;
import com.mjr.mjrbot.console.IConsoleCommand;

public class GlobalUpdateBotCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		System.out.println("UpdateBotMessage: Sending all Messages!");
		String msg = "MJRBot is updating, it will be back with you shortly!";
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
		return "Send predefined update message to all connected Twitch & Mixer channels";
	}

	@Override
	public String getParametersDescription() {
		return "";
	}

}
