package com.mjr.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.TimeZone;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;

public class Utilities {

	public static int getRandom(int min, int max) {
		if (min > max) {
			throw new IllegalArgumentException("Min " + min + " greater than max " + max);
		}
		return (int) (min + Math.random() * ((long) max - min + 1));
	}

	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static void sendMessage(BotType type, String channelName, String endMessage) {
		if(MJRBot.developmentDisableSendMessage)
			return;
		if (type == BotType.Twitch) {
			TwitchBot bot = MJRBot.getTwitchBotByChannelName(channelName);
			if (bot != null)
				bot.sendMessage(endMessage);
		} else {
			MixerBot bot = MJRBot.getMixerBotByChannelName(channelName);
			if (bot != null)
				bot.sendMessage(endMessage);
		}
	}

	public static java.util.Date convertTimeZone(java.util.Date date, TimeZone fromTZ, TimeZone toTZ) {
		long fromTZDst = 0;
		if (fromTZ.inDaylightTime(date)) {
			fromTZDst = fromTZ.getDSTSavings();
		}

		long fromTZOffset = fromTZ.getRawOffset() + fromTZDst;

		long toTZDst = 0;
		if (toTZ.inDaylightTime(date)) {
			toTZDst = toTZ.getDSTSavings();
		}
		long toTZOffset = toTZ.getRawOffset() + toTZDst;

		return new java.util.Date(date.getTime() + (toTZOffset - fromTZOffset));
	}
	
	public static String getStackTraceString(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
	     final PrintWriter pw = new PrintWriter(sw, true);
	     throwable.printStackTrace(pw);
	     return sw.getBuffer().toString();
	}
}
