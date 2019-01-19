package com.mjr.console.commands;

import com.mjr.console.ConsoleCommand;
import com.mjr.sql.MySQLConnection;

public class DisconnectChannelCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args.length == 2) {
			if (args[1].equalsIgnoreCase("Twitch") || args[1].equalsIgnoreCase("Mixer"))
				MySQLConnection.executeUpdate("DELETE from channels where name = " + "\"" + args[0].toLowerCase() + "\"" + " AND bot_type = " + "\"" + args[1].toLowerCase() + "\"");
			else
				System.out.println("Invalid platform, Use Twitch or Mixer");
		} else
			System.out.println("Invalid syntax, Use channel disconnect " + getParametersDescription());
	}

	@Override
	public String getDescription() {
		return "Disconnect a channel, and remove it from the channel list";
	}

	@Override
	public String getParametersDescription() {
		return "<channelName> <platform>";
	}

}
