package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.bases.MixerBot;
import com.mjr.mjrbot.bots.bases.TwitchBot;
import com.mjr.mjrbot.console.IConsoleCommand;

public class GlobalMessageCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args.length > 1) {
			String msg = "";
			for (int i = 0; i < args.length; i++)
				msg = msg + " " + args[i];
			System.out.println("GlobalBotMessage: Sending all Messages!");
			for (TwitchBot bot : ChatBotManager.getTwitchBots().values()) {
				bot.sendMessage(msg);
			}
			for (MixerBot bot : ChatBotManager.getMixerBots().values()) {
				bot.sendMessage(msg);
			}
			System.out.println("GlobalBotMessage: All Messages have been sent!");
		} else {
			System.out.println("Invalid syntax, Use gmsg " + getParametersDescription());
		}
	}

	@Override
	public String getDescription() {
		return "Send a message to all connected Twitch & Mixer channels";
	}

	@Override
	public String getParametersDescription() {
		return "<message>";
	}

}
