package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.console.ConsoleCommand;

public class GlobalMessageCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args.length > 1) {
			String msg = "";
			for (int i = 0; i < args.length; i++)
				msg = msg + " " + args[i];
			for (TwitchBot bot : ChatBotManager.getTwitchBots().values()) {
				bot.sendMessage(msg);
			}
			for (MixerBot bot : ChatBotManager.getMixerBots().values()) {
				bot.sendMessage(msg);
			}
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
