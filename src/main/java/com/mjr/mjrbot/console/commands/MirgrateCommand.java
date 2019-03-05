package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.MirgrationType;
import com.mjr.mjrbot.console.IConsoleCommand;

public class MirgrateCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args.length == 2) {
			MirgrationType type = null;
			if (args[1].equalsIgnoreCase("All"))
				type = MirgrationType.All;
			else if (args[1].equalsIgnoreCase("Config"))
				type = MirgrationType.Config;
			else if (args[1].equalsIgnoreCase("Points"))
				type = MirgrationType.Points;
			else if (args[1].equalsIgnoreCase("Ranks"))
				type = MirgrationType.Ranks;
			else if (args[1].equalsIgnoreCase("Quotes"))
				type = MirgrationType.Quotes;
			if (type != null)
				MJRBot.runMirgration(args[0], type);
			else
				System.out.println("Invalid Type, Use All or Config or Points or Ranks or Quotes");
		} else {
			System.out.println("Invalid syntax, Use mirgrate " + getParametersDescription());
		}
	}

	@Override
	public String getDescription() {
		return "Used to mirgrate data from File storage type to Database storage type";
	}

	@Override
	public String getParametersDescription() {
		return "<channelName> <type>";
	}
}
