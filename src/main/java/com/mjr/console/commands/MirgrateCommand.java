package com.mjr.console.commands;

import com.mjr.MJRBot;
import com.mjr.MJRBot.MirgrationType;
import com.mjr.console.ConsoleCommand;

public class MirgrateCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args.length == 3) {
			MirgrationType type = null;
			if (args[2].equalsIgnoreCase("All"))
				type = MirgrationType.All;
			else if (args[2].equalsIgnoreCase("Config"))
				type = MirgrationType.Config;
			else if (args[2].equalsIgnoreCase("Points"))
				type = MirgrationType.Points;
			else if (args[2].equalsIgnoreCase("Ranks"))
				type = MirgrationType.Ranks;
			else if (args[2].equalsIgnoreCase("Quotes"))
				type = MirgrationType.Quotes;
			if (type != null)
				MJRBot.runMirgration(args[1], type);
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
