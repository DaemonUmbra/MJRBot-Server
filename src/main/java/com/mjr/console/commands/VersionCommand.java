package com.mjr.console.commands;

import com.mjr.MJRBot;
import com.mjr.console.ConsoleCommand;

public class VersionCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		System.out.println("v" + MJRBot.VERSION);
	}

	@Override
	public String getDescription() {
		return "Get the current version";
	}

	@Override
	public String getParametersDescription() {
		return "";
	}

}
