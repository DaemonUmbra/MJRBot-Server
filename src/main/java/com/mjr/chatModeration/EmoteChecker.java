package com.mjr.chatModeration;

import java.util.ArrayList;
import java.util.List;

import com.mjr.ChatBotManager.BotType;
import com.mjr.storage.Config;
import com.mjr.util.HTTPConnect;
import com.mjr.util.TwitchMixerAPICalls;

public class EmoteChecker {
	private static List<String> emotes = new ArrayList<String>();

	public static void getEmotes(BotType type, String channel) {
		if (type == BotType.Twitch) {
			try {
				String result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetChatEmoticonsAPI(channel));
				int index = result.indexOf("regex");
				while (index > -1) {
					result = result.substring(index + 8);
					emotes.add(result.substring(0, result.indexOf("\"")));
					index = result.indexOf("regex");
				}
			} catch (Exception e) {
			}
		}
		emotes.add(":)");
		emotes.add(":(");
		emotes.add(":/");
		emotes.add(":O");
		emotes.add(":D");
		emotes.add(":P");
		emotes.add(">(");
		emotes.add(":Z");
		emotes.add("O_o");
		emotes.add("B)");
		emotes.add("<3");
		emotes.add(";)");
		emotes.add(";P");
		emotes.add("R)");

	}

	public static boolean isSpammingEmotes(String message, String user, String channelName) {
		int number = 0;
		String[] temp;
		temp = message.split(" ");
		for (int i = 0; i < temp.length; i++) {
			if (emotes.contains(temp[i].toUpperCase())) {
				number++;
			}
		}
		if (number >= Integer.parseInt(Config.getSetting("MaxEmotes", channelName)))
			return true;
		else
			return false;
	}
}
