package com.mjr.mjrbot.commands.defaultCommands;

import com.mjr.mjrbot.bots.ChatBotManager.BotType;
import com.mjr.mjrbot.bots.MixerBot;
import com.mjr.mjrbot.bots.TwitchBot;
import com.mjr.mjrbot.commands.Command;
import com.mjr.mjrbot.games.RacingGame;
import com.mjr.mjrbot.storage.Config;
import com.mjr.mjrbot.storage.PointsSystem;
import com.mjr.mjrbot.util.MJRBotUtilities;
import com.mjr.mjrbot.util.Permissions.PermissionLevel;

public class PlacebetCommand extends Command {
	@Override
	public void onCommand(BotType type, Object bot, String sender, String login, String hostname, String message, String[] args) {
		if (Config.getSetting("Games", type, bot).equalsIgnoreCase("true")) {
			if (type == BotType.Twitch) {
				TwitchBot twitchBot = ((TwitchBot) bot);
				if (twitchBot.racingGame.isGameActive) {
					if (args.length == 4) {
						if (checkForValue(twitchBot.racingGame, sender) == false) {
							String bet = args[1];
							String betType = args[2];
							String points = args[3];
							if (betType.equalsIgnoreCase("1st") || betType.equalsIgnoreCase("Top3")) {
								twitchBot.racingGame.placeBet(sender, bet, betType, points);
								PointsSystem.RemovePoints(sender, Integer.parseInt(points), type, bot);
							} else {
								MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !placebet CAR TYPE POINTS(Example !placebet 5 Top3 10) Cars range from 1 to 8, Types = Top3, 1st");
							}
						} else {
							MJRBotUtilities.sendMessage(type, bot, "@" + sender + " you have already made a bet!");
						}

					} else {
						MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !placebet CAR TYPE POINTS(Example !placebet 5 Top3 10) Cars range from 1 to 8, Types = Top3, 1st");
					}
				} else {
					MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Racing game hasnt been started yet!");
				}
			} else if (type == BotType.Mixer) {
				MixerBot mixerBot = ((MixerBot) bot);
				if (mixerBot.racingGame.isGameActive) {
					if (args.length == 4) {
						if (checkForValue(mixerBot.racingGame, sender) == false) {
							String bet = args[1];
							String betType = args[2];
							String points = args[3];
							if (betType.equalsIgnoreCase("1st") || betType.equalsIgnoreCase("Top3")) {
								mixerBot.racingGame.placeBet(sender, bet, betType, points);
								PointsSystem.RemovePoints(sender, Integer.parseInt(points), type, bot);
							} else {
								MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !placebet CAR TYPE POINTS(Example !placebet 5 Top3 10) Cars range from 1 to 8, Types = Top3, 1st");
							}
						} else {
							MJRBotUtilities.sendMessage(type, bot, sender + " you have already made a bet!");
						}

					} else {
						MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Invalid arguments! You need to enter !placebet CAR TYPE POINTS(Example !placebet 5 Top3 10) Cars range from 1 to 8, Types = Top3, 1st");
					}
				} else {
					MJRBotUtilities.sendMessage(type, bot, "@" + sender + " Racing game hasnt been started yet!");
				}
			}
		}
	}

	private static boolean checkForValue(RacingGame game, String val) {
		for (int i = 0; i < game.numberOfBets; i++) {
			if (game.userBets[0][i].contains(val))
				return true;
		}

		return false;
	}

	@Override
	public String getPermissionLevel() {
		return PermissionLevel.User.getName();
	}

	@Override
	public boolean hasCooldown() {
		return true;
	}
}
