package com.mjr.mjrbot.threads;

import java.sql.ResultSet;
import java.util.HashMap;

import com.mjr.mjrbot.bots.ChatBotManager;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.OAuthManager;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.storage.sql.MySQLConnection;
import com.mjr.mjrbot.util.HTTPConnect;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.TwitchMixerAPICalls;

public class OAuthTokenRefreshThread extends Thread {

	public OAuthTokenRefreshThread() {
		super("OAuthTokenRefreshThread");
	}

	@Override
	public void run() {
		while (true) {
			try {
				HashMap<Integer, TwitchBot> channelListTwitch = ChatBotManager.getTwitchBots();
				HashMap<String, MixerBot> channelListMixer = ChatBotManager.getMixerBots();

				for (Integer channelNameMain : channelListTwitch.keySet()) {
					ResultSet tokenSet = MySQLConnection.executeQuery("SELECT refresh_token FROM tokens WHERE channel_id = '" + channelListTwitch.get(channelNameMain).getChannelID() + "'");
					if (tokenSet.next() && tokenSet.getString("refresh_token") != null && tokenSet.getString("refresh_token") != "") {
						try {
							HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetUserAPI(tokenSet.getString("refresh_token")));
						} catch (Exception e) {
							if (e.getMessage().contains("401"))
								OAuthManager.refreshTokenTwitch(1, channelListTwitch.get(channelNameMain));
						}

					}
				}

				for (String channelNameMain : channelListMixer.keySet()) {
					ResultSet tokenSet = MySQLConnection.executeQuery("SELECT refresh_token FROM tokens WHERE channel = '" + channelListMixer.get(channelNameMain).getChannelName() + "' AND platform = 'Mixer'");
					if (tokenSet.next() && tokenSet.getString("refresh_token") != null && tokenSet.getString("refresh_token") != "") {
						HashMap<String, String> headers = new HashMap<String, String>();
						headers.put("Authorization", "Bearer " + tokenSet.getString("refresh_token"));
						try {
							HTTPConnect.getRequestCustomHeaders(TwitchMixerAPICalls.mixerGetUsersAPI(), headers);
						} catch (Exception e) {
							if (e.getMessage().contains("401"))
								OAuthManager.refreshTokenMixer(1, channelListMixer.get(channelNameMain));
						}
					}
				}
				Thread.sleep(21600000); // 6 Hours
			} catch (Exception e) {
				MJRBotUtilities.logErrorMessage(e);
			}
		}
	}

}
