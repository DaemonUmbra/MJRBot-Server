package com.mjr;

import java.util.Arrays;

import com.mjr.files.Config;

public class Permissions {

    public enum PermissionLevel {
	User("User", 0), Moderator("Moderator", 1), Streamer("Streamer", 3), Bot("Bot", 5), BotOwner("BotOwner", 4);

	private final String permission;
	private final int tierValue;

	PermissionLevel(String permission, int tierValue) {
	    this.permission = permission;
	    this.tierValue = tierValue;
	}

	public String getName() {
	    return permission;
	}

	public int getTierValue() {
	    return tierValue;
	}

	public static int getTierValueByName(String permission) {
	    for (PermissionLevel level : PermissionLevel.values())
		if (level.getName().equalsIgnoreCase(permission))
		    return level.getTierValue();
	    return 0;
	}
    }

    private static String getPermissionLevel(String user) {
	user = user.toLowerCase();
	if (MJRBot.getTwitchBot() != null) {
	    if (TwitchBot.mods != null) {
		if (user.equalsIgnoreCase(MJRBot.getChannel()))
		    return PermissionLevel.Streamer.getName();
		else if (user.equalsIgnoreCase(MJRBot.getTwitchBot().getBotName()))
		    return PermissionLevel.Bot.getName();
		else if (user.equalsIgnoreCase("mjrlegends"))
		    return PermissionLevel.BotOwner.getName();
		else if (Arrays.asList(TwitchBot.mods).contains(user) || user.equalsIgnoreCase(Config.getSetting("UserName"))
			|| user.equalsIgnoreCase(MJRBot.getChannel()))
		    return PermissionLevel.Moderator.getName();
	    } else {
		if (user.equalsIgnoreCase(MJRBot.getChannel()))
		    return PermissionLevel.Streamer.getName();
		else if (user.equalsIgnoreCase(MJRBot.getTwitchBot().getBotName()))
		    return PermissionLevel.Bot.getName();
		else if (user.equalsIgnoreCase("mjrlegends"))
		    return PermissionLevel.BotOwner.getName();
		else if (user.equalsIgnoreCase(Config.getSetting("UserName")) || user.equalsIgnoreCase(MJRBot.getChannel()))
		    return PermissionLevel.Moderator.getName();
	    }
	    return PermissionLevel.User.getName();
	} else {
	    if (!MJRBot.getMixerBot().getModerators().isEmpty()) {
		if (user.equalsIgnoreCase(MJRBot.getChannel()))
		    return PermissionLevel.Streamer.getName();
		else if (user.equalsIgnoreCase(MJRBot.getMixerBot().getBotName()))
		    return PermissionLevel.Bot.getName();
		else if (user.equalsIgnoreCase("mjrlegends"))
		    return PermissionLevel.BotOwner.getName();
		else if (MJRBot.getMixerBot().getModerators().contains(user) || user.equalsIgnoreCase(Config.getSetting("UserName"))
			|| user.equalsIgnoreCase(MJRBot.getChannel()))
		    return PermissionLevel.Moderator.getName();
	    } else {
		if (user.equalsIgnoreCase(MJRBot.getChannel()))
		    return PermissionLevel.Streamer.getName();
		else if (user.equalsIgnoreCase(MJRBot.getMixerBot().getBotName()))
		    return PermissionLevel.Bot.getName();
		else if (user.equalsIgnoreCase("mjrlegends"))
		    return PermissionLevel.BotOwner.getName();
		else if (user.equalsIgnoreCase(Config.getSetting("UserName")) || user.equalsIgnoreCase(MJRBot.getChannel()))
		    return PermissionLevel.Moderator.getName();
	    }
	    return PermissionLevel.User.getName();
	}
    }

    public static boolean hasPermission(String user, String permission) {
	String userPermissionLevel = getPermissionLevel(user);
	if (getPermissionLevel(user).equalsIgnoreCase(permission))
	    return true;
	else if (PermissionLevel.getTierValueByName(userPermissionLevel) >= PermissionLevel.getTierValueByName(permission))
	    return true;
	return false;
    }
}