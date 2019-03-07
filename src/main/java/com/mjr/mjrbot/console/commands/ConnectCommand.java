package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.ConnectionType;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.console.IConsoleCommand;
import com.mjr.mjrbot.util.HTTPConnect;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.TwitchMixerAPICalls;

public class ConnectCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		try {
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("database"))
					MJRBot.connectBot(ConnectionType.Database);
				else if (args[0].equalsIgnoreCase("manual"))
					if (args.length == 3) {
						String result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetUserIDFromChannelNameAPI(args[2]));
						String id = result.substring(result.indexOf("_id") + 6);
						id = id.substring(0, id.indexOf(",") - 1);
						MJRBot.storageType = StorageType.File;
						MJRBot.connectionType = ConnectionType.Manual;
						MJRBot.connectBot(ConnectionType.Manual, BotType.getTypeByName(args[1]), args[2], Integer.parseInt(id));
					} else
						System.out.println("Invalid syntax, Use connect " + getParametersDescription().replace("[", "<").replace("]", ">"));
				else
					System.out.println("Invalid Connection Type, Use Database or Manual");
			} else {
				System.out.println("Invalid syntax, Use connect " + getParametersDescription());
			}
		} catch (Exception e) {
			MJRBotUtilities.logErrorMessage(e);
		}
	}

	@Override
	public String getDescription() {
		return "Setup/Connect twitch/mixer bots";
	}

	@Override
	public String getParametersDescription() {
		return "<type> [platform] [channel]";
	}
}
