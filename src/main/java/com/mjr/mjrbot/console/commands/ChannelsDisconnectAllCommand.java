package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.bases.MixerBot;
import com.mjr.mjrbot.bots.bases.TwitchBot;
import com.mjr.mjrbot.console.IConsoleCommand;
import com.mjr.mjrbot.storage.sql.MySQLConnection;

public class ChannelsDisconnectAllCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		for (TwitchBot bot : ChatBotManager.getTwitchBots().values()) {
			bot.disconnectTwitch();
			MySQLConnection.executeUpdate("DELETE from channels where twitch_channel_id = " + "\"" + bot.getChannelID() + "\"" + " AND bot_type = " + "\"" + "Twitch" + "\"");
		}
		for (MixerBot bot : ChatBotManager.getMixerBots().values()) {
			bot.disconnectMixer();
			MySQLConnection.executeUpdate("DELETE from channels where name = " + "\"" + bot.getChannelName().toLowerCase() + "\"" + " AND bot_type = " + "\"" + "Mixer" + "\"");
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
