package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.console.IConsoleCommand;
import com.mjr.mjrbot.storage.sql.MySQLConnection;

public class DatabaseDebugCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args.length == 1) {
			if (MySQLConnection.showDebug == true)
				MySQLConnection.showDebug = false;
			else
				MySQLConnection.showDebug = true;
			System.out.println("Database debug is now set to: " + (MySQLConnection.showDebug ? "true" : "false"));
		} else
			System.out.println("Invalid syntax, Use db debug " + getParametersDescription());
	}

	@Override
	public String getDescription() {
		return "Disable/Enable database debug outputs to the console!";
	}

	@Override
	public String getParametersDescription() {
		return "<true/false>";
	}

}