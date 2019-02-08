package com.mjr.threads;

import com.mjr.AnalyticsData;
import com.mjr.MJRBot;
import com.mjr.MJRBot.StorageType;

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
				MJRBot.logErrorMessage(e);
			}
		}
	}
}
