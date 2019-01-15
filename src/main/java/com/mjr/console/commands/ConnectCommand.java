package com.mjr.console.commands;

import com.mjr.MJRBot;
import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot.ConnectionType;
import com.mjr.MJRBot.StorageType;
import com.mjr.console.ConsoleCommand;

public class ConnectCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if(args.length > 1) {
			if (args[1].equalsIgnoreCase("database"))
				MJRBot.connectBot(ConnectionType.Database);
			else if (args[1].equalsIgnoreCase("manual"))
				if(args.length == 5) {
					MJRBot.storageType = StorageType.File;
					MJRBot.connectionType = ConnectionType.Manual;
					MJRBot.connectBot(ConnectionType.Manual, BotType.getTypeByName(args[2]), args[3], Integer.parseInt(args[4]));
				}
				else
					System.out.println("Invalid syntax, Use connect " + getParametersDescription().replace("[", "<").replace("]", ">"));
			else
				System.out.println("Invalid Connection Type, Use Database or Manual");
		}
		else {
			System.out.println("Invalid syntax, Use connect " + getParametersDescription());
		}
	}

	@Override
	public String getDescription() {
		return "To start MJRBot";
	}

	@Override
	public String getParametersDescription() {
		return "<type> [platform] [channel] [channel_id]";
	}
}
