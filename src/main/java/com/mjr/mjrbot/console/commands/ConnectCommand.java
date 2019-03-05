package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.ConnectionType;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.console.IConsoleCommand;

public class ConnectCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("database"))
				MJRBot.connectBot(ConnectionType.Database);
			else if (args[0].equalsIgnoreCase("manual"))
				if (args.length == 4) {
					MJRBot.storageType = StorageType.File;
					MJRBot.connectionType = ConnectionType.Manual;
					MJRBot.connectBot(ConnectionType.Manual, BotType.getTypeByName(args[1]), args[2], Integer.parseInt(args[3]));
				} else
					System.out.println("Invalid syntax, Use connect " + getParametersDescription().replace("[", "<").replace("]", ">"));
			else
				System.out.println("Invalid Connection Type, Use Database or Manual");
		} else {
			System.out.println("Invalid syntax, Use connect " + getParametersDescription());
		}
	}

	@Override
	public String getDescription() {
		return "Setup/Connect twitch/mixer bots";
	}

	@Override
	public String getParametersDescription() {
		return "<type> [platform] [channel] [channel_id]";
	}
}
