package com.mjr.mjrbot.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import com.mjr.mjrbot.console.commands.ChannelDisconnectCommand;
import com.mjr.mjrbot.console.commands.ChannelInfoCommand;
import com.mjr.mjrbot.console.commands.ChannelListCommand;
import com.mjr.mjrbot.console.commands.ChannelReconnectCommand;
import com.mjr.mjrbot.console.commands.ChannelsDisconnectAllCommand;
import com.mjr.mjrbot.console.commands.ChannelsReconnectAllCommand;
import com.mjr.mjrbot.console.commands.ConnectCommand;
import com.mjr.mjrbot.console.commands.DatabaseInfoCommand;
import com.mjr.mjrbot.console.commands.DatabaseReconnectCommand;
import com.mjr.mjrbot.console.commands.DiscordBotConnectCommand;
import com.mjr.mjrbot.console.commands.DiscordBotDisconnectCommand;
import com.mjr.mjrbot.console.commands.DiscordBotInfoCommand;
import com.mjr.mjrbot.console.commands.DiscordBotReconnectCommand;
import com.mjr.mjrbot.console.commands.ExitProcessCommand;
import com.mjr.mjrbot.console.commands.GlobalMessageCommand;
import com.mjr.mjrbot.console.commands.GlobalUpdateBotCommand;
import com.mjr.mjrbot.console.commands.HelpCommand;
import com.mjr.mjrbot.console.commands.InfoCommand;
import com.mjr.mjrbot.console.commands.InfoThreadCommand;
import com.mjr.mjrbot.console.commands.MirgrateCommand;
import com.mjr.mjrbot.console.commands.OutputDependencyMessagesCommand;
import com.mjr.mjrbot.console.commands.OutputMessagesCommand;
import com.mjr.mjrbot.console.commands.SetStorageTypeCommand;
import com.mjr.mjrbot.console.commands.SyncAnalyticsCommand;
import com.mjr.mjrbot.console.commands.VersionCommand;

public class ConsoleCommandManager {
	public static String[] args;

	public static TreeMap<String, IConsoleCommand> commands = new TreeMap<String, IConsoleCommand>();

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
		commands.put("channel info", new ChannelInfoCommand());
		commands.put("channel disconnect", new ChannelDisconnectCommand());
		commands.put("channel disconnectall", new ChannelsDisconnectAllCommand());
		commands.put("channel reconnect", new ChannelReconnectCommand());
		commands.put("channel reconnectall", new ChannelsReconnectAllCommand());

		commands.put("discord disconnect", new DiscordBotDisconnectCommand());
		commands.put("discord reconnect", new DiscordBotReconnectCommand());
		commands.put("discord connect", new DiscordBotConnectCommand());
		commands.put("discord info", new DiscordBotInfoCommand());

		commands.put("toggle output", new OutputMessagesCommand());
		commands.put("toggle depoutput", new OutputDependencyMessagesCommand());
		commands.put("sync analytics", new SyncAnalyticsCommand());
		commands.put("exit", new ExitProcessCommand());

		commands.put("info", new InfoCommand());
		commands.put("threads", new InfoThreadCommand());

		commands.put("db info", new DatabaseInfoCommand());
		commands.put("db reconnect", new DatabaseReconnectCommand());
	}

	public static void onCommand(String message) {
		args = message.split(" ");

		List<String> list = new ArrayList<String>(Arrays.asList(args));

		// Check if known default command
		if (commands.containsKey(args[0].toLowerCase()) || (args.length >= 2 && commands.containsKey(args[0].toLowerCase() + " " + args[1].toLowerCase()))) {
			IConsoleCommand command = commands.get(args[0].toLowerCase());
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
