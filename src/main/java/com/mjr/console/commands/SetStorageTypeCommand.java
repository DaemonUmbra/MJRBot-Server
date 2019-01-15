package com.mjr.console.commands;

import com.mjr.MJRBot;
import com.mjr.MJRBot.StorageType;
import com.mjr.console.ConsoleCommand;

public class SetStorageTypeCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args[1].equalsIgnoreCase("database"))
			MJRBot.storageType = StorageType.Database;
		else if (args[1].equalsIgnoreCase("file"))
			MJRBot.storageType = StorageType.File;
		else
			System.out.println("Invalid Storage Type, Use Database or File");
	}

	@Override
	public String getDescription() {
		return "To set the storage type of the bot";
	}

	@Override
	public String getParametersDescription() {
		return "<type>";
	}

}
