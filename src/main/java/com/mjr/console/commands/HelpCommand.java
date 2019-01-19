package com.mjr.console.commands;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.mjr.console.ConsoleCommand;
import com.mjr.console.ConsoleCommandManager;

public class HelpCommand extends ConsoleCommand {

	@SuppressWarnings("rawtypes")
	@Override
	public void onCommand(String message, String[] args) {
		System.out.println("-----Help Commands-----");
		System.out.println("<> Required Parameters");
		System.out.println("[] Optional Parameters");
		System.out.println("");
		Iterator<Entry<String, ConsoleCommand>> it = ConsoleCommandManager.commands.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = it.next();
			ConsoleCommand command = ((ConsoleCommand) pair.getValue());
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
