package com.mjr.mjrbot.commands.defaultCommands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.commands.ICommand;
import com.mjr.mjrbot.util.HTTPConnect;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.PermissionsManager.PermissionLevel;
import com.mjr.mjrbot.util.TwitchMixerAPICalls;

public class AccountLifeCommand implements ICommand {

	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		try {
			String result = "";
			if (type == BotType.Twitch)
				result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetUsersAPI(sender.toLowerCase()));
			else if (type == BotType.Mixer)
				result = HTTPConnect.getRequest(TwitchMixerAPICalls.mixerGetChannelsAPI(sender.toLowerCase()));
			if (result.contains("created_at") || result.contains("createdAt")) {
				String time = result.substring(result.contains("created_at") ? (result.indexOf("created_at") + 13) : (result.indexOf("createdAt") + 12));

				if (type == BotType.Twitch)
					time = time.substring(0, 20);
				else if (type == BotType.Mixer)
					time = time.substring(0, 19);
				SimpleDateFormat format = null;
				if (type == BotType.Twitch)
					format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				else if (type == BotType.Mixer)
					format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				format.setTimeZone(TimeZone.getTimeZone("UTC"));
				Date parse = null;

				try {
					parse = format.parse(time);
				} catch (ParseException e) {
					MJRBotUtilities.logErrorMessage(e);
				}

				OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
				Date date = Date.from(utc.toInstant());

				long diffInMilliSec = date.getTime() - parse.getTime();
				long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMilliSec) % 60;
				long diffHours = TimeUnit.MILLISECONDS.toHours(diffInMilliSec) % 24;
				long diffDays = TimeUnit.MILLISECONDS.toDays(diffInMilliSec) % 365;
				long diffYears = TimeUnit.MILLISECONDS.toDays(diffInMilliSec) / 365l;

				ChatBotManager.sendMessage(type, bot,
						"@" + sender + " your " + (type == BotType.Twitch ? "twitch" : "mixer") + " account is " + diffYears + " year(s) " + diffDays + " day(s) " + diffHours + " hour(s) " + diffMinutes + " minute(s) old");
			}
		} catch (Exception e) {
			MJRBotUtilities.logErrorMessage(e);
		}
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
