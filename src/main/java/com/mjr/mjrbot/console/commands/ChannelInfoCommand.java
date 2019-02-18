package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.ChatBotManager;
import com.mjr.mjrbot.MixerBot;
import com.mjr.mjrbot.TwitchBot;
import com.mjr.mjrbot.console.ConsoleCommand;

public class ChannelInfoCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args.length == 2) {
			if (args[1].equalsIgnoreCase("Twitch")) {
				TwitchBot twitchBot = ChatBotManager.getTwitchBotByChannelID(TwitchBot.getChannelIDFromChannelName(args[0]));
				if (twitchBot == null) {
					System.out.println("Invalid channel name");
					return;
				}
				sendInfo("Twitch", twitchBot.channelName, twitchBot.viewers.size(), twitchBot.subscribers.size(), twitchBot.moderators.size(), twitchBot.isBotConnected());
			} else if (args[1].equalsIgnoreCase("Mixer")) {
				MixerBot mixerBot = ChatBotManager.getMixerBotByChannelName(args[0]);
				if (mixerBot == null) {
					System.out.println("Invalid channel name");
					return;
				}
				sendInfo("Mixer", mixerBot.channelName, mixerBot.getViewers().size(), mixerBot.subscribers.size(), mixerBot.getModerators().size(), mixerBot.isConnected());
			} else
				System.out.println("Invalid platform, Use Twitch or Mixer");
		} else
			System.out.println("Invalid syntax, Use channel reconnect " + getParametersDescription());
	}

	public void sendInfo(String platform, String channel, int viewerCount, int subsCount, int modsCount, boolean connected) {
		System.out.println("Platform " + platform + " \n");
		System.out.println("Channel " + channel + " \n");
		System.out.println("Connected " + connected + " \n" + " \n");
		System.out.println("Num of Viewers " + viewerCount + " \n");
		System.out.println("Num of Subs " + subsCount + " \n");
		System.out.println("Num of Moderators " + modsCount + " \n");
		System.out.println("Num of Moderators " + modsCount + " \n");
	}

	@Override
	public String getDescription() {
		return "Get infomation for a channel";
	}

	@Override
	public String getParametersDescription() {
		return "<channelName> <platform>";
	}

}
