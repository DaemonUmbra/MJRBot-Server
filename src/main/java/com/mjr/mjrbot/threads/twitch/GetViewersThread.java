package com.mjr.mjrbot.threads.twitch;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.storage.Config;
import com.mjr.mjrbot.storage.EventLog;
import com.mjr.mjrbot.storage.EventLog.EventType;
import com.mjr.mjrbot.storage.PointsSystem;
import com.mjr.mjrbot.storage.RankSystem;
import com.mjr.mjrbot.util.ConsoleUtil;
import com.mjr.mjrbot.util.ConsoleUtil.MessageType;
import com.mjr.mjrbot.util.HTTPConnect;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.TwitchMixerAPICalls;

public class GetViewersThread extends Thread {

	private TwitchBot bot;

	public GetViewersThread(TwitchBot bot) {
		super("GetViewersThread for" + bot.getChannelName());
		this.bot = bot;
	}

	@Override
	public void run() {
		while (bot.isBotConnected()) {
			try {
				String result = "";
				String viewers = "";
				String vips = "";
				String moderators = "";
				String staff = "";
				String admins = "";
				String global_moderators = "";
				result = HTTPConnect.getRequest(TwitchMixerAPICalls.twitchGetUserChattersAPI(TwitchBot.getChannelNameFromChannelID(bot.getChannelID())));
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

				if (bot.getTwitchData().getViewers().isEmpty()) {
					for (String viewer : vips.split(",")) {
						if (!viewer.equals(""))
							bot.getTwitchData().addVip(viewer);
					}

					for (String viewer : moderators.split(",")) {
						if (!viewer.equals(""))
							bot.getTwitchData().addViewer(viewer);
					}

					for (String viewer : staff.split(",")) {
						if (!viewer.equals(""))
							bot.getTwitchData().addViewer(viewer);
					}

					for (String viewer : admins.split(",")) {
						if (!viewer.equals(""))
							bot.getTwitchData().addViewer(viewer);
					}

					for (String viewer : global_moderators.split(",")) {
						if (!viewer.equals(""))
							bot.getTwitchData().addViewer(viewer);
					}

					for (String viewer : viewers.split(",")) {
						if (!viewer.equals(""))
							bot.getTwitchData().addViewer(viewer);
					}

					ConsoleUtil.textToConsole(bot, BotType.Twitch, "Bot has the list of current active viewers!", MessageType.ChatBot, null);

				} else {
					for (String viewer : vips.split(",")) {
						if (!viewer.equals("")) {
							bot.getTwitchData().addVip(viewer);
							EventLog.addEvent(BotType.Twitch, bot, viewer, "Joined the channel (Twitch)", EventType.User);
						}
					}

					for (String viewer : moderators.split(",")) {
						if (!viewer.equals("")) {
							bot.getTwitchData().addViewer(viewer);
							EventLog.addEvent(BotType.Twitch, bot, viewer, "Joined the channel (Twitch)", EventType.User);
						}
					}

					for (String viewer : staff.split(",")) {
						if (!viewer.equals("")) {
							bot.getTwitchData().addViewer(viewer);
							EventLog.addEvent(BotType.Twitch, bot, viewer, "Joined the channel (Twitch)", EventType.User);
						}
					}

					for (String viewer : admins.split(",")) {
						if (!viewer.equals("")) {
							bot.getTwitchData().addViewer(viewer);
							EventLog.addEvent(BotType.Twitch, bot, viewer, "Joined the channel (Twitch)", EventType.User);
						}
					}

					for (String viewer : global_moderators.split(",")) {
						if (!viewer.equals("")) {
							bot.getTwitchData().addViewer(viewer);
							EventLog.addEvent(BotType.Twitch, bot, viewer, "Joined the channel (Twitch)", EventType.User);
						}
					}
					for (String viewer : viewers.split(",")) {
						if (!viewer.equals("")) {
							bot.getTwitchData().addViewer(viewer);
							EventLog.addEvent(BotType.Twitch, bot, viewer, "Joined the channel (Twitch)", EventType.User);
						}
					}
					ConsoleUtil.textToConsole(bot, BotType.Twitch, "Bot has updated the list of current active viewers!", MessageType.ChatBot, null);

					moderators = moderators.replace(" ", "");
					moderators = moderators.replace("\"", "");
					for (String mod : moderators.split(",")) {
						if (!bot.getTwitchData().getModerators().contains(mod.toLowerCase())) {
							if (!mod.equals(""))
								bot.getTwitchData().addModerator(mod);
						}
					}
					ConsoleUtil.textToConsole(bot, BotType.Twitch, "Bot has updated the list of current active moderators!", MessageType.ChatBot, null);
				}

				for (int i = 1; i < bot.getTwitchData().getViewers().size(); i++) {
					if (Config.getSetting("Points", BotType.Twitch, bot).equalsIgnoreCase("true")) {
						if (!PointsSystem.isOnList(bot.getTwitchData().getViewers().get(i), BotType.Twitch, bot)) {
							PointsSystem.setPoints(bot.getTwitchData().getViewers().get(i), Integer.parseInt(Config.getSetting("StartingPoints", BotType.Twitch, bot)), BotType.Twitch, bot, false, false);
						}
					}
					if (Config.getSetting("Ranks", BotType.Twitch, bot).equalsIgnoreCase("true")) {
						if (!RankSystem.isOnList(bot.getTwitchData().getViewers().get(i), BotType.Twitch, bot)) {
							RankSystem.setRank(bot.getTwitchData().getViewers().get(i), "None", BotType.Twitch, bot);
						}
					}
					if (!bot.getTwitchData().viewersJoinedTimes.containsKey(bot.getTwitchData().getViewers().get(i).toLowerCase().toLowerCase()))
						bot.getTwitchData().viewersJoinedTimes.put(bot.getTwitchData().getViewers().get(i).toLowerCase().toLowerCase(), System.currentTimeMillis());
				}
			} catch (Exception e) {
				MJRBotUtilities.logErrorMessage(e, BotType.Twitch, bot);
			}
			try {
				Thread.sleep(60000 * 2);
			} catch (InterruptedException e) {
				MJRBotUtilities.logErrorMessage(e, BotType.Twitch, bot);
			}
		}
	}
}
