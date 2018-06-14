package com.mjr.commands.defaultCommands;

import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.commands.Command;

public class DisconnectCommand extends Command{

    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message, String[] args) {
	if (MJRBot.getTwitchBot() != null) {
	    MJRBot.getTwitchBot().MessageToChat(MJRBot.getTwitchBot().getBotName() + " Disconnected!");
	    MJRBot.getTwitchBot().disconnectTwitch();
	} else if (MJRBot.getMixerBot() != null) {
	    MJRBot.getMixerBot().sendMessage(MJRBot.getMixerBot().getBotName() + " Disconnected!");
	    MJRBot.getMixerBot().disconnect();
	}
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.Streamer.getName();
    }

}
