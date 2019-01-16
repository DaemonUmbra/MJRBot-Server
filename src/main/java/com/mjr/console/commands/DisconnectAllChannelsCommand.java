package com.mjr.console.commands;

import com.mjr.ChatBotManager;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.console.ConsoleCommand;
import com.mjr.sql.MySQLConnection;

public class DisconnectAllChannelsCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		for (TwitchBot bot : ChatBotManager.getTwitchBots().values()) {
			bot.disconnectTwitch();
			MySQLConnection.executeUpdate("DELETE from channels where name = " + "\"" + bot.channelName.toLowerCase() + "\"" + " AND bot_type = " + "\"" + "Twitch" + "\"");
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
