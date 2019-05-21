package com.mjr.mjrbot.chatModeration;

import java.util.ArrayList;
import java.util.List;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.bases.TwitchBot;
import com.mjr.mjrbot.storage.ChannelConfigManager;
import com.mjr.mjrbot.util.HTTPConnect;
import com.mjr.mjrbot.util.TwitchMixerAPICalls;

public class EmoteChecker {
	private static List<String> emotes = new ArrayList<String>();

	public static void getEmotes(BotType type, Object bot) {
		if (type == BotType.Twitch) {
			try {
				String result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetChatEmoticonsAPI(((TwitchBot) bot).getChannelID()));
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

	public static boolean isSpammingEmotes(String message, String user, BotType type, Object bot) {
		int number = 0;
		String[] temp;
		temp = message.split(" ");
		for (int i = 0; i < temp.length; i++) {
			if (emotes.contains(temp[i].toUpperCase())) {
				number++;
			}
		}
		if (number >= Integer.parseInt(ChannelConfigManager.getSetting("MaxEmotes", type, bot)))
			return true;
		else
			return false;
	}
}
