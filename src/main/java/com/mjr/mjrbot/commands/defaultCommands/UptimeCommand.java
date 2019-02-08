package com.mjr.mjrbot.commands.defaultCommands;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.mjr.mjrbot.ChatBotManager.BotType;
import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.MixerBot;
import com.mjr.mjrbot.Permissions.PermissionLevel;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.util.HTTPConnect;
import com.mjr.mjrbot.util.TwitchMixerAPICalls;
import com.mjr.mjrbot.util.Utilities;

public class UptimeCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (type == BotType.Twitch) {
			String result = null;
			try {
				result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetStreamsAPI(Utilities.getChannelIDFromBotType(type, bot)));
			} catch (IOException e) {
				MJRBot.logErrorMessage(e, Utilities.getChannelNameFromBotType(type, bot));
			}
			if (result.contains("created_at")) {
				String upTime = result.substring(result.indexOf("created_at") + 13);
				upTime = upTime.substring(0, 20);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				format.setTimeZone(TimeZone.getTimeZone("UTC"));
				Date parse = null;

				try {
					parse = format.parse(upTime);
				} catch (ParseException e) {
					MJRBot.logErrorMessage(e);
				}

				runCommand(type, bot, sender, parse);
			} else {
				Utilities.sendMessage(type, bot, "@" + sender + " " + Utilities.getChannelNameFromBotType(type, bot) + " is currently not streaming!");
			}
		} else if (type == BotType.Mixer) {
			MixerBot mixerBot = ((MixerBot) bot);
			if (mixerBot.isStreaming())
				runCommand(type, bot, sender, mixerBot.getUpdatedAt());
			else
				Utilities.sendMessage(type, bot, "@" + sender + " " + Utilities.getChannelNameFromBotType(type, bot) + " is currently not streaming!");
		}
	}

	public void runCommand(BotType type, Object bot, String sender, Date date2) {
		OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
		Date date = Date.from(utc.toInstant());
		long diffInMilliSec = date.getTime() - date2.getTime();
		long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMilliSec) % 60;
		long diffHours = TimeUnit.MILLISECONDS.toHours(diffInMilliSec) % 24;
		long diffDays = TimeUnit.MILLISECONDS.toDays(diffInMilliSec) % 365;

		Utilities.sendMessage(type, bot, "@" + sender + " " + Utilities.getChannelNameFromBotType(type, bot) + " has been live for " + diffDays + " day(s) " + diffHours + " hour(s) " + diffMinutes + " minute(s)");
	}

	@Override
	public String getPermissionLevel() {
		return PermissionLevel.User.getName();
	}

	@Override
	public boolean hasCooldown() {
		return true;
	}
}
