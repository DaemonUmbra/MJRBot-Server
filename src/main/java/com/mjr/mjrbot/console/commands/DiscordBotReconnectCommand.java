package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.console.IConsoleCommand;
import com.mjr.mjrbot.util.MJRBotUtilities;

public class DiscordBotReconnectCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (ChatBotManager.getTwitchBots().size() != 0 && ChatBotManager.getMixerBots().size() != 0) {
			MJRBot.getDiscordBot().getClient().logout();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				MJRBotUtilities.logErrorMessage(e);
			}
			MJRBot.discordConnect();
			System.out.println("Discord Bot reconnected!");
		} else
			System.out.println("You need to run the 'connect' command first!");
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
