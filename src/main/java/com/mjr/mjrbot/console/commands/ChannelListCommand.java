package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.console.IConsoleCommand;

public class ChannelListCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (ChatBotManager.getTwitchBots().size() == 0 && ChatBotManager.getMixerBots().size() == 0)
			System.out.println("No connected bots!");
		else {
			System.out.println("-------------------------------------------------------");
			System.out.format("|%10s|%20s|%20s|", "Platform", "Channel Name", "Status");
			System.out.println("\n-------------------------------------------------------");
			for (TwitchBot bot : ChatBotManager.getTwitchBots().values()) { 
				System.out.format("|%10s|%20s|%20s|", "Twitch", TwitchBot.getChannelNameFromChannelID(bot.getChannelID()), (bot.isBotSetupCompleted() ? "Setup Completed" : "Setup Failed"));
				System.out.println();
			}
			for (MixerBot bot : ChatBotManager.getMixerBots().values()) {
				System.out.format("|%10s|%20s|%20s|", "Mixer", bot.getChannelName(), (bot.isConnected() ? "Connected" : "Disconnected"));
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
