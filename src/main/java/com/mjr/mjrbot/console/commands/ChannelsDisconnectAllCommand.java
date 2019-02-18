package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.ChatBotManager;
import com.mjr.mjrbot.MixerBot;
import com.mjr.mjrbot.TwitchBot;
import com.mjr.mjrbot.console.ConsoleCommand;
import com.mjr.mjrbot.sql.MySQLConnection;

public class ChannelsDisconnectAllCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		for (TwitchBot bot : ChatBotManager.getTwitchBots().values()) {
			bot.disconnectTwitch();
			MySQLConnection.executeUpdate("DELETE from channels where twitch_channel_id = " + "\"" + bot.channelID + "\"" + " AND bot_type = " + "\"" + "Twitch" + "\"");
		}
		for (MixerBot bot : ChatBotManager.getMixerBots().values()) {
			bot.disconnectMixer();
			MySQLConnection.executeUpdate("DELETE from channels where name = " + "\"" + bot.channelName.toLowerCase() + "\"" + " AND bot_type = " + "\"" + "Mixer" + "\"");
		}
	}

	@Override
	public String getDescription() {
		return "Disconnect all channels, and remove them from the channel list";
	}

	@Override
	public String getParametersDescription() {
		return "";
	}

}
