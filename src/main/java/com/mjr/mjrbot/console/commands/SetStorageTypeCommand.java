package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.console.IConsoleCommand;

public class SetStorageTypeCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("database"))
				MJRBot.storageType = StorageType.Database;
			else if (args[0].equalsIgnoreCase("file"))
				MJRBot.storageType = StorageType.File;
			else
				System.out.println("Invalid Storage Type, Use Database or File");
		} else {
			System.out.println("Invalid syntax, Use storage " + getParametersDescription());
		}
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
