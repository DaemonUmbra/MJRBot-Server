package com.mjr.mjrbot.console.commands;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.console.IConsoleCommand;

public class VersionCommand implements IConsoleCommand {

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
