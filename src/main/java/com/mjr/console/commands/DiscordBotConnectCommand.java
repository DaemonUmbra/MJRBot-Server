package com.mjr.console.commands;

import com.mjr.MJRBot;
import com.mjr.console.ConsoleCommand;

public class DiscordBotConnectCommand extends ConsoleCommand{

	@Override
	public void onCommand(String message, String[] args) {
		if(MJRBot.bot == null) {
			MJRBot.discordConnect();
			System.out.println("Discord Bot connected!");
		}else
			System.out.println("Discord Bot already connected!");
	}

	@Override
	public String getDescription() {
		return "Setup/Connect Discord Bot";
	}

	@Override
	public String getParametersDescription() {
		return "";
	}
}
