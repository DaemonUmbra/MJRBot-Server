package com.mjr.mjrbot.console.commands;

import java.io.IOException;

import com.mjr.mjrbot.console.IConsoleCommand;
import com.mjr.mjrbot.storage.BotConfigManager;
import com.mjr.mjrbot.storage.sql.MySQLConnection;
import com.mjr.mjrbot.util.MJRBotUtilities;

public class DatabaseReconnectCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		try {
			BotConfigManager.load();
			MySQLConnection.reconnect();
		} catch (IOException e) {
			MJRBotUtilities.logErrorMessage(e);
		}
	}

	@Override
	public String getDescription() {
		return "Attempt to reconnect database connection";
	}

	@Override
	public String getParametersDescription() {
		return "";
	}

}
