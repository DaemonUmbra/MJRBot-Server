package com.mjr.console.commands;

import com.mjr.ChatBotManager;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.console.ConsoleCommand;

public class ReconnectChannelCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args.length == 3) {
			if (args[2].equalsIgnoreCase("Twitch")) {
				TwitchBot twitchBot = ChatBotManager.getTwitchBotByChannelName(args[1]);
				twitchBot.disconnectTwitch();
				ChatBotManager.removeTwitchBot(twitchBot);
			} else if (args[2].equalsIgnoreCase("Mixer")) {
				MixerBot mixerBot = ChatBotManager.getMixerBotByChannelName(args[1]);
				mixerBot.disconnectMixer();
				ChatBotManager.removeMixerBot(mixerBot);
			}
			else
				System.out.println("Invalid platform, Use Twitch or Mixer");
		} else
			System.out.println("Invalid syntax, Use reconnectChannel " + getParametersDescription());
	}

	@Override
	public String getDescription() {
		return "Reconnect a channel, and remove it from the channel list";
	}

	@Override
	public String getParametersDescription() {
		return "<channelName> <platform>";
	}

}
