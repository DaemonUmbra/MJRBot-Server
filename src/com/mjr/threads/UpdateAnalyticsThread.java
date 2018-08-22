package com.mjr.threads;

import com.mjr.AnalyticsData;
import com.mjr.MJRBot;

public class UpdateAnalyticsThread extends Thread {
    @Override
    public void run() {
	while (true) {
	    if (MJRBot.useFileSystem == false) {
		AnalyticsData.sendData();
	    }
	    try {
		Thread.sleep(300000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }
}
