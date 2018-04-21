package com.mjr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import com.mjr.files.Config;
import com.mjr.files.PointsSystem;
import com.mjr.files.Ranks;

public class Viewers {
    MJRBot bot = new MJRBot();
    public static String result = "";
    public static String Viewers = "";
    public static String Mods = "";
    public static String Staff = "";
    public static String Admins = "";
    public static String GMods = "";
    public static String newresult = "";

    public static void getViewers() {
	try {
	    if (MJRBot.getTwitchBot().viewers != null)
		Arrays.fill(MJRBot.getTwitchBot().viewers, "");
	    result = "";
	    newresult = "";
	    Viewers = "";
	    Mods = "";
	    Staff = "";
	    Admins = "";
	    GMods = "";
	    URL url = new URL("https://tmi.twitch.tv/group/user/" + MJRBot.getChannel().toLowerCase() + "/chatters");
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    connection.setRequestMethod("GET");
	    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    String line = "";
	    while ((line = reader.readLine()) != null) {
		result += line;
	    }
	    reader.close();
	    if (result.contains("viewers" + "\"" + ": [      ") || result.contains("moderators" + "\"" + ": [      ")
		    || result.contains("staff" + "\"" + ": [      ") || result.contains("admins" + "\"" + ": [      ")
		    || result.contains("global_mods" + "\"" + ": [      ")) {

		if (result.contains("moderators" + "\"" + ": [      ")) {
		    Mods = result.substring(result.indexOf("moderators") + 21, result.indexOf("],"));
		}
		if (result.contains("staff" + "\"" + ": [      ")) {
		    Staff = "," + result.substring(result.indexOf("staff") + 16);
		    Staff = Staff.substring(0, Staff.indexOf("],"));
		}
		if (result.contains("admins" + "\"" + ": [      ")) {
		    Admins = "," + result.substring(result.indexOf("admins") + 17);
		    Admins = Admins.substring(0, Admins.indexOf("],"));
		}
		if (result.contains("global_mods" + "\"" + ": [      ")) {
		    GMods = "," + result.substring(result.indexOf("global_mods") + 22);
		    GMods = GMods.substring(0, GMods.indexOf("],"));
		}
		if (result.contains("viewers" + "\"" + ": [      ")) {
		    Viewers = "," + result.substring(result.indexOf("viewers") + 18);
		    Viewers = Viewers.substring(0, Viewers.indexOf("]"));
		}

		newresult = Mods + Staff + Admins + GMods + Viewers;
		newresult = newresult.replace(" ", "");
		newresult = newresult.replace("\"", "");
		MJRBot.getTwitchBot().viewers = newresult.split(",");
		ConsoleUtil.TextToConsole("Bot has Viewers!", "Bot", null);

		for (int i = 1; i < MJRBot.getTwitchBot().viewers.length; i++) {
		    if (Config.getSetting("Points").equalsIgnoreCase("true")) {
			if (!PointsSystem.isOnList(MJRBot.getTwitchBot().viewers[i])) {
			    PointsSystem.setPoints(MJRBot.getTwitchBot().viewers[i], Integer.parseInt(Config.getSetting("StartingPoints")));
			}
		    }
		    if (Config.getSetting("Ranks").equalsIgnoreCase("true")) {
			if (!Ranks.isOnList(MJRBot.getTwitchBot().viewers[i])) {
			    Ranks.setRank(MJRBot.getTwitchBot().viewers[i], "None");
			}
		    }
		    MJRBot.getTwitchBot().viewersJoinedTimes
			    .put(MJRBot.getTwitchBot().viewers[i].toLowerCase(), System.currentTimeMillis());
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
