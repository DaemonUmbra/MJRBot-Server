package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.console.ConsoleCommand;

public class DiscordBotDisconnectCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (MJRBot.getDiscordBot() != null) {
			MJRBot.getDiscordBot().getClient().logout();
			MJRBot.setDiscordBot(null);
			System.out.println("Discord Bot disconnected!");
		} else
			System.out.println("Discord Bot already disconnected!");
	}

	@Override
	public String getDescription() {
		return "Disconnect Discord Bot";
	}

	@Override
	public String getParametersDescription() {
		return "";
	}
}
