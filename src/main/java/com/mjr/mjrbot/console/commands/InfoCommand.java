package com.mjr.mjrbot.console.commands;

import java.lang.management.ManagementFactory;

import com.mjr.mjrbot.console.ConsoleCommand;

public class InfoCommand extends ConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		System.out.println("");
		System.out.println("---------------MJRBot Proceess Info---------------");
		long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
		long second = (uptime / 1000) % 60;
		long minute = (uptime / (1000 * 60)) % 60;
		long hour = (uptime / (1000 * 60 * 60)) % 24;

		System.out.println("Uptime: " + String.format("Hours:%02d Minutes:%02d Seconds:%02d", hour, minute, second));
		System.out.println("\n");
		System.out.println("Total Mermory/RAM Assigned: " + "" + (Runtime.getRuntime().totalMemory() / 1048576) + " MB");
		System.out.println("Total Mermory/RAM Used: " + "" + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576) + " MB");
		System.out.println("Total Mermory/RAM Free: " + "" + (Runtime.getRuntime().freeMemory() / 1048576) + " MB");
		System.out.println("\n");
		System.out.println("Live Thread Count: " + ManagementFactory.getThreadMXBean().getThreadCount());
		System.out.println("\n");
		System.out.println("run command 'threads' to see a list of threads");
	}

	@Override
	public String getDescription() {
		return "Current running procress info";
	}

	@Override
	public String getParametersDescription() {
		return "";
	}

}
