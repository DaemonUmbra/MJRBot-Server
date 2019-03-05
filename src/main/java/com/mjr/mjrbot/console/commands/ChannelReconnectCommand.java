package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.console.IConsoleCommand;

public class ChannelReconnectCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args.length == 2) {
			if (args[1].equalsIgnoreCase("Twitch")) {
				TwitchBot twitchBot = ChatBotManager.getTwitchBotByChannelID(TwitchBot.getChannelIDFromChannelName(args[0]));
				if (twitchBot == null) {
					System.out.println("Invalid channel name");
					return;
				}
				twitchBot.disconnectTwitch();
				ChatBotManager.removeTwitchBot(twitchBot);
			} else if (args[1].equalsIgnoreCase("Mixer")) {
				MixerBot mixerBot = ChatBotManager.getMixerBotByChannelName(args[0]);
				if (mixerBot == null) {
					System.out.println("Invalid channel name");
					return;
				}
				mixerBot.disconnectMixer();
				ChatBotManager.removeMixerBot(mixerBot);
			} else
				System.out.println("Invalid platform, Use Twitch or Mixer");
		} else
			System.out.println("Invalid syntax, Use channel reconnect " + getParametersDescription());
	}

	@Override
	public String getDescription() {
		return "Reconnect a channel";
	}

	@Override
	public String getParametersDescription() {
		return "<channelName> <platform>";
	}

}
