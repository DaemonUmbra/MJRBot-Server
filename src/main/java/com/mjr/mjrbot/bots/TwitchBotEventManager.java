package com.mjr.mjrbot.bots;

import com.mjr.mjrbot.MJRBot;
import com.mjr.twitchframework.irc.TwitchIRCClient;
import com.mjr.twitchframework.irc.TwitchIRCManager;
import com.mjr.twitchframework.irc.events.IRCDisconnectEvent;
import com.mjr.twitchframework.irc.events.IRCJoinEvent;
import com.mjr.twitchframework.irc.events.IRCMessageEvent;
import com.mjr.twitchframework.irc.events.IRCMessageExtraEvent;
import com.mjr.twitchframework.irc.events.IRCNoticeEvent;
import com.mjr.twitchframework.irc.events.IRCPartEvent;
import com.mjr.twitchframework.irc.events.IRCPrivateMessageEvent;
import com.mjr.twitchframework.irc.events.IRCUnknownEvent;

public class TwitchBotEventManager {
	public static void initEvents() {
		TwitchIRCManager.registerEventHandler(new IRCMessageEvent() {
			@Override
			public void onEvent(final String channel, final String sender, final String login, final String hostname, final String userID, final boolean subscriber, final String message) {
				ChatBotManager.getTwitchBotByChannelName(channel.substring(1)).onMessage(channel, sender, login, hostname, userID, subscriber, message);
			}
		});
		TwitchIRCManager.registerEventHandler(new IRCMessageExtraEvent() {
			@Override
			public void onEvent(final String line, final String channel, final String sender, final String login, final String hostname, final String message) {
				ChatBotManager.getTwitchBotByChannelName(channel.substring(1)).onMessageExtra(line, channel, sender, login, hostname, message);
			}
		});
		TwitchIRCManager.registerEventHandler(new IRCNoticeEvent() {
			@Override
			public void onEvent(final String sourceNick, final String sourceLogin, final String sourceHostname, final String target, final String notice) {
				ChatBotManager.getTwitchBotByChannelName(target.substring(1)).onNotice(sourceNick, sourceLogin, sourceHostname, target, notice);
			}
		});
		TwitchIRCManager.registerEventHandler(new IRCJoinEvent() {
			@Override
			public void onEvent(final String channel, final String sender, final String login, final String hostname) {
				ChatBotManager.getTwitchBotByChannelName(channel.substring(1)).onJoin(channel, sender, login, hostname);
			}
		});
		TwitchIRCManager.registerEventHandler(new IRCPartEvent() {
			@Override
			public void onEvent(final String channel, final String sender, final String login, final String hostname) {
				ChatBotManager.getTwitchBotByChannelName(channel.substring(1)).onPart(channel, sender, login, hostname);
			}
		});
		TwitchIRCManager.registerEventHandler(new IRCPrivateMessageEvent() {
			@Override
			public void onEvent(final String sender, final String login, final String hostname, final String channel, final String message) {
				ChatBotManager.getTwitchBotByChannelName(channel.substring(1)).onPrivateMessage(sender, login, hostname, channel, message);
			}
		});
		TwitchIRCManager.registerEventHandler(new IRCUnknownEvent() {
			@Override
			public void onEvent(final String rawLine, final String channel) {
				ChatBotManager.getTwitchBotByChannelName(channel.substring(1)).onUnknown(rawLine);
			}
		});
		TwitchIRCManager.registerEventHandler(new IRCDisconnectEvent() {
			@Override
			public void onEvent(TwitchIRCClient client) {
				MJRBot.getDiscordBot().sendAdminEventMessage("[Twitch] An client connection has triggered a onDisconnect event. Trying to reconnect! List of channels connected via this connection" + String.join(", ", client.getChannelsList()));
			}
		});
	}
}
