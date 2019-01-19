package com.mjr.console.commands;

import com.mjr.MJRBot;
import com.mjr.console.ConsoleCommand;

public class DiscordBotInfoCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (MJRBot.bot != null) {
			int numGuilds = MJRBot.bot.getClient().getGuilds().count().block().intValue();
			System.out.println("---------------------------------");
			System.out.format("|%10s|%20s|", "Status", "Number of guilds");
			System.out.println("\n---------------------------------");
			System.out.format("|%10s|%20s|", (MJRBot.bot.getClient().isConnected() ? "Connected" : "Disconnected"), numGuilds);
			System.out.println("\n---------------------------------");

			System.out.println("---------------------------------");
			System.out.format("|%20s|%30s|", "Guild ID", "Guild Name");
			System.out.println("\n---------------------------------");
			for (int i = 0; i < numGuilds; i++)
				System.out.format((i != 0 ? "\n" : "") + "|%20s|%30s|", MJRBot.bot.getClient().getGuilds().collectList().block().get(i).getId().asString(), MJRBot.bot.getClient().getGuilds().collectList().block().get(i).getName());
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
