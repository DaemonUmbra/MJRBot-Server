package com.mjr.console.commands;

import com.mjr.ChatBotManager;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.console.ConsoleCommand;

public class ChannelListCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (ChatBotManager.getTwitchBots().size() == 0 && ChatBotManager.getMixerBots().size() == 0)
			System.out.println("No connected bots!");
		else {
			System.out.println("-------------------------------------------------------");
			System.out.format("|%10s|%20s|%20s|", "Platform", "Channel Name", "Status");
			System.out.println("\n-------------------------------------------------------");
			for (TwitchBot bot : ChatBotManager.getTwitchBots().values()) {
				System.out.format("|%10s|%20s|%20s|", "Twitch", bot.channelName, (bot.isConnected() ? "Connected" : "Disconnected"));
				System.out.println();
			}
			for (MixerBot bot : ChatBotManager.getMixerBots().values()) {
				System.out.format("|%10s|%20s|%20s|", "Mixer", bot.channelName, (bot.isConnected() ? "Connected" : "Disconnected"));
				System.out.println();
			}
			System.out.println("\n-------------------------------------------------------");

		}
	}

	@Override
	public String getDescription() {
		return "Gets a list of all connected channels";
	}

	@Override
	public String getParametersDescription() {
		return "";
	}

}
