package com.mjr.mjrbot.console.commands;

import org.slf4j.LoggerFactory;

import com.mjr.mjrbot.console.IConsoleCommand;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public class OutputDependencyMessagesCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("OFF") || args[0].equalsIgnoreCase("ERROR") || args[0].equalsIgnoreCase("WARN") || args[0].equalsIgnoreCase("INFO") || args[0].equalsIgnoreCase("DEBUG") || args[0].equalsIgnoreCase("TRACE")
					|| args[0].equalsIgnoreCase("ALL")) {
				LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
				for (ch.qos.logback.classic.Logger l : lc.getLoggerList()) {
					l.setLevel(Level.toLevel(args[0]));
				}
				System.out.println("Output setting for dependencies has been set to " + args[0]);
			} else
				System.out.println("Invalid type, Use OFF, ERROR, WARN, INFO, DEBUG, TRACE, ALL");
		} else
			System.out.println("Invalid syntax, Use toggle depoutput " + getParametersDescription());
	}

	@Override
	public String getDescription() {
		return "Change dependencies outputs logging level for outputs to the console!";
	}

	@Override
	public String getParametersDescription() {
		return "<type>";
	}

}
