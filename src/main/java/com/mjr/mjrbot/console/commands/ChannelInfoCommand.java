package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.bases.MixerBot;
import com.mjr.mjrbot.bots.bases.TwitchBot;
import com.mjr.mjrbot.console.IConsoleCommand;
import com.mjr.twitchframework.irc.TwitchIRCClient;
import com.mjr.twitchframework.irc.TwitchIRCManager;

public class ChannelInfoCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args.length == 2) {
			if (args[1].equalsIgnoreCase("Twitch")) {
				TwitchBot twitchBot = ChatBotManager.getTwitchBotByChannelID(TwitchBot.getChannelIDFromChannelName(args[0]));
				if (twitchBot == null) {
					System.out.println("Invalid channel name");
					return;
				}
				sendInfo("Twitch", twitchBot.getChannelName(), twitchBot.getTwitchData().getViewers().size(), twitchBot.getTwitchData().getSubscribers().size(), twitchBot.getTwitchData().getModerators().size(),
						twitchBot.getTwitchData().getVips().size(), TwitchIRCManager.getClientByChannelName(twitchBot.getChannelName()), twitchBot.isBotSetupCompleted(), false);
			} else if (args[1].equalsIgnoreCase("Mixer")) {
				MixerBot mixerBot = ChatBotManager.getMixerBotByChannelName(args[0]);
				if (mixerBot == null) {
					System.out.println("Invalid channel name");
					return;
				}
				sendInfo("Mixer", mixerBot.getChannelName(), mixerBot.getViewers().size(), mixerBot.getMixerData().getSubscribers().size(), mixerBot.getModerators().size(), 0, null, !mixerBot.isChatConnectionClosed(),
						!mixerBot.isConstellationConnectionClosed());
			} else
				System.out.println("Invalid platform, Use Twitch or Mixer");
		} else
			System.out.println("Invalid syntax, Use channel info " + getParametersDescription());
	}

	public void sendInfo(String platform, String channel, int viewerCount, int subsCount, int modsCount, int vipsCount, TwitchIRCClient client, boolean setup, boolean constellationConnected) {
		System.out.println("");
		System.out.println("---------------MJRBot Channel Info---------------");
		System.out.println("Platform: " + platform);
		System.out.println("Channel: " + channel);
		if (platform.equalsIgnoreCase("twitch")) {
			System.out.println("\nSetup/Connected to a client: " + setup);
			System.out.println("Client ID: " + client.getID());
			System.out.println("Client Connected: " + client.isClientConnected());
			System.out.println("Client Num of channels " + client.getChannelsList().size());
		} else if (platform.equalsIgnoreCase("Mixer")) {
			System.out.println("Connected: " + setup + " \n");
			System.out.println("Connected to Constellation: " + constellationConnected);
		}
		System.out.println("\nNum of Viewers: " + viewerCount);
		System.out.println("Num of Subs: " + subsCount);
		System.out.println("Num of Moderators: " + modsCount);
		if (platform.equalsIgnoreCase("twitch"))
			System.out.println("Num of VIPs: " + vipsCount);
		System.out.println("-------------------------------------------------");
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
