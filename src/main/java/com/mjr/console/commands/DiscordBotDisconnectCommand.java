package com.mjr.console.commands;

import com.mjr.MJRBot;
import com.mjr.console.ConsoleCommand;

public class DiscordBotDisconnectCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (MJRBot.bot != null) {
			MJRBot.bot.getClient().logout();
			MJRBot.bot = null;
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
