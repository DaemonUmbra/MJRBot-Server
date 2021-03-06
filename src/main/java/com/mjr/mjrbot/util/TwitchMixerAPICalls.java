package com.mjr.mjrbot.util;

import com.mjr.mjrbot.MJRBot;

public class TwitchMixerAPICalls {

	// Twitch v5
	public static String twitchGetStreamsAPI(int channelID) {
		return "https://api.twitch.tv/kraken/streams/" + channelID + "?client_id=" + MJRBot.CLIENT_ID_Twitch + "&api_version=5";
	}

	public static String twitchGetChannelsAPI(int channelID, int limit) {
		return "https://api.twitch.tv/kraken/channels/" + channelID + "?client_id=" + MJRBot.CLIENT_ID_Twitch + "&limit=" + limit + "&api_version=5";
	}

	public static String twitchGetChannelsFollowsAPI(int channelID, int limit) {
		return "https://api.twitch.tv/kraken/channels/" + channelID + "/follows?client_id=" + MJRBot.CLIENT_ID_Twitch + "&limit=" + limit + "&api_version=5";
	}

	public static String twitchGetChannelsFollowsAPI(int channelID, int limit, int offset) {
		return "https://api.twitch.tv/kraken/channels/" + channelID + "/follows?client_id=" + MJRBot.CLIENT_ID_Twitch + "&limit=" + limit + "&offset=" + offset + "&api_version=5";
	}

	public static String twitchGetChannelsSubscriptionsAPI(int channelID, String oAuthToken, int limit) {
		return "https://api.twitch.tv/kraken/channels/" + channelID + "/subscriptions?client_id=" + MJRBot.CLIENT_ID_Twitch + "&oauth_token=" + oAuthToken + "&limit=" + limit + "&api_version=5";
	}

	public static String twitchGetUserAPI(String oAuthToken) {
		return "https://api.twitch.tv/kraken/user?client_id=" + MJRBot.CLIENT_ID_Twitch + "&oauth_token=" + oAuthToken + "&api_version=5";
	}

	public static String twitchGetChatAPI(int channelID) {
		return "https://api.twitch.tv/kraken/chat/" + channelID + "?client_id=" + MJRBot.CLIENT_ID_Twitch + "&api_version=5";
	}

	public static String twitchGetChatEmoticonsAPI(int channelID) {
		return "https://api.twitch.tv/kraken/chat/" + channelID + "/emoticons?client_id=" + MJRBot.CLIENT_ID_Twitch + "&api_version=5";
	}

	public static String twitchGetoAuth2TokenAPI(String refreshToken, String clientSecret) {
		return "https://id.twitch.tv/oauth2/token?client_id=" + MJRBot.CLIENT_ID_Twitch + "&grant_type=refresh_token&refresh_token=" + refreshToken + "&client_secret=" + clientSecret + "&api_version=5";
	}

	public static String twitchCheckFollow(int channelID, String user) {
		return "https://api.twitch.tv/kraken/users/" + user + "/follows/channels" + channelID + "?client_id=" + MJRBot.CLIENT_ID_Twitch + "&api_version=5";
	}

	public static String twitchGetUsersAPI(String channelName) {
		return "https://api.twitch.tv/kraken/users/" + twitchGetUserIDFromChannelNameAPI(channelName) + "?client_id=" + MJRBot.CLIENT_ID_Twitch + "&api_version=5";
	}

	public static String twitchGetUserIDFromChannelNameAPI(String channelName) {
		return "https://api.twitch.tv/kraken/users?login=" + channelName + "&client_id=" + MJRBot.CLIENT_ID_Twitch + "&api_version=5";
	}

	public static String twitchGetUserChattersAPI(String channelID) {
		return "https://tmi.twitch.tv/group/user/" + channelID + "/chatters";
	}

	// Mixer v1

	public static String mixerGetChannelsAPI(String channelName) { // TODO: Change to use channel id
		return "https://mixer.com/api/v1/channels/" + channelName;
	}

	public static String mixerGetUsersAPI() { // TODO: Change to use channel id
		return "https://mixer.com/api/v1/users/current/";
	}

	public static String mixerCheckFollow(int channel_id, int userId) {
		return "https://mixer.com/api/v1/channels/" + channel_id + "/relationship?user=" + userId;
	}

	public static String mixerGetoAuth2TokenAPI(String refreshToken, String clientSecret) {
		return "https://mixer.com/api/v1//oauth/token?client_id=" + MJRBot.CLIENT_ID_Mixer + "&grant_type=refresh_token&refresh_token=" + refreshToken + "&client_secret=" + clientSecret;
	}
}
