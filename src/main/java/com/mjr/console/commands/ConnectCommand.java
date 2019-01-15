package com.mjr.console.commands;

import com.mjr.MJRBot;
import com.mjr.MJRBot.ConnectionType;
import com.mjr.console.ConsoleCommand;

public class ConnectCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args[1].equalsIgnoreCase("database"))
			MJRBot.connectBot(ConnectionType.Database);
		else if (args[1].equalsIgnoreCase("manual"))
			MJRBot.connectBot(ConnectionType.Manual);
		else
			System.out.println("Invalid Connection Type, Use Database or Manual");
	}

	@Override
	public String getDescription() {
		return "To start MJRBot";
	}

	@Override
	public String getParametersDescription() {
		return "<type>";
	}
}
