package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.console.IConsoleCommand;

public class DiscordBotConnectCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (MJRBot.getDiscordBot() == null) {
			if (ChatBotManager.getTwitchBots().size() != 0 && ChatBotManager.getMixerBots().size() != 0) {
				MJRBot.discordConnect();
				System.out.println("Discord Bot connected!");
			} else
				System.out.println("You need to run the 'connect' command first!");
		} else
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
