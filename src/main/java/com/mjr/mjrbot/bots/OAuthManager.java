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
	public static void refreshTokenTwitch(int tries, TwitchBot bot) {
		ConsoleUtil.textToConsole(bot, BotType.Twitch, "Refreshing access_token! Attempt " + tries + " out of 5", MessageType.ChatBot, null);
		MJRBot.getDiscordBot().sendAdminEventMessage("[" + BotType.Twitch.getTypeName() + "] **" + bot.getChannelName() + " ** Refreshing access_token! Attempt " + tries + " out of 5");
		try {
			ResultSet tokenSet = MySQLConnection.executeQuery("SELECT refresh_token FROM tokens WHERE channel_id = '" + bot.getChannelID() + "'");
			if (tokenSet.next() && tokenSet.getString("refresh_token") != null && tokenSet.getString("refresh_token") != "") {
				String result = HTTPConnect.postRequest(TwitchMixerAPICalls.twitchGetoAuth2TokenAPI(tokenSet.getString("refresh_token"), BotConfigManager.getSetting("TwitchClientSecret")));
				String access_token = result.substring(result.indexOf("access_token") + 16);
				access_token = access_token.substring(0, access_token.indexOf(",") - 2);
				String refresh_token = result.substring(result.indexOf("refresh_token") + 16);
				refresh_token = refresh_token.substring(0, refresh_token.indexOf(",") - 2);
				MySQLConnection.executeUpdate("UPDATE tokens SET access_token='" + access_token + "', refresh_token='" + refresh_token + "'WHERE channel_id='" + bot.getChannelID() + "';"); // TODO Add expires_in
			}
		} catch (SQLException | IOException e) {
			if (!e.getMessage().contains("400"))
				MJRBotUtilities.logErrorMessage(e, BotType.Twitch, bot);
		}
	}
	
	public static void refreshTokenMixer(int tries, MixerBot bot) {
		ConsoleUtil.textToConsole(bot, BotType.Mixer, "Refreshing access_token! Attempt " + tries + " out of 5", MessageType.ChatBot, null);
		MJRBot.getDiscordBot().sendAdminEventMessage("[" + BotType.Mixer.getTypeName() + "] **" + bot.getChannelName() + " ** Refreshing access_token! Attempt " + tries + " out of 5");
		try {
			ResultSet tokenSet = MySQLConnection.executeQuery("SELECT refresh_token FROM tokens WHERE channel = '" + bot.getChannelName() + "'");
			if (tokenSet.next() && tokenSet.getString("refresh_token") != null && tokenSet.getString("refresh_token") != "") {
				String result = HTTPConnect.postRequest(TwitchMixerAPICalls.mixerGetoAuth2TokenAPI(tokenSet.getString("refresh_token"), BotConfigManager.getSetting("TwitchClientSecret")));
				String access_token = result.substring(result.indexOf("access_token") + 16);
				access_token = access_token.substring(0, access_token.indexOf(",") - 2);
				String refresh_token = result.substring(result.indexOf("refresh_token") + 16);
				refresh_token = refresh_token.substring(0, refresh_token.indexOf(",") - 2);
				String expires_in = result.substring(result.indexOf("expires_in") + 13);
				expires_in = expires_in.substring(0, expires_in.indexOf(",") - 2);
				MySQLConnection.executeUpdate("UPDATE tokens SET access_token='" + access_token + "', refresh_token='" + refresh_token + "', expires_in='" + expires_in + "'WHERE channel='" + bot.getChannelName() + "' AND platform = 'Mixer';");
			}
		} catch (SQLException | IOException e) {
			if (!e.getMessage().contains("400"))
				MJRBotUtilities.logErrorMessage(e, BotType.Mixer, bot);
		}
	}
}
