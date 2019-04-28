package com.mjr.mjrbot.console.commands;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.mjr.mjrbot.console.ConsoleCommandManager;
import com.mjr.mjrbot.console.IConsoleCommand;

public class HelpCommand implements IConsoleCommand {

	@SuppressWarnings("rawtypes")
	@Override
	public void onCommand(String message, String[] args) {
		System.out.println("");
		System.out.println("---------------Help Commands---------------");
		System.out.println("<> Required Parameters");
		System.out.println("[] Optional Parameters");
		System.out.println("");
		Iterator<Entry<String, IConsoleCommand>> it = ConsoleCommandManager.commands.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = it.next();
			IConsoleCommand command = ((IConsoleCommand) pair.getValue());
			System.out.println(pair.getKey().toString() + " " + command.getParametersDescription() + " - " + command.getDescription());
		}
	}

	@Override
	public String getDescription() {
		return "Display a list of commands";
	}

	@Override
	public String getParametersDescription() {
		return "";
	}
}
