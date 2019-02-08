package com.mjr.threads.twitch;

import com.mjr.ChatBotManager.BotType;
import com.mjr.MJRBot;
import com.mjr.TwitchBot;
import com.mjr.storage.Config;
import com.mjr.storage.EventLog;
import com.mjr.storage.EventLog.EventType;
import com.mjr.storage.PointsSystem;
import com.mjr.storage.RankSystem;
import com.mjr.util.ConsoleUtil;
import com.mjr.util.ConsoleUtil.MessageType;
import com.mjr.util.HTTPConnect;
import com.mjr.util.TwitchMixerAPICalls;

public class GetViewersThread extends Thread {

	private TwitchBot bot;

	public GetViewersThread(TwitchBot bot) {
		super("GetViewersThread for" + bot.channelName);
		this.bot = bot;
	}

	@Override
	public void run() {
		while (bot.ConnectedToChannel) {
			try {
				String result = "";
				String viewers = "";
				String vips = "";
				String moderators = "";
				String staff = "";
				String admins = "";
				String global_moderators = "";
				result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetUserChattersAPI(TwitchBot.getChannelNameFromChannelID(bot.channelID)));
				if (result.contains("vips" + "\"" + ": [") && !result.contains("vips" + "\"" + ": [],")) {
					vips = result.substring(result.indexOf("moderators") + 8);
					result = vips;
					vips = vips.substring(0, vips.indexOf("],"));
				}
				if (result.contains("moderators" + "\"" + ": [") && !result.contains("moderators" + "\"" + ": [],")) {
					moderators = result.substring(result.indexOf("moderators") + 14);
					result = moderators;
					moderators = moderators.substring(0, moderators.indexOf("],"));
				}
				if (result.contains("staff" + "\"" + ": [") && !result.contains("staff" + "\"" + ": [],")) {
					staff = result.substring(result.indexOf("staff") + 10);
					result = staff;
					staff = staff.substring(0, staff.indexOf("],"));
				}
				if (result.contains("admins" + "\"" + ": [") && !result.contains("admins" + "\"" + ": [],")) {
					admins = result.substring(result.indexOf("admins") + 11);
					result = result.substring(result.indexOf("admins") + 11);
					admins = admins.substring(0, admins.indexOf("],"));
				}
				if (result.contains("global_mods" + "\"" + ": [") && !result.contains("global_mods" + "\"" + ": [],")) {
					global_moderators = result.substring(result.indexOf("global_mods") + 16);
					result = global_moderators;
					global_moderators = global_moderators.substring(0, global_moderators.indexOf("],"));
				}
				if (result.contains("viewers" + "\"" + ": [") && !result.contains("viewers" + "\"" + ": []")) {
					viewers = result.substring(result.indexOf("viewers") + 12);
					result = viewers;
					viewers = viewers.substring(0, viewers.indexOf("]"));
				}

				vips = vips.replace(" ", "").replace("\"", "");
				moderators = moderators.replace(" ", "").replace("\"", "");
				staff = staff.replace(" ", "").replace("\"", "");
				admins = admins.replace(" ", "").replace("\"", "");
				global_moderators = global_moderators.replace(" ", "").replace("\"", "");
				viewers = viewers.replace(" ", "").replace("\"", "");

				if (bot.viewers.isEmpty()) {
					for (String viewer : vips.split(",")) {
						if (!bot.vips.contains(viewer.toLowerCase())) {
							if (!viewer.equals(""))
								bot.vips.add(viewer.toLowerCase());
						}
					}
					for (String viewer : moderators.split(",")) {
						if (!bot.viewers.contains(viewer.toLowerCase())) {
							if (!viewer.equals(""))
								bot.viewers.add(viewer.toLowerCase());
						}
					}
					for (String viewer : staff.split(",")) {
						if (!bot.viewers.contains(viewer.toLowerCase())) {
							if (!viewer.equals(""))
								bot.viewers.add(viewer.toLowerCase());
						}
					}
					for (String viewer : admins.split(",")) {
						if (!bot.viewers.contains(viewer.toLowerCase())) {
							if (!viewer.equals(""))
								bot.viewers.add(viewer.toLowerCase());
						}
					}
					for (String viewer : global_moderators.split(",")) {
						if (!bot.viewers.contains(viewer.toLowerCase())) {
							if (!viewer.equals(""))
								bot.viewers.add(viewer.toLowerCase());
						}
					}
					for (String viewer : viewers.split(",")) {
						if (!bot.viewers.contains(viewer.toLowerCase())) {
							if (!viewer.equals(""))
								bot.viewers.add(viewer.toLowerCase());
						}
					}
					ConsoleUtil.textToConsole(bot, BotType.Twitch, "Bot has the list of current active viewers!", MessageType.ChatBot, null);

				} else {
					for (String viewer : vips.split(",")) {
						if (!bot.vips.contains(viewer.toLowerCase())) {
							if (!viewer.equals("")) {
								bot.vips.add(viewer.toLowerCase());
								EventLog.addEvent(BotType.Twitch, bot, viewer, "Joined the channel (Twitch)", EventType.User);
							}
						}
					}
					for (String viewer : moderators.split(",")) {
						if (!bot.viewers.contains(viewer.toLowerCase())) {
							if (!viewer.equals("")) {
								bot.viewers.add(viewer.toLowerCase());
								EventLog.addEvent(BotType.Twitch, bot, viewer, "Joined the channel (Twitch)", EventType.User);
							}
						}
					}
					for (String viewer : staff.split(",")) {
						if (!bot.viewers.contains(viewer.toLowerCase())) {
							if (!viewer.equals("")) {
								bot.viewers.add(viewer.toLowerCase());
								EventLog.addEvent(BotType.Twitch, bot, viewer, "Joined the channel (Twitch)", EventType.User);
							}
						}
					}
					for (String viewer : admins.split(",")) {
						if (!bot.viewers.contains(viewer.toLowerCase())) {
							if (!viewer.equals("")) {
								bot.viewers.add(viewer.toLowerCase());
								EventLog.addEvent(BotType.Twitch, bot, viewer, "Joined the channel (Twitch)", EventType.User);
							}
						}
					}
					for (String viewer : global_moderators.split(",")) {
						if (!bot.viewers.contains(viewer.toLowerCase())) {
							if (!viewer.equals("")) {
								bot.viewers.add(viewer.toLowerCase());
								EventLog.addEvent(BotType.Twitch, bot, viewer, "Joined the channel (Twitch)", EventType.User);
							}
						}
					}
					for (String viewer : viewers.split(",")) {
						if (!bot.viewers.contains(viewer.toLowerCase())) {
							if (!viewer.equals("")) {
								bot.viewers.add(viewer.toLowerCase());
								EventLog.addEvent(BotType.Twitch, bot, viewer, "Joined the channel (Twitch)", EventType.User);
							}

						}
					}
					ConsoleUtil.textToConsole(bot, BotType.Twitch, "Bot has updated the list of current active viewers!", MessageType.ChatBot, null);

					moderators = moderators.replace(" ", "");
					moderators = moderators.replace("\"", "");
					for (String mod : moderators.split(",")) {
						if (!bot.moderators.contains(mod.toLowerCase())) {
							if (!mod.equals(""))
								bot.moderators.add(mod.toLowerCase());
						}
					}
					ConsoleUtil.textToConsole(bot, BotType.Twitch, "Bot has updated the list of current active moderators!", MessageType.ChatBot, null);
				}

				for (int i = 1; i < bot.viewers.size(); i++) {
					if (Config.getSetting("Points", BotType.Twitch, bot).equalsIgnoreCase("true")) {
						if (!PointsSystem.isOnList(bot.viewers.get(i), BotType.Twitch, bot)) {
							PointsSystem.setPoints(bot.viewers.get(i), Integer.parseInt(Config.getSetting("StartingPoints", BotType.Twitch, bot)), BotType.Twitch, bot, false, false);
						}
					}
					if (Config.getSetting("Ranks", BotType.Twitch, bot).equalsIgnoreCase("true")) {
						if (!RankSystem.isOnList(bot.viewers.get(i), BotType.Twitch, bot)) {
							RankSystem.setRank(bot.viewers.get(i), "None", BotType.Twitch, bot);
						}
					}
					if (!bot.viewersJoinedTimes.containsKey(bot.viewers.get(i).toLowerCase().toLowerCase()))
						bot.viewersJoinedTimes.put(bot.viewers.get(i).toLowerCase().toLowerCase(), System.currentTimeMillis());
				}
			} catch (Exception e) {
				MJRBot.logErrorMessage(e, BotType.Twitch, bot);
			}
			try {
				Thread.sleep(60000 * 2);
			} catch (InterruptedException e) {
				MJRBot.logErrorMessage(e, BotType.Twitch, bot);
			}
		}
	}
}
