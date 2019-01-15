package com.mjr.console;

import java.util.HashMap;

import com.mjr.console.commands.ChannelListCommand;
import com.mjr.console.commands.ConnectCommand;
import com.mjr.console.commands.GlobalMessageCommand;
import com.mjr.console.commands.GlobalUpdateBotCommand;
import com.mjr.console.commands.HelpCommand;
import com.mjr.console.commands.MirgrateCommand;
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
	}

	public static void onCommand(String message) {
		args = message.split(" ");

		// Check if known default command
		if (commands.containsKey(args[0].toLowerCase())) {
			ConsoleCommand command = commands.get(args[0].toLowerCase());
			command.onCommand(message, args);
		} else
			System.out.println("Invalid command, You can use 'help' at any time to see possible commands");
	}
}
