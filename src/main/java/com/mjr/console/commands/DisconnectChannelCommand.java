package com.mjr.console.commands;

import com.mjr.console.ConsoleCommand;
import com.mjr.sql.MySQLConnection;

public class DisconnectChannelCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args.length == 3) {
			if(args[2].equalsIgnoreCase("Twitch") || args[2].equalsIgnoreCase("Mixer"))
				MySQLConnection.executeUpdate("DELETE from channels where name = " + "\"" + args[1].toLowerCase() + "\"" + " AND bot_type = " + "\"" + args[2].toLowerCase() + "\"");
			else
				System.out.println("Invalid platform, Use Twitch or Mixer");
		}
		else
			System.out.println("Invalid syntax, Use disconnectChannel " + getParametersDescription());
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
