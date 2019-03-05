package com.mjr.mjrbot.commands.defaultCommands;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.util.HTTPConnect;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;
import com.mjr.mjrbot.util.TwitchMixerAPICalls;

public class UptimeCommand implements ICommand {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (type == BotType.Twitch) {
			String result = null;
			try {
				result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetStreamsAPI(MJRBotUtilities.getChannelIDFromBotType(type, bot)));
			} catch (IOException e) {
				MJRBotUtilities.logErrorMessage(e, MJRBotUtilities.getChannelNameFromBotType(type, bot));
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
					MJRBotUtilities.logErrorMessage(e);
				}

				runCommand(type, bot, sender, parse);
			} else {
				MJRBotUtilities.sendMessage(type, bot, "@" + sender + " " + MJRBotUtilities.getChannelNameFromBotType(type, bot) + " is currently not streaming!");
			}
		} else if (type == BotType.Mixer) {
			MixerBot mixerBot = ((MixerBot) bot);
			if (mixerBot.isStreaming())
				runCommand(type, bot, sender, mixerBot.getUpdatedAt());
			else
				MJRBotUtilities.sendMessage(type, bot, "@" + sender + " " + MJRBotUtilities.getChannelNameFromBotType(type, bot) + " is currently not streaming!");
		}
	}

	public void runCommand(BotType type, Object bot, String sender, Date date2) {
		OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
		Date date = Date.from(utc.toInstant());
		long diffInMilliSec = date.getTime() - date2.getTime();
		long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMilliSec) % 60;
		long diffHours = TimeUnit.MILLISECONDS.toHours(diffInMilliSec) % 24;
		long diffDays = TimeUnit.MILLISECONDS.toDays(diffInMilliSec) % 365;

		MJRBotUtilities.sendMessage(type, bot, "@" + sender + " " + MJRBotUtilities.getChannelNameFromBotType(type, bot) + " has been live for " + diffDays + " day(s) " + diffHours + " hour(s) " + diffMinutes + " minute(s)");
	}

	@Override
	public PermissionLevel getPermissionLevel() {
		return PermissionLevel.User;
	}

	@Override
	public boolean hasCooldown() {
		return true;
	}
}
