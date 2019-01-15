package com.mjr.console.commands;

import com.mjr.ChatBotManager;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.console.ConsoleCommand;

public class ChannelListCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if(ChatBotManager.getTwitchBots().size() == 0 && ChatBotManager.getMixerBots().size() == 0)
			System.out.println("No connected bots!");
		else {
			System.out.println("Platform | Channel Name | Status");
			for (TwitchBot bot : ChatBotManager.getTwitchBots().values()) {
				System.out.println("Twitch | " + bot.channelName + " | " + (bot.isConnected() ? "Connected" : "Disconnected"));
			}
			for (MixerBot bot : ChatBotManager.getMixerBots().values()) {
				System.out.println("Mixer | " + bot.channelName + " | " + (bot.isConnected() ? "Connected" : "Disconnected"));
			}
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
