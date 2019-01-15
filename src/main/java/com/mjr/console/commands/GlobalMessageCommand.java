package com.mjr.console.commands;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.console.ConsoleCommand;

public class GlobalMessageCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args.length > 2) {
			String msg = "";
			for (int i = 1; i < args.length; i++)
				msg = msg + " " + args[i];
			for (TwitchBot bot : MJRBot.getTwitchBots().values()) {
				bot.sendMessage(msg);
			}
			for (MixerBot bot : MJRBot.getMixerBots().values()) {
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
