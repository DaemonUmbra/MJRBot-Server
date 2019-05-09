package com.mjr.mjrbot.bots;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mjr.mjrbot.MJRBot;
import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.storage.BotConfigManager;
import com.mjr.mjrbot.storage.sql.MySQLConnection;
import com.mjr.mjrbot.util.ConsoleUtil;
import com.mjr.mjrbot.util.ConsoleUtil.MessageType;
import com.mjr.mjrbot.util.HTTPConnect;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.TwitchMixerAPICalls;

public class OAuthManager {
	public static void refreshTokenTwitch(int maxTries, TwitchBot bot) {
		for (int i = 0; i < maxTries; i++) {
			ConsoleUtil.textToConsole(bot, BotType.Twitch, "Refreshing access_token! Attempt " + (i + 1) + " out of " + maxTries, MessageType.ChatBot, null);
			MJRBot.getDiscordBot().sendAdminEventMessage("[" + BotType.Twitch.getTypeName() + "] **" + bot.getChannelName() + " ** Refreshing access_token! Attempt " + (i + 1) + " out of " + maxTries);
			try {
				ResultSet tokenSet = MySQLConnection.executeQuery("SELECT refresh_token FROM tokens WHERE channel_id = '" + bot.getChannelID() + "'");
				if (tokenSet.next() && tokenSet.getString("refresh_token") != null && tokenSet.getString("refresh_token") != "") {
					String result = HTTPConnect.postRequest(TwitchMixerAPICalls.twitchGetoAuth2TokenAPI(tokenSet.getString("refresh_token"), BotConfigManager.getSetting("TwitchClientSecret")));
					String access_token = result.substring(result.indexOf("access_token") + 15);
					access_token = access_token.substring(0, access_token.indexOf(",") - 1);
					String refresh_token = result.substring(result.indexOf("refresh_token") + 16);
					refresh_token = refresh_token.substring(0, refresh_token.indexOf(",") - 1);
					if (access_token.length() == 30) {
						if (refresh_token.length() == 50) {
							MySQLConnection.executeUpdate("UPDATE tokens SET access_token='" + access_token + "', refresh_token='" + refresh_token + "'WHERE channel_id='" + bot.getChannelID() + "';"); // TODO Add expires_in
						}
						else
							MJRBotUtilities.logErrorMessage("Invalid length for Refresh Token for Twitch channel " + bot.getChannelName());
					}
					else
						MJRBotUtilities.logErrorMessage("Invalid length for Access Token for Twitch channel " + bot.getChannelName());
					return;
				}
			} catch (SQLException | IOException e) {
				if (!e.getMessage().contains("400"))
					MJRBotUtilities.logErrorMessage(e, BotType.Twitch, bot);
			}
		}
		ConsoleUtil.textToConsole(bot, BotType.Twitch, "Unable to refresh token after " + maxTries + " Attempts!", MessageType.ChatBot, null);
		MJRBot.getDiscordBot().sendAdminEventMessage("[" + BotType.Twitch.getTypeName() + "] **" + bot.getChannelName() + " ** Unable to refresh token after " + maxTries + " Attempts!");
	}

	public static void refreshTokenMixer(int maxTries, MixerBot bot) {
		for (int i = 0; i < maxTries; i++) {
			ConsoleUtil.textToConsole(bot, BotType.Mixer, "Refreshing access_token! Attempt " + (i + 1) + " out of " + maxTries, MessageType.ChatBot, null);
			MJRBot.getDiscordBot().sendAdminEventMessage("[" + BotType.Mixer.getTypeName() + "] **" + bot.getChannelName() + " ** Refreshing access_token! Attempt " + (i + 1) + " out of " + maxTries);
			try {
				ResultSet tokenSet = MySQLConnection.executeQuery("SELECT refresh_token FROM tokens WHERE channel = '" + bot.getChannelName() + "'");
				if (tokenSet.next() && tokenSet.getString("refresh_token") != null && tokenSet.getString("refresh_token") != "") {
					String result = HTTPConnect.postRequest(TwitchMixerAPICalls.mixerGetoAuth2TokenAPI(tokenSet.getString("refresh_token"), BotConfigManager.getSetting("TwitchClientSecret")));
					String access_token = result.substring(result.indexOf("access_token") + 15);
					access_token = access_token.substring(0, access_token.indexOf(",") - 1);
					String refresh_token = result.substring(result.indexOf("refresh_token") + 16);
					refresh_token = refresh_token.substring(0, refresh_token.indexOf(",") - 1);
					String expires_in = result.substring(result.indexOf("expires_in") + 13);
					expires_in = expires_in.substring(0, expires_in.indexOf(","));
					if (access_token.length() == 64) {
						if (refresh_token.length() == 64) {
							MySQLConnection.executeUpdate("UPDATE tokens SET access_token='" + access_token + "', refresh_token='" + refresh_token + "', expires_in='" + expires_in + "'WHERE channel='" + bot.getChannelName() + "' AND platform = 'Mixer';");
						}
						else
							MJRBotUtilities.logErrorMessage("Invalid length for Refresh Token for Twitch channel " + bot.getChannelName());
					}
					else
						MJRBotUtilities.logErrorMessage("Invalid length for Access Token for Twitch channel " + bot.getChannelName());
					return;
				}
			} catch (SQLException | IOException e) {
				if (!e.getMessage().contains("400"))
					MJRBotUtilities.logErrorMessage(e, BotType.Mixer, bot);
			}
		}
		ConsoleUtil.textToConsole(bot, BotType.Twitch, "Unable to refresh token after " + maxTries + " Attempts!", MessageType.ChatBot, null);
		MJRBot.getDiscordBot().sendAdminEventMessage("[" + BotType.Twitch.getTypeName() + "] **" + bot.getChannelName() + " ** Unable to refresh token after " + maxTries + " Attempts!");
	}
}
