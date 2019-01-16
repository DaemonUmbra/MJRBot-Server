package com.mjr.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.mjr.console.commands.ChannelListCommand;
import com.mjr.console.commands.ConnectCommand;
import com.mjr.console.commands.DisconnectChannelCommand;
import com.mjr.console.commands.DiscordBotConnectCommand;
import com.mjr.console.commands.DiscordBotDisconnectCommand;
import com.mjr.console.commands.DiscordBotReconnectCommand;
import com.mjr.console.commands.GlobalMessageCommand;
import com.mjr.console.commands.GlobalUpdateBotCommand;
import com.mjr.console.commands.HelpCommand;
import com.mjr.console.commands.MirgrateCommand;
import com.mjr.console.commands.ReconnectChannelCommand;
import com.mjr.console.commands.SetStorageTypeCommand;
import com.mjr.console.commands.VersionCommand;

public class ConsoleCommandManager {
	public static String[] args;

	public static HashMap<String, ConsoleCommand> commands = new HashMap<String, ConsoleCommand>();

	public static void loadCommands() {
		commands.clear();
		commands.put("connect", new ConnectCommand());
		commands.put("help", new HelpCommand());
		commands.put("storage", new SetStorageTypeCommand());
		commands.put("mirgrate", new MirgrateCommand());
		commands.put("gmsg", new GlobalMessageCommand());
		commands.put("gmsgupdate", new GlobalUpdateBotCommand());
		commands.put("version", new VersionCommand());
		commands.put("channels", new ChannelListCommand());
		commands.put("channel disconnect", new DisconnectChannelCommand());
		commands.put("channel reconnect", new ReconnectChannelCommand());
		commands.put("discord disconnect", new DiscordBotDisconnectCommand());
		commands.put("discord reconnect", new DiscordBotReconnectCommand());
		commands.put("discord connect", new DiscordBotConnectCommand());
	}

	public static void onCommand(String message) {
		args = message.split(" ");

		List<String> list = new ArrayList<String>(Arrays.asList(args));

		// Check if known default command
		if (commands.containsKey(args[0].toLowerCase()) || commands.containsKey(args[0].toLowerCase() + " " + args[1].toLowerCase())) {
			ConsoleCommand command = commands.get(args[0].toLowerCase());
			if (command == null) {
				command = commands.get(args[0].toLowerCase() + " " + args[1].toLowerCase());
				list.remove(0);
				list.remove(0);
				args = list.toArray(new String[0]);
			} else {
				list.remove(0);
				args = list.toArray(new String[0]);
			}
			command.onCommand(message, args);
		} else
			System.out.println("Invalid command, You can use 'help' at any time to see possible commands");
	}
}
