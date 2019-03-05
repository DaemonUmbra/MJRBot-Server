package com.mjr.mjrbot.console;

public interface IConsoleCommand {
	public void onCommand(String message, String[] args);

	public String getDescription();

	public String getParametersDescription();
}
