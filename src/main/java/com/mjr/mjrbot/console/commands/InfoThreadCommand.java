package com.mjr.mjrbot.console.commands;

import java.lang.management.ManagementFactory;
import java.util.Comparator;
import java.util.stream.Stream;

import com.mjr.mjrbot.console.IConsoleCommand;

public class InfoThreadCommand implements IConsoleCommand {

	@Override
	public void onCommand(String message, String[] args) {
		System.out.println("Threads");
		System.out.format("|%50s|%10s|%20s|%20s|", "Name", "Alive", "State", "Total Tick Time");
		System.out.println("\n");
		Stream<Thread> set = Thread.getAllStackTraces().keySet().stream().sorted(Comparator.comparing(Thread::getName));
		Object[] setArray = set.toArray();
		for (int i = 0; i < setArray.length; i++) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Thread t = (Thread) setArray[i];
			System.out.format("\n|%50s|%10s|%20s|%20s|", t.getName(), t.isAlive(), t.getState(), (ManagementFactory.getThreadMXBean().getThreadCpuTime(t.getId()) / 1000000) + " ms");
		}
	}

	@Override
	public String getDescription() {
		return "Current running procress thread info";
	}

	@Override
	public String getParametersDescription() {
		return "";
	}

}
