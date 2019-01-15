package com.mjr.console.commands;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.console.ConsoleCommand;

public class GlobalUpdateBotCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		String msg = "MJRBot is updating, it will be back with your shortly!";
		for (TwitchBot bot : MJRBot.getTwitchBots().values()) {
			bot.sendMessage(msg);
		}
		for (MixerBot bot : MJRBot.getMixerBots().values()) {
			bot.sendMessage(msg);
		}
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
