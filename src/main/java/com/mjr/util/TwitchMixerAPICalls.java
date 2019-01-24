package com.mjr.util;

import com.mjr.MJRBot;

public class TwitchMixerAPICalls {

	// Twitch v3
	public static String twitchGetStreamsAPI(String channelName) {
		return "https://api.twitch.tv/kraken/streams/" + channelName + "/?client_id=" + MJRBot.CLIENT_ID;
	}

	public static String twitchGetChannelsAPI(String channelName, int limit) {
		return "https://api.twitch.tv/kraken/channels/" + channelName + "/?client_id=" + MJRBot.CLIENT_ID + "&limit=" + limit;
	}

	public static String twitchGetChannelsFollowsAPI(String channelName, int limit) {
		return "https://api.twitch.tv/kraken/channels/" + channelName + "/follows/?client_id=" + MJRBot.CLIENT_ID + "&limit=" + limit;
	}

	public static String twitchGetChannelsSubscriptionsAPI(String channelName, String oAuthToken, int limit) {
		return "https://api.twitch.tv/kraken/channels/" + channelName + "/subscriptions/?client_id=" + MJRBot.CLIENT_ID + "&oauth_token=" + oAuthToken + "&limit=" + limit;
	}

	public static String twitchGetChatAPI(String channelName) {
		return "https://api.twitch.tv/kraken/chat/" + channelName + "/?client_id=" + MJRBot.CLIENT_ID;
	}

	public static String twitchGetChatEmoticonsAPI(String channelName) {
		return "https://api.twitch.tv/kraken/chat/" + channelName + "/emoticons/?client_id=" + MJRBot.CLIENT_ID;
	}

	public static String twitchGetUsersAPI(String channelName) {
		return "https://api.twitch.tv/kraken/users/" + channelName + "/?client_id=" + MJRBot.CLIENT_ID;
	}

	public static String twitchGetUserChattersAPI(String channelName) {
		return "https://tmi.twitch.tv/group/user/" + channelName + "/chatters/";
	}

	public static String twitchGetoAuth2TokenAPI(String refreshToken, String clientSecret) {
		return "https://id.twitch.tv/oauth2/token/?client_id=" + MJRBot.CLIENT_ID + "&grant_type=refresh_token&refresh_token=" + refreshToken + "&client_secret=" + clientSecret;
	}
	
	public static String twitchCheckFollow(String channelName, String user) {
		return "https://api.twitch.tv/kraken/users/" + user + "/follows/channels/" + channelName + "?client_id=" + MJRBot.CLIENT_ID;
	}

	// Mixer v1
	
	public static String mixerGetChannelsAPI(String channelName) {
		return "https://mixer.com/api/v1/channels/" + channelName;
	}
	
	public static String mixerCheckFollow(int channel_id, int userId) {
		return "https://mixer.com/api/v1/channels/" + channel_id + "/relationship?user=" + userId;
	}
}
