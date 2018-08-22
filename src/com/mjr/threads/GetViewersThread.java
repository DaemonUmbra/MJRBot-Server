package com.mjr.threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.mjr.ConsoleUtil;
import com.mjr.ConsoleUtil.MessageType;
import com.mjr.MJRBot.BotType;
import com.mjr.storage.Config;
import com.mjr.storage.EventLog;
import com.mjr.storage.PointsSystem;
import com.mjr.storage.RankSystem;
import com.mjr.storage.EventLog.EventType;
import com.mjr.TwitchBot;

public class GetViewersThread extends Thread {
    public String result = "";
    public String Viewers = "";
    public String Mods = "";
    public String Staff = "";
    public String Admins = "";
    public String GMods = "";
    public String newresult = "";

    private TwitchBot bot;

    public GetViewersThread(TwitchBot bot) {
	super();
	this.bot = bot;
    }

    @Override
    public void run() {
	while (bot.ConnectedToChannel) {
	    try {
		result = "";
		newresult = "";
		Viewers = "";
		Mods = "";
		Staff = "";
		Admins = "";
		GMods = "";
		URL url = new URL("https://tmi.twitch.tv/group/user/" + bot.channelName + "/chatters");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = "";
		while ((line = reader.readLine()) != null) {
		    result += line;
		}
		reader.close();
		if (result.contains("viewers" + "\"" + ": [") || result.contains("moderators" + "\"" + ": [")
			|| result.contains("staff" + "\"" + ": [") || result.contains("admins" + "\"" + ": [")
			|| result.contains("global_mods" + "\"" + ": [")) {

		    if (result.contains("moderators" + "\"" + ": [") && !result.contains("moderators" + "\"" + ": [],")) {
			Mods = result.substring(result.indexOf("moderators") + 21, result.indexOf("],"));
		    }
		    if (result.contains("staff" + "\"" + ": [") && !result.contains("staff" + "\"" + ": [],")) {
			Staff = "," + result.substring(result.indexOf("staff") + 16);
			Staff = Staff.substring(0, Staff.indexOf("],"));
		    }
		    if (result.contains("admins" + "\"" + ": [") && !result.contains("admins" + "\"" + ": [],")) {
			Admins = "," + result.substring(result.indexOf("admins") + 17);
			Admins = Admins.substring(0, Admins.indexOf("],"));
		    }
		    if (result.contains("global_mods" + "\"" + ": [") && !result.contains("global_mods" + "\"" + ": [],")) {
			GMods = "," + result.substring(result.indexOf("global_mods") + 22);
			GMods = GMods.substring(0, GMods.indexOf("],"));
		    }
		    if (result.contains("viewers" + "\"" + ": [") && !result.contains("viewers" + "\"" + ": []")) {
			Viewers = "," + result.substring(result.indexOf("viewers") + 18);
			Viewers = Viewers.substring(0, Viewers.indexOf("]"));
		    }

		    newresult = Mods + Staff + Admins + GMods + Viewers;
		    newresult = newresult.replace(" ", "");
		    newresult = newresult.replace("\"", "");
		    if (bot.viewers.isEmpty()) {
			for (String viewer : newresult.split(",")) {
			    if (!bot.viewers.contains(viewer.toLowerCase())) {
				bot.viewers.add(viewer.toLowerCase());
			    }
			}
			ConsoleUtil.TextToConsole(bot, BotType.Twitch, bot.channelName, "Bot has the list of current viewers!",
				MessageType.Bot, null);

		    } else {
			for (String viewer : newresult.split(",")) {
			    if (!bot.viewers.contains(viewer.toLowerCase())) {
				bot.viewers.add(viewer.toLowerCase());
				EventLog.addEvent(bot.channelName, viewer, "Joined the channel (Twitch)", EventType.User);
			    }
			}
			ConsoleUtil.TextToConsole(bot, BotType.Twitch, bot.channelName, "Bot has updated the list of current viewers!",
				MessageType.Bot, null);

			Mods = Mods.replace(" ", "");
			Mods = Mods.replace("\"", "");
			for (String mod : Mods.split(",")) {
			    if (!bot.moderators.contains(mod.toLowerCase())) {
				bot.moderators.add(mod.toLowerCase());
			    }
			}
			ConsoleUtil.TextToConsole(bot, BotType.Twitch, bot.channelName, "Bot has updated the list of current moderators!",
				MessageType.Bot, null);
		    }

		    for (int i = 1; i < bot.viewers.size(); i++) {
			if (Config.getSetting("Points", bot.channelName).equalsIgnoreCase("true")) {
			    if (!PointsSystem.isOnList(bot.viewers.get(i), bot.channelName)) {
				PointsSystem.setPoints(bot.viewers.get(i),
					Integer.parseInt(Config.getSetting("StartingPoints", bot.channelName)), bot.channelName, false);
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
