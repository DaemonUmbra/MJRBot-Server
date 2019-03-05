package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.console.IConsoleCommand;

public class DiscordBotInfoCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (MJRBot.getDiscordBot() != null) {
			int numGuilds = MJRBot.getDiscordBot().getClient().getGuilds().count().block().intValue();
			System.out.println("---------------------------------");
			System.out.format("|%10s|%20s|", "Status", "Number of guilds");
			System.out.println("\n---------------------------------");
			System.out.format("|%10s|%20s|", (MJRBot.getDiscordBot().getClient().isConnected() ? "Connected" : "Disconnected"), numGuilds);
			System.out.println("\n---------------------------------");

			System.out.println("---------------------------------");
			System.out.format("|%20s|%30s|", "Guild ID", "Guild Name");
			System.out.println("\n---------------------------------");
			for (int i = 0; i < numGuilds; i++)
				System.out.format((i != 0 ? "\n" : "") + "|%20s|%30s|", MJRBot.getDiscordBot().getClient().getGuilds().collectList().block().get(i).getId().asString(),
						MJRBot.getDiscordBot().getClient().getGuilds().collectList().block().get(i).getName());
			System.out.println("\n---------------------------------");
		} else
			System.out.println("Discord Bot is disconnected!");
	}

	@Override
	public String getDescription() {
		return "Display all guilds the discord bot is connected too";
	}

	@Override
	public String getParametersDescription() {
		return "";
	}

}
