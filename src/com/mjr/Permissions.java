package com.mjr;

import java.util.Arrays;

import com.mjr.files.Config;

public class Permissions {
    public static String getPermissionLevel(String user) {
	String User2 = user.toLowerCase();
	if (MJRBot.getTwitchBot() != null) {
	    if (TwitchBot.mods != null) {
		if (User2.equalsIgnoreCase(MJRBot.getTwitchBot().getBotName()))
		    return "Bot";
		else if (Arrays.asList(TwitchBot.mods).contains(User2) || User2.equalsIgnoreCase("mjrlegends")
			|| User2.equalsIgnoreCase(Config.getSetting("UserName")))
		    return "Moderator";
	    } else {
		if (User2.equalsIgnoreCase(MJRBot.getTwitchBot().getBotName()))
		    return "Bot";
		else if (User2.equalsIgnoreCase("mjrlegends") || User2.equalsIgnoreCase(Config.getSetting("UserName")))
		    return "Moderator";
	    }
	    return "User";
	} else {
	    if (!MJRBot.getMixerBot().getModerators().isEmpty()) {
		if (User2.equalsIgnoreCase(MJRBot.getMixerBot().getUsername()))
		    return "Bot";
		else if (MJRBot.getMixerBot().getModerators().contains(User2) || User2.equalsIgnoreCase("mjrlegends")
			|| User2.equalsIgnoreCase(Config.getSetting("UserName")))
		    return "Moderator";
	    } else {
		if (User2.equalsIgnoreCase(MJRBot.getMixerBot().getUsername()))
		    return "Bot";
		else if (User2.equalsIgnoreCase("mjrlegends") || User2.equalsIgnoreCase(Config.getSetting("UserName")))
		    return "Moderator";
	    }
	    return "User";

	}
    }
}