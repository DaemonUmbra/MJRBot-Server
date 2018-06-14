package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;

public class DisconnectCommand extends Command{

    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (type == BotType.Twitch) {
	    MJRBot.getTwitchBotByChannelName(channel).MessageToChat(MJRBot.getTwitchBotByChannelName(channel).getBotName() + " Disconnected!");
	    MJRBot.getTwitchBotByChannelName(channel).disconnectTwitch();
	} else if (type == BotType.Mixer) {
	    MJRBot.getMixerBotByChannelName(channel).sendMessage(MJRBot.getMixerBotByChannelName(channel).getBotName() + " Disconnected!");
	    MJRBot.getMixerBotByChannelName(channel).disconnect();
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.Streamer.getName();
    }

}
