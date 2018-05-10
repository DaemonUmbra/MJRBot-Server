package com.mjr;

import java.util.Arrays;

import com.mjr.files.Config;

public class Permissions {
    public static String getPermissionLevel(String user) {
	user = user.toLowerCase();
	if (MJRBot.getTwitchBot() != null) {
	    if (TwitchBot.mods != null) {
		if (user.equalsIgnoreCase(MJRBot.getTwitchBot().getBotName()))
		    return "Bot";
		else if (Arrays.asList(TwitchBot.mods).contains(user) || user.equalsIgnoreCase("mjrlegends")
			|| user.equalsIgnoreCase(Config.getSetting("UserName")) || user.equalsIgnoreCase(MJRBot.getChannel()))
		    return "Moderator";
	    } else {
		if (user.equalsIgnoreCase(MJRBot.getTwitchBot().getBotName()))
		    return "Bot";
		else if (user.equalsIgnoreCase("mjrlegends") || user.equalsIgnoreCase(Config.getSetting("UserName")) || user.equalsIgnoreCase(MJRBot.getChannel()))
		    return "Moderator";
	    }
	    return "User";
	} else {
	    if (!MJRBot.getMixerBot().getModerators().isEmpty()) {
		if (user.equalsIgnoreCase(MJRBot.getMixerBot().getBotName()))
		    return "Bot";
		else if (MJRBot.getMixerBot().getModerators().contains(user) || user.equalsIgnoreCase("mjrlegends")
			|| user.equalsIgnoreCase(Config.getSetting("UserName")) || user.equalsIgnoreCase(MJRBot.getChannel()))
		    return "Moderator";
	    } else {
		if (user.equalsIgnoreCase(MJRBot.getMixerBot().getBotName()))
		    return "Bot";
		else if (user.equalsIgnoreCase("mjrlegends") || user.equalsIgnoreCase(Config.getSetting("UserName")) || user.equalsIgnoreCase(MJRBot.getChannel()))
		    return "Moderator";
	    }
	    return "User";

	}
    }
}