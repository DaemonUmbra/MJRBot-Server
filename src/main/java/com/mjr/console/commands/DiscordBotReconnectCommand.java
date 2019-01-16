package com.mjr.console.commands;

import com.mjr.MJRBot;
import com.mjr.console.ConsoleCommand;

public class DiscordBotReconnectCommand extends ConsoleCommand{

	@Override
	public void onCommand(String message, String[] args) {
		MJRBot.bot.getClient().logout();
		MJRBot.bot = null;
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			MJRBot.logErrorMessage(e);
		}
		MJRBot.discordConnect();
		System.out.println("Discord Bot reconnected!");
	}

	@Override
	public String getDescription() {
		return "Reconnect Discord Bot";
	}

	@Override
	public String getParametersDescription() {
		return "";
	}
}
