package com.mjr.mjrbot.console.commands;

import java.io.IOException;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.MirgrationType;
import com.mjr.mjrbot.MJRBot.PlatformType;
import com.mjr.mjrbot.console.IConsoleCommand;
import com.mjr.mjrbot.util.HTTPConnect;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.TwitchMixerAPICalls;

public class MirgrateCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args.length == 2) {
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
			if (type != null) {
				PlatformType platform = PlatformType.getTypeByString(args[1]);
				String id = "0";
				if(platform == PlatformType.TWITCH) {
					String result = null;
					try {
						result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetUserIDFromChannelNameAPI(args[0]));
					} catch (IOException e) {
						MJRBotUtilities.logErrorMessage(e);
					}
					id = result.substring(result.indexOf("_id") + 6);
					id = id.substring(0, id.indexOf(",") - 1);
				}
				MJRBot.runMirgration(Integer.parseInt(id), args[0], PlatformType.getTypeByString(args[1]), type);
			}
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
		return "<channelName/ID> <platform> <type>";
	}
}
