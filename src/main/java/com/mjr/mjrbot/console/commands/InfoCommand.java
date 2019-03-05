package com.mjr.mjrbot.console.commands;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import com.mjr.mjrbot.console.IConsoleCommand;

public class InfoCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		System.out.println("");
		System.out.println("---------------MJRBot Process Info---------------");
		long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
		final long day = TimeUnit.MILLISECONDS.toDays(uptime);
		final long hours = TimeUnit.MILLISECONDS.toHours(uptime) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(uptime));
		final long minutes = TimeUnit.MILLISECONDS.toMinutes(uptime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(uptime));
		final long seconds = TimeUnit.MILLISECONDS.toSeconds(uptime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(uptime));
		final long ms = TimeUnit.MILLISECONDS.toMillis(uptime) - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(uptime));
		System.out.println("Uptime: " + String.format("%d Days %d Hours %d Minutes %d Seconds %d Milliseconds", day, hours, minutes, seconds, ms));
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
