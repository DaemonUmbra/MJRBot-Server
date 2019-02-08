package com.mjr.mjrbot.console;

public abstract class ConsoleCommand {
	public abstract void onCommand(String message, String[] args);

	public abstract String getDescription();

	public abstract String getParametersDescription();
}
