package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.AnalyticsData;
import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.console.IConsoleCommand;
import com.mjr.mjrbot.storage.sql.MySQLConnection;

public class SyncAnalyticsCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (MJRBot.storageType == StorageType.Database && MySQLConnection.isConnected())
			AnalyticsData.sendData();
		else
			System.out.println("Storage Type needs to be set to 'database' to use this command!");
	}

	@Override
	public String getDescription() {
		return "Sends Analytics to the DB";
	}

	@Override
	public String getParametersDescription() {
		return "";
	}

}
