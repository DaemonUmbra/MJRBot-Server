package com.mjr.mjrbot.console.commands;

import java.sql.SQLException;

import com.mjr.mjrbot.console.IConsoleCommand;
import com.mjr.mjrbot.storage.sql.MySQLConnection;
import com.mjr.mjrbot.util.MJRBotUtilities;

public class DatabaseInfoCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		try {
			System.out.println("");
			System.out.println("---------------Database Info---------------");
			System.out.println("Connected: " + MySQLConnection.getConnection().isValid(0));
			if (MySQLConnection.isConnected()) {
				System.out.println("IpAddress/Username: " + MySQLConnection.getConnection().getMetaData().getUserName());
			}
		} catch (SQLException e) {
			MJRBotUtilities.logErrorMessage(e);
		}
	}

	@Override
	public String getDescription() {
		return "Display database info";
	}

	@Override
	public String getParametersDescription() {
		return "";
	}
}
