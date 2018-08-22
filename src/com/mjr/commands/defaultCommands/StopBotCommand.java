package com.mjr.commands.defaultCommands;

import com.mjr.AnalyticsData;
import com.mjr.MJRBot;
import com.mjr.MJRBot.BotType;
import com.mjr.MixerBot;
import com.mjr.Permissions.PermissionLevel;
import com.mjr.TwitchBot;
import com.mjr.commands.Command;

public class StopBotCommand extends Command {

    @Override
    public void onCommand(BotType type, Object bot, String channel, String sender, String login, String hostname, String message,
	    String[] args) {
	for (String channelNameMain : MJRBot.getTwitchBots().keySet()) {
	    TwitchBot tempBot = MJRBot.getTwitchBotByChannelName(channelNameMain);
	    tempBot.disconnectTwitch();
	    MJRBot.removeTwitchBot(channelNameMain);
	}
	for (String channelNameMain : MJRBot.getMixerBots().keySet()) {
	    MixerBot tempBot = MJRBot.getMixerBotByChannelName(channelNameMain);
	    tempBot.disconnectMixer();
	    MJRBot.removeTwitchBot(channelNameMain);
	}
	AnalyticsData.sendData();
	System.exit(0);
    }

    @Override
    public String getPermissionLevel() {
	return PermissionLevel.BotOwner.getName();
    }

    @Override
    public boolean hasCooldown() {
	return false;
    }

}
