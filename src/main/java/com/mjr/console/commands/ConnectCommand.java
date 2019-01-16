package com.mjr.console.commands;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.MJRBot.ConnectionType;
import com.mjr.MJRBot.StorageType;
import com.mjr.console.ConsoleCommand;

public class ConnectCommand extends ConsoleCommand {

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
