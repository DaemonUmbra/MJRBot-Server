package com.mjr.console.commands;

import com.mjr.ChatBotManager;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.console.ConsoleCommand;

public class GlobalUpdateBotCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		String msg = "MJRBot is updating, it will be back with you shortly!";
		for (TwitchBot bot : ChatBotManager.getTwitchBots().values()) {
			bot.sendMessage(msg);
		}
		for (MixerBot bot : ChatBotManager.getMixerBots().values()) {
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
