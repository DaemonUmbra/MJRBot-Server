package com.mjr.mjrbot.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.TimeZone;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.util.ConsoleUtil.MessageType;

public class MJRBotUtilities {

	public static int getRandom(int min, int max) {
		if (min > max) {
			throw new IllegalArgumentException("Min " + min + " greater than max " + max);
		}
		return (int) (min + Math.random() * ((long) max - min + 1));
	}

	@SuppressWarnings("unused")
	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
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

	public static void logErrorMessage(String error, final Throwable throwable) {
		String stackTrace = MJRBotUtilities.getStackTraceString(throwable);
		logErrorMessage(error + " - " + stackTrace);
	}

	public static void logErrorMessage(final Throwable throwable, String channelName) {
		String stackTrace = MJRBotUtilities.getStackTraceString(throwable);
		logErrorMessage(channelName + " - " + stackTrace);
	}

	public static void logErrorMessage(final Throwable throwable, BotType type, Object bot) {
		String stackTrace = MJRBotUtilities.getStackTraceString(throwable);
		logErrorMessage(type.getTypeName() + ": " + ChatBotManager.getChannelNameFromBotType(type, bot) + " - " + stackTrace);
	}

	public static void logErrorMessage(final Throwable throwable) {
		String stackTrace = MJRBotUtilities.getStackTraceString(throwable);
		logErrorMessage(stackTrace);
	}

	public static void logErrorMessage(String stackTrace) {
		ConsoleUtil.outputMessage(MessageType.Error, stackTrace);
		if (MJRBot.getDiscordBot() != null)
			MJRBot.getDiscordBot().sendErrorMessage(stackTrace);
	}
}
