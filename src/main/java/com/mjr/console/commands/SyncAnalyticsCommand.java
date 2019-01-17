package com.mjr.console.commands;

import com.mjr.AnalyticsData;
import com.mjr.MJRBot;
import com.mjr.MJRBot.StorageType;
import com.mjr.console.ConsoleCommand;

public class SyncAnalyticsCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (MJRBot.storageType == StorageType.Database)
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
