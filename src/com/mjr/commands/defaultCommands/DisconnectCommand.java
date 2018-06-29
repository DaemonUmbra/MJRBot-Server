package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot;
import com.mjr.MixerBot;
import com.mjr.TwitchBot;
import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;

public class DisconnectCommand extends Command{

    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (type == BotType.Twitch) { // TODO Fix this
	    TwitchBot twitchBot = MJRBot.getTwitchBotByChannelName(channel);
	    twitchBot.MessageToChat(twitchBot.getBotName() + " Disconnected!");
	    twitchBot.disconnectTwitch();
	} else if (type == BotType.Mixer) {
	    MixerBot mixerBot = MJRBot.getMixerBotByChannelName(channel);
	    mixerBot.sendMessage(mixerBot.getBotName() + " Disconnected!");
	    mixerBot.disconnect();
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
