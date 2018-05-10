package com.mjr;

import java.util.Arrays;

import com.mjr.files.Config;

public class Permissions {

    public enum Permission {
	User("User"), Moderator("Moderator"), Streamer("Streamer"), Bot("Bot"), BotOwner("BotOwner");

	private final String name;

	Permission(String name) {
	    this.name = name;
	}

	public String getName() {
	    return name;
	}
    }

    private static String getPermissionLevel(String user) {
	user = user.toLowerCase();
	if (MJRBot.getTwitchBot() != null) {
	    if (TwitchBot.mods != null) {
		if (user.equalsIgnoreCase(MJRBot.getTwitchBot().getBotName()))
		    return Permission.Bot.getName();
		else if (user.equalsIgnoreCase("mjrlegends"))
		    return Permission.BotOwner.getName();
		else if (Arrays.asList(TwitchBot.mods).contains(user) || user.equalsIgnoreCase(Config.getSetting("UserName"))
			|| user.equalsIgnoreCase(MJRBot.getChannel()))
		    return Permission.Moderator.getName();
	    } else {
		if (user.equalsIgnoreCase(MJRBot.getTwitchBot().getBotName()))
		    return Permission.Bot.getName();
		else if (user.equalsIgnoreCase("mjrlegends"))
		    return Permission.BotOwner.getName();
		else if (user.equalsIgnoreCase(Config.getSetting("UserName")) || user.equalsIgnoreCase(MJRBot.getChannel()))
		    return Permission.Moderator.getName();
	    }
	    return Permission.User.getName();
	} else {
	    if (!MJRBot.getMixerBot().getModerators().isEmpty()) {
		if (user.equalsIgnoreCase(MJRBot.getMixerBot().getBotName()))
		    return Permission.Bot.getName();
		else if (user.equalsIgnoreCase("mjrlegends"))
		    return Permission.BotOwner.getName();
		else if (MJRBot.getMixerBot().getModerators().contains(user) || user.equalsIgnoreCase(Config.getSetting("UserName"))
			|| user.equalsIgnoreCase(MJRBot.getChannel()))
		    return Permission.Moderator.getName();
	    } else {
		if (user.equalsIgnoreCase(MJRBot.getMixerBot().getBotName()))
		    return Permission.Bot.getName();
		else if (user.equalsIgnoreCase("mjrlegends"))
		    return Permission.BotOwner.getName();
		else if (user.equalsIgnoreCase(Config.getSetting("UserName")) || user.equalsIgnoreCase(MJRBot.getChannel()))
		    return Permission.Moderator.getName();
	    }
	    return Permission.User.getName();
	}
    }

    public static boolean hasPermission(String user, String permission) {
	if (permission.equalsIgnoreCase(Permission.User.getName()))
	    return true;
	else if (permission.equalsIgnoreCase(Permission.Moderator.getName())){
	    String returnLevel = getPermissionLevel(user);
	    if(returnLevel.equalsIgnoreCase(Permission.BotOwner.getName()))
		return true;
	    else
		return returnLevel.equalsIgnoreCase(permission); 
	}
	return false;
    }
}