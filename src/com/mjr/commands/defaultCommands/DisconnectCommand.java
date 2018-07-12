package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.MixerBot;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;

public class DisconnectCommand extends Command {

    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	if (type == BotType.Twitch) {
	    TwitchBot twitchBot = (TwitchBot) bot;
	    twitchBot.disconnectTwitch();
	    MJRBot.removeTwitchBot(twitchBot);
	} else if (type == BotType.Mixer) {
	    MixerBot mixerBot = (MixerBot) bot;
	    mixerBot.disconnectMixer();
	    MJRBot.removeMixerBot(mixerBot);
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.Streamer.getName();
    }

    @Override
    public boolean hasCooldown() {
	return false;
    }

}
