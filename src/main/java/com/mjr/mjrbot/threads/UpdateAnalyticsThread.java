package com.mjr.mjrbot.threads;

import com.mjr.mjrbot.AnalyticsData;
import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MJRBot.StorageType;
import com.mjr.mjrbot.util.MJRBotUtilities;

public class UpdateAnalyticsThread extends Thread {

	public UpdateAnalyticsThread() {
		super("UpdateAnalyticsThread");
	}

	@Override
	public void run() {
		while (true) {
			if (MJRBot.storageType == StorageType.Database) {
				AnalyticsData.sendData();
			}
			try {
				Thread.sleep(300000);
			} catch (InterruptedException e) {
				MJRBotUtilities.logErrorMessage(e);
			}
		}
	}
}
