package com.mjr.threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.mjr.ConsoleUtil;
import com.mjr.ConsoleUtil.MessageType;
import com.mjr.MJRBot.BotType;
import com.mjr.TwitchBot;
import com.mjr.storage.Config;
import com.mjr.storage.EventLog;
import com.mjr.storage.EventLog.EventType;
import com.mjr.storage.PointsSystem;
import com.mjr.storage.RankSystem;

public class GetViewersThread extends Thread {

    private TwitchBot bot;

    public GetViewersThread(TwitchBot bot) {
	super();
	this.bot = bot;
    }

    @Override
    public void run() {
	while (bot.ConnectedToChannel) {
	    try {
		String result = "";
		String viewers = "";
		String moderators = "";
		String staff = "";
		String admins = "";
		String global_moderators = "";
		String end_result = "";
		URL url = new URL("https://tmi.twitch.tv/group/user/" + bot.channelName + "/chatters");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = "";
		while ((line = reader.readLine()) != null) {
		    result += line;
		}
		reader.close();
		if (result.contains("moderators" + "\"" + ": [") && !result.contains("moderators" + "\"" + ": [],")) {
		    moderators = result.substring(result.indexOf("moderators") + 14);
		    result = moderators;
		    moderators = moderators.substring(0, moderators.indexOf("],"));
		}
		if (result.contains("staff" + "\"" + ": [") && !result.contains("staff" + "\"" + ": [],")) {
		    staff = result.substring(result.indexOf("staff") + 10);
		    result = staff;
		    staff = staff.substring(0, staff.indexOf("],"));
		}
		if (result.contains("admins" + "\"" + ": [") && !result.contains("admins" + "\"" + ": [],")) {
		    admins = result.substring(result.indexOf("admins") + 11);
		    result = result.substring(result.indexOf("admins") + 11);
		    admins = admins.substring(0, admins.indexOf("],"));
		}
		if (result.contains("global_mods" + "\"" + ": [") && !result.contains("global_mods" + "\"" + ": [],")) {
		    global_moderators = result.substring(result.indexOf("global_mods") + 16);
		    result = global_moderators;
		    global_moderators = global_moderators.substring(0, global_moderators.indexOf("],"));
		}
		if (result.contains("viewers" + "\"" + ": [") && !result.contains("viewers" + "\"" + ": []")) {
		    viewers = result.substring(result.indexOf("viewers") + 12);
		    result = viewers;
		    viewers = viewers.substring(0, viewers.indexOf("]"));
		}

		end_result = moderators + staff + admins + global_moderators + viewers;
		end_result = end_result.replace(" ", "");
		end_result = end_result.replace("\"", "");
		if (bot.viewers.isEmpty()) {
		    for (String viewer : end_result.split(",")) {
			if (!bot.viewers.contains(viewer.toLowerCase())) {
			    bot.viewers.add(viewer.toLowerCase());
			}
		    }
		    ConsoleUtil.TextToConsole(bot, BotType.Twitch, bot.channelName, "Bot has the list of current active viewers!",
			    MessageType.Bot, null);

		} else {
		    for (String viewer : end_result.split(",")) {
			if (!bot.viewers.contains(viewer.toLowerCase())) {
			    bot.viewers.add(viewer.toLowerCase());
			    EventLog.addEvent(bot.channelName, viewer, "Joined the channel (Twitch)", EventType.User);
			}
		    }
		    ConsoleUtil.TextToConsole(bot, BotType.Twitch, bot.channelName, "Bot has updated the list of current active viewers!",
			    MessageType.Bot, null);

		    moderators = moderators.replace(" ", "");
		    moderators = moderators.replace("\"", "");
		    for (String mod : moderators.split(",")) {
			if (!bot.moderators.contains(mod.toLowerCase())) {
			    bot.moderators.add(mod.toLowerCase());
			}
		    }
		    ConsoleUtil.TextToConsole(bot, BotType.Twitch, bot.channelName,
			    "Bot has updated the list of current active moderators!", MessageType.Bot, null);
		}

		for (int i = 1; i < bot.viewers.size(); i++) {
		    if (Config.getSetting("Points", bot.channelName).equalsIgnoreCase("true")) {
			if (!PointsSystem.isOnList(bot.viewers.get(i), bot.channelName)) {
			    PointsSystem.setPoints(bot.viewers.get(i),
				    Integer.parseInt(Config.getSetting("StartingPoints", bot.channelName)), bot.channelName, false, false);
			}
		    }
		    if (Config.getSetting("Ranks", bot.channelName).equalsIgnoreCase("true")) {
			if (!RankSystem.isOnList(bot.viewers.get(i), bot.channelName)) {
			    RankSystem.setRank(bot.viewers.get(i), "None", bot.channelName);
			}
		    }
		    if (!bot.viewersJoinedTimes.containsKey(bot.viewers.get(i).toLowerCase().toLowerCase()))
			bot.viewersJoinedTimes.put(bot.viewers.get(i).toLowerCase().toLowerCase(), System.currentTimeMillis());
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    try {
		Thread.sleep(60000 * 2);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }
}
